package lockin.fleetflow.deliveries.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lockin.fleetflow.deliveries.domain.Address;
import lockin.fleetflow.deliveries.domain.RouteStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RouteDTO {
    private UUID id;                 // read-only: set by the server
    private UUID driverId;           // read-only for now (set by "assign" later)
    private UUID vehicleId;          // read-only for now
    @NotNull(message = "Origin is mandatory")
    @Valid
    private Address origin;
    @NotNull(message = "Scheduled date is mandatory")
    private LocalDateTime scheduledDate;
    private Double estimatedDistance; // read-only
    private RouteStatus status;       // read-only
    private List<DeliveryDTO> deliveries; // read-only: use /routes/{id}/deliveries to change
}