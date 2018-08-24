package callApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;

import dto.Location;
 
public class Search {
    public static StringBuilder sb;//
    public static int display;
    public static String clientId = "QUyHkL9SA1c0aTQjz197";
   public static String clientSecret = "d4nYetPHwT";
    public static  String findLocation;
    static LinkedList<Location> ld = new LinkedList<Location>();
    
    public Search(String si){ 
       findLocation = si;   //지도에서 받은 시의 위치를 넣어준다.
    }
   
   public static String getImage(String imgTitle) {
      //System.out.println("이미지 불러오기");
      try {
         String text = URLEncoder.encode(imgTitle, "utf-8");
         // 여기에 있는 display 값을 조정함에 따라 사진을 긁어오는게 달라진다. 
         String apiURL = "https://openapi.naver.com/v1/search/image?query=" + text + "&display=" + 100 + "&";
         URL url = new URL(apiURL);
         HttpURLConnection con = (HttpURLConnection) url.openConnection();
         con.setRequestMethod("GET");
         con.setRequestProperty("X-Naver-Client-Id", clientId);
         con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
         //System.out.println("URL : " + apiURL);
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
      //   System.out.println(sb);
         String data = sb.toString();
         String[] array;
         array = data.split("\"");
         
         for (int i = 0; i < array.length; i++) {
            if (array[i].equals("thumbnail") && array[i+2]!=null) 
               return array[i+2];
         }
      }catch(Exception e) {
         System.out.println(e);
      }
      return null;
   }
   
   // 후에 함수로 변경 : 매개변수(findLocation) : 시주소
   public LinkedList<Location> getRecommendData() {
      ld.clear();
       display = 20;
        try {
        	System.out.println("검색 호출");
            String text = URLEncoder.encode(findLocation + " 여행지", "utf-8");
            String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text + "&display=" + display + "&";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
          //  System.out.println("URL : " + apiURL);
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
           // System.out.println(sb); 
            String data = sb.toString();
            String[] array;
            array = data.split("\"");
            
            Location location = new Location();
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals("title")) {
                   location = new Location();
                   location.setTitle(array[i+2]);
                   location.setImgUrl(getImage(array[i+2])); // title로 이미지 검색해서 넣어주기
                }
                if (array[i].equals("mapx"))
                   location.setMapx(array[i+2]);
                if (array[i].equals("mapy")) {
                    location.setMapy(array[i+2]);
                    ld.add(location);    
                }
            }
            //System.out.println(sb);
        } catch (Exception e) {
            System.out.println(e);
        }
        return ld;
    }
  
}