package weatherapp.test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;

public class WeatherApp {
    public static void main(String[] args) {
        String stars = "************";
        System.out.println();
        System.out.println(stars + " Welcome to the Nieves Weather Application! " + stars);
        System.out.println("This application provides current weather details for your location.");
        
        APIHelper ah = new APIHelper();
        ah.getZipCode();
    } // end 
} // end class main
class APIHelper {

    private HttpURLConnection fetchApiResponse(String urlString){
        try{
            // attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod("GET");

            // connect to our API
            conn.connect();
            return conn;
        }catch(IOException e){
            e.printStackTrace();
        } // end try/cacth

        // could not make connection
        return null;
    } // end fetchApiResponse

    public String getLocationData(double lon, double lat) {
        // build API url with zipcode parameter
        String urlString = "http://api.openweathermap.org/geo/1.0/reverse?lat=" + lat + "&lon=" +lon + "&limit=1&appid=b60d7b0a5ce799edc23dd07e098249a4";
        String cityName;
        
        try{
            // call api and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check response status
            // 200 means successful connection
            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
            }else{
                // store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                // read and store the resulting json data into our string builder
                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }

                // close scanner
                scanner.close();

                // close url connection
                conn.disconnect();

                // parse through data
                JSONParser parser = new JSONParser();
                // parse input stream into JSON object
                JSONArray resultJsonObj = (JSONArray) parser.parse(String.valueOf(resultJson));
                JSONObject locObject = (JSONObject) resultJsonObj.get(0);
                cityName = (String) locObject.get("name");

                return cityName;
            } // end if/else
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("API Error: " + e);
        } // end try/catch
        return null;
    }  // end getLocationData

    public void getWeatherData(int zipCode) {
        // build API url with zipcode parameter
        String urlString = "https://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + ",us&appid=b60d7b0a5ce799edc23dd07e098249a4&units=imperial";
        
        try{
            // call api and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check response status
            // 200 means successful connection
            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
            }else{
                // store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                // read and store the resulting json data into our string builder
                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }
                // close scanner
                scanner.close();

                // close url connection
                conn.disconnect();

                // parse through data
                JSONParser parser = new JSONParser();
                // parse input stream into JSON object
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
                // get main object
                JSONObject mainObject = (JSONObject) resultJsonObj.get("main");
                JSONArray weatherObject = (JSONArray) resultJsonObj.get("weather");
                JSONObject weatherData = (JSONObject) weatherObject.get(0);
                JSONObject locationObject = (JSONObject) resultJsonObj.get("coord");
                
                // assign response to var
                String mainWeather = (String) weatherData.get("main");
                double temp = (double) mainObject.get("temp");
                long humidity = (long) mainObject.get("humidity");
                double longitude = (double) locationObject.get("lon");
                double latitude = (double) locationObject.get("lat");

                String cityName = getLocationData(longitude, latitude);
                System.out.println("\nGetting current weather conditions for: " +
                    cityName + " (" + zipCode + ")" + ".....\n");
                WeatherData wd = new WeatherData(mainWeather, temp, humidity);

            } // end if/else
        } catch (Exception e) {
            System.out.println("API Error: " + e);
        } // end try/catch   
    }  // end getResponse

    public boolean validateZipCode(int userZip) {
        int zipLength = String.valueOf(userZip).length();

        while (true) {
            if (zipLength == 5) {
                return true;
            } else {
                System.out.println("Invalid Zip Code! Please try again.");
                break;
            }
        }
        return false;
    } // end validateZipCode

    public void getZipCode() {
        // create a scanner object
        Scanner scanInt = new Scanner (System.in); 
        int userZip;
        
        while (true) {
            try {
                System.out.print("\nEnter Zip Code: ");
                userZip = scanInt.nextInt();
                if (validateZipCode(userZip)) {
                    getWeatherData(userZip);
                    scanInt.close();
                    break;
                } // end if
            } catch (InputMismatchException a) {
                System.out.print("Invalid Zip Code! Please try again.");
                // clear input stream
                scanInt.nextLine();
                continue; 
                //System.out.println(a);
            } // end try/catch
        } // end while
    } // end getZipCode()
} // end class
