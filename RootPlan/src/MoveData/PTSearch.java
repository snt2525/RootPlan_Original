package MoveData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class PTSearch {
   public static StringBuilder sb;
   static String key = "5FtIuAS9YmPfOD56TV5NHqYE6EivPWAAIBCZcy6V72c";
   static LinkedList<MapData.Address> ad;   
   static int[][] getTime;
   public PTSearch(LinkedList<MapData.Address> ad){
      this.ad = ad;
      this.getTime = new int[ad.size()][ad.size()];  //최단 시간을 넣어줄 배열을 생성한다.
   }
   
   public static void callTransportApi(){
      int size = ad.size();
      for(int i =0;i<size;i++) {
         for(int j =0;j<size;j++) {
            if(i == j)
               getTime[i][j] = Integer.MAX_VALUE;
            else
               allApi(i,j);               
         }
      }
   }
   
   static void allApi(int i,int j){
      try {
          String apiURL = "https://api.odsay.com/v1/api/searchPubTransPath?SX="+Double.toString(ad.get(i).getLat())+"&SY="+Double.toString(ad.get(i).getLng())
          +"&EX="+Double.toString(ad.get(j).getLat())+"&EY="+Double.toString(ad.get(j).getLng())+"&apiKey="+key+"";
          URL url = new URL(apiURL);
          HttpURLConnection con = (HttpURLConnection) url.openConnection();
          con.setRequestMethod("GET");          
          
          int responseCode = con.getResponseCode();
          BufferedReader br;
          if (responseCode == 200) {
              br = new BufferedReader(new InputStreamReader(con.getInputStream()));
          } else {
              br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
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
             
             for(int k=0; k<array.length; k++) {
                if(array[k].equals("totalTime")) {
                   getTime[i][j] = Integer.parseInt(array[k+1].substring(1, array[k+1].length()-1));
                    System.out.println(getTime[i][j] ); 
                   break;
                }
             }   
      }catch(Exception e) {
      
      }
   }
}