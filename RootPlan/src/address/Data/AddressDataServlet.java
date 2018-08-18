package address.Data;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MapData.Address;
import MapData.AddressDataManager;
import RouteData.ApiCarSearch;
import RouteData.ApiPTSearch;
import ShortestPath.Route;
import ShortestPath.SetData;

@WebServlet("/AddressDataServlet")
public class AddressDataServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   AddressDataManager ad = new AddressDataManager();  
   SetData sd = new SetData();
   Route r = new Route();
   ApiPTSearch pt;
   ApiCarSearch cs;
   boolean apiFlag= true; 
    public AddressDataServlet() {
        super();
    }
    
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.getWriter().append("Served at: ").append(request.getContextPath());
      this.doPost(request, response);
   }
   
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //contextType 
      response.setContentType("text/html;charset=UTF-8");
      
      PrintWriter out = response.getWriter();
      System.out.print("연결");
      int optionNum = Integer.parseInt(request.getParameter("menuIndex"));   
      //System.out.print(optionNum);
      switch(optionNum) {
         case 1:  //정보 저장
            if(ad.addressData.size() == 7) {
               out.print(7);
            }else {
               double lat = Double.parseDouble(request.getParameter("lat"));
               double lng = Double.parseDouble(request.getParameter("lng"));
               String address = request.getParameter("address");
               String si = request.getParameter("si");
               Address aTmp = new Address(lat,lng,address,si);   
               String result= ad.addData(aTmp);
               out.print(result); 
            }
            break;
            
         case 2: //삭제
            int deleteIndex =  Integer.parseInt(request.getParameter("deleteIndex"));
            int result1 = ad.deleteData(deleteIndex-1);   
            out.print(result1); 
            break;
            
         case 3: //주소보내기
            int addressIndex =  Integer.parseInt(request.getParameter("addressIndex"));
            String result2 = ad.callAddress(addressIndex);
            out.print(result2);          
            break;
            
         case 4: //위도 경도
            System.out.print("위도 경도,");
            String result3 = ad.callLatLng(sd);
            out.print(result3); 
            break;
            
         case 5: //주소 리스트 호출
            String result4 = ad.callAllAddress();
            out.print(result4); 
            break;      
            
         case 6: //크롤링한 데이터를 받아서 넘겨준다.
            String Si = request.getParameter("Si");
            String clickSi = request.getParameter("clickSi");
               if(!Si.equals(clickSi)) {
                  SearchData.LocationDataManager location = new SearchData.LocationDataManager();
                  
                  String  result5 = location.getLocation(clickSi);
                  out.print(result5);
               }else {
                  String  result5 = "null";
                  out.print(result5);
               }
            break;         
            
         case 7: //reset
            int result6 = ad.resetData();
            out.print(result6);
            break;   
            
         case 8: //목적지가 몇 개인지
            int result7 = ad.listSize();
            out.print(result7);
            break;   
            
         case 9:  //start, last 데이터 넣기
            int index = Integer.parseInt(request.getParameter("index"));
            int startLast = Integer.parseInt(request.getParameter("startLast"));
            System.out.print(index);
            if(startLast == 0) 
               sd.SetStartData(index);
            else if(startLast == 1) 
               sd.SetLastData(index);
            out.print(1);
            break;
            
         case 10:  //시작관 끝이 같은지 체크
            int result8 = sd.isSame();
            out.print(result8);
            break;
         
         case 11:  //fourth의 getdata부분_모든 주소 호출 + start 선택 데이터 부분
            String result9 = ad.callAllAddress_StartData(sd);
            out.print(result9);
            break;
            
         case 12: //api 호출 여부
            apiFlag = true;    
            this.r = new Route(); //다시 초기화
            break;
            
         case 13:  //결과 로딩이 끝났는지 
        	 int what = Integer.parseInt(request.getParameter("what"));
        	 if(what == 0) { //대중교통
	        	 if(r.ptFlag == 1)  
	        		 out.print(1);
	        	 else
	        		 out.print(0);
        	 }else { //자동차
        		 if(r.carFlag == 1)  
	        		 out.print(1);
	        	 else
	        		 out.print(0);
        	 }
        	 break;
            
         case 14: //전체 latlng이랑 사이즈 넘기기
            String result10 = ad.callAllLatLng();
            System.out.print(result10);
            out.print(result10);
            break;
            
         case 15:             
           if(apiFlag) {
              int a = Integer.parseInt(request.getParameter("a"));
              int b = Integer.parseInt(request.getParameter("b"));
              String car = request.getParameter("carBlock");
              System.out.println("대중교통API 호출 시작"); 
               apiFlag = r.callApi(a, b, car, ad, sd);      
           }
            break;
            
         case 16:  //대중교통 dfs or 마커 결과 재호출 
           int how = Integer.parseInt(request.getParameter("how"));
           System.out.println("16번 연결");
           if(how == 0)
        	   r.callShortestPath(ad, sd.GetStartData(),sd.GetLastData(), sd.isSame(), how); //dfs , 대중교통   
           String result11 = r.orderResult(how, ad);         
           out.print(result11);
           break;
         
      }               
   }
}