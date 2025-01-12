package fi.mqanaa.weatherapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import com.google.gson.JsonSyntaxException;

/**
 * This class keeps track of the program state, including weather data, current location
 * and the units currently in use. It also implements favorites and search history, 
 * which can be read from a file and written to a file.
 */
public class ProgramState {
 
    final private TreeSet<String> favorites;
    final private LinkedList<String> history;
    final private int maxHistorySize = 25;
    final private int maxFavoritesSize = 5;
    final private String NO_HISTORY = "No search history";
    private String currentLocation;
    private HourlyWeatherDataEntry currentWeather;
    private ArrayList<HourlyWeatherDataEntry> hourlyWeathers;
    private ArrayList<DailyWeatherDataEntry> dailyWeathers;
    private String units;
    private String currentTempUnit;
    private String currentWsUnit;
    private JsonFileReaderWriter jfrw = new JsonFileReaderWriter();
    
    /**
     * Constructor to initialize ProgramState.
     */
    public ProgramState() {
        this.favorites = new TreeSet<>();
        this.history = new LinkedList<>();
        this.currentLocation = "";
        this.hourlyWeathers = new ArrayList<>();
        this.dailyWeathers = new ArrayList<>();
        this.units = "metric";
        this.currentTempUnit = "C";
        this.currentWsUnit = "m/s";
        this.jfrw = new JsonFileReaderWriter();
    }
 
     /**
     * Loads weather data and saves them as weather data objects
     *
     * @throws Exception if an error occurs while loading weather data.
     */
    public void loadWeatherData() throws Exception {
        WeatherAPI api = new WeatherAPI();
        
        String jsonHourlyData;
        String jsonDailyData;
        String jsonCurrentData;
        try {
            jsonHourlyData = api.getForecast(currentLocation, currentTempUnit);
            jsonDailyData = api.getDailyData(currentLocation, currentTempUnit);
            jsonCurrentData = api.getCurrentWeather(currentLocation, currentTempUnit);
            String currentLocationFromApi = api.getCurrentLocationName();
            setCurrentLocation(currentLocationFromApi);
            addToSearchHistory(currentLocationFromApi);
            
            JsonToWeatherDataEntries jtwde = new JsonToWeatherDataEntries();
            hourlyWeathers = jtwde.createHourlyWeatherDataObjects(jsonHourlyData);
            dailyWeathers = jtwde.createDailyWeatherDataObjects(jsonDailyData);
            currentWeather = jtwde.createCurrentWeatherDataObject(jsonCurrentData); 
            
        } catch (Exception e) {
            throw e;
        }
               
        
    }

 
    /**
     * Returns the current location.
     *
     * @return the current location.
     */    
    public String getCurrentLocation() {
        return currentLocation;
    }
    
    /**
     * Sets the current location.
     *
     * @param newLocation the new location to set.
     */    
    public void setCurrentLocation(String newLocation) {
        currentLocation = newLocation;
    }
    
    /**
     * Returns the units.
     *
     * @return the units.
     */
    public String getUnits() {
        return units;
    }
    
    /**
     * Returns the temperature units.
     *
     * @return the temperature units.
     */    
    public String getTempUnits() {
        return currentTempUnit;
    }

    /**
     * Returns the wind speed units.
     *
     * @return the wind speed units.
     */    
    public String getWsUnits() {
        return currentWsUnit;
    }

     /**
     * Changes the units between metric and imperial.
     */
    public void changeUnits() {
        if (units.equals("metric")) {
            units = "imperial";
            currentTempUnit = "F";
            currentWsUnit = "mph";
        } else {
            units = "metric";
            currentTempUnit = "C";
            currentWsUnit = "m/s";
        }
    }
    
     /**
     * Returns the current weather data entry.
     *
     * @return the current weather data entry.
     */
    public HourlyWeatherDataEntry getCurrentWeather() {
        return currentWeather;
    }

    /**
     * Returns the list of daily weather data entries.
     *
     * @return the list of daily weather data entries.
     */    
    public ArrayList<DailyWeatherDataEntry> getDailyWeathers() {
        return dailyWeathers;
    }

