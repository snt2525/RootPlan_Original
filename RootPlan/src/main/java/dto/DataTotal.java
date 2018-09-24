package dto;

import java.util.LinkedList;

public class DataTotal {
	public TimeMethod[][] carDist; // 자동차 최단 거리 저장
    public TimeMethod[][] ptDist; // 대중교통 최단 거리 저장
    public int carAns[];
    public int ptAns[];
    public LinkedList<InfoCar> carList;
    public LinkedList<InfoPT> ptList;
    int listSize;
    
    public DataTotal(int listSize) {
    	listSize++; // 사이클이 생성될 경우를 대비
    	this.listSize = listSize;
    	carDist = new TimeMethod[listSize][listSize];
        ptDist = new TimeMethod[listSize][listSize];  
        carAns = new int[listSize];
        ptAns = new int[listSize];
        carList = new LinkedList<InfoCar>();
        ptList = new LinkedList<InfoPT>();
    }
    
    public void initPtDist() {
    	 for (int i = 0; i < listSize; i++) {
             for (int j = 0; j < listSize; j++) {
            	 ptDist[i][j] = new TimeMethod(Integer.MAX_VALUE, false);
             }
          }
    }
    
    public void initCarDist() {
    	 for(int i=0; i<listSize; i++) {
  		   for(int j=0; j<listSize; j++) {
  			   carDist[i][j] = new TimeMethod(Integer.MAX_VALUE,false);
  		   }
  	   }
    }  
}
