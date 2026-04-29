package app.weather.model;

/**
 * Stores a single forecast entry returned by the weather API.
 *
 * @author Prason KC
 */
public class ForecastData {

    /**
     * Date and time label.
     */
    private final String dateTime;

    /**
     * Forecast temperature in Kelvin.
     */
    private final double temp;

    /**
     * Description of the forecasted weather conditions.
     */
    private final String forecast;

    /**
     * Icon code used to render the forecast image.
     */
    private final String icon;

    /**
     * Creates a forecast data record.
     *
     * @param dateTime the date and time string for the forecast entry
     * @param temp the forecast temperature in Kelvin
     * @param forecast the description of the weather conditions
     * @param icon the icon code for the weather image
     */
    public ForecastData(
            String dateTime,
            double temp,
            String forecast,
            String icon) {
        this.dateTime = dateTime;
        this.temp = temp;
        this.forecast = forecast;
        this.icon = icon;
    }

    /**
     * Returns the forecast date and time string.
     *
     * @return the forecast date and time
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Returns the forecast temperature in Kelvin.
     *
     * @return the forecast temperature
     */
    public double getTemp() {
        return temp;
    }

    /**
     * Returns the icon code for the forecast conditions.
     *
     * @return the forecast icon code
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Returns the forecast description text.
     *
     * @return the forecast description
     */
    public String getForecast() {
        return forecast;
    }
}
