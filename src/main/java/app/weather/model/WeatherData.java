package app.weather.model;

/**
 * Stores the current weather details returned for a city.
 *
 * @author Prason KC
 */
public class WeatherData {

    /**
     * Name of the city associated with this weather record.
     */
    private final String city;

    /**
     * Current temperature in Kelvin.
     */
    private final double temp;

    /**
     * Minimum temperature in Kelvin.
     */
    private final double minTemp;

    /**
     * Maximum temperature in Kelvin.
     */
    private final double maxTemp;

    /**
     * Feels-like temperature in Kelvin.
     */
    private final double tempFeelsLike;

    /**
     * Humidity percentage reported by the API.
     */
    private final double humidity;

    /**
     * Wind speed in meters per second.
     */
    private final double windSpeed;

    /**
     * Icon code used to render the weather image.
     */
    private final String icon;

    /**
     * Human-readable description of the weather conditions.
     */
    private final String description;

    /**
     * Timestamp recording when the weather data was accessed.
     */
    private final String accessTimestamp;

    /**
     * Local date and time of the selected city.
     */
    private final String cityTimeStamp;

    /**
     * Unix timestamp for the weather observation time.
     */
    private final long observationTime;

    /**
     * Unix timestamp for local sunrise.
     */
    private final long sunriseTime;

    /**
     * Unix timestamp for local sunset.
     */
    private final long sunsetTime;

    /**
     * Creates a weather data record with all values needed by the UI.
     *
     * @param city the city name
     * @param temp the current temperature in Kelvin
     * @param minTemp the minimum temperature in Kelvin
     * @param maxTemp the maximum temperature in Kelvin
     * @param tempFeelsLike the feels-like temperature in Kelvin
     * @param humidity the humidity percentage
     * @param windSpeed the wind speed in meters per second
     * @param icon the icon code for the weather image
     * @param description the weather description text
     * @param accessTimestamp the time when the record was fetched
     * @param cityTimeStamp the local date and time in the city
     * @param observationTime the Unix timestamp of the observation
     * @param sunriseTime the Unix timestamp of sunrise
     * @param sunsetTime the Unix timestamp of sunset
     */
    public WeatherData(
            String city,
            double temp,
            double minTemp,
            double maxTemp,
            double tempFeelsLike,
            double humidity,
            double windSpeed,
            String icon,
            String description,
            String accessTimestamp,
            String cityTimeStamp,
            long observationTime,
            long sunriseTime,
            long sunsetTime) {
        this.city = city;
        this.temp = temp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.tempFeelsLike = tempFeelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.icon = icon;
        this.description = description;
        this.accessTimestamp = accessTimestamp;
        this.cityTimeStamp = cityTimeStamp;
        this.observationTime = observationTime;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
    }

    /**
     * Returns the city name.
     *
     * @return the city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the current temperature in Kelvin.
     *
     * @return the current temperature
     */
    public double getTemp() {
        return temp;
    }

    /**
     * Returns the weather description.
     *
     * @return the weather description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the maximum temperature in Kelvin.
     *
     * @return the maximum temperature
     */
    public double getMaxTemp() {
        return maxTemp;
    }

    /**
     * Returns the feels-like temperature in Kelvin.
     *
     * @return the feels-like temperature
     */
    public double getTempFeelsLike() {
        return tempFeelsLike;
    }

    /**
     * Returns the icon code for the weather condition.
     *
     * @return the weather icon code
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Returns the minimum temperature in Kelvin.
     *
     * @return the minimum temperature
     */
    public double getMinTemp() {
        return minTemp;
    }

    /**
     * Returns the timestamp recording when the data was fetched.
     *
     * @return the access timestamp string
     */
    public String getAccessTimestamp() {
        return accessTimestamp;
    }

    /**
     * Returns the local date and time for the selected city.
     *
     * @return the city's local date-time string
     */
    public String getCityTimestamp() {
        return cityTimeStamp;
    }

    /**
     * Returns the Unix observation timestamp.
     *
     * @return the observation time
     */
    public long getObservationTime() {
        return observationTime;
    }

    /**
     * Returns the Unix sunrise timestamp.
     *
     * @return the sunrise time
     */
    public long getSunriseTime() {
        return sunriseTime;
    }

    /**
     * Returns the Unix sunset timestamp.
     *
     * @return the sunset time
     */
    public long getSunsetTime() {
        return sunsetTime;
    }

    /**
     * Returns the humidity percentage.
     *
     * @return the humidity value
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Returns the wind speed in meters per second.
     *
     * @return the wind speed
     */
    public double getWindSpeed() {
        return windSpeed;
    }
}