    /**
     * Returns the list of hourly weather data entries.
     *
     * @return the list of hourly weather data entries.
     */    
    public ArrayList<HourlyWeatherDataEntry> getHourlyWeathers() {
        return hourlyWeathers;
    }

    /**
     * Adds a city to favorites.
     *
     * @param city the city to add to favorites.
     * @return true if the city was added successfully
     * @throws Exception if inputted location can't be found in the WeatherAPI, 
 if the favorite slots are full, or if location is already in favorites
     */    
    public boolean addFavorite(String city) throws Exception {
        
        // Check if favorite slots are full
        if (favorites.size() >= maxFavoritesSize) {
            throw new Exception ("Favorite slots full");
        }
        
        WeatherAPI api = new WeatherAPI();
        
        String currentLocationFromApi;
        try {
            // Checks if the WeatherAPI can find the city
            api.getCurrentWeather(city, currentTempUnit);
            currentLocationFromApi = api.getCurrentLocationName();
        } catch (Exception e) {
            throw new Exception ("Location not found");
        }
        
        //checking for duplicate entries
        if (favorites.add(currentLocationFromApi)) {
                return true;
            } else {
                throw new Exception ("Location already in favorites");
        }
        
        
    }
 
    /**
     * Removes a city from favorites.
     *
     * @param city the city to remove from favorites.
     * @return true if the city was removed successfully, false otherwise.
     */    
    public boolean removeFavorite(String city) {
        boolean removedSuccesfully = favorites.remove(city);
        return removedSuccesfully;
    }

    /**
     * Adds a city to search history. Keeps the number of locations in history
     * capped to maxHistorySize.
     *
     * @param city the city to add to search history.
     */    
    public void addToSearchHistory(String city) { 
        // If the city is already in the history, move it to the beginning of the list
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).equals(city)) {
                String cityToMove = history.remove(i);
                history.addFirst(cityToMove);
                return;
            }
        }
        
        // Otherwise add the city to history
        history.addFirst(city);
        
        if (history.size() > maxHistorySize) {
            history.removeLast();
        }
    }

    /**
     * Returns the latest city in the search history.
     *
     * @return the latest city in the search history.
     */    
    public String getLatestCity() {
        if (history.isEmpty()) {
            return NO_HISTORY;
        }
        return history.getFirst();
    }

    /**
     * Returns the set of favorite cities.
     *
     * @return the set of favorite cities.
     */    
    public TreeSet<String> getFavorites() {
        return favorites;
    }
    
    /**
     * Returns the search history.
     *
     * @return the search history.
     */    
    public LinkedList<String> getHistory() {
        return history;
    }

    /**
     * Loads the program state from a JSON file.
     * @throws Exception if the file cannot be found or isn't properly formatted
     */    
    public void loadProgramState() throws Exception {
        // Extract data from the json and create WeatherDataEntries
        Gson gson = new Gson();

        try {
            
            String jsonData = jfrw.readFromFile("programState.json");
            JsonObject programStateData = gson.fromJson(jsonData, JsonObject.class);

            JsonArray favoriteEntries = programStateData.getAsJsonArray("favorites");
            for (var city : favoriteEntries) {
                favorites.add(city.getAsString());
            }

            JsonArray historyEntries = programStateData.getAsJsonArray("history");
            for (var city : historyEntries) {
                history.add(city.getAsString());
            }
            if (!history.isEmpty()) {
                currentLocation = history.getFirst(); 
            }
        } catch (NullPointerException | JsonSyntaxException e) {

            throw new Exception("Error reading programState.json");
        }
    }

    /**
     * Saves the program state to a JSON file.
     */    
    public void saveProgramState() {
        Map<String, Object> programStateData = new TreeMap<>();
        programStateData.put("favorites", favorites);
        programStateData.put("history", history);
        Gson gson = new Gson();
        String json = gson.toJson(programStateData);
        System.out.println(json);
        JsonFileReaderWriter JFRW = new JsonFileReaderWriter();
        try {
            JFRW.writeToFile("programState.json", json);
        } catch (Exception e) {
            System.err.println("Error when trying to write file programstate.json: " + e.getMessage());
        }
        
    }
}