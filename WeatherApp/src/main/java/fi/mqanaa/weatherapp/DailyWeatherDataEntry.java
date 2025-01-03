package fi.mqanaa.weatherapp;

/**
 * Represents a weather data entry for daily weather.
 */
public class DailyWeatherDataEntry extends WeatherDataEntry {
    /**
    * Minimum temperature of the day.
    */     
    final private String tempMin;
    /**
    * Maximum temperature of the day.
    */      
    final private String tempMax;

    /**
    * Constructor for setting weather ID, date and temperature information.
    * @param weatherId ID for the weather.
    * @param date Date in string format.
    * @param tempMin Minimum temperature of the day.
    * @param tempMax Maximum temperature of the day.
    */      
    public DailyWeatherDataEntry(String weatherId, String date, String tempMin, String tempMax) {
        super(weatherId, date);
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    /**
    * Getter for the minimum temperature of the day.
    * @return minimum temperature of the day.
    */      
    public String getTempMin() {
        return tempMin;
    }

    /**
    * Getter for the maximum temperature of the day.
    * @return maximum temperature of the day.
    */      
    public String getTempMax() {
        return tempMax;
    }
}
