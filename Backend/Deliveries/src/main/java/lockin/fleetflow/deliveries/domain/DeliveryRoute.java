package lockin.fleetflow.deliveries.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lockin.fleetflow.deliveries.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "delivery_routes")
public class DeliveryRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // optimistic locking: prevents two concurrent updates silently overwriting each other
    @Version
    private Long version;

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
    @Column(nullable = false)
    private RouteStatus status = RouteStatus.PLANNED;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sequenceNumber ASC")
    private List<Delivery> deliveries = new ArrayList<>();

    public List<Delivery> getDeliveries() {
        return Collections.unmodifiableList(deliveries);
    }

    /** Appends a delivery at the end of the route (PLANNED only) and refreshes the distance. */
    public Delivery addDelivery(UUID orderId, Address destination) {
        assertPlanned();
        boolean duplicate = deliveries.stream().anyMatch(d -> d.getOrderId().equals(orderId));
        if (duplicate) {
            throw new BusinessRuleException("Order " + orderId + " is already in this route");
        }
        Delivery delivery = new Delivery(this, orderId, destination, deliveries.size());
        deliveries.add(delivery);
        calculateDistance();
        return delivery;
    }

    /** Removes a delivery (PLANNED only), re-numbers the others and refreshes the distance. */
    public void removeDelivery(UUID deliveryId) {
        assertPlanned();
        boolean removed = deliveries.removeIf(d -> deliveryId.equals(d.getId()));
        if (!removed) {
            throw new BusinessRuleException("Delivery " + deliveryId + " is not in this route");
        }
        for (int i = 0; i < deliveries.size(); i++) {
            deliveries.get(i).setSequenceNumber(i);
        }
        calculateDistance();
    }

    /** Total distance in km: origin -> delivery 1 -> delivery 2 -> ... */
    public void calculateDistance() {
        double total = 0;
        Address previous = origin;
        for (Delivery d : deliveries) {
            total += previous.distanceTo(d.getDeliveryAddress());
            previous = d.getDeliveryAddress();
        }
        this.estimatedDistance = total;
    }

    /** Reorders the stops by nearest neighbour starting from the origin (PLANNED only). */
    public void optimizeDeliveries() {
        assertPlanned();
        List<Delivery> remaining = new ArrayList<>(deliveries);
        Address current = origin;
        int position = 0;
        while (!remaining.isEmpty()) {
            final Address from = current;
            Delivery nearest = remaining.stream()
                    .min(Comparator.comparingDouble(d -> from.distanceTo(d.getDeliveryAddress())))
                    .orElseThrow();
            nearest.setSequenceNumber(position++);
            current = nearest.getDeliveryAddress();
            remaining.remove(nearest);
        }
        deliveries.sort(Comparator.comparingInt(Delivery::getSequenceNumber));
        calculateDistance();
    }

    /** Sets driver and vehicle (PLANNED only). Availability is checked by the service. */
    public void assign(UUID driverId, UUID vehicleId) {
        assertPlanned();
        if (driverId == null || vehicleId == null) {
            throw new BusinessRuleException("Both driverId and vehicleId are required");
        }
        this.driverId = driverId;
        this.vehicleId = vehicleId;
    }

    /** PLANNED -> IN_PROGRESS. Needs at least one delivery, a driver and a vehicle. */
    public void start() {
        assertPlanned();
        if (deliveries.isEmpty()) {
            throw new BusinessRuleException("A route needs at least one delivery to start");
        }
        if (driverId == null || vehicleId == null) {
            throw new BusinessRuleException("Assign a driver and a vehicle before starting the route");
        }
        this.status = RouteStatus.IN_PROGRESS;
    }

    /** PLANNED or IN_PROGRESS -> CANCELLED. Completed routes are read-only. */
    public void cancel() {
        if (status != RouteStatus.PLANNED && status != RouteStatus.IN_PROGRESS) {
            throw new BusinessRuleException("Cannot cancel a " + status + " route");
        }
        this.status = RouteStatus.CANCELLED;
    }

    /** Called after each delivery status change: the route completes when every delivery is finished. */
    void completeIfDone() {
        if (status == RouteStatus.IN_PROGRESS
                && !deliveries.isEmpty()
                && deliveries.stream().allMatch(d -> d.getStatus().isFinished())) {
            this.status = RouteStatus.COMPLETED;
        }
    }

    // Update route details only if the route is PLANNED
    public void updateDetails(Address origin,
                              LocalDateTime scheduledDate) {
        assertPlanned();

        this.origin = origin;
        this.scheduledDate = scheduledDate;
        calculateDistance();
    }

    void assertPlanned() {
        if (this.status != RouteStatus.PLANNED) {
            throw new BusinessRuleException("Only PLANNED routes can be modified");
        }
    }

    void assertInProgress() {
        if (this.status != RouteStatus.IN_PROGRESS) {
            throw new BusinessRuleException(
                    "Route must be IN_PROGRESS (current status: " + status + ")");
        }
    }
}