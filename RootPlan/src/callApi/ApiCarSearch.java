package callApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import dao.CalculateDist;
import dto.Address;
import dto.DataPair;
import dto.DataTotal;
import dto.InfoCar;
import dto.TimeMethod;

public class ApiCarSearch {
   CalculateDist cd = new CalculateDist();
   StringBuilder sb;
   String key = "9974a775-4c3d-48f1-8df7-650b3f2debfc";
   LinkedList<Address> ad;
   ApiWalkSearch ws;
<<<<<<< HEAD
   DataTotal dataTotal;
   int listSize;
 
=======
   int adSize;
   int id = 0;
>>>>>>> refs/heads/branch4
   // 생성자, 이차원 배열 초기화
<<<<<<< HEAD
   public ApiCarSearch(LinkedList<Address> ad, DataTotal dataTotal, int listSize ){
=======
   public ApiCarSearch(LinkedList<Address> ad,int id){
	   int adSize = ad.size();
>>>>>>> refs/heads/branch4
	   this.ad = ad;
	   this.ws  = new ApiWalkSearch();
<<<<<<< HEAD
	   this.dataTotal = dataTotal;
	   this.listSize = listSize;
	   
	  dataTotal.initCarDist();
=======
	   this.id = id;
	   for(int i=0; i<adSize; i++) {
		   for(int j=i; j<adSize; j++) {
			   Route.carDist[id][i][j] = new TimeMethod(Integer.MAX_VALUE,false);
			   Route.carDist[id][j][i] = new TimeMethod(Integer.MAX_VALUE,false);
		   }
	   }
>>>>>>> refs/heads/branch4
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
<<<<<<< HEAD
	   CalculateDist calDist = new CalculateDist();
	   double distanceMeter =  calDist.distance(sx, sy, ex, ey, "meter"); // 직선거리 구하기     
=======
	   double distanceMeter =  cd.distance(sx, sy, ex, ey, "meter"); // 직선거리 구하기     
>>>>>>> refs/heads/branch4
       if(distanceMeter <= 800) {     	   // 직선거리 800m이하이면 걷기로 넘기기
    	   int walkTime = ws.walkApi(sno, eno, sx, sy, ex, ey);
<<<<<<< HEAD
    	   dataTotal.carDist[sno][eno]  = new TimeMethod(walkTime/ 60, true);
    	   dataTotal.carDist[eno][sno]  = new TimeMethod(walkTime/ 60, true);
=======
    	   Route.carDist[id][sno][eno]  = new TimeMethod(walkTime/ 60, true);
    	   Route.carDist[id][eno][sno]  = new TimeMethod(walkTime/ 60, true);
>>>>>>> refs/heads/branch4
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
<<<<<<< HEAD
	              	dataTotal.carDist[sno][eno]  = new TimeMethod(walkTime/ 60, true);
	       	   		dataTotal.carDist[eno][sno]  = new TimeMethod(walkTime/ 60, true);
=======
	       	   		Route.carDist[id][sno][eno]  = new TimeMethod(walkTime/ 60, true);
	       	   		Route.carDist[id][eno][sno]  = new TimeMethod(walkTime/ 60, true);
>>>>>>> refs/heads/branch4
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
<<<<<<< HEAD
	        		   dataTotal.carDist[sno][eno] = new TimeMethod(Integer.parseInt(array[i+1]) / 60 ,false); 	       
	        		   dataTotal.carDist[eno][sno] = new TimeMethod(Integer.parseInt(array[i+1]) / 60 ,false); 
=======
	        		   Route.carDist[id][sno][eno] = new TimeMethod(Integer.parseInt(array[i+1]) / 60 ,false); 	       
	        		   Route.carDist[id][eno][sno] = new TimeMethod(Integer.parseInt(array[i+1]) / 60 ,false); 
>>>>>>> refs/heads/branch4
	        		   break;
	        	  }
	          }	        	          
	          
	      }catch(Exception e) { }
       }
   }

   public void resultOrderCall(int[] result, int start, int end) { //결과 순서로 api 호출
	   if(start==end) listSize++;
	   
	   System.out.println("차 결과 api호출");
		  for(int i =0;i<listSize-1;i++)
			  System.out.print(result[i]+" ");

	   System.out.println("result 보기");
	   for(int i=0; i<listSize-1; i++) {
		   System.out.print(result[i]+ " ");
	   }
	   System.out.println();
	   try {		   
		   for(int i =0; i < listSize-1; i++) {
			   dataTotal.carList.add(callResultCar( ad.get(result[i]).getLat(), ad.get(result[i]).getLng(),
					   ad.get(result[i+1]).getLat(), ad.get(result[i+1]).getLng()));
			   Thread.sleep(550);
		   }
		} catch (Exception e) {
			System.out.println("문제발생쓰");
		} 
   }

   // 마지막에 결과 재호출해서 한노드에서 한 노드로 총 정보 가져오기 
   public InfoCar callResultCar(double sx, double sy, double ex, double ey) {
		InfoCar carData = new InfoCar();
<<<<<<< HEAD
		CalculateDist calDist = new CalculateDist();
		double distanceMeter = calDist.distance(sx, sy, ex, ey, "meter");
=======
		double distanceMeter = cd.distance(sx, sy, ex, ey, "meter");
>>>>>>> refs/heads/branch4
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

				//System.out.println("자동차 불러옴");
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
					} else if (array[i].
							equals("tmap:totalTime")) {
						carData.setTime(Integer.parseInt(array[i + 1]));
					} else if (array[i].equals("tmap:taxiFare")) {
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
