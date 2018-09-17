package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AddressDataManager;
import dao.ConnectDB;
import dao.Route;
import dto.Address;
import dto.SetData;

@WebServlet("/AddressDataServlet")
public class AddressDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    ConnectDB db = new ConnectDB();
    AddressDataManager[] ad = new AddressDataManager[20];  
    SetData[] sd = new SetData[20];
    Route[] r = new Route[20];
    Address[] aTmp = new Address[20];
    boolean[] apiFlag = new boolean[20]; // 로그아웃시 or session죽을 떄 자원해제해야하니까 apiFlag 변경해야함 
   
    public AddressDataServlet() {
        super();
        for(int i=0; i<20; i++) {
            ad[i]=new AddressDataManager();
            sd[i] = new SetData();
         }
    }
    
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.getWriter().append("Served at: ").append(request.getContextPath());
      this.doPost(request, response);
   }
   
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //contextType 
      response.setContentType("text/html;charset=UTF-8");
      
      PrintWriter out = response.getWriter();
      System.out.println("연결: "+ request.getParameter("menuIndex"));
      int optionNum = Integer.parseInt(request.getParameter("menuIndex"));
      int ID = Integer.parseInt(request.getParameter("customerID"));
      System.out.println("customerID : "+ID);
      
      
      switch(optionNum) {
         case 0: //경로 데이터 저장
        	 // 0: 데이터 이미 있어서 저장안함, 1 :  저장해서 완료
        	 String cID = request.getParameter("cID");   
        	 System.out.println("cID(서블렛) : "+cID);
        	 String resultFlag = db.CheakSameData(ad[ID].addressData,cID);
        	 if(resultFlag.equals("1")) { // 저장됫으면 route2에 데이터를 저장해줘야한다.
        		 
        	 }
        	 out.print(resultFlag); //1이면 저장 된거고,0이면 저장 중복
        	 break;
         case 1:  //정보 저장
        	 apiFlag[ID] = true;
            if(ad[ID].addressData.size() == 7) {
               out.print(7);
            }else {
               double lat = Double.parseDouble(request.getParameter("lat"));
               double lng = Double.parseDouble(request.getParameter("lng"));
               String address = request.getParameter("address");
               //String si = request.getParameter("si");
               aTmp[ID] = new Address(lat,lng,address);   
               String result= ad[ID].addData(aTmp[ID]);
               out.print(result); 
            }
            break;
            
         case 2: //삭제
            int deleteIndex =  Integer.parseInt(request.getParameter("deleteIndex"));
            int result1 = ad[ID].deleteData(deleteIndex-1);   
            out.print(result1); 
            break;
            
         case 3: //주소보내기
            int addressIndex =  Integer.parseInt(request.getParameter("addressIndex"));
            String result2 = ad[ID].callAddress(addressIndex);
            out.print(result2);          
            break;
            
         case 4: //위도 경도
        	  System.out.print("위도 경도,"); 
        	  String result3 = ad[ID].callLatLng(sd[ID]); 
        	  out.print(result3);  
            break;
            
         case 5: //주소 리스트 호출
            String result4 = ad[ID].callAllAddress();
            int num = Integer.parseInt(request.getParameter("num"));
            if(num== 0) {  //3번째 페이지에 오면 시작과 끝을 초기화 해준다.
            	System.out.println("초기화");
            	this.r[ID] = new Route(ad[ID].addressData.size());
            	apiFlag[ID] = true;   
               // r[ID].Clear();
                System.out.println("자동차 flag ="+ r[ID].carFlag+", 대중교통 flag="+ r[ID].ptFlag);
            	sd[ID].SetStartData(-1);
            	sd[ID].SetLastData(-1);
            }
            out.print(result4); 
            break;      
            
         case 6: //DB의 사용자 저장한  데이터  모두 호출
        	 cID = request.getParameter("customerID");
        	// String resultDB = db.GetAllData(cID); // 모든 데이터 파싱해서 가져옴
        	 //out.print(resultDB);
        	 out.print(0);
        	 break;
            
         case 7: //reset
            int result6 = ad[ID].resetData();
            out.print(result6);
            break;   
            
         case 8: //목적지가 몇 개인지
            int result7 = ad[ID].listSize();
            out.print(result7);
            break;   
            
         case 9:  //start, last 데이터 넣기
        	 System.out.println("호출");
            int index = Integer.parseInt(request.getParameter("index"));
            int startLast = Integer.parseInt(request.getParameter("startLast"));
            System.out.print(index);
            if(startLast == 0) 
               sd[ID].SetStartData(index);
            else if(startLast == 1) 
               sd[ID].SetLastData(index);
            out.print(1);
            break;
            
         case 10:  //시작관 끝이 같은지 체크
            int result8 = sd[ID].isSame();
            out.print(result8);
            break;
         
         case 11:  //fourth의 getdata부분_모든 주소 호출 + start 선택 데이터 부분
            String result9 = ad[ID].callAllAddress_StartData(sd[ID]);
            out.print(result9);
            break;
            
         case 12: //api 호출 여부
        	 System.out.println("19번 연결");       
             String result14 = "";
          		  result14 += "<Data>";
          		  result14 += "<lat>" + Double.toString(ad[ID].addressData.get(0).getLat()) + "</lat>";
          		  result14 += "<lng>" + Double.toString(ad[ID].addressData.get(0).getLng()) + "</lng>"; 
          		  result14 += "</Data>";
             out.print(result14);	  
             break;
             
         case 13:  //결과 로딩이 끝났는지 
        	 int what = Integer.parseInt(request.getParameter("what"));
        	 System.out.println("13," +what +":"+ r[ID].ptFlag);
        	 int result13 = 0; 
        	 if(what == 0) { //대중교통
	        		 try {
	        			 while(true) {
	        				 Thread.sleep(500);
	        				 if(r[ID].ptFlag == 1 && sd[ID].GetStartData() != -1 && sd[ID].GetLastData() != -1) {
	    	        			 System.out.println("대:"+r[ID].ptFlag+", "+sd[ID].GetStartData() +", "+sd[ID].GetLastData());
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
						if(r[ID].carFlag == 1 && sd[ID].GetStartData() != -1 && sd[ID].GetLastData() != -1) {
	        				System.out.println("자동차While:"+r[ID].carFlag);
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
            String result10 = ad[ID].callAllLatLng();
            System.out.print(result10);
            out.print(result10);
            break;
            
         case 15:             
           if(apiFlag[ID]) {
              int a = Integer.parseInt(request.getParameter("a"));
              int b = Integer.parseInt(request.getParameter("b"));
              String car = request.getParameter("carBlock");
              apiFlag[ID] = r[ID].callAPIData(a, b, car, ad[ID], sd[ID]);      
           }
            break;
            
         case 16:  //대중교통 dfs or 마커 결과 재호출 
           int how = Integer.parseInt(request.getParameter("how"));
           System.out.println("16번 연결");            
           r[ID].callShortestPath(ad[ID], sd[ID].GetStartData(),sd[ID].GetLastData(), sd[ID].isSame(), how); // 자동차 1, 대중교통  0           
           break;
         
         case 17:  //마크를 위한 호출
           int how2 = Integer.parseInt(request.getParameter("how")); 
           System.out.println("17번 연결");           
           String result11 = r[ID].orderResult(how2, ad[ID]);
           out.print(result11);
           break;
         
         case 18: //폴리라인 그리기 위한 latlng 데이터 호출
           int how3 =  Integer.parseInt(request.getParameter("how"));
           out.print(r[ID].resultPoly(how3));
           break;
           
         case 19: //사용자가 선택한 저장된 DB데이터를 불러오고 list에 있는데이터 바꾸기
        	 String rID = request.getParameter("rID");   
        	 String cID2 = request.getParameter("cID");  
        	 ad[ID].callSaveDBData(rID, cID2);
        	 out.print("1");
        	 break;
        	 
         case 20: // 대중교통 left 에 뿌려줌
        	 int how4 = Integer.parseInt(request.getParameter("how"));
        	 System.out.println("서블렛 20번 들어옴");
         	out.print(r[ID].resultList(how4, ad[ID], sd[ID], r[ID]));
         	break; 
         
         
      }               
   }
}