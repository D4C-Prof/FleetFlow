package lockin.fleetflow.deliveries.domain;

import jakarta.persistence.*;
import lockin.fleetflow.deliveries.exception.BusinessRuleException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/** One delivery = one order (MS1) to drop at an address. Belongs to a route. */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries",
        uniqueConstraints = @UniqueConstraint(columnNames = "orderId"))
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // id of the order in MS1: no cross-service foreign key
    @Column(nullable = false)
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id")
    private DeliveryRoute route;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "dest_street")),
            @AttributeOverride(name = "city", column = @Column(name = "dest_city")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "dest_postal_code")),
            @AttributeOverride(name = "latitude", column = @Column(name = "dest_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "dest_longitude"))
    })
    private Address deliveryAddress;

    // position in the route (0-based)
    private int sequenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    // when the driver reached the stop
    private LocalDateTime arrivalTime;

    private LocalDateTime deliveredAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "delivery_incidents", joinColumns = @JoinColumn(name = "delivery_id"))
    private List<Incident> incidents = new ArrayList<>();

    // package-private: only DeliveryRoute.addDelivery creates deliveries
    Delivery(DeliveryRoute route, UUID orderId, Address deliveryAddress, int sequenceNumber) {
        this.route = route;
        this.orderId = orderId;
        this.deliveryAddress = deliveryAddress;
        this.sequenceNumber = sequenceNumber;
    }

    /** Forward-only status change. The route must be IN_PROGRESS. */
    public void advanceTo(DeliveryStatus next) {
        route.assertInProgress();
        if (!status.canMoveTo(next)) {
            throw new BusinessRuleException(
                    "Cannot move delivery from " + status + " to " + next);
        }
        this.status = next;
        if (next == DeliveryStatus.DELIVERED) {
            this.deliveredAt = LocalDateTime.now();
        }
        route.completeIfDone();
    }

    /** The driver reached the stop (timestamp only, status is unchanged). */
    public void recordArrival() {
        route.assertInProgress();
        if (status != DeliveryStatus.IN_PROGRESS) {
            throw new BusinessRuleException("Arrival can only be recorded on an IN_PROGRESS delivery");
        }
        this.arrivalTime = LocalDateTime.now();
    }

    public void markDelivered() {
        advanceTo(DeliveryStatus.DELIVERED);
    }

    public void markFailed() {
        advanceTo(DeliveryStatus.FAILED);
    }

    public void reportIncident(IncidentType type, String description, UUID reportedBy) {
        route.assertInProgress();
        incidents.add(new Incident(type, description, reportedBy));
    }

    public List<Incident> getIncidents() {
        return Collections.unmodifiableList(incidents);
    }

    void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
