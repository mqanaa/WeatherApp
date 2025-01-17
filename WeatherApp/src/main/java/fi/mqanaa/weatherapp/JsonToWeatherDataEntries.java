package fi.mqanaa.weatherapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Converts JSON data into WeatherDataEntry objects.
 */
public class JsonToWeatherDataEntries {
    
    private final Gson gson = new Gson();
    
        /**
     * Creates a current weather entry from JSON data.
     * 
     * @param jsonData the JSON string containing current weather data.
     * @return a populated HourlyWeatherDataEntry object.
     * @throws JsonSyntaxException if the JSON format is invalid.
     */
    public HourlyWeatherDataEntry createCurrentWeatherDataObject(String jsonData) throws JsonSyntaxException {
        JsonObject currentData = parseJsonObject(jsonData);
        return parseHourlyWeatherData(currentData, true);
    }
    
    /**
     * Creates a list of hourly weather data entries from JSON data.
     * 
     * @param jsonData the JSON string containing hourly weather data.
     * @return a list of HourlyWeatherDataEntry objects.
     * @throws JsonSyntaxException if the JSON format is invalid.
     */
    public ArrayList<HourlyWeatherDataEntry> createHourlyWeatherDataObjects(String jsonData) throws JsonSyntaxException {
        JsonObject allData = parseJsonObject(jsonData);
        JsonArray weatherDataEntries = allData.getAsJsonArray("list");
        ArrayList<HourlyWeatherDataEntry> hourlyData = new ArrayList<>();
        
        for (var entry : weatherDataEntries) {
            hourlyData.add(parseHourlyWeatherData(entry.getAsJsonObject(), false));
        }
        return hourlyData;
    }    
    
    /**
     * Creates a list of daily weather data entries from JSON data.
     * 
     * @param jsonData the JSON string containing daily weather data.
     * @return a list of DailyWeatherDataEntry objects.
     * @throws JsonSyntaxException if the JSON format is invalid.
     */
    public ArrayList<DailyWeatherDataEntry> createDailyWeatherDataObjects(String jsonData) throws JsonSyntaxException {
        JsonObject allData = parseJsonObject(jsonData);
        JsonArray weatherDataEntries = allData.getAsJsonArray("list");
        ArrayList<DailyWeatherDataEntry> dailyData = new ArrayList<>();
        
        for (var entry : weatherDataEntries) {
            dailyData.add(parseDailyWeatherData(entry.getAsJsonObject()));
        }
        return dailyData;
    }
    
    // Helper Methods

    private JsonObject parseJsonObject(String jsonData) {
        return gson.fromJson(jsonData, JsonObject.class);
    }

    private HourlyWeatherDataEntry parseHourlyWeatherData(JsonObject data, boolean isCurrent) {
        JsonObject mainField = data.getAsJsonObject("main");
        JsonObject weatherField = data.getAsJsonArray("weather").get(0).getAsJsonObject();
        JsonObject windField = data.getAsJsonObject("wind");
        JsonObject sysField = isCurrent ? data.getAsJsonObject("sys") : null;
        
        double temp = mainField.get("temp").getAsDouble();
        double feelsLike = mainField.get("feels_like").getAsDouble();
        double windSpeed = windField.get("speed").getAsDouble();
        long timestamp = data.get("dt").getAsLong();
        
        String date = formatUnixTimestampToWeekdayDate(timestamp);
        String hours = getHoursFromUnixTimestamp(timestamp);
        boolean isDayTime = isCurrent 
            ? checkDaytimeFromUnixTimestamp(timestamp, sysField.get("sunrise").getAsLong(), sysField.get("sunset").getAsLong()) 
            : data.getAsJsonObject("sys").get("pod").getAsString().equals("d");

        return new HourlyWeatherDataEntry(
            weatherField.get("id").getAsString(), 
            date, 
            hours, 
            formatTempToString(temp), 
            formatTempToString(feelsLike), 
            formatWindSpeed(windSpeed), 
            isDayTime, 
            mainField.get("humidity").getAsString() + " %"
        );
    }

    private DailyWeatherDataEntry parseDailyWeatherData(JsonObject data) {
        JsonObject tempField = data.getAsJsonObject("temp");
        JsonObject weatherField = data.getAsJsonArray("weather").get(0).getAsJsonObject();

        double tempMin = tempField.get("min").getAsDouble();
        double tempMax = tempField.get("max").getAsDouble();
        long timestamp = data.get("dt").getAsLong();

        return new DailyWeatherDataEntry(
            weatherField.get("id").getAsString(), 
            formatUnixTimestampToWeekdayDate(timestamp), 
            formatTempToString(tempMin), 
            formatTempToString(tempMax)
        );
    }

    private String formatTempToString(double temp) {
        String formattedTemp = String.format("%.0f°", temp);
        return formattedTemp.startsWith("-") ? formattedTemp : (temp == 0 ? " " : "+") + formattedTemp;
    }

    private String getHoursFromUnixTimestamp(long timestamp) {
        return String.format("%02d", LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).getHour());
    }

    private String formatUnixTimestampToWeekdayDate(long timestamp) {
        LocalDate date = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        String weekday = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US);
        return weekday + " " + date.format(DateTimeFormatter.ofPattern("dd.MM."));
    }

    private boolean checkDaytimeFromUnixTimestamp(long current, long sunrise, long sunset) {
        return current > sunrise && current < sunset;
    }

    private String formatWindSpeed(double windSpeed) {
        return String.format("%.2f", windSpeed);
    }
}