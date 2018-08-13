package RouteData;

public class TimeMethod {
	public static int time;
	public static boolean method;  //true가 걷기 api
	public TimeMethod(int t, boolean m){
		this.time = t;
		this.method = m;
	}
	public static int getTime() {
		return time;
	}
	public static void setTime(int time) {
		TimeMethod.time = time;
	}
	public static boolean isMethod() {
		return method;
	}
	public static void setMethod(boolean method) {
		TimeMethod.method = method;
	}
	
}
