package dao;

import callApi.ApiCarSearch;
import callApi.ApiPTSearch;
import dto.DataPair;
import dto.DataTotal;
import dto.InfoCar;
import dto.InfoPT;
import dto.InfoSectionPT;
import dto.SetData;

public class Route {
	ApiPTSearch pt;
    ApiCarSearch cs;
    Shortpath sp;
    int listSize;
    public int carFlag = 0;
    public int ptFlag = 0;
    public int size = 0;
    public DataTotal dataTotal;
    
    public Route(int listSize){
    	this.listSize = listSize;
    	dataTotal = new DataTotal(listSize);
        sp = new Shortpath();
    }
    
    public void Clear() {
    	dataTotal.carList.clear();
    	dataTotal.ptList.clear();
        carFlag = 0;
        ptFlag = 0;        
    }
    
   public boolean callApi(int a, int b, String car, AddressDataManager ad, SetData sd) {     
	   //size = ad.addressData.size();
	   if(ad.sd.GetStartData() == ad.sd.GetLastData()) listSize++;
	   
       pt = new ApiPTSearch(ad.getList(), dataTotal, listSize);
      //대중교통  API 호출 & 동시에 걷기도 호출해서 이차원배열 채우기
        System.out.println("대중교통 호출");
        pt.callTransportApi(a, b);    
        //System.out.println("car : " + car);
        if(car.equals("0")) {      
           sp.init(ad.addressData.size(),dataTotal);
        	//sp = new Shortpath(ad.addressData.size(),dataTotal);
           //자동차 api호출
            System.out.println("자동차호출");           
            ptFlag = 1; //대중됴통 호출 끌
            cs = new ApiCarSearch(ad.getList(), dataTotal, listSize);
            cs.carApi(); //자동차 API call 
 		    carFlag = 1; //자동차 호출 끌 
            return false;
        }
        return true;
	}	
   
   void print(int size) {
      System.out.print("자동차 : ");
      for(int i=0; i<size; i++) {
         System.out.print(dataTotal.carAns[i]+" ");
      }
      System.out.println();
      System.out.print("대중교통 : ");
      for(int i=0; i<size; i++) {
         System.out.print(dataTotal.ptAns[i]+" ");
      }
      System.out.println();
   }

   public void recallApiData(int how, int start, int end) {
	   if(how == 0) {
		   pt.resultOrderCall(dataTotal.ptAns, start, end);
	   }
	   else {
		  cs.resultOrderCall(dataTotal.carAns, start, end);
	   }
	   
   }
   
   public void callShortestPath(AddressDataManager ad,int start, int last, int isSame, int how) { 
	   if(how == 1) {
         sp.callDFS(start, last, 1, isSame);
         recallApiData(1, start, last);  // 자동차 재호출
      	 //cs.resultOrderCall(sp.carAns); //결과 순서로 api 다시 호출, 자동차
      }else { 
         sp.callDFS(start, last, 0, isSame);
         recallApiData(0, start, last);  // 대중교통 재호출
        // pt.resultOrderCall(sp.ptAns); //결과 순서로 api 다시 호출, 대중교통
      }
   } 
   
   public String orderResult(int how, AddressDataManager ad) { //결과 데이터 보내기 
	   String  result = "";
	   if(how == 0) { 
		   result += "<ResultData>";
		   for(int i = 0;i<size;i++) {
			  result += "<Data>"; 
		      result += "<lat>"+ Double.toString(ad.addressData.get(dataTotal.ptAns[i]).getLat()) +"</lat>";
		      result += "<lng>"+ Double.toString(ad.addressData.get(dataTotal.ptAns[i]).getLng()) +"</lng>";
		      result += "</Data>"; 
		   }
		   result += "</ResultData>";
	   }else if(how == 1) {
		   result += "<ResultData>";
		   for(int i = 0;i<size;i++) {
			  result += "<Data>"; 
		      result += "<lat>"+ Double.toString(ad.addressData.get(dataTotal.carAns[i]).getLat()) +"</lat>";
		      result += "<lng>"+ Double.toString(ad.addressData.get(dataTotal.carAns[i]).getLng()) +"</lng>";
		      result += "</Data>"; 
		   }
		   result += "</ResultData>";		   
	   }
	   return result;
   }
   
