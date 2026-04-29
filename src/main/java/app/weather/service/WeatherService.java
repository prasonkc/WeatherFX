package app.weather.service;

import app.weather.model.ForecastData;
import app.weather.model.WeatherData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves current weather conditions, and forecast data.
 *
 * <p>The service expects a valid OpenWeather API key.
 *
 * @author Prason KC
 */
public class WeatherService {
    private static String API_KEY = "";

    /**
     * Read an API key from the file. Must have a valid OpenWeather API KEY
     */
    static {
        try {
            API_KEY = Files.readString(Paths.get("src/main/resources/API_KEY")).trim();
        } catch (IOException e) {
            System.err.println("CRITICAL: API Key not found in file!");
            API_KEY = "";
        }
    }

    /**
     * Base URL to request current weather information.
     */
    private static final String BASE_WEATHER_URL =
            "https://api.openweathermap.org/data/2.5/weather?q=";

    /**
     * Base URL used to request forecast information.
     */
    private static final String BASE_FORECAST_URL =
            "https://api.openweathermap.org/data/2.5/forecast?q=";

    /**
     * Retrieves current weather data for a city.
     *
     * <p>Precondition: {@code city} must name a city accepted by the
     * OpenWeather API, and a valid API key must be configured before the
     * method is called.
     *
     * @param city the city whose current weather should be retrieved
     * @return a {@code WeatherData} object containing the current weather conditions
     * @throws IOException if the API request fails or the response is invalid
     * @throws InterruptedException if the HTTP request is interrupted
     */
    public WeatherData fetchWeather(String city)
            throws IOException, InterruptedException {
        try {
            String urlString = BASE_WEATHER_URL + city + "&appid=" + API_KEY;

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .build();
            HttpResponse<String> response = httpClient.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            JSONObject json = new JSONObject(response.body());
            if (response.statusCode() != 200 || !json.has("main")) {
                throw new IOException(
                        "Failed to fetch current weather for: " + city
                );
            }

            double temperature = json.getJSONObject("main").getDouble("temp");
            double minimumTemperature = json.getJSONObject("main")
                    .getDouble("temp_min");
            double maximumTemperature = json.getJSONObject("main")
                    .getDouble("temp_max");
            double feelsLikeTemperature = json.getJSONObject("main")
                    .getDouble("feels_like");
            double humidity = json.getJSONObject("main").getDouble("humidity");
            double windSpeed = json.getJSONObject("wind").getDouble("speed");
            String icon = json.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("icon");
            String description = json.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss"
            );
            String accessTimestamp = city + " - " + now.format(formatter);

            int timezoneOffsetSeconds = json.getInt("timezone");
            long observationTime = json.getLong("dt");
            long sunriseTime = json.getJSONObject("sys").getLong("sunrise");
            long sunsetTime = json.getJSONObject("sys").getLong("sunset");

            String cityLocalTime = Instant.ofEpochSecond(observationTime)
                    .atOffset(
                            java.time.ZoneOffset.ofTotalSeconds(
                                    timezoneOffsetSeconds
                            )
                    )
                    .toLocalDateTime()
                    .format(formatter);

            return new WeatherData(
                    city,
                    temperature,
                    minimumTemperature,
                    maximumTemperature,
                    feelsLikeTemperature,
                    humidity,
                    windSpeed,
                    icon,
                    description,
                    accessTimestamp,
                    cityLocalTime,
                    observationTime,
                    sunriseTime,
                    sunsetTime
            );
        } catch (JSONException exception) {
            throw new IOException(
                    "Invalid weather data received for: " + city,
                    exception
            );
        }
    }

    /**
     * Retrieves a short-term forecast for a city.
     *
     * <p>Precondition: {@code city} must name a city accepted by the
     * OpenWeather API, and a valid API key must be configured before the
     * method is called.
     *
     * @param city the city whose forecast should be retrieved
     * @return a list containing up to three future forecast entries
     * @throws IOException if the API request fails or the response is invalid
     * @throws InterruptedException if the HTTP request is interrupted
     */
    public List<ForecastData> fetchWeatherForecast(String city)
            throws IOException, InterruptedException {
        try {
            List<ForecastData> forecastEntries = new ArrayList<>();
            String url = BASE_FORECAST_URL + city + "&appid=" + API_KEY;

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url))
                    .build();
            HttpResponse<String> response = httpClient.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            JSONObject json = new JSONObject(response.body());
            if (response.statusCode() != 200 || !json.has("list")) {
                throw new IOException("Failed to fetch forecast for: " + city);
            }

            JSONArray forecastList = json.getJSONArray("list");

            /*
             * The API returns one entry every three hours. These indexes target
             * roughly 24, 48, and 72 hours into the future.
             */
            int[] forecastIndexes = {8, 16, 24};

            for (int forecastIndex : forecastIndexes) {
                if (forecastIndex >= forecastList.length()) {
                    break;
                }

                JSONObject dayData = forecastList.getJSONObject(forecastIndex);
                double temperature = dayData.getJSONObject("main")
                        .getDouble("temp");
                String icon = dayData.getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("icon");
                String forecast = dayData.getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("description");
                String dateTime = dayData.getString("dt_txt");

                forecastEntries.add(
                        new ForecastData(dateTime, temperature, forecast, icon)
                );
            }

            return forecastEntries;
        } catch (JSONException exception) {
            throw new IOException(
                    "Invalid forecast data received for: " + city,
                    exception
            );
        }
    }

    /**
     * Attempts to determine the user's city from the current public IP
     * address.
     *
     * <p>Precondition: the IP geolocation service must be reachable if the
     * detected city is expected to be accurate.
     *
     * @return the detected city name, or {@code London} if detection fails
     */
    public String getGeolocation() {
        try {
            String url = "http://ip-api.com/json/";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            JSONObject json = new JSONObject(response.body());
            if (response.statusCode() == 200
                    && json.has("status")
                    && "success".equals(json.getString("status"))) {
                return json.getString("city");
            }
        } catch (Exception exception) {
            System.out.println("Defaulting to London.");
        }

        return "London";
    }
}
