package ShortestPath;

import MapData.AddressDataManager;
import RouteData.ApiCarSearch;
import RouteData.ApiPTSearch;
import RouteData.TimeMethod;
import address.Data.AddressDataServlet;

public class Route {
    static ApiPTSearch pt;
    static ApiCarSearch cs;
    static Shortpath sp;
    public static TimeMethod[][] carDist; // 자동차 최단 거리 저장
    public static TimeMethod[][] ptDist; // 대중교통 최단 거리 저장
    public int carFlag = 0;
    public int ptFlag = 0;
    public int size = 0;
    public Route(){
       sp = new Shortpath();
       carDist = new TimeMethod[7][7];
       ptDist = new TimeMethod[7][7];       
    }
    
   public boolean callApi(int a, int b, String car, AddressDataManager ad, SetData sd) {     
	   size = ad.addressData.size();
        pt = new ApiPTSearch(ad.getList());
      //대중교통  API 호출 & 동시에 걷기도 호출해서 이차원배열 채우기
        System.out.println("대중교통 호출");
        pt.callTransportApi(a, b);                
        if(car.equals("0")) {      
           ptFlag = 1;
           sp.init(ad.addressData.size());
           //자동차 api호출
            System.out.println("자동차호출");
            cs = new ApiCarSearch(ad.getList());
            cs.carApi(); //자동차 API call 
            
           //callShortestPath(ad, sd.startIndex,sd.lastIndex, sd.isSame(), 1); //dfs 순서를 찾는다 , 자동차
           // cs.resultOrderCall(sp.carAns); //결과 순서로 api 다시 호출, 자동차
            carFlag = 1; //자동차 호출 끌
            return false;
        }
        return true;
	}	
/*	
	// 알고리즘 구현
=======
   }   
   
   // 알고리즘 구현
>>>>>>> branch 'master' of https://github.com/snt2525/RootPlan.git

<<<<<<< HEAD
	void print(int size) {
		System.out.print("자동차 : ");
		for(int i=0; i<size; i++) {
			System.out.print(Shortpath.carAns[i]+" ");
		}
		System.out.println();
		System.out.print("대중교통 : ");
		for(int i=0; i<size; i++) {
			System.out.print(Shortpath.ptAns[i]+" ");
		}
		System.out.println();
	}*/
   
   void print(int size) {
      System.out.print("자동차 : ");
      for(int i=0; i<size; i++) {
         System.out.print(Shortpath.carAns[i]+" ");
      }
      System.out.println();
      System.out.print("대중교통 : ");
      for(int i=0; i<size; i++) {
         System.out.print(Shortpath.ptAns[i]+" ");
      }
      System.out.println();
   }

   public static void callShortestPath(AddressDataManager ad,int start, int last, int isSame, int how) { 
   System.out.println("start , end = " + start + " , " + last);
      if(how == 1) {
         sp.callDFS(start, last, 1, isSame);
      	 cs.resultOrderCall(sp.carAns); //결과 순서로 api 다시 호출, 자동차
      }else { 
         sp.callDFS(start, last, 0, isSame);
         pt.resultOrderCall(sp.ptAns); //결과 순서로 api 다시 호출, 대중교통
      }
   } 
   
   public String orderResult(int how, AddressDataManager ad) { //결과 데이터 보내기 
	   String  result = "";
	   if(how == 0) { 
		   result += "<ResultData>";
		   for(int i = 0;i<size;i++) {
			  result += "<Data>"; 
		      result += "<no>"+i+"</no>";
		      result += "<lat>"+ Double.toString(ad.addressData.get(sp.ptAns[i]).getLat()) +"</lat>";
		      result += "<lng>"+ Double.toString(ad.addressData.get(sp.ptAns[i]).getLng()) +"</lng>";
		      result += "</Data>"; 
		   }
		   result += "</ResultData>";
		   System.out.println(result);
	   }else if(how == 1) {
		   result += "<ResultData>";
		   for(int i = 0;i<size;i++) {
			  result += "<Data>"; 
		      result += "<no>"+i+"</no>";
		      result += "<lat>"+ Double.toString(ad.addressData.get(sp.carAns[i]).getLat()) +"</lat>";
		      result += "<lng>"+ Double.toString(ad.addressData.get(sp.carAns[i]).getLng()) +"</lng>";
		      result += "</Data>"; 
		   }
		   result += "</ResultData>";		   
	   }
	   return result;
   }
}