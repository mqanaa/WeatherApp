package fi.mqanaa.weatherapp;

import com.google.gson.JsonElement;

/**
 * Interface for extracting data from the OpenWeatherMap API.
 */
public interface iAPI {

    /**
     * Returns the current weather for the given location.
     * @param loc The name of the location.
     * @param unit The currently selected unit system, "C" or "F".
     * @throws Exception if weather data can't be read or location is not found.
     * @return String (JSON) containing current weather data in target location.
     */
    public String getCurrentWeather(String loc, String unit) throws 
                                                            Exception;

    /**
     * Returns the hourly forecast (next four days) for the given location.
     * @param loc The name of the location.
     * @param unit The currently selected unit system, "C" or "F".
     * @throws Exception if weather data can't be read or location is not found.
     * @return String (JSON) containing hourly forecast data in target location.
     */
    public String getForecast(String loc, String unit) throws Exception;
    
    /**
     * Returns the daily forecast (next four days) for the given location.
     * @param loc The name of the location.
     * @param unit The currently selected unit system, "C" or "F".
     * @throws Exception if weather data can't be read or location is not found.
     * @return String (JSON) containing daily forecast data in target location.
     */
    public String getDailyData(String loc, String unit) throws Exception;
    
    /**
     * Helper function used by the other functions
     * to actually make the API calls.
     * @param urlString The URL in string form, created by the other methods.
     * @return The full API call as a JsonElement, or null if an error occurs.
     */
    public JsonElement makeAPICall(String urlString);
    
    /**
     * Helper method used to fetch coordinates for a location.
     * @param loc Name of the location for which coordinates should be fetched.
     * @return double[] containing latitude and longitude at 0 and 1 respectively
     */
    public String[] lookUpLocation(String loc);
}
