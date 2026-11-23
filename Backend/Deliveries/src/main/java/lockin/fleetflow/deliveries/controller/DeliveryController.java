package lockin.fleetflow.deliveries.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lockin.fleetflow.deliveries.dto.*;
import lockin.fleetflow.deliveries.service.IDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Tag(name = "Deliveries", description = "Delivery tracking, incidents and statistics")
@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {

    // Until the gateway/auth exists, the caller identity is sent in this header.
    // Later the gateway fills it in from the authenticated user.
    private static final String USER_HEADER = "X-User-Id";

    private final IDeliveryService deliveryService;

    @Operation(summary = "Update a delivery status (assigned driver only, forward-only)")
    @PutMapping("/{id}/status")
    public DeliveryDTO updateStatus(@PathVariable UUID id,
                                    @Parameter(description = "Acting driver id") @RequestHeader(USER_HEADER) UUID driverId,
                                    @Valid @RequestBody StatusUpdateRequest request) {
        return deliveryService.updateDeliveryStatus(id, driverId, request.status());
    }

    @Operation(summary = "Report an incident on a delivery (assigned driver only)")
    @PostMapping("/{id}/incidents")
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentDTO reportIncident(@PathVariable UUID id,
                                      @Parameter(description = "Acting driver id") @RequestHeader(USER_HEADER) UUID driverId,
                                      @Valid @RequestBody IncidentRequest request) {
        return deliveryService.reportIncident(id, driverId, request.type(), request.description());
    }

    @Operation(summary = "List the incidents of a delivery")
    @GetMapping("/{id}/incidents")
    public List<IncidentDTO> incidents(@PathVariable UUID id) { return deliveryService.getDeliveryIncidents(id); }

    @Operation(summary = "Progress of the route this delivery belongs to")
    @GetMapping("/{id}/progress")
    public RouteProgress progress(@PathVariable UUID id) { return deliveryService.getDeliveryProgress(id); }

    @Operation(summary = "All deliveries of a driver (also used by MS4)")
    @GetMapping("/driver/{driverId}")
    public List<DeliveryDTO> driverDeliveries(@PathVariable UUID driverId) {
        return deliveryService.getDriverDeliveries(driverId);
    }

    @Operation(summary = "Delivery statistics of a driver (consumed by MS4)")
    @GetMapping("/statistics")
    public StatisticsDTO statistics(@RequestParam UUID driverId) {
        return deliveryService.getDeliveryStatistics(driverId);
    }
}
