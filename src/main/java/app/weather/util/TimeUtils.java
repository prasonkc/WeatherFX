package app.weather.util;

/**
 * Provides helper utility methods for application.
 *
 * @author Prason KC
 */
public class TimeUtils {

    /**
     * Number of seconds after sunrise that should still use the morning theme.
     */
    private static final long MORNING_WINDOW_SECONDS = 4 * 60 * 60;

    /**
     * Number of seconds before sunset that should use the evening theme.
     */
    private static final long EVENING_WINDOW_SECONDS = 3 * 60 * 60;

    /**
     * Prevents instantiation of this utility class.
     */
    private TimeUtils() {
    }

    /**
     * Determines whether an observation time falls during daylight hours.
     *
     * <p>Precondition: the timestamps should all be Unix timestamps measured
     * in seconds. If any value is invalid, the method treats the
     * result as daytime by default.
     *
     * @param observationTime the time of the weather observation
     * @param sunriseTime the sunrise time for the same location
     * @param sunsetTime the sunset time for the same location
     * @return {@code true} when the observation occurs after sunrise and before
     *         sunset, or when the inputs are incomplete
     */
    public static boolean isDaytime(
            long observationTime,
            long sunriseTime,
            long sunsetTime) {
        if (observationTime <= 0 || sunriseTime <= 0 || sunsetTime <= 0) {
            return true;
        }

        return observationTime >= sunriseTime
                && observationTime < sunsetTime;
    }

    /**
     * Chooses the CSS theme class that best matches the supplied times.
     *
     * <p>Precondition: the timestamps should all be Unix timestamps measured
     * in seconds. If any value is invalid, the method falls back to
     * the daytime theme.
     *
     * @param observationTime the time of the weather observation
     * @param sunriseTime the sunrise time for the same location
     * @param sunsetTime the sunset time for the same location
     * @return one of {@code morning-theme}, {@code day-theme},
     *         {@code evening-theme}, or {@code night-theme}
     */
    public static String getThemeClass(
            long observationTime,
            long sunriseTime,
            long sunsetTime) {
        if (observationTime <= 0 || sunriseTime <= 0 || sunsetTime <= 0) {
            return "day-theme";
        }

        if (observationTime < sunriseTime || observationTime >= sunsetTime) {
            return "night-theme";
        }

        if (observationTime < sunriseTime + MORNING_WINDOW_SECONDS) {
            return "morning-theme";
        }

        if (observationTime >= sunsetTime - EVENING_WINDOW_SECONDS) {
            return "evening-theme";
        }

        return "day-theme";
    }
}
