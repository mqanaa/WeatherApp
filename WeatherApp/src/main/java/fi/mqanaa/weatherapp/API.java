package fi.mqanaa.weatherapp;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.net.MalformedURLException;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * Class used to make API calls to various OpenWeatherMaps APIs
 * 
 */

public class API implements iAPI {
    
    private String currentLocation;
    private static String API_KEY;
    
    static {
        try {
            // Load the API key from the config file
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));
            API_KEY = properties.getProperty("api.key");

            if (API_KEY == null || API_KEY.isEmpty()) {
                throw new RuntimeException("API key is missing in config.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load API key from config.properties", e);
        }
    }
    
    /**
     * Returns the current weather for the given location.
     * @param loc The name of the location.
     * @param unitCode The currently selected unit system, "C" or "F".
     * @throws Exception if weather data can't be read or location is not found.
     * @return String (JSON) containing current weather data in target location.
     */
    @Override
    public String getCurrentWeather(String loc, String unitCode) throws 
                                                                Exception {
        
        String unit;
        if ("C".equals(unitCode)) {
            unit = "metric";
        } else {
            unit = "imperial";
        }
        
        String[] coordinates = lookUpLocation(loc);
        String lat = coordinates[0];
        String lon = coordinates[1];
        
        String urlString = "https://api.openweathermap.org/data/2.5/weather?"
                            + "lat=" + lat
                            + "&lon=" + lon 
                            + "&appid=" + API_KEY
                            + "&units=" + unit;
        
        JsonObject jsonObject = makeAPICall(urlString);
        
        if ("0.0".equals(lat) && "0.0".equals(lon)) {
            throw new Exception ("Failed to find location");
            
        } else if (jsonObject != null) { // Success

            // Parse the JSON response
            String currentWeatherData = new Gson().toJson(jsonObject);
            return currentWeatherData;
            
        } else {
            throw new Exception ("Failed to read weather data AA");
        }
        
        
    }
    
    /**
     * Returns the hourly forecast (next four days) for the given location.
     * @param loc The name of the location.
     * @param unitCode The currently selected unit system, "C" or "F".
     * @throws Exception if weather data can't be read or location is not found.
     * @return String (JSON) containing hourly forecast data in target location.
     */
    @Override
    public String getForecast(String loc, String unitCode) throws 
                                                        Exception {
        
        String unit;
        if ("C".equals(unitCode)) {
            unit = "metric";
        } else {
            unit = "imperial";
        }
        
        String[] coordinates = lookUpLocation(loc);
        String lat = coordinates[0];
        String lon = coordinates[1];
        
        String urlString = 
                "https://pro.openweathermap.org/data/2.5/forecast/hourly?"
                + "lat=" + lat
                + "&lon=" + lon
                + "&appid=" + API_KEY
                + "&units=" + unit;
        
        JsonObject jsonObject = makeAPICall(urlString);
        
        if ("0.0".equals(lat) && "0.0".equals(lon)) {
            throw new Exception ("Failed to find location");
            
        } else if (jsonObject != null) {
            // Parse the JSON response
            String forecastWeatherData = new Gson().toJson(jsonObject);
            return forecastWeatherData;
            
        } else {
            throw new Exception ("Failed to read weather data BB");
        }
    }
    
    /**
     * Returns the daily forecast (next four days) for the given location.
     * @param loc The name of the location.
     * @param unitCode The currently selected unit system, "C" or "F".
     * @throws Exception if weather data can't be read or location is not found.
     * @return String (JSON) containing daily forecast data in target location.
     */
    @Override
    public String getDailyData(String loc, String unitCode) throws 
                                                            Exception {
        
        String unit;
        if ("C".equals(unitCode)) {
            unit = "metric";
        } else {
            unit = "imperial";
        }
         
        String[] coordinates = lookUpLocation(loc);
        String lat = coordinates[0];
        String lon = coordinates[1];
         
        String urlString = 
            "https://pro.openweathermap.org/data/2.5/forecast/daily?"
            + "lat=" + lat
            + "&lon=" + lon
            + "&cnt=5"
            + "&appid=" + API_KEY
            + "&units=" + unit;
         
        JsonObject jsonObject = makeAPICall(urlString);
        
        if ("0.0".equals(lat) && "0.0".equals(lon)) {
            throw new Exception ("Failed to find location");
            
        } else if (jsonObject != null) {
            // Parse the JSON response
            String forecastWeatherData = new Gson().toJson(jsonObject);
            return forecastWeatherData;
            
        } else {
            throw new Exception ("Failed to read weather data CC");
        }
    }
    
    /**
     * Helper function used by the other functions
     * to actually make the API calls.
     * @param urlString The URL in string form, created by the other methods.
     * @return The full API call as a JsonElement, or null if an error occurs.
     */
    @Override
    public JsonObject makeAPICall(String urlString) {
        
        // Create URL object from the URL String
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            return null;
        }

        int responseCode;
        HttpURLConnection connection;
        try {
            
            // Open the connection and set request method
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            responseCode = connection.getResponseCode();

        } catch (IOException e) {
            return null;
        }

        // Check the response code
        if (responseCode == HttpURLConnection.HTTP_OK) {
            JsonObject jsonObject;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                
                // Parse the JSON response
                JsonElement rootElement = JsonParser.parseString(response.toString());
                if (rootElement.isJsonObject()) {
                    jsonObject = rootElement.getAsJsonObject();
                } else {
                    JsonArray jsonArray = rootElement.getAsJsonArray();
                    jsonObject = jsonArray.get(0).getAsJsonObject();
                }
                
                return jsonObject;
                
            } catch ( JsonSyntaxException 
                    | IndexOutOfBoundsException 
                    | IOException e) {
                
                return null; 
            }
            
        } else {
            return null; // Return null to indicate failure
        }
    }
    
    /**
     * Helper method used to fetch coordinates for a location.
     * @param loc Name of the location for which coordinates should be fetched.
     * @return double[] containing latitude and longitude at 0 and 1 respectively
     */
    @Override
    public String[] lookUpLocation(String loc) {
        
        currentLocation = "";
        
        String urlString = "http://api.openweathermap.org/geo/1.0/direct?q="
                            + loc + "&limit=1&appid=" + API_KEY;
                
        JsonObject jsonObject = makeAPICall(urlString);
        
        String latitude;
        String longitude;
        String[] coordinates = new String[2];
        
        if (jsonObject != null) {

            // Extract the "lat" and "lon" values
            latitude = jsonObject.get("lat").getAsString();
            longitude = jsonObject.get("lon").getAsString();
            currentLocation = jsonObject.get("name").getAsString();
            
        } else {
            
            latitude = "0.0";
            longitude = "0.0";
            
        }
        
        coordinates[0] = latitude;
        coordinates[1] = longitude;
        return coordinates;
    }
    
   /**
    * Getter function used by other classes to get location names given by API
    * @return String of the location last searched using the API
    */
    public String getCurrentLocationName() {
        return currentLocation;
    }
}
