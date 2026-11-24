package lockin.fleetflow.deliveries.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lockin.fleetflow.deliveries.domain.Address;

import java.util.UUID;

public record AddDeliveryRequest(
        @NotNull(message = "orderId is mandatory") UUID orderId,
        @NotNull(message = "deliveryAddress is mandatory") @Valid Address deliveryAddress) {
}
