package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AddressDataManager;
import dao.Route;
import dto.Address;
import dto.SetData;

@WebServlet("/AddressDataServlet")
public class AddressDataServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   AddressDataManager ad = new AddressDataManager();  
   SetData sd = new SetData();
   Route r;
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
            break;
            
         case 5: //주소 리스트 호출
            String result4 = ad.callAllAddress();
            int num = Integer.parseInt(request.getParameter("num"));
            if(num== 0) {  //3번째 페이지에 오면 시작과 끝을 초기화 해준다.
            	this.r = new Route();
            	apiFlag = true;   
                r.Clear();
                System.out.println("자동차 flag ="+ r.carFlag+", 대중교통 flag="+ r.ptFlag);
            	sd.SetStartData(-1);
            	sd.SetLastData(-1);
            }
            out.print(result4); 
            break;      
            
       /*  case 6: //크롤링한 데이터를 받아서 넘겨준다.
            String Si = request.getParameter("Si");
            String clickSi = request.getParameter("clickSi");
               if(!Si.equals(clickSi)) {
                  dao.LocationDataManager location = new dao.LocationDataManager();
                  
                  String  result5 = location.getLocation(clickSi);
                  out.print(result5);
               }else {
                  String  result5 = "null";
                  out.print(result5);
               }
            break;        
        */ 
            
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
        	 System.out.println("19번 연결");       
             String result14 = "";
          		  result14 += "<Data>";
          		  result14 += "<lat>" + Double.toString(ad.addressData.get(0).getLat()) + "</lat>";
          		  result14 += "<lng>" + Double.toString(ad.addressData.get(0).getLng()) + "</lng>"; 
          		  result14 += "</Data>";
             out.print(result14);	  
             break;
             
         case 13:  //결과 로딩이 끝났는지 
        	 int what = Integer.parseInt(request.getParameter("what"));
        	 System.out.println("13" +what + r.ptFlag);
        	 int result13 = 0; 
        	 if(what == 0) { //대중교통
	        		 try {
	        			 while(true) {
	        				 Thread.sleep(500);
	        				 if(r.ptFlag == 1 && sd.GetStartData() != -1 && sd.GetLastData() != -1) {
	    	        			 System.out.println("대:"+r.ptFlag+", "+sd.GetStartData() +", "+sd.GetLastData());
	    	        			 result13 = 1;
	    	        			 break;
	    	        		 }
	        			 }
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	 }else if(what == 1){ //자동차        		  			 
        			try {
        				while(true) {         
						Thread.sleep(500);
						if(r.carFlag == 1 && sd.GetStartData() != -1 && sd.GetLastData() != -1) {
	        				System.out.println("자동차While:"+r.carFlag);
		        			 result13 = 1;
		        			 break;
		        		 }
        				}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();    			 
	        	 }
        	 }
        	 System.out.println(result13);
        	 out.print(result13);
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
              apiFlag = r.callApi(a, b, car, ad, sd);      
           }
            break;
            
         case 16:  //대중교통 dfs or 마커 결과 재호출 
           int how = Integer.parseInt(request.getParameter("how"));
           System.out.println("16번 연결");            
           r.callShortestPath(ad, sd.GetStartData(),sd.GetLastData(), sd.isSame(), how); // 자동차 1, 대중교통  0           
           break;
         
         case 17:  //마크를 위한 호출
           int how2 = Integer.parseInt(request.getParameter("how")); 
           System.out.println("17번 연결");           
           String result11 = r.orderResult(how2, ad);
           out.print(result11);
           break;
         
         case 18: //폴리라인 그리기 위한 latlng 데이터 호출
        	 int how3 =  Integer.parseInt(request.getParameter("how"));
           out.print(r.resultPoly(how3));
           break;
        	 
         case 20: // 대중교통 left 에 뿌려줌
        	 int how4 = Integer.parseInt(request.getParameter("how"));
        	 System.out.println("서블렛 20번 들어옴");
         	out.print(r.resultList(how4));
         	break;
         	
      }               
   }
}