   public String resultPoly(int how) { // 0:pt, 1:car
	   
	   // 그릴떄 0:도보, 1:그외
	   String result ="";
	   if(how==0) {
		   result += "<ptData>";
		   for(int i=0; i<dataTotal.ptList.size();i++) {
			   int InfoPTSize = dataTotal.ptList.get(i).getLineListSize();
			   boolean isWalk = dataTotal.ptList.get(i).isWalk();
			  
			   // 이부분 이상함, 왜그러는지 모르겠음 
			   System.out.println("sx, sy : " + Double.toString(dataTotal.ptList.get(i).getSx()) +", " + Double.toString(dataTotal.ptList.get(i).getSy()));
			   result += "<Data>";
			   if(isWalk) {
				   result += "<walk>0</walk>";
			   }else {
				   result += "<walk>1</walk>";
			   }
			   result += "<lat>" +Double.toString(dataTotal.ptList.get(i).getSx())+ "</lat>";
			   result += "</lng>" +Double.toString(dataTotal.ptList.get(i).getSy())+ "</lng>";
			   result += "</Data>";
			   
			   for(int j=0; j<InfoPTSize; j++) {
				   result += "<Data>";
				   if(isWalk) { // 도보
					   result += "<walk>0</walk>";
				   }else {
					   result += "<walk>1</walk>";
				   }
				   DataPair pair = dataTotal.ptList.get(i).getLineList(j);
				   System.out.println("x, y : " +pair.getX()  +", " + pair.getY());
				   result += "<lat>" + Double.toString(pair.getX()) + "</lat>";
				   result += "<lng>" + Double.toString(pair.getY()) + "</lng>";
				   result += "</Data>";
			   }
			   
			   if(i==dataTotal.ptList.size()-1) {
				   System.out.println("ex, ey : " + Double.toString(dataTotal.ptList.get(i).getEx()) +", " + Double.toString(dataTotal.ptList.get(i).getEy()));
				  result += "<Data>";
				  if(isWalk) {
					   result += "<walk>0</walk>";
				   }else {
					   result += "<walk>1</walk>";
				   }
				   result += "<lat>" +Double.toString(dataTotal.ptList.get(i).getEx())+ "</lat>";
				   result += "</lng>" +Double.toString(dataTotal.ptList.get(i).getEy())+ "</lng>";
				   result += "</Data>";
			   }
		   }
		   result += "</ptData>";
	   }else if(how==1) {
		   result += "<carData>";
		   for(int i=0; i<dataTotal.carList.size(); i++) {
			   int lineListSize = dataTotal.carList.get(i).getLineListSize();
			   for(int j=0; j<lineListSize; j++) {
				   result += "<Data>";
				   if(dataTotal.carList.get(i).isWalk()) { // 도보
					   result += "<walk>0</walk>";
				   }else {
					   result += "<walk>1</walk>";
				   }
				   DataPair pair = dataTotal.carList.get(i).getLineList(j);
				   result += "<lat>" + Double.toString(pair.getX()) + "</lat>";
				   result += "<lng>" + Double.toString(pair.getY()) + "</lng>";
				   result += "</Data>";
			   }
		   }
		   result += "</carData>";
	   }
	   return result;
   }   
   
   public String resultList(int how, AddressDataManager ad) { // 0:pt, 1:car
	   String result="";
	   int adSize = ad.addressData.size();
	   if(ad.sd.GetStartData() == ad.sd.GetLastData()) {
		   adSize++;
	   }
	   
	   if(how==0) {
		   result += "<resultPTList>";
		   int sectionSize=0;
		   
		   // -1번 지점 // adSize 보내기 
		   result += "<Data>";
		   result += "<check>-1</check>";
		   result += "<wayCount>"+adSize+"</wayCount>";
		   result += "</Data>";
		   
		   // 0번 지점
		   for(int k=0; k<ad.addressData.size(); k++) {
			   result += "<Data>";
			   result += "<check>0</check>";
			   result += "<title>" + ad.addressData.get(ad.r.dataTotal.ptAns[k]).getAddress() + "</title>";
			   result += "</Data>";
		   }
		   if(ad.sd.GetStartData()==ad.sd.GetLastData()) {
			   result += "<Data>";
			   result += "<check>0</check>";
			   result += "<title>" + ad.addressData.get(ad.sd.GetLastData()).getAddress() + "</title>";
			   result += "</Data>";
		   }
		   
		   System.out.println("dataTotal.ptList.size : " +dataTotal.ptList.size());
		   
		   for(int i=0; i<dataTotal.ptList.size(); i++) {
			   InfoPT info = dataTotal.ptList.get(i);
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
				   //System.out.println("trafficType은 뭐지 : " + tmpSec.getTrafficType());
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
		   
		   // -1번 지점 // adSize 보내기 
		   result += "<Data>";
		   result += "<check>-1</check>";
		   result += "<wayCount>"+adSize+"</wayCount>";
		   result += "</Data>";
		   
		   // 0번 지점
		   for(int k=0; k<ad.addressData.size(); k++) {
			   result += "<Data>";
			   result += "<check>0</check>";
			   result += "<title>" + ad.addressData.get(ad.r.dataTotal.carAns[k]).getAddress() + "</title>";
			   result += "</Data>";
		   }
		   
		   if(ad.sd.GetStartData() == ad.sd.GetLastData()) {
			   result += "<Data>";
			   result += "<check>0</check>";
			   result += "<title>" + ad.addressData.get(ad.sd.GetLastData()).getAddress() + "</title>";
			   result += "</Data>";
		   }
		   
		   for(int i=0; i<dataTotal.carList.size(); i++) {
			   result += "<Data>";
			   InfoCar info = dataTotal.carList.get(i);
			   result += "<check>1</check>";
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