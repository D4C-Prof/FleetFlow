package lockin.fleetflow.Vehicules.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.Date;


@Document(collection = "Maintenances")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@CompoundIndex(
        name = "vehicleId_maintenanceDate",
        def = "{'vehicle_id': 1, 'maintenance_date': -1}"
)
public class Maintenance {

    @Id
    private String id;

    @Field("vehicle_id")
    @NotBlank
    private String vehicleId;

    @Indexed
    @NotNull
    private MaintenanceType type;

    private String description;

    @Field("maintenance_date")
    @NotNull
    private Date maintenanceDate;

    @Field(targetType = FieldType.DECIMAL128)
    @NotNull
    @PositiveOrZero
    private BigDecimal cost;

    @Indexed
    @NotNull
    private MaintenanceStatus status;

}
