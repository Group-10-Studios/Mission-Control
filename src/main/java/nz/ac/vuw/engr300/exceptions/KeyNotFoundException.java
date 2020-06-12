package nz.ac.vuw.engr300.exceptions;

/**
 * Basic exception used for when a key from keys.json is not found or keys.json is just not there.
 *
 * @author Tim Salisbury
 */
public class KeyNotFoundException extends Exception {

    public KeyNotFoundException(String message) {
        super(message);
    }

    public KeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
