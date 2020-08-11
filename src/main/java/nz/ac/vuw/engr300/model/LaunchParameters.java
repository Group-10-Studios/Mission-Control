package nz.ac.vuw.engr300.model;

/**
 * This class represents the different Launch Parameters.
 *
 * @author Ahad Rahman, Tim Salisbury
 */
public class LaunchParameters {
    public double maximumLaunchAngle;
    public double maximumWindSpeed;
    public double latitude;
    public double longitude;

    /**
     * Default values are set when the LaunchParameters object is created.
     */
    public LaunchParameters() {
        this.maximumLaunchAngle = -1;
        this.maximumWindSpeed = -1;
        this.latitude = -41.300442;
        this.longitude = 174.780319;
    }
}
