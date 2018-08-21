package ShortestPath;

import java.util.LinkedList;

import MapData.AddressDataManager;
import RouteData.ApiCarSearch;
import RouteData.ApiPTSearch;
import RouteData.DataPair;
import RouteData.InfoCar;
import RouteData.InfoPT;
import RouteData.TimeMethod;

public class Route {
    ApiPTSearch pt;
    ApiCarSearch cs;
    Shortpath sp;
    public static TimeMethod[][] carDist; // 자동차 최단 거리 저장
    public static TimeMethod[][] ptDist; // 대중교통 최단 거리 저장
    public static LinkedList<InfoCar> carList;
    public static LinkedList<InfoPT> ptList;
    public int carFlag = 0;
    public int ptFlag = 0;
    public int size = 0;
    
    public Route(){
       sp = new Shortpath();
       carList = new LinkedList<InfoCar>();
       ptList = new LinkedList<InfoPT>();
       carDist = new TimeMethod[7][7];
       ptDist = new TimeMethod[7][7];      
    }
    
    public void Clear() {
        carList.clear();
        ptList.clear();
        carFlag = 0;
        ptFlag = 0;        
    }
    
   public boolean callApi(int a, int b, String car, AddressDataManager ad, SetData sd) {     
	   size = ad.addressData.size();
       pt = new ApiPTSearch(ad.getList());
      //대중교통  API 호출 & 동시에 걷기도 호출해서 이차원배열 채우기
        System.out.println("대중교통 호출");
        pt.callTransportApi(a, b);                
        if(car.equals("0")) {      
           sp.init(ad.addressData.size());
           //자동차 api호출
            System.out.println("자동차호출");
            recallApiData(0);  // 대중교통 재호출
            ptFlag = 1; //대중됴통 호출 끌
            cs = new ApiCarSearch(ad.getList());
            cs.carApi(); //자동차 API call 
            recallApiData(1); //대중교통 재호출
 		    carFlag = 1; //자동차 호출 끌 
            return false;
        }
        return true;
	}	
   
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

   public void recallApiData(int how) {
	   if(how == 0)
		   pt.resultOrderCall(sp.ptAns);
	   else
		  cs.resultOrderCall(sp.carAns);
	   
   }
   
   public void callShortestPath(AddressDataManager ad,int start, int last, int isSame, int how) { 
      if(how == 1) {
         sp.callDFS(start, last, 1, isSame);
      	 //cs.resultOrderCall(sp.carAns); //결과 순서로 api 다시 호출, 자동차
      }else { 
         sp.callDFS(start, last, 0, isSame);
        // pt.resultOrderCall(sp.ptAns); //결과 순서로 api 다시 호출, 대중교통
      }
   } 
   
   public String orderResult(int how, AddressDataManager ad) { //결과 데이터 보내기 
	   String  result = "";
	   if(how == 0) { 
		   result += "<ResultData>";
		   for(int i = 0;i<size;i++) {
			  result += "<Data>"; 
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
		      result += "<lat>"+ Double.toString(ad.addressData.get(sp.carAns[i]).getLat()) +"</lat>";
		      result += "<lng>"+ Double.toString(ad.addressData.get(sp.carAns[i]).getLng()) +"</lng>";
		      result += "</Data>"; 
		   }
		   result += "</ResultData>";		   
	   }
	   return result;
   }
   
   public String resultPoly(int how) { // 0:pt, 1:car
	   
	   String result ="";
	   if(how==0) {
		   result += "<ptData>";
		   for(int i=0; i<ptList.size();i++) {
			   int InfoPTSize = ptList.get(i).getLineListSize();
			   for(int j=0; j<InfoPTSize; i++) {
				   result += "<Data>";
				   DataPair pair = ptList.get(i).getLineList(j);
				   result += "<lat>" + Double.toString(pair.getX()) + "</lat>";
				   result += "<lng>" + Double.toString(pair.getY()) + "</lng>";
				   result += "</Data>";
			   }
		   }
		   result += "</ptData>";
	   }else if(how==1) {
		   result += "<carData>";
		   for(int i=0; i<carList.size(); i++) {
			   int lineListSize = carList.get(i).getLineListSize();
			   for(int j=0; j<lineListSize; j++) {
				   result += "<Data>";
				   DataPair pair = carList.get(i).getLineList(j);
				   result += "<lat>" + Double.toString(pair.getX()) + "</lat>";
				   result += "<lng>" + Double.toString(pair.getY()) + "</lng>";
				   result += "</Data>";
			   }
		   }
		   result += "</carData>";
	   }
	   System.out.println(result);
	   return result;
   }   
}