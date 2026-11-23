package lockin.fleetflow.Vehicules.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lockin.fleetflow.Vehicules.domain.MaintenanceStatus;
import lockin.fleetflow.Vehicules.domain.MaintenanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object representing Vehicle Maintenance")
public class MaintenanceDTO {

    @Schema(description = "Unique identifier of the maintenance record", example = "60c72b2f9b1d8b2bad745679")
    private String id;

    @NotBlank(message = "Vehicle ID is mandatory")
    @Schema(description = "Identifier of the vehicle under maintenance", example = "6b0360ef14d22854183f10a5", requiredMode = Schema.RequiredMode.REQUIRED)
    private String vehicleId;

    @NotNull(message = "Maintenance type is mandatory")
    @Schema(description = "Type of maintenance performed", example = "CURATIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    private MaintenanceType type;

    @Schema(description = "Detailed description of the maintenance reason or work done", example = "Engine misfire diagnostics and spark plug replacement")
    private String description;

    @NotNull(message = "Maintenance date is mandatory")
    @Schema(description = "Date and time of the maintenance", example = "2026-11-23T10:00:00.000Z", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date maintenanceDate;

    @NotNull(message = "Cost is mandatory")
    @PositiveOrZero(message = "Cost must be zero or positive")
    @Schema(description = "Cost incurred for the maintenance", example = "152.25", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal cost;

    @NotNull(message = "Maintenance status is mandatory")
    @Schema(description = "Current status of the maintenance", example = "IN_PROGRESS", requiredMode = Schema.RequiredMode.REQUIRED)
    private MaintenanceStatus status;
}
