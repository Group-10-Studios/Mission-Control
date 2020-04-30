package nz.ac.vuw.engr300.communications.model;

import java.util.Objects;

/**
 * Implementation of RocketData that defines incoming data as status updates, E.G. just providing information about
 * the different measured metrics such as altitude, vertical velocity and many others.
 *
 * @author Tim Salisbury
 */
public class RocketStatus implements RocketData {

    private double time;
    private double altitude;
    private double totalVelocity;
    private double totalAcceleration;
    private double latitude;
    private double longitude;
    private double angleOfAttack;

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

    public void setTime(double time) {
        this.time = time;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getTotalVelocity() {
        return totalVelocity;
    }

    public void setTotalVelocity(double totalVelocity) {
        this.totalVelocity = totalVelocity;
    }

    public double getTotalAcceleration() {
        return totalAcceleration;
    }

    public void setTotalAcceleration(double totalAcceleration) {
        this.totalAcceleration = totalAcceleration;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAngleOfAttack() {
        return angleOfAttack;
    }

    public void setAngleOfAttack(double angleOfAttack) {
        this.angleOfAttack = angleOfAttack;
    }
}
