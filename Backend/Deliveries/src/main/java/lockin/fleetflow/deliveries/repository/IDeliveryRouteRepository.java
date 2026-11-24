package lockin.fleetflow.deliveries.repository;

import lockin.fleetflow.deliveries.domain.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IDeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {
}