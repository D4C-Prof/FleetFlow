package lockin.fleetflow.deliveries.exception;

/** Thrown when an operation violates a business rule (wrong status, empty route...). Mapped to HTTP 409. */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
