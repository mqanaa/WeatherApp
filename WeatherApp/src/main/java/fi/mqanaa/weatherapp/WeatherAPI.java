package fi.mqanaa.weatherapp;

import java.io.*;
import java.net.*;
import java.util.Properties;

import com.google.gson.*;

/**
 * Class used to make WeatherAPI calls to various OpenWeatherMaps APIs.
 */
public class WeatherAPI {

    private String currentLocation;
    private static final String API_KEY;

    static {
        API_KEY = loadApiKey();
    }

    /**
     * Loads the API key from the configuration file.
     * 
     * @return the API key as a string.
     * @throws RuntimeException if the key is missing or cannot be loaded.
     */
    private static String loadApiKey() {
        try (FileInputStream input = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            String key = properties.getProperty("api.key");
            if (key == null || key.isEmpty()) {
                throw new RuntimeException("API key is missing in config.properties");
            }
            return key;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load API key from config.properties", e);
        }
    }
    
    /**
     * Gets the current weather for a given location.
     * 
     * @param location the name of the location.
     * @param unitCode the unit system, "C" for metric or "F" for imperial.
     * @return a JSON string containing the current weather data.
     * @throws Exception if weather data cannot be fetched.
     */
    public String getCurrentWeather(String location, String unitCode) throws Exception {
        return fetchWeatherData("weather", location, unitCode);
    }

    /**
     * Gets the hourly forecast for a given location.
     * 
     * @param location the name of the location.
     * @param unitCode the unit system, "C" for metric or "F" for imperial.
     * @return a JSON string containing the hourly forecast data.
     * @throws Exception if forecast data cannot be fetched.
     */
    public String getForecast(String location, String unitCode) throws Exception {
        return fetchWeatherData("forecast/hourly", location, unitCode);
    }

    /**
     * Gets the daily forecast for the next five days for a given location.
     * 
     * @param location the name of the location.
     * @param unitCode the unit system, "C" for metric or "F" for imperial.
     * @return a JSON string containing the daily forecast data.
     * @throws Exception if daily data cannot be fetched.
     */
    public String getDailyData(String location, String unitCode) throws Exception {
        return fetchWeatherData("forecast/daily", location, unitCode, "&cnt=5");
    }

    /**
     * Fetches weather data from the specified endpoint.
     * 
     * @param endpoint the API endpoint (e.g., "weather" or "forecast/daily").
     * @param location the name of the location.
     * @param unitCode the unit system, "C" for metric or "F" for imperial.
     * @param extraParams additional query parameters.
     * @return a JSON string containing the weather data.
     * @throws Exception if weather data cannot be fetched.
     */
    private String fetchWeatherData(String endpoint, String location, String unitCode, String... extraParams) throws Exception {
        String unit = "C".equals(unitCode) ? "metric" : "imperial";
        String[] coordinates = lookUpLocation(location);
        validateCoordinates(coordinates);

        String urlString = String.format(
            "https://api.openweathermap.org/data/2.5/%s?lat=%s&lon=%s&appid=%s&units=%s%s",
            endpoint, coordinates[0], coordinates[1], API_KEY, unit, String.join("", extraParams)
        );

        JsonObject jsonObject = makeAPICall(urlString);
        if (jsonObject != null) {
            return new Gson().toJson(jsonObject);
        } else {
            throw new Exception("Failed to read weather data.");
        }
    }

    /**
     * Validates the coordinates retrieved for a location.
     * 
     * @param coordinates the latitude and longitude of the location.
     * @throws Exception if the coordinates are invalid.
     */
    private void validateCoordinates(String[] coordinates) throws Exception {
        if ("0.0".equals(coordinates[0]) && "0.0".equals(coordinates[1])) {
            throw new Exception("Failed to find location");
        }
    }

    /**
     * Makes an API call and returns the result as a JSON object.
     * 
     * @param urlString the URL string for the API request.
     * @return a JsonObject containing the response, or null if an error occurs.
     */
    public JsonObject makeAPICall(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return JsonParser.parseString(response.toString()).getAsJsonObject();
                }
            }
        } catch (IOException | JsonSyntaxException e) {
            return null;
          
        }
        return null;
    }

    /**
     * Looks up the latitude and longitude for a given location.
     * 
     * @param location the name of the location.
     * @return an array containing latitude at index 0 and longitude at index 1.
     */
    public String[] lookUpLocation(String location) {
        currentLocation = "";
        String urlString = String.format(
            "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
            location, API_KEY
        );

        JsonObject jsonObject = makeAPICall(urlString);
        String[] coordinates = {"0.0", "0.0"};
        if (jsonObject != null && jsonObject.has("lat") && jsonObject.has("lon")) {
            coordinates[0] = jsonObject.get("lat").getAsString();
            coordinates[1] = jsonObject.get("lon").getAsString();
            currentLocation = jsonObject.get("name").getAsString();
        }
        return coordinates;
    }

    /**
     * Gets the name of the most recently queried location.
     * 
     * @return the name of the current location.
     */
    public String getCurrentLocationName() {
        return currentLocation;
    }
}
