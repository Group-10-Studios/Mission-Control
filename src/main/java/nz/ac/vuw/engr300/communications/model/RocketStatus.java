package nz.ac.vuw.engr300.communications.model;

/**
 * Implementation of RocketData that defines incoming data as status updates,
 * E.G. just providing information about the different measured metrics such as
 * altitude, vertical velocity and many others.
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
    private final double lateralVelocity;
    private final double lateralAcceleration;
    private final double longitudinalVelocity;
    private final double longitudinalAcceleration;
    private final double verticalAcceleration;
    private final double verticalVelocity;
    private final double rollRate;
    private final double pitchRate;
    private final double yawRate;


    /**
     * Create a new RocketStatus with the following information received from incoming data.
     * @param time Time of this message.
     * @param altitude Current Altitude of the rocket
     * @param totalVelocity Total Velocity of the rocket
     * @param totalAcceleration Total Acceleration of the rocket
     * @param latitude Current latitude of the rocket
     * @param longitude Current longitude of the rocket
     * @param angleOfAttack Current angle the rocket is traveling
     */
    public RocketStatus(double time, double altitude, double totalVelocity,
                        double totalAcceleration, double latitude, double longitude,
                        double angleOfAttack, double lateralVelocity, double lateralAcceleration,
                        double longitudinalVelocity, double longitudinalAcceleration,
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
        this.longitudinalVelocity = longitudinalVelocity;
        this.longitudinalAcceleration = longitudinalAcceleration;
        this.verticalAcceleration = verticalAcceleration;
        this.verticalVelocity = verticalVelocity;
        this.rollRate = rollRate;
        this.pitchRate = pitchRate;
        this.yawRate = yawRate;
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

    public double getLongitudinalVelocity() {
        return longitudinalVelocity;
    }

    public double getLongitudinalAcceleration() {
        return longitudinalAcceleration;
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
