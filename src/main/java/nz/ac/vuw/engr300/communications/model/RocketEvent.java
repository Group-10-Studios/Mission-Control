package nz.ac.vuw.engr300.communications.model;

/**
 * Implementation of RocketData that defines incoming data as Events, such as launch, main engine burnout,
 * and parachute deployed.
 */
public class RocketEvent implements RocketData {

    /**
     * Defines the different types of events that can occur
     */
    public enum EventType{
        LAUNCH,
        IGNITION,
        LIFTOFF,
        LAUNCHROD,
        BURNOUT,
        APOGEE,
        EJECTION_CHARGE,
        RECOVERY_DEVICE_DEPLOYMENT,
        GROUND_HIT,
        SIMULATION_END
    }

    private final EventType eventType;
    private final double time;

    public RocketEvent(EventType eventType, double time) {
        this.eventType = eventType;
        this.time = time;
    }

    public EventType getEventType() {
        return eventType;
    }

    /**
     * @inheritDoc
     */
    public double getTime() {
        return time;
    }
}
