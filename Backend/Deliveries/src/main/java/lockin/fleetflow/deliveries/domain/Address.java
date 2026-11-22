package lockin.fleetflow.deliveries.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    private String street;

    private String city;

    private String postalCode;

    private Double latitude;

    private Double longitude;
}