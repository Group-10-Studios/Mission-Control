package nz.ac.vuw.engr300.weather.model;

/**
 * Represents the detailed weather condition, including Weather type and Weather
 * description for further detail.
 * 
 * @author Nathan Duckett
 *
 */
public class WeatherConditionData {
  private String state;
  private String desc;

  /**
   * Create a new WeatherConditionData instance with the following information.
   * 
   * @param weatherState       State of the weather extracted from the data.
   * @param weatherDescription Description of the weather state extracted.
   */
  public WeatherConditionData(String weatherState, String weatherDescription) {
    this.state = weatherState;
    this.desc = weatherDescription;
  }

  /**
   * Get the current state of the Weather.
   * 
   * @return String stating the state of the Weather.
   */
  public String getWeatherState() {
    return state;
  }

  /**
   * Get the description of the current state of the Weather.
   * 
   * @return String describing the state of the Weather.
   */
  public String getWeatherDescription() {
    return desc;
  }

}
