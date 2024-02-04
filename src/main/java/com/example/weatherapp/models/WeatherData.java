package com.example.weatherapp.models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherData {
    private String cityName; // city name assoc w provided zip code
    private String conditions; // main weather conditions (i.e. rain, clear, etc.)
    private String icon; // string variable to hold icon code for html styling
    private double temp; // in fahrenheit
    private double humidity; // % humidity
    private double lo; // min temp of the day in fahrenheit
    private double hi; // max temp of the day in fahrenheit

    public WeatherData(String cityName, String conditions, String icon, double temp, double humidity, double lo, double hi) {
        this.cityName = cityName;
        this.conditions = conditions;
        this.icon = icon;
        this.temp = temp;
        this.humidity = humidity;
        this.lo = lo;
        this.hi = hi;
    } //end constructor
    
    // Getter methods
    //public int getZipCode() {
      //  return;
    //}

    public String getCityName() {
        return this.cityName;
    }
    
    public String getConditions() {
        return this.conditions;
    }
    
    public double getTemp() {
        return this.temp;
    }
    
    public double getHumidity() {
        return this.humidity;
    }

    public String getIcon() {
        return this.icon;
    } // end getIcon

    public double getLo(){
        return this.lo;
    }

    public double getHi(){
        return this.hi;
    }

    // Setter methods
    //public void setZipCode(int zipCode) {
    //    this.zipCode = zipCode;
    //}

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
    
    public void setTemp(double temp) {
        this.temp = temp;
    }
    
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    } // end setIcon

    public void setLo(double lo) {
        this.lo = lo;
    }

    public void setHi(double hi) {
        this.hi = hi;
    }

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
            //return null;
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
                String icon = (String) weatherData.get("icon");
                double temp = (double) mainObject.get("temp");
                long humidity = (long) mainObject.get("humidity");
                double hi = (double) mainObject.get("temp_max");
                double lo = (double) mainObject.get("temp_min");
                double longitude = (double) locationObject.get("lon");
                double latitude = (double) locationObject.get("lat");
                setCityName(getLocationData(longitude, latitude));
                setConditions(mainWeather);
                setIcon(icon);
                setTemp(temp);
                setHumidity(humidity);
                setHi(hi);
                setLo(lo);
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
} // end class
