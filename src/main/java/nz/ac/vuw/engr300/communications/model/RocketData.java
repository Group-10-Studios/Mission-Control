package nz.ac.vuw.engr300.communications.model;

/**
 * Base interface for all types of data that can come from the rocket. This should expand in the future to
 * provide more functionality, but for now the only shared attribute is the time at which the "event" of the data
 * occurred.
 *
 * @author Tim Salisbury
 */
public interface RocketData {

    /**
     * Gets the time of that which the event occurred.
     *
     * @return  The time.
     */
    double getTime();
}
