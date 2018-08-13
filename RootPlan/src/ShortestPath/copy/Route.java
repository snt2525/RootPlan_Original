package ShortestPath.copy;

import MapData.AddressDataManager;
import RouteData.ApiCarSearch;
import RouteData.ApiPTSearch;

public class Route {
	static int[][] dist;
    static ApiPTSearch pt;
    static ApiCarSearch cs;
    
	public static boolean callApi(int a, int b, String car, AddressDataManager ad) {		
        pt = new ApiPTSearch(ad.getList());
        pt.callTransportApi(a, b); //대중교통  API call       
        if(car.equals("0")) {
        	System.out.println("자동차호출");
            cs = new ApiCarSearch(ad.getList());
            cs.carApi(); //자동차 API call 
            return false;
        }
        return true;
	}	
}
