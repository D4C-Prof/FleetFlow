package tn.esprit.fleetflow.orderspackagesservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.fleetflow.orderspackagesservice.domain.OrderStatus;
import tn.esprit.fleetflow.orderspackagesservice.domain.ShipmentOrder;

public interface ShipmentOrderRepository extends JpaRepository<ShipmentOrder, Long> {

    List<ShipmentOrder> findByCustomerId(Long customerId);

    long countByStatus(OrderStatus status);

    long countByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}