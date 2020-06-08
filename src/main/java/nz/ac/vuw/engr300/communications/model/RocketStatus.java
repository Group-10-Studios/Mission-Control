package nz.ac.vuw.engr300.communications.model;

import java.lang.reflect.Field;

/**
 * Implementation of RocketData that defines incoming data as status updates,
 * E.G. just providing information about the different measured metrics such as
 * altitude, vertical velocity and many others.
 *
 * @author Tim Salisbury
 */
public class RocketStatus implements RocketData {

    // NOTE: This list has to be in order of the list of required fields in OpenRocketImporter
    private double time;
    private double altitude;
    private double verticalAcceleration;
    private double verticalVelocity;
    private double totalVelocity;
    private double totalAcceleration;
    private double lateralVelocity;
    private double lateralAcceleration;
    // Removed as we have no data for it
//    private double longitudinalVelocity;
//    private double longitudinalAcceleration;
    private double latitude;
    private double longitude;
    private double angleOfAttack;
    private double rollRate;
    private double pitchRate;
    private double yawRate;


    /**
     * Create a new RocketStatus with the following information received from incoming data.
     *
     * @param time              Time of this message.
     * @param altitude          Current Altitude of the rocket
     * @param totalVelocity     Total Velocity of the rocket
     * @param totalAcceleration Total Acceleration of the rocket
     * @param latitude          Current latitude of the rocket
     * @param longitude         Current longitude of the rocket
     * @param angleOfAttack     Current angle the rocket is traveling
     */
    public RocketStatus(double time, double altitude, double totalVelocity,
                        double totalAcceleration, double latitude, double longitude,
                        double angleOfAttack, double lateralVelocity, double lateralAcceleration,
                        double verticalAcceleration, double verticalVelocity,
                        double rollRate, double pitchRate, double yawRate) {
        this.time = time;
        this.altitude = altitude;
        this.totalVelocity = totalVelocity;
        this.totalAcceleration = totalAcceleration;
        this.latitude = latitude;
        this.longitude = longitude;
        this.angleOfAttack = angleOfAttack;
        this.lateralVelocity = lateralVelocity;
        this.lateralAcceleration = lateralAcceleration;
        this.verticalAcceleration = verticalAcceleration;
        this.verticalVelocity = verticalVelocity;
        this.rollRate = rollRate;
        this.pitchRate = pitchRate;
        this.yawRate = yawRate;
    }

    public RocketStatus(double... values) {
        Class<? extends RocketStatus> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (values.length != fields.length) {
            throw new RuntimeException("Incorrect number of values passed to Rocket Status!");
        }
        // Loop through and dynamically assign
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].set(this, values[i]);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access specific field!", e);
            }
        }
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

    public double getLateralVelocity() {
        return lateralVelocity;
    }

    public double getLateralAcceleration() {
        return lateralAcceleration;
    }

    public double getVerticalAcceleration() {
        return verticalAcceleration;
    }

    public double getVerticalVelocity() {
        return verticalVelocity;
    }

    public double getRollRate() {
        return rollRate;
    }

    public double getPitchRate() {
        return pitchRate;
    }

    public double getYawRate() {
        return yawRate;
    }
}
