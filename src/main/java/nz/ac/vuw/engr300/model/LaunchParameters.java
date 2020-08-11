package nz.ac.vuw.engr300.model;

public class LaunchParameters {
    public double maximumLaunchAngle;
    public double maximumWindSpeed;
    public double latitude;
    public double longitude;

    public LaunchParameters() {
        this.maximumLaunchAngle = -1;
        this.maximumWindSpeed = -1;
        this.latitude = -41.300442;
        this.longitude = 174.780319;
    }
}
