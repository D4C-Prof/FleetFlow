package lockin.fleetflow.deliveries.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, UUID id) {
        super("Could not find " + resource + " with id " + id);
    }
}