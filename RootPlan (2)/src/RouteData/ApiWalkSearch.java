package RouteData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiWalkSearch{
   public StringBuilder sb;
   String key = "9974a775-4c3d-48f1-8df7-650b3f2debfc";
   String startName = "start";
   String endName = "end";

   public int walkApi(int sno, int eno, double sx, double sy, double ex, double ey) {
      int findTime = 0;
      try {
          String apiURL = "https://api2.sktelecom.com/tmap/routes/pedestrian?version=1&format=xml&startX="
        		  	+Double.toString(sx)+"&startY="+Double.toString(sy)+"&endX="+Double.toString(ex)+"&endY="+Double.toString(ey) 
        		  	+ "&startName="+startName+"&endName="+endName;
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
              System.out.print("실패");
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
          
          //InfoWalk walk = new InfoWalk();
          for(int i=0; i<array.length; i++) {
        	  /*if(array[i].equals("tmap:totalDistance")) {
        		walk.setSno(sno);
        		walk.setEno(eno);
        		walk.setSx(sx);
        		walk.setSy(sy);
        		walk.setEx(ex);
        		walk.setEy(ey);
        		walk.setDistance(Integer.parseInt(array[i+1]));
        	  }else*/ 
        	  if(array[i].equals("tmap:totalTime")) {
        		  // 여기서 다른 클래스 이차원 배열에 넣어주기
        		  findTime =  Integer.parseInt(array[i+1]);
        		  break;
        	  }
          }
      }catch(Exception e) {}
	return findTime;
   }
}
