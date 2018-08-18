package ShortestPath.copy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class callWalkApi {
	StringBuilder sb;
	String key = "9974a775-4c3d-48f1-8df7-650b3f2debfc";

	// 대중교통 걷기 전용 만들어줘야함 안그러면 상속써야됨..
	
	public InfoCar walkCarApi(double sx, double sy, double ex, double ey) { // 자동차 걷기 전용
		InfoCar walkData = new InfoCar(); // car class 재활용
		try {
			String apiURL = "https://api2.sktelecom.com/tmap/routes/pedestrian?version=1&format=xml&startX="
					+ Double.toString(sx) + "&startY=" + Double.toString(sy) + "&endX=" + Double.toString(ex) + "&endY="
					+ Double.toString(ey) + "&startName=start&endName=end";
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

			System.out.println("걷기 불러옴");
			br.close();
			con.disconnect();
			String data = sb.toString();
			System.out.println(data);

			String[] array;
			array = data.split("<|>");

			for (int i = 0; i < array.length; i++) {
				if (array[i].equals("tmap:totalDistance")) {
					walkData.setSx(sx);
					walkData.setSy(sy);
					walkData.setEx(ex);
					walkData.setEy(ey);
					walkData.setDistance(Integer.parseInt(array[i + 1]));
				} else if (array[i].equals("tmap:totalTime")) {
					walkData.setTime(Integer.parseInt(array[i + 1]));
				} else if (array[i].equals("coordinates")) {
					if (array[i - 2].equals("Point"))
						continue;
					String[] temp = array[i + 1].split("\\s+|,");
					for (int j = 0; j < temp.length; j += 2) {
						walkData.addLineList(
								new DataPair(Double.parseDouble(temp[j]), Double.parseDouble(temp[j + 1])));
					}
				}
			}

			walkData.print();
		} catch (Exception e) {

		}
		return walkData;
	}
}
