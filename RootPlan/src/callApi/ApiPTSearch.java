package callApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import dao.CalculateDist;
import dao.Route;
import dto.DataPair;
import dto.InfoPT;
import dto.InfoSectionPT;
import dto.TimeMethod;

public class ApiPTSearch {
   StringBuilder sb;
   String key = "5FtIuAS9YmPfOD56TV5NHqYE6EivPWAAIBCZcy6V72c";
   LinkedList<dto.Address> ad;
   // 이차원 배열을 Route.java에다가 넣어주기
   ApiWalkSearch ws;
   boolean flag = false; // 대중교통에서 걷기 api호출에 쿨타임을 주기 위해서 만들었다.
   int adSize;
   
   // 생성자, 이차원 배열 초기화
   public ApiPTSearch(LinkedList<dto.Address> ad) {
      adSize = ad.size();
      this.ad = ad;
      this.ws = new ApiWalkSearch();
      // 배열 초기화
      for (int i = 0; i < adSize; i++) {
         for (int j = 0; j < adSize; j++) {
            Route.ptDist[i][j] = new TimeMethod(Integer.MAX_VALUE, false);
         }
      }
   }   

   // 출력 함수 : 대중교통 거리 출력
   static void ptPrint(int size) {
      System.out.println("대중교통 거리 출력");
      for(int i=0; i<size; i++) {
         for(int j=0; j<size; j++) {
            System.out.print(Route.ptDist[i][j].getTime() + " "); 
         }
         System.out.println();
      }
   }
   
   // 이차원 배열 돌면서 callPTApi 호출
   public void callTransportApi(int a, int b) {
      int size = ad.size();
      for (int i = a; i < b; i++) {
         for (int j = 0; j < size; j++) {
            if (Route.ptDist[i][j].getMethod())
               continue; // 걷기데이터가 호출되었었기 때문에
            else if (i == j)
               Route.ptDist[i][j] = new TimeMethod(Integer.MAX_VALUE, false);
            else
               callPTApi( ad.get(i).getLat(),  ad.get(i).getLng(), ad.get(j).getLat(), ad.get(j).getLng(),i ,j );
         }
      }
      System.out.println("대중교통끝");
   }
 
   // callTransportApi 호출당해, 대중교통 호출
  public void callPTApi(double sx, double sy, double ex , double ey, int i, int j) {
      double distanceMeter = CalculateDist.distance(sx, sy, ex, ey, "meter");
      if (distanceMeter <= 800) {
         // 이전에 있던 애가 걷기 호출을 했었는지
         int tmpTime = 0;
         if (flag == true) {
            try {
               Thread.sleep(550);
               tmpTime = ws.walkApi(i, j, sx, sy, ex, ey)/60;  // 60 없애고 분으로 해야할듯
            } catch (Exception e) {}
         } else {
            tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60;
         }
         
         // 걷기일 경우 양방향 같으니 같은 데이터 넣어주기
         Route.ptDist[i][j] = new TimeMethod(tmpTime, true);
         Route.ptDist[j][i] = new TimeMethod(tmpTime, true);
         flag = true;
      } else {
         flag = false;
         try {
            String apiURL = "https://api.odsay.com/v1/api/searchPubTransPath?SX=" + Double.toString(sx) + "&SY="
                  + Double.toString(sy) + "&EX=" + Double.toString(ex) + "&EY=" + Double.toString(ey) + "&apiKey="
                  + key + "";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
               br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
               br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
               //에러가 발생하면 걷기로 대체
               int tmpTime = 0;
               if (flag == true) {
                  try {
                     Thread.sleep(550);
                     tmpTime = ws.walkApi(i, j, sx, sy, ex, ey)/60;  // 60 없애고 분으로 해야할듯
                  } catch (Exception e) {}
               } else {
                  tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60;
               }
               
               // 걷기일 경우 양방향 같으니 같은 데이터 넣어주기
               Route.ptDist[i][j] = new TimeMethod(tmpTime, true);
               Route.ptDist[j][i] = new TimeMethod(tmpTime, true);
               flag = true;

               return ;
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
            array = data.split("\"");
            
            for (int k = 0; k < array.length; k++) {
               if (array[k].equals("totalTime")) {
                  Route.ptDist[i][j] = new TimeMethod(Integer.parseInt(array[k + 1].substring(1, array[k + 1].length() - 1)), false);
                  break;
               }
            }
         } catch (Exception e) { }
      }
   }

