package ShortestPath.copy;

import MapData.AddressDataManager;
import RouteData.ApiCarSearch;
import RouteData.ApiPTSearch;
import RouteData.TimeMethod;

public class Route {
	public static TimeMethod[][] carDist; // 자동차 최단 거리 저장
	public static TimeMethod[][] ptDist; // 대중교통 최단 거리 저장
    static ApiPTSearch pt;
    static ApiCarSearch cs;
    
	public static boolean callApi(int a, int b, String car, AddressDataManager ad) {		
        pt = new ApiPTSearch(ad.getList());
        
      //대중교통  API 호출 & 동시에 걷기도 호출해서 이차원배열 채우기
        System.out.println("대중교통 호출");
        pt.callTransportApi(a, b); 
        
        
        if(car.equals("0")) {
        	System.out.println("자동차호출");
            cs = new ApiCarSearch(ad.getList());
            cs.carApi(); //자동차 API call 
            return false;
        }
        return true;
	}	
	
	
	
}
