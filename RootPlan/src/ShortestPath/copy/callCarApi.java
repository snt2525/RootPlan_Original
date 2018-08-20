package ShortestPath.copy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import RouteData.CalculateDist;

public class callCarApi {
	StringBuilder sb;
	String key = "9974a775-4c3d-48f1-8df7-650b3f2debfc";
	callWalkApi wa;
	//LinkedList<InfoCar> carList = new LinkedList<>(); // 이거는 carApi를 call하는 객체에서 만들기

	public callCarApi() {
		this.wa = new callWalkApi();
	}
	
	public InfoCar carApi(double sx, double sy, double ex, double ey) {
		InfoCar carData = new InfoCar();
		double distanceMeter = CalculateDist.distance(sx, sy, ex, ey, "meter");
		if (distanceMeter <= 800) {
			carData = wa.walkCarApi(sx, sy, ex, ey);
		} else {
			try {
				String apiURL = "https://api2.sktelecom.com/tmap/routes?version=1&format=xml&startX="
						+ Double.toString(sx) + "&startY=" + Double.toString(sy) + "&endX=" + Double.toString(ex)
						+ "&endY=" + Double.toString(ey);
				URL url = new URL(apiURL);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();

				con.setRequestMethod("POST");
				con.setRequestProperty("appKey", key);
				con.setDoOutput(true);

				int responseCode = con.getResponseCode();
				BufferedReader br;
				if (responseCode == 200) {
					br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				} else {
					br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					System.out.println("d실패");
				}
				sb = new StringBuilder();
				String line;

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}

				System.out.println("자동차 불러옴");
				br.close();
				con.disconnect();
				String data = sb.toString();
				// System.out.println(data);

				String[] array;
				array = data.split("<|>");

				for (int i = 0; i < array.length; i++) {
					if (array[i].equals("tmap:totalDistance")) {
						carData.setSx(sx);
						carData.setSy(sy);
						carData.setEx(ex);
						carData.setEy(ey);
						carData.setDistance(Integer.parseInt(array[i + 1]));
					} else if (array[i].equals("tmap:totalTime")) {
						carData.setTime(Integer.parseInt(array[i + 1]));
					} else if (array[i].equals("tmap:totalFare")) {
						carData.setFare(Integer.parseInt(array[i + 1]));
					} else if (array[i].equals("coordinates")) {
						if (array[i - 2].equals("Point"))
							continue;
						String[] temp = array[i + 1].split("\\s+|,");
						for (int j = 0; j < temp.length; j += 2) {
							carData.addLineList(new DataPair(Double.parseDouble(temp[j]), Double.parseDouble(temp[j + 1])));
						}
					}
				}

				//carData.print();
			} catch (Exception e) {

			}
		}
		return carData;
	}

}
