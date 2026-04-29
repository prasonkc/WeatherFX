package app.weather.util;

/**
 * Provides utility methods for converting between temperature and wind speed units.
 *
 * @author Prason KC
 */
public class UnitConverter {

    /**
     * Prevents instantiation of this utility class.
     */
    private UnitConverter() {
    }

    /**
     * Converts a temperature from Kelvin to Celsius.
     *
     * <p>Precondition: {@code temperatureInKelvin} should represent a valid
     * temperature in Kelvin.
     *
     * @param temperatureInKelvin the temperature in Kelvin
     * @return the converted temperature in Celsius
     */
    public static double kelvinToCelsius(double temperatureInKelvin) {
        return temperatureInKelvin - 273.15;
    }

    /**
     * Converts a temperature from Kelvin to Fahrenheit.
     *
     * <p>Precondition: {@code temperatureInKelvin} should represent a valid
     * temperature in Kelvin.
     *
     * @param temperatureInKelvin the temperature in Kelvin
     * @return the converted temperature in Fahrenheit
     */
    public static double kelvinToFahrenheit(double temperatureInKelvin) {
        return (temperatureInKelvin - 273.15) * 9 / 5 + 32;
    }

    /**
     * Converts a wind speed from meters per second to miles per hour.
     *
     * <p>Precondition: {@code speedInMetersPerSecond} should be a valid non-negative
     * value.
     *
     * @param speedInMetersPerSecond the speed in meters per second
     * @return the converted speed in miles per hour
     */
    public static double mpsToMilesPerHour(double speedInMetersPerSecond) {
        return speedInMetersPerSecond * 2.23694;
    }
}
