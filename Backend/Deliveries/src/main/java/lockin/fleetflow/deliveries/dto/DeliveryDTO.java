package lockin.fleetflow.deliveries.dto;

import lockin.fleetflow.deliveries.domain.Address;
import lockin.fleetflow.deliveries.domain.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryDTO(
        UUID id,
        UUID routeId,
        UUID orderId,
        Address deliveryAddress,
        int sequenceNumber,
        DeliveryStatus status,
        LocalDateTime arrivalTime,
        LocalDateTime deliveredAt) {
}
