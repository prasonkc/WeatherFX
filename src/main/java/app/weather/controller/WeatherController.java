package app.weather.controller;

import app.weather.model.ForecastData;
import app.weather.model.WeatherData;
import app.weather.service.WeatherService;
import app.weather.util.TimeUtils;
import app.weather.util.UnitConverter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Coordinates the JavaFX weather interface by responding to user actions and
 * displaying weather data returned by the service layer.
 *
 * @author Prason KC
 */
public class WeatherController {

    /**
     * Text field where the user enters the city to search.
     */
    @FXML
    private TextField cityField;

    /**
     * Label that displays the main temperature reading.
     */
    @FXML
    private Label resultLabel;

    /**
     * Root container whos style changes with the time of day.
     */
    @FXML
    private VBox mainContainer;

    /**
     * Image view used to show the icon for the current weather conditions.
     */
    @FXML
    private ImageView weatherIcon;

    /**
     * Label that displays the current weather description.
     */
    @FXML
    private Label detailsLabel;

    /**
     * Container that holds the forecast cards.
     */
    @FXML
    private HBox forecastBox;

    /**
     * List view that stores recent searches.
     */
    @FXML
    private ListView<String> historyList;

    /**
     * Radio button for displaying temperatures in Celsius.
     */
    @FXML
    private RadioButton celsiusRadio;

    /**
     * Radio button for displaying temperatures in Fahrenheit.
     */
    @FXML
    private RadioButton fahrenheitRadio;

    /**
     * Radio button for displaying wind speed in meters per second.
     */
    @FXML
    private RadioButton metersPerSecondRadio;

    /**
     * Radio button for displaying wind speed in miles per hour.
     */
    @FXML
    private RadioButton milesPerHourRadio;

    /**
     * Toggle group for the temperature unit selection.
     */
    @FXML
    private ToggleGroup unitGroup;

    /**
     * Toggle group for the wind-speed unit selection.
     */
    @FXML
    private ToggleGroup windUnitGroup;

    /**
     * Label that displays the selected city's name.
     */
    @FXML
    private Label cityNameLabel;

    /**
     * Label that displays the selected city's local time.
     */
    @FXML
    private Label localTimeLabel;

    /**
     * Label that displays the feels-like temperature.
     */
    @FXML
    private Label feelsLikeLabel;

    /**
     * Label that displays the current humidity percentage.
     */
    @FXML
    private Label humidityLabel;

    /**
     * Label that displays the current wind speed.
     */
    @FXML
    private Label windLabel;

    /**
     * Label that displays the minimum temperature.
     */
    @FXML
    private Label minTempLabel;

    /**
     * Label that displays the maximum temperature.
     */
    @FXML
    private Label maxTempLabel;

    /**
     * Service object responsible for retrieving weather data.
     */
    private final WeatherService weatherService = new WeatherService();

    /**
     * Cached weather data when the weather display is refreshed.
     */
    private WeatherData currentWeather;

    /**
     * Cached forecast data when the forecast display is refreshed.
     */
    private List<ForecastData> currentForecast;

