package fi.mqanaa.weatherapp;

/**
 * Represents a simple weather data entry, featuring all the common attributes of the other weather data classes.
 */
public class WeatherDataEntry {
    /**
    * ID for the weather.
    */
    final private String weatherId;
    /**
    * Date in string format.
    */    
    final private String date;

    /**
    * Constructor for setting weather ID and date.
    * @param weatherId ID for the weather.
    * @param date Date in string format.
    */    
    public WeatherDataEntry(String weatherId, String date) {
        this.weatherId = weatherId;
        this.date = date;
    }

    /**
    * Getter for the weather ID.
    * @return ID of the weather.
    */        
    public String getWeatherId() {
        return weatherId;
    }

    /**
    * Getter for the date.
    * @return the date the weather entry is associated with.
    */     
    public String getDate() {
        return date;
    }
}
