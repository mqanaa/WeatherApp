# WeatherApp

## Overview

This project is a weather application built with Java and JavaFX for the graphical user interface (GUI). It retrieves and displays weather data using an external weather API. The application supports storing user preferences and program state, including search history and favorite locations.

## Features

- **Weather Data Retrieval**: Fetches and displays current weather, hourly forecasts, and daily forecasts.
- **Favorites Management**: Add and remove favorite locations.
- **Search History**: Maintains a history of searched locations.
- **Unit Conversion**: Toggle between metric and imperial units.
- **Program State Persistence**: Saves and loads program state to/from a JSON file.

## Technologies Used

- **Java**: Core programming language.
- **JavaFX**: For building the GUI.
- **Gson**: For JSON parsing and serialization.
- **Custom File Handling**: Handles reading and writing JSON files.
- **WeatherAPI**: Interface for fetching weather data from an external service.

## Installation

1. Ensure you have Java 8 or later installed.
2. Clone or download this repository.
3. Add your API key to the `config.properties` file located in the working directory:
   ```
   api.key=your_api_key_here
   ```
4. Compile the project with your preferred Java IDE or build tool.
5. Run the `WeatherApp` main class to start the application.

## API key details
Your API key needs to have access to the following OpenWeatherMap APIs for the app to work properly:
- [Current Weather data] (https://openweathermap.org/current)
- [Hourly Forecast 4 days] (https://openweathermap.org/api/hourly-forecast)
- [Daily Forecast 16 days] (https://openweathermap.org/forecast16)


### Dependencies

- Gson (com.google.code.gson:gson:2.8.9)

If using Maven, add:
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.9</version>
</dependency>
```

## Usage

- Use the search bar to look up weather information for a city.
- Add cities to your favorites for quick access.
- Toggle between metric and imperial units.
- View weather forecasts for the current day or upcoming days.

### Key Commands
- Add a city to favorites.
- Remove a city from favorites.
- Load current weather data.
- Change between metric and imperial units.

## File Structure

- `ProgramState.java`: Manages application state (favorites, history, current location, etc.).
- `JsonFileHandler.java`: Handles reading and writing JSON files.
- `WeatherAPI.java`: Interface for interacting with a weather API.
- `HourlyWeatherDataEntry.java` and `DailyWeatherDataEntry.java`: Represent weather data objects.
- `WeatherApp`: Provides an interactive user interface.

## Configuration

The application uses `programState.json` for persistence. Ensure this file is located in the working directory or provide a path in `PROGRAM_STATE_FILE` within `ProgramState.java`.

Example `programState.json` structure:
```json
{
  "favorites": ["New York", "Los Angeles"],
  "history": ["London", "Paris"]
}
```

## Error Handling

- Displays error messages for invalid cities or network issues.
- Logs errors when loading or saving program state fails.

## Future Enhancements

- Add more detailed weather visualizations.
- Add localization for multiple languages.

## License
This project is licensed under the MIT License.

## Acknowledgments

- Weather data provided by [OpenWeatherMap](https://openweathermap.org/)
