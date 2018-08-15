package RouteData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import ShortestPath.copy.Route;

public class ApiPTSearch {
   public static StringBuilder sb;
   static String key = "5FtIuAS9YmPfOD56TV5NHqYE6EivPWAAIBCZcy6V72c";
   static LinkedList<MapData.Address> ad;
   // 이차원 배열을 Route.java에다가 넣어주기
   static ApiWalkSearch ws;
   static boolean flag = false; // 대중교통에서 걷기 api호출에 쿨타임을 주기 위해서 만들었다.
   static int adSize;
   
   public ApiPTSearch(LinkedList<MapData.Address> ad) {
      adSize = ad.size();
      this.ad = ad;
      this.ws = new ApiWalkSearch();
      // 배열 초기화
      for (int i = 0; i < adSize; i++) {
         for (int j = 0; j < adSize; j++) {
            Route.ptDist[i][j] = new TimeMethod(0, false);
         }
      }
   }

   static void ptPrint(int size) {
      System.out.println("대중교통 거리 출력");
      for(int i=0; i<size; i++) {
         for(int j=0; j<size; j++) {
            System.out.print(Route.ptDist[i][j].getTime() + " "); 
         }
         System.out.println();
      }
   }
   public static void callTransportApi(int a, int b) {
      int size = ad.size();
      for (int i = a; i < b; i++) {
         for (int j = 0; j < size; j++) {
            if (Route.ptDist[i][j].getMethod())
               continue; // 걷기데이터가 호출되었었기 때문에
            else if (i == j)
            	Route.ptDist[i][j] = new TimeMethod(Integer.MAX_VALUE, false);
            else
               allApi(i, j);
         }
      }
      System.out.println("대중교통끝");
      //ptPrint(adSize);
   }

   static void allApi(int i, int j) {
      double sx = ad.get(i).getLat();
      double sy = ad.get(i).getLng();
      double ex = ad.get(j).getLat();
      double ey = ad.get(j).getLng();

      double distanceMeter = CalculateDist.distance(sx, sy, ex, ey, "meter");
      if (distanceMeter <= 800) {
         // 이전에 있던 애가 걷기 호출을 했었는지
         int tmpTime = 0;
         if (flag == true) {
            try {
               Thread.sleep(500);
               tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60; // 시간 초로 넣기
            } catch (Exception e) {
            }
         } else {
            tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60; // 시간 초로 넣기
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
            array = data.split("\"");
            
            for (int k = 0; k < array.length; k++) {
               if (array[k].equals("totalTime")) {
            	   Route.ptDist[i][j] = new TimeMethod(Integer.parseInt(array[k + 1].substring(1, array[k + 1].length() - 1)), false);
                  break;
               }
            }
         } catch (Exception e) {

         }
      }
   }
}