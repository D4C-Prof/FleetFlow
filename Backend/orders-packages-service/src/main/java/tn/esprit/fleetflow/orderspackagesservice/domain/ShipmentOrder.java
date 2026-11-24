package tn.esprit.fleetflow.orderspackagesservice.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "shipment_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    @Builder.Default
    private Double totalWeight = 0.0;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Package> packages = new ArrayList<>();

    public void addPackage(Package pkg) {
        packages.add(pkg);
        pkg.setOrder(this);
        recalculateTotalWeight();
    }

    public void recalculateTotalWeight() {
        this.totalWeight = packages.stream()
                .mapToDouble(p -> p.getWeight() != null ? p.getWeight() : 0.0)
                .sum();
    }
}