package nz.ac.vuw.engr300.weather.model;

/**
 * Representation of weather data. This contains the details of the weather at a
 * specific time. This is comparable with other weather data to order the data
 * by time if required in the list.
 * 
 * @author Nathan Duckett
 *
 */
public class WeatherData implements Comparable<WeatherData> {
    private Long timestamp;
    private double windSpeed;
    private double windAngle;
    private double temp;
    private double pressure;
    private double humidity;
    private WeatherConditionData condition;

    /**
     * Create a new instance of WeatherData.
     * 
     * @param timestamp epoch time stamp from the weather API.
     * @param windSpeed Wind speed in km/hr from the weather API.
     * @param windAngle Wind angle representing the direction of the wind.
     * @param temp      Current temperature recorded from the API,
     * @param pressure  Current pressure recorded from the API.
     * @param humidity  Current humidity recorded from the API.
     * @param condition WeatherConditionData containing the information provided in
     *                  the API.
     */
    public WeatherData(long timestamp, double windSpeed, double windAngle, double temp, double pressure,
            double humidity, WeatherConditionData condition) {
        this.timestamp = timestamp;
        this.windSpeed = windSpeed;
        this.windAngle = windAngle;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.condition = condition;
    }

    /**
     * Get the time stamp of this weather data.
     * 
     * @return Long representation of the epoch time
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Get the wind speed of this weather data.
     * 
     * @return Double representation of the wind speed.
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Get the wind angle of this weather data.
     * 
     * @return Double representation of the wind angle.
     */
    public double getWindAngle() {
        return windAngle;
    }

    /**
     * Get the temperature of this weather data.
     * 
     * @return Double representation of the temperature.
     */
    public double getTemp() {
        return temp;
    }

    /**
     * Get the pressure of this weather data.
     * 
     * @return Double representation of the pressure.
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * Get the humidity of this weather data.
     * 
     * @return Double representation of the humidity.
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Get the weather condition in an accessible object.
     * 
     * @return WeatherConditionData containing access to the information of the
     *         current weather from the API.
     */
    public WeatherConditionData getCondition() {
        return condition;
    }

    @Override
    public int compareTo(WeatherData o) {
        return this.timestamp.compareTo(o.getTimestamp());
    }
}
