package lockin.fleetflow.deliveries.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lockin.fleetflow.deliveries.dto.*;
import lockin.fleetflow.deliveries.service.IRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Tag(name = "Routes", description = "Plan and manage delivery routes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/routes")
public class RouteController {

    private final IRouteService routeService;

    @Operation(summary = "List all routes")
    @GetMapping
    public List<RouteDTO> findAll() { return routeService.findAll(); }

    @Operation(summary = "Create a route")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RouteDTO create(@Valid @RequestBody RouteDTO dto) { return routeService.createRoute(dto); }

    @Operation(summary = "Get one route")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Route found")
    @ApiResponse(responseCode = "404", description = "Route not in database")
    public RouteDTO get(@PathVariable UUID id) { return routeService.findById(id); }

    @Operation(summary = "Update a PLANNED route")
    @PutMapping("/{id}")
    public RouteDTO update(@PathVariable UUID id, @Valid @RequestBody RouteDTO dto) {
        return routeService.updateRoute(id, dto);
    }

    @Operation(summary = "Delete a PLANNED route")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { routeService.deleteRoute(id); }

    // ---- deliveries of a route (PLANNED routes only) ----

    @Operation(summary = "Add a delivery (one order) to a PLANNED route")
    @PostMapping("/{id}/deliveries")
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryDTO addDelivery(@PathVariable UUID id, @Valid @RequestBody AddDeliveryRequest request) {
        return routeService.addDelivery(id, request);
    }

    @Operation(summary = "List the deliveries of a route, in visiting order")
    @GetMapping("/{id}/deliveries")
    public List<DeliveryDTO> getDeliveries(@PathVariable UUID id) { return routeService.getDeliveries(id); }

    @Operation(summary = "Remove a delivery from a PLANNED route")
    @DeleteMapping("/{id}/deliveries/{deliveryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeDelivery(@PathVariable UUID id, @PathVariable UUID deliveryId) {
        routeService.removeDelivery(id, deliveryId);
    }

    @Operation(summary = "Reorder deliveries by nearest neighbour from the origin (PLANNED only)")
    @PostMapping("/{id}/optimize")
    public RouteDTO optimize(@PathVariable UUID id) { return routeService.optimizeDeliveries(id); }

    // ---- lifecycle ----

    @Operation(summary = "Assign a driver and a vehicle to a PLANNED route")
    @PutMapping("/{id}/assign")
    public RouteDTO assign(@PathVariable UUID id, @Valid @RequestBody AssignRequest request) {
        return routeService.assignRoute(id, request.driverId(), request.vehicleId());
    }

    @Operation(summary = "Start a route (needs deliveries, a driver and a vehicle)")
    @PostMapping("/{id}/start")
    public RouteDTO start(@PathVariable UUID id) { return routeService.startRoute(id); }

    @Operation(summary = "Cancel a PLANNED or IN_PROGRESS route")
    @PostMapping("/{id}/cancel")
    public RouteDTO cancel(@PathVariable UUID id) { return routeService.cancelRoute(id); }

    @Operation(summary = "Progress of a route: counts per delivery status and percent complete")
    @GetMapping("/{id}/progress")
    public RouteProgress progress(@PathVariable UUID id) { return routeService.getRouteProgress(id); }
}
