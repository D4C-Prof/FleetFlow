package lockin.fleetflow.Vehicules.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lockin.fleetflow.Vehicules.domain.VehicleStatus;
import lockin.fleetflow.Vehicules.services.IVehicleService;
import lockin.fleetflow.Vehicules.web.dto.VehicleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vehicles", description = "Endpoints for managing fleet vehicles")
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final IVehicleService vehicleService;

    @Operation(summary = "List all vehicles", description = "Retrieve all vehicles with optional filters for status or brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicles",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VehicleDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(
            @Parameter(description = "Filter by vehicle status") @RequestParam(required = false) VehicleStatus status,
            @Parameter(description = "Filter by vehicle brand") @RequestParam(required = false) String brand) {
        if (status != null) {
            return ResponseEntity.ok(vehicleService.findByStatus(status));
        }
        if (brand != null && !brand.isBlank()) {
            return ResponseEntity.ok(vehicleService.findByBrand(brand));
        }
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @Operation(summary = "Get a vehicle by ID", description = "Retrieve detailed information of a vehicle by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(
            @Parameter(description = "Unique ID of the vehicle", example = "60c72b2f9b1d8b2bad745678")
            @PathVariable String id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @Operation(summary = "Get a vehicle by registration", description = "Retrieve a vehicle by its registration/license number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle found",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content)
    })
    @GetMapping("/registration/{registration}")
    public ResponseEntity<VehicleDTO> getVehicleByRegistration(
            @Parameter(description = "Registration / License plate number", example = "138 Tun 41925")
            @PathVariable String registration) {
        return ResponseEntity.ok(vehicleService.findByRegistration(registration));
    }

    @Operation(summary = "Create a new vehicle", description = "Register a new vehicle in the fleet management system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle successfully created",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content)
    })
    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO created = vehicleService.create(vehicleDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a vehicle", description = "Update the attributes of an existing vehicle by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle successfully updated",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @Parameter(description = "Unique ID of the vehicle to update", example = "60c72b2f9b1d8b2bad745678")
            @PathVariable String id,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.update(id, vehicleDTO));
    }

    @Operation(summary = "Delete a vehicle", description = "Remove a vehicle from the fleet by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehicle successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteVehicle(
            @Parameter(description = "Unique ID of the vehicle to delete", example = "60c72b2f9b1d8b2bad745678")
            @PathVariable String id) {
        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
