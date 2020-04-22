package nz.ac.vuw.engr300.communications.model;

import java.util.Objects;

/**
 * Implementation of RocketData that defines incoming data as status updates, E.G. just providing information about
 * the different measured metrics such as altitude, vertical velocity and many others.
 *
 * @author Tim Salisbury
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RocketStatus that = (RocketStatus) o;
        return Double.compare(that.time, time) == 0 &&
                Double.compare(that.altitude, altitude) == 0 &&
                Double.compare(that.verticalVelocity, verticalVelocity) == 0 &&
                Double.compare(that.verticalAcceleration, verticalAcceleration) == 0 &&
                Double.compare(that.totalVelocity, totalVelocity) == 0 &&
                Double.compare(that.totalAcceleration, totalAcceleration) == 0 &&
                Double.compare(that.distanceEastOfLaunch, distanceEastOfLaunch) == 0 &&
                Double.compare(that.distanceNorthOfLaunch, distanceNorthOfLaunch) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, altitude, verticalVelocity, verticalAcceleration, totalVelocity, totalAcceleration, distanceEastOfLaunch, distanceNorthOfLaunch);
    }
}
