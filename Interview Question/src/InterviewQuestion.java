import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class InterviewQuestion {
//  	Using OpenWeather API for weather data
	private static final String APIKEY = "2f0d4394e775ce0fd6a681968bce8d0b";
	public static void main(String[] args) {
		HashMap<String, Double> geoResultMap = getGeoResult();
		JSONParser parser = new JSONParser();
		String WeatherResultStr = "";
	    try {
	    	double lat = geoResultMap.get("lat");
	    	double lon = geoResultMap.get("lon");
			String weatherAPIUrl = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&units=metric&appid="
					+APIKEY;
			URL url = new URL(weatherAPIUrl);
			HttpURLConnection conn = null;		
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responsecode = conn.getResponseCode();
			if (responsecode != 200) {
				    throw new RuntimeException("HttpResponseCode: " + responsecode);
				} else {			  
				    
				    Scanner scanner = new Scanner(url.openStream());
				    while (scanner.hasNext()) {
				    	WeatherResultStr += scanner.nextLine();
				    }
				    scanner.close();
//				    System.out.println(WeatherResultStr);
				    parser = new JSONParser();
				}
			JSONObject weatherData = (JSONObject)parser.parse(WeatherResultStr);
			double temperature = (double) ((JSONObject)weatherData.get("current")).get("temp");
			System.out.println("Current outside temperature: "+ temperature+"\u00B0"+"C");	
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Function for getting Geo Data
	public static HashMap<String, Double> getGeoResult() {
		HashMap<String, Double> result = new HashMap<String, Double>();
		Scanner input = new Scanner(System.in);
		System.out.print("City:");
		String city = input.nextLine();
		System.out.print("State:");
		String state = input.nextLine();
		System.out.print("Zip Code:");
		int zipCode = input.nextInt();
		String urlStr = "http://api.openweathermap.org/geo/1.0/direct?q="
						+city+","+state+","+zipCode+"&limit=1&appid="+APIKEY;
//		System.out.println(urlStr);
		try {
			//Consturct url for API for geo location
			URL url = new URL(urlStr);
			HttpURLConnection conn = null;		
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responsecode = conn.getResponseCode();
			if (responsecode != 200) {
			    	throw new RuntimeException("HttpResponseCode: " + responsecode);
				} else {			  
				    String inline = "";
				    Scanner scanner = new Scanner(url.openStream());
				    while (scanner.hasNext()) {
				       inline += scanner.nextLine();
				    }
				    scanner.close();
//				    System.out.println(inline);
				    JSONParser parser = new JSONParser();
				    JSONArray dataObject = (JSONArray)parser.parse(inline);
					JSONObject cityData = (JSONObject) dataObject.get(0);
					Double lat = (Double) cityData.get("lat");
					Double lon = (Double) cityData.get("lon");
//					System.out.println("Cordinate: "+lat+","+lon);
					result.put("lat", lat);
					result.put("lon", lon);		    
				}
			conn.disconnect();
			} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		return result;
	}
}