  public void resultOrderCall(int[] result) {  //결과대로 호출
      for(int i =0; i < result.length - 1; i++) {
    	  //System.out.println("좌표보기 : " +  ad.get(result[i]).getLat() + " " + ad.get(result[i]).getLng()
          //     + " " + ad.get(result[i+1]).getLat()+ " " + ad.get(result[i+1]).getLng());
         Route.ptList.add(callResultPT( ad.get(result[i]).getLat(), ad.get(result[i]).getLng(),
               ad.get(result[i+1]).getLat(), ad.get(result[i+1]).getLng()));
      }
  }
  
  // 대중교통 재호출할 때, 마지막에 결과 한노드에서 한 노드로 총 정보 가져오기 
  public InfoPT callResultPT(double sx, double sy, double ex, double ey) {
	  System.out.println("callResultPT 들어옴");
      InfoPT infopt = new InfoPT(); // 1-2 지점 이동시
      InfoSectionPT infoSec = new InfoSectionPT();
      
      double distanceMeter = CalculateDist.distance(sx, sy, ex, ey, "meter");
      System.out.println("거리 : " + distanceMeter);
      if (distanceMeter <= 800) {
    	  System.out.println("걷기 호출");
         // 이전에 있던 애가 걷기 호출을 했었는지
         if (flag == true) {
            try {
               Thread.sleep(550);
               infopt = ws.resultWalkPTApi(sx, sy, ex, ey); 
            } catch (Exception e) {}
         } else {
            infopt = ws.resultWalkPTApi(sx, sy, ex, ey);
         }
         
         // 걷기일 경우 양방향 같으니 같은 데이터 넣어주기
         flag = true;
      } else {
         flag=false;
         try {
            String apiURL = "https://api.odsay.com/v1/api/searchPubTransPath?SX=" + Double.toString(sx) + "&SY="
                  + Double.toString(sy) + "&EX=" + Double.toString(ex) + "&EY=" + Double.toString(ey) + "&apiKey="
                  + key + "";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
               br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
               br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
               
               System.out.println("대중교통 실패");
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
            array = data.split("\\{|\\}|\"|\\,|\\:");

            int trafficType = 0;
            double x = 0, y = 0;
            boolean flag = false;
            for (int i = 0; i < array.length; i++) {
               if (array[i].equals("result")) {
                  infopt.setSx(sx);
                  infopt.setSy(sy);
                  infopt.setEx(ex);
                  infopt.setEy(ey);
                  infopt.setWalk(false); // 여기까지 온건 walk 아니라 pt
               } else if (array[i].equals("trafficType")) {
                  trafficType = Integer.parseInt(array[i + 2]);
               } else if (array[i].equals("lane")) {
                  if (flag)
                     infopt.addSection(infoSec); // 처음이 아니면 넣어주기
                  flag = true;
                  infoSec = new InfoSectionPT();
                  infoSec.setTrafficType(trafficType); // lane이 trafficType보다 나중에 나오니까
               } else if (array[i].equals("busNo")) { // 버스일 경우
                  if (trafficType != 2)
                     continue;
                  infoSec.addBusNoList(array[i + 3]);
               } else if (array[i].equals("name")) { // 지하철일 경우
                  if (trafficType != 1)
                     continue;
                  infoSec.setSubwayLine(array[i + 3]);
               } else if (array[i].equals("x")) {
                  x = Double.parseDouble(array[i + 3]);
               } else if (array[i].equals("y")) {
                  y = Double.parseDouble(array[i + 3]);
                  infopt.addLineList(new DataPair(x, y));
               } else if (array[i].equals("distance")) {
                  infoSec.setSectionDistance(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("sectionTime")) {
                  infoSec.setSectionTime(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("startName")) {
                  infoSec.setStartStation(array[i + 3]);
               } else if (array[i].equals("endName")) {
                  infoSec.setEndStation(array[i + 3]);
               } else if (array[i].equals("payment")) {
                  infopt.setFare(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("totalTime")) {
                  infopt.setTotalTime(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("totalDistance")) {
                  infopt.setTotalDistance(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("firstStartStation")) {
                  infopt.setFirstStartStation(array[i + 3]);
               } else if (array[i].equals("lastEndStation")) {
                  infopt.setLastEndStation(array[i + 3]);
                  break; // 하나 다 불러온 거니 for문 나감
               }
            }
            // infopt.print();
         } catch (Exception e) {}
      }
      return infopt;
  }
  
}