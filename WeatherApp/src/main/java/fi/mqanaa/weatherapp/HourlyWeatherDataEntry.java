package fi.mqanaa.weatherapp;

/**
 * Represents a weather data entry for hourly weather.
 */
public class HourlyWeatherDataEntry extends WeatherDataEntry {
    /**
    * The current hour.
    */     
    final private String hours;   
    /**
    * Temperature of this hour.
    */       
    final private String temp;
    /**
    * "Feels like" temperature of this hour.
    */       
    final private String feelsLike;
    /**
    * Wind speed in this hour.
    */   
    final private String windSpeed;
    /**
    * Boolean value expressing if this hour is daytime.
    */       
    final private boolean isDayTime;
    /**
    * Humidity in this hour.
    */   
    final private String humidity;

    /**
    * Constructor for setting weather info about hourly data entry.
    * @param weatherId ID for the weather.
    * @param date Date in string format.
    * @param hours The current hour.
    * @param temp Temperature of this hour.
    * @param feelsLike "Feels like" temperature of this hour.
    * @param windSpeed Wind speed in this hour.
    * @param isDayTime Boolean value expressing if this hour is daytime.
    * @param humidity Humidity in this hour.
    */     
    public HourlyWeatherDataEntry(String weatherId, String date, String hours, 
            String temp, String feelsLike, String windSpeed, 
            boolean isDayTime, String humidity) {
        super(weatherId, date);
        this.hours = hours;        
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.windSpeed = windSpeed;
        this.isDayTime = isDayTime;
        this.humidity = humidity;
    }

    /**
    * Getter for the represented hour.
    * @return the represented hour.
    */        
    public String getHours() {
        return hours;
    }
    
    /**
    * Getter for the hour's temperature.
    * @return temperature of the hour.
    */  
    public String getTemp() {
        return temp;
    }

    /**
    * Getter for the hour's "feels like" temperature.
    * @return "feels like" temperature of the hour.
    */      
    public String getFeelsLike() {
        return feelsLike;
    }

    /**
    * Getter for the hour's wind speed.
    * @return wind speed of the hour.
    */          
    public String getWindSpeed() {
        return windSpeed;
    }

    /**
    * Getter for the daytime info.
    * @return true if the represented hour is daytime, otherwise false.
    */          
    public boolean isDayTime() {
        return isDayTime;
    }

    /**
    * Getter for the hour's humidity.
    * @return humidity of the hour.
    */  
    public String getHumidity() {
        return humidity;
    }
}