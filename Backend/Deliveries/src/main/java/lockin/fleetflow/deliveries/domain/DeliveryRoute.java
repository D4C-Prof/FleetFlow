package lockin.fleetflow.deliveries.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "delivery_routes")
public class DeliveryRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID driverId;

    private UUID vehicleId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",
                    column = @Column(name = "origin_street")),
            @AttributeOverride(name = "city",
                    column = @Column(name = "origin_city")),
            @AttributeOverride(name = "postalCode",
                    column = @Column(name = "origin_postal_code")),
            @AttributeOverride(name = "latitude",
                    column = @Column(name = "origin_latitude")),
            @AttributeOverride(name = "longitude",
                    column = @Column(name = "origin_longitude"))
    })
    @Setter
    private Address origin;

    @Setter
    private LocalDateTime scheduledDate;

    private Double estimatedDistance;

    @Enumerated(EnumType.STRING)
    private RouteStatus status = RouteStatus.PLANNED;

    // Update route details only if the route is PLANNED
    public void updateDetails(Address origin,
                              LocalDateTime scheduledDate) {
        assertPlanned();

        this.origin = origin;
        this.scheduledDate = scheduledDate;
    }

    private void assertPlanned() {
        if (this.status != RouteStatus.PLANNED) {
            throw new IllegalStateException(
                    "Only PLANNED routes can be modified"
            );
        }
    }
}