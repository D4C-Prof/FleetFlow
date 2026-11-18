package lockin.fleetflow.drivers.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message, long number) {
        super("Could not find " + message + ". ID Problem: " + number);
    }
}
