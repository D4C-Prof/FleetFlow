package lockin.fleetflow.deliveries.exception;

/** Thrown when the caller is not allowed to do this (e.g. not the assigned driver). Mapped to HTTP 403. */
public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
