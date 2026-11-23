package lockin.fleetflow.deliveries.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignRequest(
        @NotNull(message = "driverId is mandatory") UUID driverId,
        @NotNull(message = "vehicleId is mandatory") UUID vehicleId) {
}
