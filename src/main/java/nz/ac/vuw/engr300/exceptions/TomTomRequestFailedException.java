package nz.ac.vuw.engr300.exceptions;

public class TomTomRequestFailedException extends RuntimeException {

    public TomTomRequestFailedException(String message) {
        super(message);
    }

    public TomTomRequestFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
