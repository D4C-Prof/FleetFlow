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
import lockin.fleetflow.Vehicules.domain.MaintenanceStatus;
import lockin.fleetflow.Vehicules.domain.MaintenanceType;
import lockin.fleetflow.Vehicules.services.IMaintenanceService;
import lockin.fleetflow.Vehicules.web.dto.MaintenanceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Maintenances", description = "Endpoints for managing vehicle maintenance tasks and history")
@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @Operation(summary = "List all maintenance records", description = "Retrieve all maintenance logs with optional status or type filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved maintenance records",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MaintenanceDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<MaintenanceDTO>> getAllMaintenances(
            @Parameter(description = "Filter by maintenance status") @RequestParam(required = false) MaintenanceStatus status,
            @Parameter(description = "Filter by maintenance type") @RequestParam(required = false) MaintenanceType type) {
        if (status != null) {
            return ResponseEntity.ok(maintenanceService.findByStatus(status));
        }
        if (type != null) {
            return ResponseEntity.ok(maintenanceService.findByType(type));
        }
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    @Operation(summary = "Get a maintenance record by ID", description = "Retrieve a specific maintenance record by its database ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance record found",
                    content = @Content(schema = @Schema(implementation = MaintenanceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Maintenance record not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> getMaintenanceById(
            @Parameter(description = "Unique ID of the maintenance record", example = "60c72b2f9b1d8b2bad745679")
            @PathVariable String id) {
        return ResponseEntity.ok(maintenanceService.findById(id));
    }

    @Operation(summary = "Get maintenances by vehicle ID", description = "Retrieve all maintenance records for a specific vehicle ordered by date descending")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicle maintenance history",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MaintenanceDTO.class))))
    })
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<MaintenanceDTO>> getMaintenancesByVehicleId(
            @Parameter(description = "ID of the vehicle", example = "6b0360ef14d22854183f10a5")
            @PathVariable String vehicleId) {
        return ResponseEntity.ok(maintenanceService.findByVehicleId(vehicleId));
    }

    @Operation(summary = "Create a maintenance record", description = "Schedule or log a new vehicle maintenance record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Maintenance record created successfully",
                    content = @Content(schema = @Schema(implementation = MaintenanceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MaintenanceDTO> createMaintenance(@Valid @RequestBody MaintenanceDTO maintenanceDTO) {
        MaintenanceDTO created = maintenanceService.create(maintenanceDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a maintenance record", description = "Update details of an existing maintenance record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance record updated successfully",
                    content = @Content(schema = @Schema(implementation = MaintenanceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Maintenance record not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> updateMaintenance(
            @Parameter(description = "Unique ID of the maintenance record to update", example = "60c72b2f9b1d8b2bad745679")
            @PathVariable String id,
            @Valid @RequestBody MaintenanceDTO maintenanceDTO) {
        return ResponseEntity.ok(maintenanceService.update(id, maintenanceDTO));
    }

    @Operation(summary = "Delete a maintenance record", description = "Remove a maintenance record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Maintenance record successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Maintenance record not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteMaintenance(
            @Parameter(description = "Unique ID of the maintenance record to delete", example = "60c72b2f9b1d8b2bad745679")
            @PathVariable String id) {
        maintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
