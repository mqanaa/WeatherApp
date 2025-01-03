package fi.mqanaa.weatherapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Contains functionality to transform weather data from json format to different WeatherDataEntry objects
 */
public class JsonToWeatherDataEntries {
    /**
     * Create a HourlyWeatherDataEntry from json data
     * @param jsonData the json data of current weather as a String
     * @return HourlyWeatherDataEntry object
     * @throws JsonSyntaxException if the parameter contains invalid json data
     */
    public HourlyWeatherDataEntry createCurrentWeatherDataObject(String jsonData) throws JsonSyntaxException {
        try {
            // Extract data from the json and create WeatherDataEntries
            Gson gson = new Gson();
            JsonObject currentData = gson.fromJson(jsonData, JsonObject.class);
            JsonArray weatherArray = currentData.getAsJsonArray("weather");
            JsonObject weatherField = weatherArray.get(0).getAsJsonObject();
            JsonObject mainField = currentData.getAsJsonObject("main");
            JsonObject windField = currentData.getAsJsonObject("wind");
            JsonObject sysField = currentData.getAsJsonObject("sys");

            double tempDouble = mainField.get("temp").getAsDouble();
            String temp = formatTempToString(tempDouble);
            double feelsLikeDouble = mainField.get("feels_like").getAsDouble();
            String feelsLike = formatTempToString(feelsLikeDouble);
            double windSpeedDouble = windField.get("speed").getAsDouble();
            String windSpeed = formatWindSpeed(windSpeedDouble);
            String weatherId = weatherField.get("id").getAsString();
            String humidity = mainField.get("humidity").getAsString() + " %";
            long dt = currentData.get("dt").getAsLong();
            String hours = getHoursFromUnixTimestamp(dt);
            String date = formatUnixTimestampToWeekdayDate(dt);
            long sunrise = sysField.get("sunrise").getAsLong();
            long sunset = sysField.get("sunset").getAsLong();
            boolean isDayTime = checkDaytimeFromUnixTimestamp(dt, sunrise, sunset);

            HourlyWeatherDataEntry entry = new HourlyWeatherDataEntry(weatherId, date, hours, temp, feelsLike, windSpeed, isDayTime, humidity);             
            return entry;
        } catch (JsonSyntaxException e) {
            throw e;
        }
    }
    
    /**
     * Create an ArrayList of HourlyWeatherDataEntries from json data
     * @param jsonData the json data of hourly weather timestamps as a String
     * @return ArrayList of HourlyWeatherDataEntries
     * @throws JsonSyntaxException if the parameter contains invalid json data
     */
    public ArrayList<HourlyWeatherDataEntry> createHourlyWeatherDataObjects(String jsonData) throws JsonSyntaxException {
        
        // A container for the hourly weather data entries
        ArrayList<HourlyWeatherDataEntry> hourlyData = new ArrayList<>();

        try {
            // Extract data from the json and create WeatherDataEntries
            Gson gson = new Gson();
            JsonObject allData = gson.fromJson(jsonData, JsonObject.class);
            JsonArray weatherDataEntries = allData.getAsJsonArray("list");
            for (int i = 0; i < weatherDataEntries.size(); i++) {
                JsonObject jo = weatherDataEntries.get(i).getAsJsonObject();

                JsonObject mainField = jo.getAsJsonObject("main");
                JsonArray weatherArray = jo.getAsJsonArray("weather");
                JsonObject weatherField = weatherArray.get(0).getAsJsonObject();
                JsonObject windField = jo.getAsJsonObject("wind");
                JsonObject sysField = jo.getAsJsonObject("sys");

                double tempDouble = mainField.get("temp").getAsDouble();
                String temp = formatTempToString(tempDouble);
                double feelsLikeDouble = mainField.get("feels_like").getAsDouble();
                String feelsLike = formatTempToString(feelsLikeDouble);
                double windSpeedDouble = windField.get("speed").getAsDouble();
                String windSpeed = formatWindSpeed(windSpeedDouble);
                long timestamp = jo.get("dt").getAsLong();
                String hours = getHoursFromUnixTimestamp(timestamp);
                String date = formatUnixTimestampToWeekdayDate(timestamp);
                String weatherId = weatherField.get("id").getAsString();
                boolean isDayTime = sysField.get("pod").getAsString().equals("d");
                String humidity = mainField.get("humidity").getAsString() + " %";

                HourlyWeatherDataEntry entry = new HourlyWeatherDataEntry(weatherId, date, hours, temp, feelsLike, windSpeed, isDayTime, humidity);
                hourlyData.add(entry);             
            }      
            return hourlyData;
        } catch (JsonSyntaxException e) {
            throw e;
        }
    }    
  
