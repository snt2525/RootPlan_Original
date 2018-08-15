package ShortestPath.copy;

import MapData.AddressDataManager;
import RouteData.ApiCarSearch;
import RouteData.ApiPTSearch;
import RouteData.TimeMethod;

public class Route {
    ApiPTSearch pt;
    ApiCarSearch cs;
    Shortpath sp;
	public static TimeMethod[][] carDist; // 자동차 최단 거리 저장
	public static TimeMethod[][] ptDist; // 대중교통 최단 거리 저장
    
    public Route(){
       sp = new Shortpath();
       carDist = new TimeMethod[7][7];
       ptDist = new TimeMethod[7][7];       
    }
    
	public boolean callApi(int a, int b, String car, AddressDataManager ad) {		
        pt = new ApiPTSearch(ad.getList());
      //대중교통  API 호출 & 동시에 걷기도 호출해서 이차원배열 채우기
        System.out.println("대중교통 호출");
        pt.callTransportApi(a, b); 
               
        if(car.equals("0")) {
           sp.init(ad.listSize());
           System.out.println("자동차호출");
            cs = new ApiCarSearch(ad.getList());
            cs.carApi(); //자동차 API call 
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
	
	public void callShortestPath(AddressDataManager ad, SetData sd, int how) { 
	System.out.println("start , end = " + sd.startIndex + " , " + sd.lastIndex);
		if(how == 1)
			sp.callDFS(sd.startIndex,sd.lastIndex,1,sd.isSame());
		else 
			sp.callDFS(sd.startIndex,sd.lastIndex,0,sd.isSame());
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
}
