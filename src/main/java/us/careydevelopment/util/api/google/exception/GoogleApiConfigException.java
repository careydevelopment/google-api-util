package us.careydevelopment.util.api.google.exception;

/**
 * This exception gets thrown when there's a proble instantiating
 * or using GoogleApiConfig.
 */
public class GoogleApiConfigException extends RuntimeException {

    public GoogleApiConfigException(String s) {
        super(s);
    }
}
