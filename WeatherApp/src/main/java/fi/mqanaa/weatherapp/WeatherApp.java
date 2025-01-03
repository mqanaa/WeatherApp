package fi.mqanaa.weatherapp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * JavaFX application for displaying weather information.
 * This application retrieves and displays current weather, daily forecasts, and hourly forecasts.
 * Users can search for weather information by location and add locations to favorites.
 * Implements the Application interface from JavaFX for creating the UI.
 */
public class WeatherApp extends Application {
    private Stage stage;
    private VBox mainLayout;
    private ScrollPane hourlyForecastBox;
    private GridPane favoritesPane;
    private final Text infoText = new Text("");
    private final ArrayList<VBox> dailyWeatherBoxes = new ArrayList<>();
    private static ProgramState state;
    
    // Mapping of weather IDs to icons
    private static final Map<String, Map<String, String>> icons = new HashMap<>();
    static {
        Map<String, String> icons200 = new HashMap<>();
        icons200.put("day", "/images/thunderstormrain.png");
        icons200.put("night", "/images/thunderstormrain.png");
        
        Map<String, String> icons201 = new HashMap<>();
        icons201.put("day", "/images/thunderstormrain.png");
        icons201.put("night", "/images/thunderstormrain.png");
        
        Map<String, String> icons202 = new HashMap<>();
        icons202.put("day", "/images/thunderstormrain.png");
        icons202.put("night", "/images/thunderstormrain.png");
        
        Map<String, String> icons210 = new HashMap<>();
        icons210.put("day", "/images/thunderstorm.png");
        icons210.put("night", "/images/thunderstorm.png");
        
        Map<String, String> icons211 = new HashMap<>();
        icons211.put("day", "/images/thunderstorm.png");
        icons211.put("night", "/images/thunderstorm.png");
        
        Map<String, String> icons212 = new HashMap<>();
        icons212.put("day", "/images/thunderstorm.png");
        icons212.put("night", "/images/thunderstorm.png");
        
        Map<String, String> icons221 = new HashMap<>();
        icons221.put("day", "/images/thunderstorm.png");
        icons221.put("night", "/images/thunderstorm.png");
        
        Map<String, String> icons230 = new HashMap<>();
        icons230.put("day", "/images/thunderstormrain.png");
        icons230.put("night", "/images/thunderstormrain.png");
        
        Map<String, String> icons231 = new HashMap<>();
        icons231.put("day", "/images/thunderstormrain.png");
        icons231.put("night", "/images/thunderstormrain.png");
        
        Map<String, String> icons232 = new HashMap<>();
        icons232.put("day", "/images/thunderstormrain.png");
        icons232.put("night", "/images/thunderstormrain.png");
        
        Map<String, String> icons300 = new HashMap<>();
        icons300.put("day", "/images/moderaterain.png");
        icons300.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons301 = new HashMap<>();
        icons301.put("day", "/images/moderaterain.png");
        icons301.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons302 = new HashMap<>();
        icons302.put("day", "/images/moderaterain.png");
        icons302.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons310 = new HashMap<>();
        icons310.put("day", "/images/moderaterain.png");
        icons310.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons311 = new HashMap<>();
        icons311.put("day", "/images/moderaterain.png");
        icons311.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons312 = new HashMap<>();
        icons312.put("day", "/images/moderaterain.png");
        icons312.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons313 = new HashMap<>();
        icons313.put("day", "/images/moderaterain.png");
        icons313.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons314 = new HashMap<>();
        icons314.put("day", "/images/moderaterain.png");
        icons314.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons321 = new HashMap<>();
        icons321.put("day", "/images/moderaterain.png");
        icons321.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons500 = new HashMap<>();
        icons500.put("day", "/images/lightrain_day.png");
        icons500.put("night", "/images/lightrain_night.png");
        
        Map<String, String> icons501 = new HashMap<>();
        icons501.put("day", "/images/moderaterain.png");
        icons501.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons502 = new HashMap<>();
        icons502.put("day", "/images/heavyrain.png");
        icons502.put("night", "/images/heavyrain.png");
        
        Map<String, String> icons503 = new HashMap<>();
        icons503.put("day", "/images/extremerain.png");
        icons503.put("night", "/images/extremerain.png");
        
        Map<String, String> icons504 = new HashMap<>();
        icons504.put("day", "/images/extremerain.png");
        icons504.put("night", "/images/extremerain.png");
        
        Map<String, String> icons511 = new HashMap<>();
        icons511.put("day", "/images/freezingrain.png");
        icons511.put("night", "/images/freezingrain.png");
        
        Map<String, String> icons520 = new HashMap<>();
        icons520.put("day", "/images/moderaterain.png");
        icons520.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons521 = new HashMap<>();
        icons521.put("day", "/images/moderaterain.png");
        icons521.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons522 = new HashMap<>();
        icons522.put("day", "/images/moderaterain.png");
        icons522.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons531 = new HashMap<>();
        icons531.put("day", "/images/moderaterain.png");
        icons531.put("night", "/images/moderaterain.png");
        
        Map<String, String> icons600 = new HashMap<>();
        icons600.put("day", "/images/lightsnow_day.png");
        icons600.put("night", "/images/lightsnow_night.png");
        
        Map<String, String> icons601 = new HashMap<>();
        icons601.put("day", "/images/lightsnow_day.png");
        icons601.put("night", "/images/lightsnow_night.png");
        
        Map<String, String> icons602 = new HashMap<>();
        icons602.put("day", "/images/heavysnow.png");
        icons602.put("night", "/images/heavysnow.png");
        
        Map<String, String> icons611 = new HashMap<>();
        icons611.put("day", "/images/snow.png");
        icons611.put("night", "/images/snow.png");
        
        Map<String, String> icons612 = new HashMap<>();
        icons612.put("day", "/images/snow.png");
        icons612.put("night", "/images/snow.png");
        
        Map<String, String> icons613 = new HashMap<>();
        icons613.put("day", "/images/snow.png");
        icons613.put("night", "/images/snow.png");
        
        Map<String, String> icons615 = new HashMap<>();
        icons615.put("day", "/images/snow.png");
        icons615.put("night", "/images/snow.png");
        
        Map<String, String> icons616 = new HashMap<>();
        icons616.put("day", "/images/snow.png");
        icons616.put("night", "/images/snow.png");
        
        Map<String, String> icons620 = new HashMap<>();
        icons620.put("day", "/images/snow.png");
        icons620.put("night", "/images/snow.png");
        
        Map<String, String> icons621 = new HashMap<>();
        icons621.put("day", "/images/snow.png");
        icons621.put("night", "/images/snow.png");
        
        Map<String, String> icons622 = new HashMap<>();
        icons622.put("day", "/images/snow.png");
        icons622.put("night", "/images/snow.png");
        
        Map<String, String> icons701 = new HashMap<>();
        icons701.put("day", "/images/mist.png");
        icons701.put("night", "/images/mist.png");
        
        Map<String, String> icons711 = new HashMap<>();
        icons711.put("day", "/images/mist.png");
        icons711.put("night", "/images/mist.png");
        
        Map<String, String> icons721 = new HashMap<>();
        icons721.put("day", "/images/mist.png");
        icons721.put("night", "/images/mist.png");
        
        Map<String, String> icons731 = new HashMap<>();
        icons731.put("day", "/images/mist.png");
        icons731.put("night", "/images/mist.png");
        
        Map<String, String> icons741 = new HashMap<>();
        icons741.put("day", "/images/mist.png");
        icons741.put("night", "/images/mist.png");
        
        Map<String, String> icons751 = new HashMap<>();
        icons751.put("day", "/images/mist.png");
        icons751.put("night", "/images/mist.png");
        
        Map<String, String> icons761 = new HashMap<>();
        icons761.put("day", "/images/mist.png");
        icons761.put("night", "/images/mist.png");
        
        Map<String, String> icons762 = new HashMap<>();
        icons762.put("day", "/images/mist.png");
        icons762.put("night", "/images/mist.png");
        
        Map<String, String> icons771 = new HashMap<>();
        icons771.put("day", "/images/tornado.png");
        icons771.put("night", "/images/tornado.png");
        
        Map<String, String> icons781 = new HashMap<>();
        icons781.put("day", "/images/tornado.png");
        icons781.put("night", "/images/tornado.png");
        
        Map<String, String> icons800 = new HashMap<>();
        icons800.put("day", "/images/clearsky_day.png");
        icons800.put("night", "/images/clearsky_night.png");
        
        Map<String, String> icons801 = new HashMap<>();
        icons801.put("day", "/images/fewclouds_day.png");
        icons801.put("night", "/images/fewclouds_night.png");
        
        Map<String, String> icons802 = new HashMap<>();
        icons802.put("day", "/images/fewclouds_day.png");
        icons802.put("night", "/images/fewclouds_night.png");
        
        Map<String, String> icons803 = new HashMap<>();
        icons803.put("day", "/images/brokenclouds_day.png");
        icons803.put("night", "/images/brokenclouds_night.png");
        
        Map<String, String> icons804 = new HashMap<>();
        icons804.put("day", "/images/clouds.png");
        icons804.put("night", "/images/clouds.png");
        
        icons.put("200", icons200);
        icons.put("201", icons201);
        icons.put("202", icons202);
        icons.put("210", icons210);
        icons.put("211", icons211);
        icons.put("212", icons212);
        icons.put("221", icons221);
        icons.put("230", icons230);
        icons.put("231", icons231);
        icons.put("232", icons232);
        icons.put("300", icons300);
        icons.put("301", icons301);
        icons.put("302", icons302);
        icons.put("310", icons310);
        icons.put("311", icons311);
        icons.put("312", icons312);
        icons.put("313", icons313);
        icons.put("314", icons314);
        icons.put("321", icons321);
        icons.put("500", icons500);
        icons.put("501", icons501);
        icons.put("502", icons502);
        icons.put("503", icons503);
        icons.put("504", icons504);
        icons.put("511", icons511);
        icons.put("520", icons520);
        icons.put("521", icons521);
        icons.put("522", icons522);
        icons.put("531", icons531);
        icons.put("600", icons600);
        icons.put("601", icons601);
        icons.put("602", icons602);
        icons.put("611", icons611);
        icons.put("612", icons612);
        icons.put("613", icons613);
        icons.put("615", icons615);
        icons.put("616", icons616);
        icons.put("620", icons620);
        icons.put("621", icons621);
        icons.put("622", icons622);
        icons.put("701", icons701);
        icons.put("711", icons711);
        icons.put("721", icons721);
        icons.put("731", icons731);
        icons.put("741", icons741);
        icons.put("751", icons751);
        icons.put("761", icons761);
        icons.put("762", icons762);
        icons.put("771", icons771);
        icons.put("781", icons781);
        icons.put("800", icons800);
        icons.put("801", icons801);
        icons.put("802", icons802);
        icons.put("803", icons803);
        icons.put("804", icons804);
    }

