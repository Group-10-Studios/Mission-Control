package nz.ac.vuw.engr300.communications.model;

/**
 * Implementation of RocketData that defines incoming data as status updates, E.G. just providing information about
 * the different measured metrics such as altitude, vertical velocity and many others.
 */
public class RocketStatus implements RocketData {

    private final double time;
    private final double altitude;
    private final double verticalVelocity;
    private final double verticalAcceleration;
    private final double totalVelocity;
    private final double totalAcceleration;
    private final double distanceEastOfLaunch;
    private final double distanceNorthOfLaunch;

    public RocketStatus(double time, double altitude, double verticalVelocity, double verticalAcceleration,
                        double totalVelocity, double totalAcceleration, double distanceEastOfLaunch,
                        double distanceNorthOfLaunch) {
        this.time = time;
        this.altitude = altitude;
        this.verticalVelocity = verticalVelocity;
        this.verticalAcceleration = verticalAcceleration;
        this.totalVelocity = totalVelocity;
        this.totalAcceleration = totalAcceleration;
        this.distanceEastOfLaunch = distanceEastOfLaunch;
        this.distanceNorthOfLaunch = distanceNorthOfLaunch;
    }

    public double getTime() {
        return time;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getVerticalVelocity() {
        return verticalVelocity;
    }

    public double getVerticalAcceleration() {
        return verticalAcceleration;
    }

    public double getTotalVelocity() {
        return totalVelocity;
    }

    public double getTotalAcceleration() {
        return totalAcceleration;
    }

    public double getDistanceEastOfLaunch() {
        return distanceEastOfLaunch;
    }

    public double getDistanceNorthOfLaunch() {
        return distanceNorthOfLaunch;
    }
}
