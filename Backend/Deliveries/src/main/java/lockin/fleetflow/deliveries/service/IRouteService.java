package lockin.fleetflow.deliveries.service;

import lockin.fleetflow.deliveries.dto.AddDeliveryRequest;
import lockin.fleetflow.deliveries.dto.DeliveryDTO;
import lockin.fleetflow.deliveries.dto.RouteDTO;
import lockin.fleetflow.deliveries.dto.RouteProgress;
import java.util.List;
import java.util.UUID;

public interface IRouteService {
    RouteDTO createRoute(RouteDTO dto);
    RouteDTO findById(UUID id);
    List<RouteDTO> findAll();
    RouteDTO updateRoute(UUID id, RouteDTO dto);
    void deleteRoute(UUID id);

    DeliveryDTO addDelivery(UUID routeId, AddDeliveryRequest request);
    List<DeliveryDTO> getDeliveries(UUID routeId);
    void removeDelivery(UUID routeId, UUID deliveryId);
    RouteDTO optimizeDeliveries(UUID routeId);

    RouteDTO assignRoute(UUID routeId, UUID driverId, UUID vehicleId);
    RouteDTO startRoute(UUID routeId);
    RouteDTO cancelRoute(UUID routeId);
    RouteProgress getRouteProgress(UUID routeId);
}
