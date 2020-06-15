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

    // NOTE: This list has to be in order of the list of required fields in
    // OpenRocketImporter
    private double time;
    private double altitude;
    private double accelerationZ;
    private double velocityZ;
    private double totalVelocity;
    private double totalAcceleration;
    private double velocityY;
    private double accelerationY;
    // Removed as we have no data for it
    // private double velocityX;
    // private double accelerationX;
    private double latitude;
    private double longitude;
    private double angleOfAttack;
    private double rollRate;
    private double pitchRate;
    private double yawRate;

    /**
     * Create a new RocketStatus with the following information received from
     * incoming data.
     *
     * @param time                  Time of this message.
     * @param altitude              Current Altitude of the rocket.
     * @param totalVelocity         The total velocity of the rocket.
     * @param totalAcceleration     The total acceleration of the rocket.
     * @param latitude              The current latitude of the rocket.
     * @param longitude             The current longitude of the rocket.
     * @param angleOfAttack         The angle of attack of the rocket.
     * @param velocityY             The current Y velocity of the rocket.
     * @param accelerationY         The current Y acceleration of the rocket.
     * @param accelerationZ         The current Z acceleration of the rocket.
     * @param velocityZ             The current Z velocity of the rocket.
     * @param rollRate              The current roll rate of the rocket.
     * @param pitchRate             The current pitch rate of the rocket.
     * @param yawRate               The current yaw rate of the rocket.
     */
    public RocketStatus(double time, double altitude, double totalVelocity, double totalAcceleration, double latitude,
                    double longitude, double angleOfAttack, double velocityY, double accelerationY,
                    double accelerationZ, double velocityZ, double rollRate, double pitchRate, double yawRate) {
        this.time = time;
        this.altitude = altitude;
        this.totalVelocity = totalVelocity;
        this.totalAcceleration = totalAcceleration;
        this.latitude = latitude;
        this.longitude = longitude;
        this.angleOfAttack = angleOfAttack;
        this.velocityY = velocityY;
        this.accelerationY = accelerationY;
        this.accelerationZ = accelerationZ;
        this.velocityZ = velocityZ;
        this.rollRate = rollRate;
        this.pitchRate = pitchRate;
        this.yawRate = yawRate;
    }

    /**
     * Create a new RocketStatus with the information received from incoming data in a dynamic nature.
     * 
     * @param values Array of double values to be assigned to each field.
     */
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

    public double getVelocityY() {
        return velocityY;
    }

    public double getAccelerationY() {
        return accelerationY;
    }

    public double getAccelerationZ() {
        return accelerationZ;
    }

    public double getVelocityZ() {
        return velocityZ;
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
