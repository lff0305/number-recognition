package bp.test.stock;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class StockGrabber {
	public static void main(String[] argu) throws IOException {
		Date d = new Date();
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		System.out.println(year + " " + month + " " + day);
		
		for (int i=1; i<10; i++) {
			
			System.out.println("=== Page " + i + " ===");
			
			String urlString  = "http://finance.cn.yahoo.com/mark/agents/yahoo/historyDetails.php?start_year=2000&start_month=1&start_day=1&end_year=" + year + "&end_month=" + month + "&end_day=" + day + "&his_type=day&code=sh000001";
		// 	page 2 :          http://finance.cn.yahoo.com/mark/agents/yahoo/historyDetails.php?start_year=2000&start_month=1&start_day=1&end_year=2011&end_month=4&end_day=22&his_type=day&code=sh000001&page=2
			if (i != 1) {
				grab(urlString + "&page=" + i);
			} else {
				grab(urlString);
			}
		}
		
		System.out.println("=== Finished. ===");
	}

	private static void grab(String urlString) throws MalformedURLException, IOException {
				URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.connect();
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		boolean f = false;
		while ((inputLine = in.readLine()) != null) {
			inputLine = inputLine.trim();
			if (inputLine.startsWith("<tbody>")) {
				f = true;
				continue;
			}
			if (inputLine.startsWith("</table>")) {
				f = false;
				continue;
			}
			if (f) {
				sb.append(inputLine);
				sb.append("\n");
			}

		}
		process(sb.toString());
	}

	private static void process(String data) {
		data = data.replaceAll("</tbody>", "");
		data = data.replaceAll("<tr class=\"hover\">", "<tr>");
		data = data.replaceAll("</tr>", "");
		data = data.replaceAll("</td>", "");
		String[] day = data.split("<tr>");
		for (int i=0; i<day.length; i++) {
			System.out.println(day[i]);
		}
	}
}
