package dao;

import java.util.LinkedList;

import callApi.ApiCarSearch;
import callApi.ApiPTSearch;
import dto.DataPair;
import dto.InfoCar;
import dto.InfoPT;
import dto.InfoSectionPT;
import dto.SetData;
import dto.TimeMethod;

public class Route {
	ApiPTSearch pt;
    ApiCarSearch cs;
    Shortpath sp;
    public static TimeMethod[][] carDist; // 자동차 최단 거리 저장
    public static TimeMethod[][] ptDist; // 대중교통 최단 거리 저장
    public int carFlag = 0;
    public int ptFlag = 0;
    public int size = 0;
    public static LinkedList<InfoCar> carList;
    public static LinkedList<InfoPT> ptList;
    
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
        System.out.println("car : " + car);
        if(car.equals("0")) {      
           sp.init(ad.addressData.size());
           //자동차 api호출
            System.out.println("자동차호출");           
            ptFlag = 1; //대중됴통 호출 끌
            cs = new ApiCarSearch(ad.getList());
            cs.carApi(); //자동차 API call 
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
         recallApiData(1);  // 자동차 재호출
      	 //cs.resultOrderCall(sp.carAns); //결과 순서로 api 다시 호출, 자동차
      }else { 
         sp.callDFS(start, last, 0, isSame);
         recallApiData(0);  // 대중교통 재호출
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
		   for(int j=0; j<InfoPTSize; j++) {
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
	   return result;
   }   
   
   public String resultList(int how) { // 0:pt, 1:car
	   String result="";
	   
	   if(how==0) {
		   result += "<resultPTList>";
		   int sectionSize=0;
		   
		   for(int i=0; i<ptList.size(); i++) {
			   InfoPT info = ptList.get(i);
			   // 처음에 버스번호 여러개(시작 역이름)->지하철 번호(시작 역이름) -> 버스번호 여러개(시작 역이름)
			   
			   // 1번 지점에서 보일 내용 : 경로 1->2 지점에서 띄어줄 내용 
			   result += "<Data>";
			   result += "<check>1</check>";
			   if(info.isWalk()) result += "<walk>true</walk>";
			   else result += "<walk>false</walk>";
			   result += "<totalTime>" + info.getTotalTime() + "</totalTime>";
			   result += "<totalDistance>" + info.getTotalDistance() + "</totalDistance>";
			   result += "<totalFare>" + info.getFare() + "</totalFare>";
			   result += "<totalStationCount>" + info.getStationCount() + "</totalStationCount>";
			   // 각 세부의 정보 
			   sectionSize = info.getSectionSize();
			   result += "<sectionSize>"+Integer.toString(info.getSectionSize()) + "</sectionSize>";
			   result += "</Data>";
			   
			   // 2번 지점에서 보일 내용
			   for(int j=0; j<info.getSectionSize(); j++) {
				   InfoSectionPT tmpSec = info.getSection(j);
				   result += "<Data>";
				   result += "<check>2</check>";
				   if(tmpSec.getTrafficType()==1) result += "<trafficType>지하철</trafficType>";
				   else result += "<trafficType>버스</trafficType>";
				   result += "<bus>";
				   for(int k=0; k<tmpSec.getBusNoListSize(); k++) {
					   result += tmpSec.getBusNoList(k);
					   if(k!=tmpSec.getBusNoListSize()-1) result += ", ";
				   }
				   result += "</bus>";
				   result += "<subwayLine>" + tmpSec.getSubwayLine()+"</subwayLine>";
				   result +="<stationName>"+tmpSec.getStartStation()+"</stationName>"; // 처음 역 이름
				   result += "</Data>";
			   }
			   
			   if(info.isWalk()) {
				   result += "<Data>";
				   result += "<check>3</check>";
				   result += "<walk>true</walk>";
				   result += "</Data>";
			   }else {
				   for(int j=0; j<sectionSize; j++) {
					   result += "<Data>";
					   result += "<check>3</check>";
					   result += "<walk>false</walk>";
					   
					   InfoSectionPT sec = info.getSection(j);
					   if(sec.getTrafficType()==1) result += "<trafficType>지하철</trafficType>";
					   else result += "<trafficType>버스</trafficType>";
					   result += "<startStation>"+sec.getStartStation() + "</startStation>";
					   result += "<endStation>" + sec.getEndStation() + "</endStation>";
					   result += "<sectionDistance>" +Integer.toString(sec.getSectionDistance()) + "</sectionDistance>";
					   result += "<sectionTime>" + Integer.toString(sec.getSectionTime()) +"</sectionTime>";
					   if(sec.getTrafficType()==1) {
						   result += "<line>" +sec.getSubwayLine() + "</line>";
					   }else {
						   result += "<line>";
						   int busSize = sec.getBusNoListSize();
						   for(int a=0; a<busSize; a++) {
							   result += sec.getBusNoList(a);
							   if(a!=busSize-1) result += ", ";
						   }
						   result += "</line>";
					   }
					   
					   result += "</Data>";
				   }
			   }
		   }
		   result += "</resultPTList>";
	   }else {
		   result += "<resultCarList>";
		   for(int i=0; i<carList.size(); i++) {
			   result += "<Data>";
			   InfoCar info = carList.get(i);
			   result += "<distance>" + Integer.toString(info.getDistance())+"</distance>";
			   result += "<walk>" + info.isWalk() + "</walk>";
			   result += "<time>" + Integer.toString(info.getTime()) + "</time>";
			   if(!info.isWalk()) result += "<fare>" + Integer.toString(info.getFare()) + "</fare>";
			   result += "</Data>";
		   }
		   result += "</resultCarList>";
	   }
	   return result;
   }
}