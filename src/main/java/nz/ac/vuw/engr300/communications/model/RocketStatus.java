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
    private final double totalVelocity;
    private final double totalAcceleration;
    private final double latitude;
    private final double longitude;
    private final double angleOfAttack;

    public RocketStatus(double time, double altitude, double totalVelocity, double totalAcceleration, double latitude, double longitude, double angleOfAttack) {
        this.time = time;
        this.altitude = altitude;
        this.totalVelocity = totalVelocity;
        this.totalAcceleration = totalAcceleration;
        this.latitude = latitude;
        this.longitude = longitude;
        this.angleOfAttack = angleOfAttack;
    }

    @Override
    public double getTime() {
        return time;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getTotalVelocity() {
        return totalVelocity;
    }

    public double getTotalAcceleration() {
        return totalAcceleration;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAngleOfAttack() {
        return angleOfAttack;
    }
}