    /**
     * Create an ArrayList of DailyWeatherDataEntries from json data
     * @param jsonData the json data of daily weather timestamps as a String
     * @return ArrayList of DailyWeatherDataEntries
     * @throws JsonSyntaxException if the parameter contains invalid json data
     */
    public ArrayList<DailyWeatherDataEntry> createDailyWeatherDataObjects(String jsonData) throws JsonSyntaxException {
        
        // A container for the weather data entries
        ArrayList<DailyWeatherDataEntry> dailyData = new ArrayList<>();

        try {
            // Extract data from the json and create WeatherDataEntries
            Gson gson = new Gson();
            JsonObject allData = gson.fromJson(jsonData, JsonObject.class);
            JsonArray weatherDataEntries = allData.getAsJsonArray("list");
            for (int i = 0; i < weatherDataEntries.size(); i++) {
                JsonObject jo  = weatherDataEntries.get(i).getAsJsonObject();

                JsonObject tempField = jo.getAsJsonObject("temp");
                JsonArray weatherArray = jo.getAsJsonArray("weather");
                JsonObject weatherField = weatherArray.get(0).getAsJsonObject();

                double tempMinDouble = tempField.get("min").getAsDouble();
                String tempMin = formatTempToString(tempMinDouble);
                double tempMaxDouble = tempField.get("max").getAsDouble();
                String tempMax = formatTempToString(tempMaxDouble);
                String weatherId = weatherField.get("id").getAsString();
                long unixTimestamp = jo.get("dt").getAsLong();
                String formattedDate = formatUnixTimestampToWeekdayDate(unixTimestamp);

                DailyWeatherDataEntry entry = new DailyWeatherDataEntry(weatherId, formattedDate, tempMin, tempMax);
                dailyData.add(entry);             
            }      
            return dailyData;
        } catch (JsonSyntaxException e) {
            throw e;
        }
    }
    
    // Below are the private functions used for formatting the extracted json data into correct form
  
    /**
     * Format double temperature to string
     * @param tempDouble temperature of type double
     * @return rounded string representation of the temperature parameter
     */    
    private String formatTempToString(double tempDouble) {
        String temp = String.valueOf(Math.round(tempDouble)) + '°';
        if (temp.charAt(0) == '-') {
            return temp;
        } else if (temp.charAt(0) == '0') {
            return " " + temp;
        }
        return "+" + temp;
    }
    
    /**
     * Extract the current hours from Unix timestamp
     * @param timestamp Unix timestamp
     * @return the hours of a moment represented by Unix timestamp
     */     
    private String getHoursFromUnixTimestamp(long timestamp) {
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        int hours = ldt.getHour();
        if (hours < 10) {
            return '0' + String.valueOf(ldt.getHour());
        }
        return String.valueOf(ldt.getHour());
    }

    /**
     * Convert Unix timestamp to string representation of the weekday and date
     * @param timestamp Unix timestamp
     * @return string representation of the weekday and date
     */    
    private String formatUnixTimestampToWeekdayDate(long timestamp) {
        LocalDate ld = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        DayOfWeek dow = ld.getDayOfWeek();
        return dow.getDisplayName(TextStyle.SHORT, Locale.US) + " " + formatUnixTimestampToDate(timestamp);
    }
    
    /**
     * Convert Unix timestamp to string representation of the date
     * @param timestamp Unix timestamp
     * @return string representation of the date
     */    
    private String formatUnixTimestampToDate(long timestamp) {
        Instant i = Instant.ofEpochSecond(timestamp);
        LocalDateTime ldt = LocalDateTime.ofInstant(i, ZoneId.systemDefault());
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.");
        return ldt.format(f);
    }
    
    /**
     * Check if the given timestamp is daytime by comparing it to sunrise and sunset timestamps
     * @param current Unix timestamp to be checked
     * @param sunrise Unix timestamp for sunrise
     * @param sunset Unix timestamp for sunset
     * @return true if current is daytime, otherwise false
     */       
    private boolean checkDaytimeFromUnixTimestamp(long current, long sunrise, long sunset) {
        return current > sunrise && current < sunset;
    }
    
    /**
     * Format double wind speed to string
     * @param windSpeed wind speed of type double
     * @return string representation of the temperature parameter
     */     
    private String formatWindSpeed(double windSpeed) {
        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        return df.format(windSpeed);
    }
}