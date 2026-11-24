package lockin.fleetflow.deliveries.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lockin.fleetflow.deliveries.domain.IncidentType;

public record IncidentRequest(
        @NotNull(message = "type is mandatory") IncidentType type,
        @Size(max = 500, message = "description is limited to 500 characters") String description) {
}
