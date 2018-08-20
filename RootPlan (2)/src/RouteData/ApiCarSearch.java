package RouteData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import MapData.Address;
import ShortestPath.copy.Route;

public class ApiCarSearch {
   public StringBuilder sb;
   String key = "9974a775-4c3d-48f1-8df7-650b3f2debfc";
   
   LinkedList<Address> ls;
   ApiWalkSearch ws;
   int adSize;
 //  static TimeMethod[][] carDist = new TimeMethod[7][7];
   
   public ApiCarSearch(LinkedList<Address> ad){
	   adSize = ad.size();
	   this.ls = ad;
	   this.ws  = new ApiWalkSearch();
	   
	   for(int i=0; i<adSize; i++) {
		   for(int j=i; j<adSize; j++) {
			   Route.carDist[i][j] = new TimeMethod(Integer.MAX_VALUE,false);
			   Route.carDist[j][i] = new TimeMethod(Integer.MAX_VALUE,false);
		   }
	   }
   }

   public void callApi(int sno, int eno, double sx, double sy, double ex, double ey) {
	   double distanceMeter =  CalculateDist.distance(sx, sy, ex, ey, "meter"); // 직선거리 구하기     
       if(distanceMeter <= 800) {     	   // 직선거리 800m이하이면 걷기로 넘기기
    	   int walkTime = ws.walkApi(sno, eno, sx, sy, ex, ey);
    	   Route.carDist[sno][eno]  = new TimeMethod(walkTime/ 60, true);
    	   Route.carDist[eno][sno]  = new TimeMethod(walkTime/ 60, true);
       }else {
	      try {
	          String apiURL = "https://api2.sktelecom.com/tmap/routes?version=1&format=xml&startX="
	        		  +Double.toString(sx)+"&startY="+Double.toString(sy)+"&endX="+Double.toString(ex)+"&endY="+Double.toString(ey);
	          URL url = new URL(apiURL);
	          HttpURLConnection con = (HttpURLConnection) url.openConnection();
	          
	          con.setRequestMethod("POST");
	          con.setRequestProperty("appKey", key);
	          con.setDoOutput(true);
	          
	          int responseCode = con.getResponseCode();
	          BufferedReader br;
	          if (responseCode == 200) {
	              br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	          } else {
	              br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	              System.out.println("d실패");
	          }
	          sb = new StringBuilder();
	          String line;
	      
	          while ((line = br.readLine()) != null) {
	              sb.append(line + "\n");
	          }
	          
	          System.out.println("자동차 불러옴");
	          br.close();
	          con.disconnect();
	          String data = sb.toString();
	          
	          String[] array;
	          array = data.split("<|>");
	          for(int i=0; i<array.length; i++) {
	        	   if(array[i].equals("tmap:totalTime")) {
	        		   Route.carDist[sno][eno] = new TimeMethod(Integer.parseInt(array[i+1]) / 60 ,false); 	       
	        		   Route.carDist[eno][sno] = new TimeMethod(Integer.parseInt(array[i+1]) / 60 ,false); 
	        		   break;
	        	  }
	          }	        	          
	          
	        /*  Car car = new Car(); 
	          // sx, sy, ex, ey, distance, second, fee Car에 매핑하기
	          for(int i=0; i<array.length; i++) {
	        	  if(array[i].equals("tmap:totalDistance")) {
	        		  car.setSno(sno); // 혹시를 위해 넣어놓기
	        		  car.setEno(eno);
	        		  car.setSx(sx);
	        		  car.setSy(sy);
	        		  car.setEx(ex);
	        		  car.setEy(ey);
	        		  car.setDistance(Integer.parseInt(array[i+1])); // 거리 m 넣기
	        	  }else if(array[i].equals("tmap:totalTime")) {
	        		 car.setSecond(Integer.parseInt(array[i+1])); // 시간 초로 넣기
	        	  }else if(array[i].equals("tmap:totalFare")) {
	        		  car.setFee(Integer.parseInt(array[i+1])); // 요금 넣기
	        		  // 여기서 다른 클래스 이차원 배열에 넣어주기!!!!! 할거 많음 
	        		  
	        		  break;
	        	  }
	          }*/
	      }catch(Exception e) {
	    	  
	      }
       }
   }
   
   // 리스트 매개변수로 받기, 이중포문으로 시작점, 끝점 정해서 callApi 함수로 넘기기
   public void carApi() {
	   // 이차원 배열로 for문 돌린다음
	   int len = ls.size();
	   try {
		   for(int i=0; i<len-1; i++) {
			   for(int j=i+1; j<len; j++) {
				   //if(i == j)  Route.carDist[i][j] = new TimeMethod(Integer.MAX_VALUE,false);
				   callApi(i, j, ls.get(i).getLat(), ls.get(i).getLng(), ls.get(j).getLat(), ls.get(j).getLng()); 
				   Thread.sleep(550);
			   }
		   }
		} catch (Exception e) {
			System.out.println("문제발생쓰");
		} 
	   System.out.println("자동차 끝");
	   carPrint(adSize);
   }
   void carPrint(int size) {
		System.out.println("자동차 거리 출력");
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				System.out.print(Route.carDist[i][j].getTime() + " ");
			}
			System.out.println();
		}
	}
}