package RouteData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import MapData.Address;
import ShortestPath.Route;

public class ApiCarSearch {
   StringBuilder sb;
   String key = "9974a775-4c3d-48f1-8df7-650b3f2debfc";
   static LinkedList<Address> ad;
   static  ApiWalkSearch ws;
   int adSize;
 
   // 생성자, 이차원 배열 초기화
   public ApiCarSearch(LinkedList<Address> ad){
	   int adSize = ad.size();
	   this.ad = ad;
	   this.ws  = new ApiWalkSearch();
	   
	   for(int i=0; i<adSize; i++) {
		   for(int j=i; j<adSize; j++) {
			   Route.carDist[i][j] = new TimeMethod(Integer.MAX_VALUE,false);
			   Route.carDist[j][i] = new TimeMethod(Integer.MAX_VALUE,false);
		   }
	   }
   }
   
   // 차 api 이차원 배열 돌면서 몇번쨰인지 보기 
   public void carApi() {
	   int len = ad.size();
	   try {
		   for(int i=0; i<len-1; i++) {
			   for(int j=i+1; j<len; j++) {
				   callApi(i, j, ad.get(i).getLat(), ad.get(i).getLng(), ad.get(j).getLat(), ad.get(j).getLng()); 
				   Thread.sleep(550);
			   }
		   }
		} catch (Exception e) {
			System.out.println("문제발생쓰");
		} 
	   System.out.println("자동차 이차원 배열 다 채움, 자동차 끝");
   }

   // carApi에서 호출당해, 자동차 호출
   public void callApi(int sno, int eno, double sx, double sy, double ex, double ey) {
	   double distanceMeter =  CalculateDist.distance(sx, sy, ex, ey, "meter"); // 직선거리 구하기     
       if(distanceMeter <= 800) {     	   // 직선거리 800m이하이면 걷기로 넘기기
    	   int walkTime = ws.walkApi(sno, eno, sx, sy, ex, ey);
    	   Route.carDist[sno][eno]  = new TimeMethod(walkTime/ 60, true);
    	   Route.carDist[eno][sno]  = new TimeMethod(walkTime/ 60, true);
       }else {
	      try {
	          String apiURL = "https://api2.sktelecom.com/tmap/routes?version=1&format=xml&totalValue=2&startX="
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
	          } else { //경로 찾기 실패시 도보로 대체
	              br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	              int walkTime = ws.walkApi(sno, eno, sx, sy, ex, ey);
	       	   		Route.carDist[sno][eno]  = new TimeMethod(walkTime/ 60, true);
	       	   		Route.carDist[eno][sno]  = new TimeMethod(walkTime/ 60, true);
	       	   	return;
	          }
	          sb = new StringBuilder();
	          String line;
	      
	          while ((line = br.readLine()) != null) {
	              sb.append(line + "\n");
	          }
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
	          
	      }catch(Exception e) { }
       }
   }

    // 이거 뭔지 다시 물어보기, 왠지 같은거인거 같음 
   public void resultOrderCall(int[] result) { //결과 순서로 api 호출
	   for(int i =0; i < result.length - 1; i++) {
		   callResultCar( ad.get(result[i]).getLat(), ad.get(result[i]).getLng(),
				   ad.get(result[i+1]).getLat(), ad.get(result[i+1]).getLng());
	   }
   }

   // 마지막에 결과 재호출해서 한노드에서 한 노드로 총 정보 가져오기 
   public InfoCar callResultCar(double sx, double sy, double ex, double ey) {
		InfoCar carData = new InfoCar();
		double distanceMeter = CalculateDist.distance(sx, sy, ex, ey, "meter");
		if (distanceMeter <= 800) {
			carData = ws.resultWalkCarApi(sx, sy, ex, ey);
		} else {
			try {
				String apiURL = "https://api2.sktelecom.com/tmap/routes?version=1&format=xml&startX="
						+ Double.toString(sx) + "&startY=" + Double.toString(sy) + "&endX=" + Double.toString(ex)
						+ "&endY=" + Double.toString(ey);
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

				for (int i = 0; i < array.length; i++) {
					if (array[i].equals("tmap:totalDistance")) {
						carData.setSx(sx);
						carData.setSy(sy);
						carData.setEx(ex);
						carData.setEy(ey);
						carData.setDistance(Integer.parseInt(array[i + 1]));
					} else if (array[i].equals("tmap:totalTime")) {
						carData.setTime(Integer.parseInt(array[i + 1]));
					} else if (array[i].equals("tmap:totalFare")) {
						carData.setFare(Integer.parseInt(array[i + 1]));
					} else if (array[i].equals("coordinates")) {
						if (array[i - 2].equals("Point"))
							continue;
						String[] temp = array[i + 1].split("\\s+|,");
						for (int j = 0; j < temp.length; j += 2) {
							carData.addLineList(new DataPair(Double.parseDouble(temp[j]), Double.parseDouble(temp[j + 1])));
						}
					}
				}

				//carData.print();
			} catch (Exception e) {}
		}
		return carData;
	}
   
}