     /**
     * Starts the JavaFX application by setting up the stage and initializing the UI elements.
     * @param stage The primary stage for the application.
     * @throws Exception If an error occurs during application startup.
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("WeatherApp");
        
        state = new ProgramState();
        try {
            state.loadProgramState();
        } catch (Exception e) {
            infoText.setText(e.getMessage());
        }
        
        String latestLocation = state.getLatestCity();
        if (!latestLocation.equals("No search history")) {
            try {
                state.loadWeatherData();
            } catch (Exception e) {
                System.err.println(e);
                state.setCurrentLocation("");
            }
        }
        setStage(); 
        stage.show();
        
        stage.setOnCloseRequest(event -> {
            state.saveProgramState();
        });
    }
    
    /**
     * The main entry point for the JavaFX application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {     
        launch();
    }
     
     /**
     * Sets up the primary stage with the appropriate layout based on the current state.
     */
    private void setStage() {
        String currentLocation = state.getCurrentLocation();
        HourlyWeatherDataEntry currentWeather = state.getCurrentWeather();
        
        if (currentLocation.isEmpty()) {
            VBox searchLayout = getSearchLayout();
            Scene scene2 = new Scene(searchLayout, 400, 600);
            scene2.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene2);
        } else {
            mainLayout = new VBox();
            mainLayout.getChildren().add(getTopBar());
            mainLayout.getChildren().add(getCurrentWeatherBox());
            mainLayout.getChildren().add(getTitleBar("4-day Forecast"));
            mainLayout.getChildren().add(getDailyForecastsBox());
            mainLayout.getChildren().add(getTitleBar("Hourly Forecast"));
            hourlyForecastBox = getHourlyForecastBox(currentWeather.getDate());
            mainLayout.getChildren().add(hourlyForecastBox);
            Scene scene1 = new Scene(mainLayout, 400, 600);
            scene1.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene1);
        }
        stage.setResizable(false);
    }

     /**
     * Creates the top bar containing location information, unit conversion controls
     * and a button to access the search interface.
     * @return The top bar as a ToolBar.
     */ 
    private ToolBar getTopBar() {
        String currentLocation = state.getCurrentLocation();
        ToolBar topBar = new ToolBar();

        Label locationLabel = new Label(currentLocation);
        locationLabel.getStyleClass().add("location-label");
        
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            state.setCurrentLocation("");
            setStage();
        });
        
        Button unitButton = new Button("Imperial/Metric");
        unitButton.setOnAction(e -> {
            state.changeUnits();
            try {
                state.loadWeatherData();
                setStage();
            } catch (Exception ex) {
                System.err.println(ex);
            }
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getItems().addAll(locationLabel, spacer, unitButton, searchButton);

        return topBar;
    }

    /**
     * Creates a VBox containing current weather information.
     * @return The VBox containing current weather information.
     */    
    private VBox getCurrentWeatherBox() {
        HourlyWeatherDataEntry currentWeather = state.getCurrentWeather();
        String currentTempUnit = state.getTempUnits();
        String currentWsUnit = state.getWsUnits();
        String currentHour = currentWeather.getHours();
        
        VBox weatherBox = new VBox();
        weatherBox.getStyleClass().add("current-weather");
        
        String currentDate = currentWeather.getDate();
        Label weatherLabel = new Label("Current weather on " + currentDate 
                + " at " + currentHour + ":00" );
        
        String weatherId = currentWeather.getWeatherId();
        boolean isDayTime = currentWeather.isDayTime();
        ImageView weatherIcon = getWeatherIcon(weatherId, isDayTime);
        weatherIcon.setFitWidth(90);
        weatherIcon.setFitHeight(90);

        String currentTemp = currentWeather.getTemp() + currentTempUnit; 
        Label iconTempLabel = new Label(currentTemp, weatherIcon); 
        iconTempLabel.getStyleClass().add("main-temp");
        
        String feelsLikeText = "Feels like " + currentWeather.getFeelsLike() 
                + currentTempUnit;
        Label feelsLikeLabel = new Label(feelsLikeText);
        
        String windText = "Wind speed: " + currentWeather.getWindSpeed() + " " 
                + currentWsUnit;
        Label windLabel = new Label(windText);
        
        String humidityText = "Humidity: " + currentWeather.getHumidity();
        Label humidityLabel = new Label(humidityText);
        
        HBox windHumidityBox = new HBox(windLabel, humidityLabel);
        windHumidityBox.getStyleClass().add("centered-spaced");
        
        weatherBox.getChildren().addAll(weatherLabel, iconTempLabel, feelsLikeLabel, 
                windHumidityBox);
        return weatherBox;
    }

     /**
     * Retrieves the appropriate weather icon based on weather ID and time of day.
     * @param weatherId The weather ID.
     * @param isDayTime True if it's daytime, false otherwise.
     * @return An ImageView containing the weather icon.
     */
    private ImageView getWeatherIcon(String weatherId, boolean isDayTime) {
        String iconPath = "/images/error.png";
        if (icons.containsKey(weatherId)) {
            Map<String, String> weatherIcons = icons.get(weatherId);
            iconPath = isDayTime ? weatherIcons.get("day") : weatherIcons.get("night");
        }
        ImageView weatherIcon = new ImageView(new Image(WeatherApp.class.getResourceAsStream(iconPath)));
        weatherIcon.setFitWidth(50);
        weatherIcon.setFitHeight(50);
        return weatherIcon;
    }
    
    /**
     * Creates a HBox for displaying a title bar.
     * @param text The text to display in the title bar.
     * @return The HBox containing the title bar.
     */    
    private HBox getTitleBar(String text) {
        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("title-bar");
        Label forecastLabel = new Label(text);
        forecastLabel.getStyleClass().add("location-label");
        titleBar.getChildren().add(forecastLabel);
        return titleBar;
    }
    
     /**
     * Creates a HBox containing daily weather forecasts.
     * @return The HBox containing daily weather forecasts.
     */
    private HBox getDailyForecastsBox() {
        ArrayList<DailyWeatherDataEntry> dailyWeathers = state.getDailyWeathers();
        HBox forecastBox = new HBox();
        forecastBox.getStyleClass().add("daily-forecast");
        
        dailyWeatherBoxes.clear();

        for (var day : dailyWeathers) {      
            VBox dayBox = new VBox();
            dayBox.setAlignment(Pos.CENTER);
            dayBox.setSpacing(5);
            
            String date = day.getDate();
            Label dateLabel = new Label(date);
            String MinMaxTemp = day.getTempMin() + ".." + day.getTempMax();
            Label tempLabel = new Label(MinMaxTemp);
            String weatherId = day.getWeatherId();
            ImageView weatherIcon = getWeatherIcon(weatherId, true);
            
            dayBox.setOnMouseClicked(event -> {
                for (var dailyBox : dailyWeatherBoxes) {
                   dailyBox.getStyleClass().clear();
                }
                dayBox.getStyleClass().add("selected");
                
                mainLayout.getChildren().remove(hourlyForecastBox);
                hourlyForecastBox = getHourlyForecastBox(date);
                mainLayout.getChildren().add(hourlyForecastBox);
            });
            
            dailyWeatherBoxes.add(dayBox);
            dayBox.getChildren().addAll(dateLabel, weatherIcon, tempLabel);
            forecastBox.getChildren().add(dayBox);
        }
        return forecastBox;
    }

     /**
     * Creates a ScrollPane containing hourly weather forecasts for a specific date.
     * @param date The date for which to retrieve hourly forecasts.
     * @return The ScrollPane containing hourly weather forecasts.
     */
    private ScrollPane getHourlyForecastBox(String date) {
        ArrayList<HourlyWeatherDataEntry> hourlyWeathers = state.getHourlyWeathers();
        String currentTempUnit = state.getTempUnits();
        String currentWsUnit = state.getWsUnits();
        
        VBox hourlyForecasts = new VBox();
        hourlyForecasts.getStyleClass().add("hourly-forecast");
        
        int counter = 0;
        for (var hour : hourlyWeathers) {
            if (!date.equals(hour.getDate())) {
                continue;
            }
            GridPane hourBox = new GridPane();
            hourBox.setHgap(10);
            hourBox.getStyleClass().add("centered-spaced");
            
            if (counter % 2 != 0) {
                hourBox.getStyleClass().add("odd-row");
            }
            counter++;
            
            String weatherId = hour.getWeatherId();
            boolean isDayTime = hour.isDayTime();
            ImageView weatherIcon = getWeatherIcon(weatherId, isDayTime);
            
            String time = hour.getHours();
            Label timeLabel = new Label(time);
            
            String temp = hour.getTemp() + currentTempUnit;
            Label tempLabel = new Label(temp);
            
            String wind = "Wind: " + hour.getWindSpeed() + " " + currentWsUnit;
            Label windLabel = new Label(wind);
            
            String humidity = "Humidity: " + hour.getHumidity();
            Label humidityLabel = new Label(humidity);
            
            hourBox.add(timeLabel, 0, 0);
            hourBox.add(weatherIcon, 1, 0);
            hourBox.add(tempLabel, 2, 0);
            hourBox.add(windLabel, 3, 0);
            hourBox.add(humidityLabel, 4, 0);
            
            hourlyForecasts.getChildren().add(hourBox);
        }
        ScrollPane scrollPane = new ScrollPane(hourlyForecasts);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
   /**
    * Creates a VBox for the search layout.
    * @return The VBox containing the search layout.
    */     
    private VBox getSearchLayout() {
        VBox layout = new VBox();
        layout.getStyleClass().add("search-layout");
        
        TextField search = new TextField();
        Button getWeatherButton = new Button("Get Weather");
        Button addFavoriteButton = new Button("Add to favorites");
        getWeatherButton.setOnAction(event -> handleNewSearch(search.getText()));
        addFavoriteButton.setOnAction(event -> {
            
            // Added functionality to deal with trying to add
            // erroneous favorites
            try {
                infoText.setText("");
                if (state.addFavorite(search.getText())) {
                    updateFavoritesPane();
                }
            } catch (Exception e) {
                infoText.setText(e.getMessage());
            }
        });

        HBox favoritesTitle = getTitleBar("Favorites:");
        favoritesPane = getFavoritesGridPane();
        HBox searchHistTitle = getTitleBar("Search History:");
        ListView searchHistoryView = getSearchHistoryView();
        
        layout.getChildren().addAll(infoText, search, getWeatherButton, addFavoriteButton, 
                favoritesTitle, favoritesPane, searchHistTitle, searchHistoryView);
        return layout;
    }
    
    /**
     * Updates the favorites pane with the latest list of favorite locations.
     */    
    private void updateFavoritesPane() {
        favoritesPane.getChildren().clear();
        favoritesPane.getChildren().addAll(getFavoritesGridPane().getChildren());
    }
    
    /**
     * Creates a GridPane containing the list of favorite locations.
     * @return The GridPane containing the list of favorite locations.
     */    
    private GridPane getFavoritesGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("centered-spaced");
        gridPane.setHgap(5);
        gridPane.setVgap(5); 

        TreeSet<String> favorites = state.getFavorites();
        int row = 0;
        for (String favorite : favorites) {
            Label label = new Label(favorite);
            label.getStyleClass().add("favorite");
            label.setOnMouseClicked(event -> handleNewSearch(label.getText()));

            Button removeButton = new Button("Remove");
            removeButton.setOnAction(event -> {
                state.removeFavorite(favorite);
                updateFavoritesPane();
            });

            gridPane.add(label, 0, row); 
            gridPane.add(removeButton, 1, row); 
            row++;
        }
        return gridPane;
    }

    /**
     * Creates a ListView for displaying the search history.
     * @return The ListView containing the search history.
     */    
    private ListView<String> getSearchHistoryView() {
        LinkedList<String> searchHistory = state.getHistory();
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(searchHistory);
        listView.setItems(items);
        listView.setOnMouseClicked(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            handleNewSearch(selectedItem);
        });
        return listView;
    }
    
    /**
     * Handles a new search by updating the current location and loading weather data.
     * @param searchText The text entered for the search.
     */    
    private void handleNewSearch(String searchText) {
        try {
            state.setCurrentLocation(searchText);
            state.loadWeatherData();
            infoText.setText("");
            setStage();
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            state.setCurrentLocation("");
            infoText.setText("Error loading weather data");
        }
        
    }
}