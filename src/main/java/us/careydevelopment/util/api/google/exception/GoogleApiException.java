package us.careydevelopment.util.api.google.exception;

/**
 * This exception gets thrown when there's any kind of problem
 * accessing the downstream services.
 */
public class GoogleApiException extends RuntimeException {

    public GoogleApiException(String s) {
        super(s);
    }
}
