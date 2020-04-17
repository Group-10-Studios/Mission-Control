package nz.ac.vuw.engr300.weather.model;

/**
 * Representation of weather data. This contains the details of the weather at a specific time.
 * This is comparable with other weather data to order the data by time if required in the list.
 * 
 * @author Nathan Duckett
 *
 */
public class WeatherData implements Comparable<WeatherData> {
	private Long timestamp;
	private double windSpeed;
	private double windAngle;

	/**
	 * Create a new instance of WeatherData.
	 * 
	 * @param timestamp epoch time stamp from the weather API.
	 * @param windSpeed Wind speed in km/hr from the weather API.
	 * @param windAngle Wind angle representing the direction of the wind.
	 */
	public WeatherData(long timestamp, double windSpeed, double windAngle) {
		this.timestamp = timestamp;
		this.windSpeed = windSpeed;
		this.windAngle = windAngle;
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
	
	@Override
	public int compareTo(WeatherData o) {
		return this.timestamp.compareTo(o.getTimestamp());
	}
}
