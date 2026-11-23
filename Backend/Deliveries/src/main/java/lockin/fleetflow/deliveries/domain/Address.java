package lockin.fleetflow.deliveries.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @NotBlank(message = "Street is mandatory")
    private String street;

    @NotBlank(message = "City is mandatory")
    private String city;

    private String postalCode;

    @NotNull(message = "Latitude is mandatory")
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;

    @NotNull(message = "Longitude is mandatory")
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double longitude;

    /** Great-circle distance to another address, in km (Haversine formula). */
    public double distanceTo(Address other) {
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(other.latitude);
        double dLat = lat2 - lat1;
        double dLon = Math.toRadians(other.longitude - longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return EARTH_RADIUS_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
