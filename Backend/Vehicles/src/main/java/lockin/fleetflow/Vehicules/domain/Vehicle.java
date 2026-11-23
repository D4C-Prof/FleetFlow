package lockin.fleetflow.Vehicules.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("registration_number")
    private String registration;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    @Positive
    private Number year;

    @NotNull
    @PositiveOrZero
    private Number mileage;

    @Indexed
    private VehicleStatus status;

}
