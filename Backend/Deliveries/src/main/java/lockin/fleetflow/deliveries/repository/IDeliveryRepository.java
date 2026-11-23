package lockin.fleetflow.deliveries.repository;

import lockin.fleetflow.deliveries.domain.Delivery;
import lockin.fleetflow.deliveries.domain.DeliveryStatus;
import lockin.fleetflow.deliveries.domain.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IDeliveryRepository extends JpaRepository<Delivery, UUID> {

    @Query("select d from Delivery d where d.route.driverId = :driverId "
            + "order by d.route.scheduledDate, d.sequenceNumber")
    List<Delivery> findByDriver(@Param("driverId") UUID driverId);

    @Query("select count(d) from Delivery d where d.route.driverId = :driverId and d.status = :status")
    long countByDriverAndStatus(@Param("driverId") UUID driverId, @Param("status") DeliveryStatus status);

    @Query("select count(distinct d) from Delivery d join d.incidents i "
            + "where d.route.driverId = :driverId and i.type = :type")
    long countWithIncidentType(@Param("driverId") UUID driverId, @Param("type") IncidentType type);

    @Query("select count(i) from Delivery d join d.incidents i where d.route.driverId = :driverId")
    long countIncidents(@Param("driverId") UUID driverId);
}
