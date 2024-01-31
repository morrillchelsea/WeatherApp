package weatherapp.test;

public class WeatherData {
    //private float latitude; // latitude of location
    //private float longitude; // longitude of location
    //private String cityName; // name of city
    private double humidity; // % humidity
    private double temp; // current temp in fahrenheit
    //private int zipCode; // zip code of weather location
    private String mainWeather; // description of current weather conditions (e.g. "Clear", "Rain", etc.)
    // private int feelsLike;

    // Constructor
    public WeatherData(String mainWeather, double temp, double humidity){
        this.mainWeather = mainWeather;
        this.temp = temp;
        this.humidity = humidity;
        outputWeather();
    } // end WeatherData

    // Getter Methods
    public String getMainWeather() {
        return this.mainWeather;
    } // end getMainWeather

    public double getTemp() {
        return this.temp;
    } // end getTemp

    public double getHumidity() {
        return this.humidity;
    } // end getHumidity

    public void outputWeather() {
        System.out.println("Current Weather : " + getMainWeather());
        System.out.println("Temperature (Fahrenheit): " + getTemp());
        System.out.println("Humidity %: " + getHumidity());
        System.out.println();
    }
} // end class WeatherData
