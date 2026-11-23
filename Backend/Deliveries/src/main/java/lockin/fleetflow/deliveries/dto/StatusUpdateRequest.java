package lockin.fleetflow.deliveries.dto;

import jakarta.validation.constraints.NotNull;
import lockin.fleetflow.deliveries.domain.DeliveryStatus;

public record StatusUpdateRequest(@NotNull(message = "status is mandatory") DeliveryStatus status) {
}
