package lockin.fleetflow.Vehicules.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lockin.fleetflow.Vehicules.domain.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object representing a Vehicle")
public class VehicleDTO {

    @Schema(description = "Unique identifier of the vehicle", example = "60c72b2f9b1d8b2bad745678")
    private String id;

    @NotBlank(message = "Registration number is mandatory")
    @Schema(description = "Registration number / license plate", example = "138 Tun 41925", requiredMode = Schema.RequiredMode.REQUIRED)
    private String registration;

    @NotBlank(message = "Brand is mandatory")
    @Schema(description = "Brand / manufacturer of the vehicle", example = "Volkswagen", requiredMode = Schema.RequiredMode.REQUIRED)
    private String brand;

    @NotBlank(message = "Model is mandatory")
    @Schema(description = "Model of the vehicle", example = "Polo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String model;

    @NotNull(message = "Year is mandatory")
    @Positive(message = "Year must be a positive number")
    @Schema(description = "Manufacturing year", example = "2020", requiredMode = Schema.RequiredMode.REQUIRED)
    private Number year;

    @NotNull(message = "Mileage is mandatory")
    @PositiveOrZero(message = "Mileage must be zero or positive")
    @Schema(description = "Vehicle mileage in kilometers", example = "150000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Number mileage;

    @NotNull(message = "Status is mandatory")
    @Schema(description = "Operational status of the vehicle", example = "Available", requiredMode = Schema.RequiredMode.REQUIRED)
    private VehicleStatus status;
}
