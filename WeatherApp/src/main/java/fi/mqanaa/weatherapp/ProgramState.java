package fi.mqanaa.weatherapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.*;

/**
 * Manages the program state, including weather data, location, units, favorites,
 * and search history. Supports loading and saving state to a file.
 */
public class ProgramState {
 
    
    private static final String PROGRAM_STATE_FILE = "programState.json";
    private static final String NO_HISTORY = "No search history";
    private static final int MAX_HISTORY_SIZE = 25;
    private static final int MAX_FAVORITES_SIZE = 5;

    private final TreeSet<String> favorites;
    private final LinkedList<String> history;
    private final JsonFileHandler fileHandler;
    private final WeatherAPI weatherAPI;

    private String currentLocation;
    private HourlyWeatherDataEntry currentWeather;
    private List<HourlyWeatherDataEntry> hourlyWeathers;
    private List<DailyWeatherDataEntry> dailyWeathers;
    private String units;
    private String currentTempUnit;
    private String currentWsUnit;
    
    /**
     * Initializes a new ProgramState instance with default values.
     *
     * @param fileHandler the file handler for reading and writing JSON data
     * @param weatherAPI the API interface for fetching weather data
     */
    public ProgramState(JsonFileHandler fileHandler, WeatherAPI weatherAPI) {
        this.favorites = new TreeSet<>();
        this.history = new LinkedList<>();
        this.fileHandler = fileHandler;
        this.weatherAPI = weatherAPI;
        this.currentLocation = "";
        this.hourlyWeathers = new ArrayList<>();
        this.dailyWeathers = new ArrayList<>();
        this.units = "metric";
        this.currentTempUnit = "C";
        this.currentWsUnit = "m/s";
    }
 
    /**
     * Loads weather data from the API.
     *
     * @throws Exception if weather data cannot be loaded
     */
    public void loadWeatherData() throws Exception {
        try {
            fetchWeatherDataFromAPI();
            addToSearchHistory(currentLocation);
        } catch (Exception e) {
            throw new Exception("Failed to load weather data", e);
        }
    }
    
    /**
     * Fetches weather data from the API and updates the program state.
     *
     * @throws Exception if an error occurs while fetching data
     */
    private void fetchWeatherDataFromAPI() throws Exception {
        JsonToWeatherDataEntries jtwde = new JsonToWeatherDataEntries();

        String jsonHourlyData = weatherAPI.getForecast(currentLocation, currentTempUnit);
        String jsonDailyData = weatherAPI.getDailyData(currentLocation, currentTempUnit);
        String jsonCurrentData = weatherAPI.getCurrentWeather(currentLocation, currentTempUnit);

        currentLocation = weatherAPI.getCurrentLocationName();
        hourlyWeathers = jtwde.createHourlyWeatherDataObjects(jsonHourlyData);
        dailyWeathers = jtwde.createDailyWeatherDataObjects(jsonDailyData);
        currentWeather = jtwde.createCurrentWeatherDataObject(jsonCurrentData);
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
     * Toggles the unit system between metric and imperial.
     */
    public void changeUnits() {
        if (units.equals("metric")) {
            setUnits("imperial", "F", "mph");
        } else {
            setUnits("metric", "C", "m/s");
        }
    }
    
    /**
     * Updates the unit system and related unit values.
     *
     * @param units the unit system
     * @param tempUnit the temperature unit
     * @param wsUnit the wind speed unit
     */
    private void setUnits(String units, String tempUnit, String wsUnit) {
        this.units = units;
        this.currentTempUnit = tempUnit;
        this.currentWsUnit = wsUnit;
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
    public List<DailyWeatherDataEntry> getDailyWeathers() {
        return dailyWeathers;
    }

    /**
     * Returns the list of hourly weather data entries.
     *
     * @return the list of hourly weather data entries.
     */    
    public List<HourlyWeatherDataEntry> getHourlyWeathers() {
        return hourlyWeathers;
    }

    /**
     * Adds a city to the favorites list.
     *
     * @param city the city to add to favorites
     * @return true if the city was added successfully
     * @throws Exception if the city cannot be added
     */
    public boolean addFavorite(String city) throws Exception {
        if (favorites.size() >= MAX_FAVORITES_SIZE) {
            throw new Exception("Favorite slots full");
        }

        try {
            weatherAPI.getCurrentWeather(city, currentTempUnit);
            String currentLocationFromApi = weatherAPI.getCurrentLocationName();
            if (!favorites.add(currentLocationFromApi)) {
                throw new Exception("Location already in favorites");
            }
            return true;
        } catch (Exception e) {
            throw new Exception("Location not found", e);
        }
    }
 
    /**
     * Removes a city from the favorites list.
     *
     * @param city the city to remove.
     * @return true if the city was removed successfully.
     */    
    public boolean removeFavorite(String city) {
        return favorites.remove(city);
    }

    /**
     * Adds a city to the search history.
     *
     * @param city the city to add
     */
    public void addToSearchHistory(String city) {
        addCityToHistory(city);
    }
    
    /**
     * Helper method to add a city to the search history.
     * Removes duplicates and caps history size.
     *
     * @param city the city to add
     */
    private void addCityToHistory(String city) {
        history.remove(city);
        history.addFirst(city);
        if (history.size() > MAX_HISTORY_SIZE) {
            history.removeLast();
        }
    }

    /**
     * Returns the most recent city from the search history.
     *
     * @return the latest city, or a default message if history is empty
     */
    public String getLatestCity() {
        return history.isEmpty() ? NO_HISTORY : history.getFirst();
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
     * @throws Exception if the file cannot be read or parsed.
     */    
    public void loadProgramState() throws Exception {
        Gson gson = new Gson();

        try {
            String jsonData = fileHandler.readJsonFromFile(PROGRAM_STATE_FILE);
            JsonObject programStateData = gson.fromJson(jsonData, JsonObject.class);

            loadFavoritesFromJson(programStateData);
            loadHistoryFromJson(programStateData);

            if (!history.isEmpty()) {
                currentLocation = history.getFirst();
            }
        } catch (NullPointerException | JsonSyntaxException e) {
            throw new Exception("Error reading programState.json", e);
        }
    }
    
    /**
     * Loads the favorites list from the program state JSON.
     *
     * @param programStateData the JSON object containing program state data
     */
    private void loadFavoritesFromJson(JsonObject programStateData) {
        JsonArray favoriteEntries = programStateData.getAsJsonArray("favorites");
        if (favoriteEntries != null) {
            for (var city : favoriteEntries) {
                favorites.add(city.getAsString());
            }
        }
    }

    /**
     * Loads the search history from the program state JSON.
     *
     * @param programStateData the JSON object containing program state data
     */
    private void loadHistoryFromJson(JsonObject programStateData) {
        JsonArray historyEntries = programStateData.getAsJsonArray("history");
        if (historyEntries != null) {
            for (var city : historyEntries) {
                history.add(city.getAsString());
            }
        }
    }

    /**
     * Saves the program state to a JSON file.
     */    
    public void saveProgramState() {
        Map<String, Object> programStateData = new TreeMap<>();
        programStateData.put("favorites", favorites);
        programStateData.put("history", history);

        String jsonState = new Gson().toJson(programStateData);

        try {
            fileHandler.writeJsonToFile(PROGRAM_STATE_FILE, jsonState);
        } catch (Exception e) {
            System.err.println("Error when trying to write file " + PROGRAM_STATE_FILE + e.getMessage());
        }
        
    }
}