package lockin.fleetflow.deliveries.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lockin.fleetflow.deliveries.dto.RouteDTO;
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
}