    /**
     * Initializes the controller after the FXML fields have been injected.
     *
     * <p>Precondition: all required {@code @FXML} fields must be available.
     */
    @FXML
    public void initialize() {
        new Thread(() -> {
            String city = weatherService.getGeolocation();
            Platform.runLater(() -> {
                cityField.setText(city);
                onSearchClick();
            });
        }).start();

        unitGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> {
                    refreshWeatherDisplay();
                    refreshForecastDisplay();
                }
        );
        windUnitGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> refreshWeatherDisplay()
        );
    }

    /**
     * Handles the search action by requesting both current weather and forecast
     * data for the city entered by the user.
     *
     * <p>Precondition: the city text field should contain a non-empty city
     * name.
     */
    @FXML
    public void onSearchClick() {
        String city = cityField.getText();

        if (city.isEmpty()) {
            showErrorPopup("Empty Input", "Please enter a valid city name.");
            return;
        }

        new Thread(() -> {
            try {
                WeatherData data = weatherService.fetchWeather(city);

                Platform.runLater(() -> {
                    currentWeather = data;
                    refreshWeatherDisplay();
                    historyList.getItems().add(
                            0,
                            "[" + data.getAccessTimestamp().split(" - ")[1]
                                    + "] - "
                                    + data.getCity()
                                    + " - "
                                    + formatTemperature(data.getTemp())
                                    + "  "
                                    + data.getDescription()
                    );
                });
            } catch (Exception exception) {
                Platform.runLater(
                        () -> showErrorPopup(
                                "City Not Found",
                                "Could not find weather data for: " + city
                        )
                );
            }
        }).start();

        new Thread(() -> {
            try {
                List<ForecastData> data =
                        weatherService.fetchWeatherForecast(city);
                Platform.runLater(() -> {
                    currentForecast = data;
                    refreshForecastDisplay();
                });
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                Platform.runLater(
                        () -> showErrorPopup(
                                "Forecast Not Found",
                                "Could not find forecast data for: " + city
                        )
                );
            }
        }).start();
    }

    /**
     * Updates the main weather panel with the provided weather data.
     *
     * <p>Precondition: {@code data} must not be {@code null}.
     *
     * @param data the current weather data
     */
    public void handleUI(WeatherData data) {
        currentWeather = data;
        refreshWeatherDisplay();
    }

    /**
     * Refreshes the current weather area using the cached weather data.
     */
    private void refreshWeatherDisplay() {
        if (currentWeather == null) {
            return;
        }

        WeatherData data = currentWeather;
        mainContainer.getStyleClass().removeAll(
                "day-theme",
                "night-theme",
                "morning-theme",
                "evening-theme"
        );

        mainContainer.getStyleClass().add(
                TimeUtils.getThemeClass(
                        data.getObservationTime(),
                        data.getSunriseTime(),
                        data.getSunsetTime()
                )
        );

        try {
            String iconUrl = "https://openweathermap.org/img/wn/"
                    + data.getIcon()
                    + "@2x.png";
            Image weatherImage = new Image(iconUrl, true);

            weatherIcon.setImage(weatherImage);
            resultLabel.setText(formatTemperature(data.getTemp()));
            localTimeLabel.setText(data.getCityTimestamp().split(" ")[1]);
            detailsLabel.setText(data.getDescription().toUpperCase());
            feelsLikeLabel.setText(formatTemperature(data.getTempFeelsLike()));
            humidityLabel.setText(String.format("%.0f%%", data.getHumidity()));
            windLabel.setText(formatWindSpeed(data.getWindSpeed()));
            minTempLabel.setText(formatTemperature(data.getMinTemp()));
            maxTempLabel.setText(formatTemperature(data.getMaxTemp()));
            cityNameLabel.setText(data.getCity().toUpperCase());
        } catch (Exception exception) {
            showErrorPopup("Internal Error", "Some error occurred.");
        }
    }

    /**
     * Refreshes the forecast cards using the cached forecast data.
     */
    private void refreshForecastDisplay() {
        if (currentForecast == null) {
            return;
        }

        forecastBox.getChildren().clear();

        for (ForecastData day : currentForecast) {
            VBox card = new VBox(8);
            card.setAlignment(Pos.CENTER);
            card.getStyleClass().add("forecast-card");

            String rawDate = day.getDateTime().split(" ")[0];
            String formattedDate = rawDate.substring(5);

            Label dateLabel = new Label(formattedDate);
            dateLabel.getStyleClass().add("forecast-date");

            ImageView icon = new ImageView(
                    new Image(
                            "https://openweathermap.org/img/wn/"
                                    + day.getIcon()
                                    + ".png"
                    )
            );
            icon.setFitWidth(45);
            icon.setPreserveRatio(true);

            Label infoLabel = new Label(formatTemperature(day.getTemp()));
            infoLabel.getStyleClass().add("forecast-info");
            infoLabel.setWrapText(true);
            infoLabel.setAlignment(Pos.CENTER);

            Label forecastLabel = new Label(day.getForecast());
            forecastLabel.getStyleClass().add("forecast-info");
            forecastLabel.setWrapText(true);
            forecastLabel.setAlignment(Pos.CENTER);

            card.getChildren().addAll(dateLabel, icon, infoLabel, forecastLabel);
            forecastBox.getChildren().add(card);
        }
    }

    /**
     * Formats a temperature according to the selected temperature unit.
     *
     * @param tempInKelvin the temperature value reported in Kelvin
     * @return the formatted temperature string for the selected unit
     */
    private String formatTemperature(double tempInKelvin) {
        if (fahrenheitRadio.isSelected()) {
            return String.format(
                    "%.1f°F",
                    UnitConverter.kelvinToFahrenheit(tempInKelvin)
            );
        }

        return String.format(
                "%.1f°C",
                UnitConverter.kelvinToCelsius(tempInKelvin)
        );
    }

    /**
     * Formats a wind speed according to the selected wind-speed unit.
     *
     * @param speedInMetersPerSecond the wind speed reported in meters per
     *                               second
     * @return the formatted wind-speed string for the selected unit
     */
    private String formatWindSpeed(double speedInMetersPerSecond) {
        if (milesPerHourRadio.isSelected()) {
            return String.format(
                    "%.1f mph",
                    UnitConverter.mpsToMilesPerHour(speedInMetersPerSecond)
            );
        }

        return String.format("%.1f m/s", speedInMetersPerSecond);
    }

    /**
     * Displays an error message to the user in a dialog.
     *
     * @param title the title shown on the dialog window
     * @param message the message shown inside the dialog
     */
    private void showErrorPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
