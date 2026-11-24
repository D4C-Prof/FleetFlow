package lockin.fleetflow.deliveries.service;

import lockin.fleetflow.deliveries.domain.DeliveryStatus;
import lockin.fleetflow.deliveries.domain.IncidentType;
import lockin.fleetflow.deliveries.dto.DeliveryDTO;
import lockin.fleetflow.deliveries.dto.IncidentDTO;
import lockin.fleetflow.deliveries.dto.RouteProgress;
import lockin.fleetflow.deliveries.dto.StatisticsDTO;
import java.util.List;
import java.util.UUID;

public interface IDeliveryService {
    // actingDriverId = the caller; must be the driver assigned to the delivery's route
    DeliveryDTO updateDeliveryStatus(UUID deliveryId, UUID actingDriverId, DeliveryStatus status);
    IncidentDTO reportIncident(UUID deliveryId, UUID actingDriverId, IncidentType type, String description);
    RouteProgress getDeliveryProgress(UUID deliveryId);
    List<DeliveryDTO> getDriverDeliveries(UUID driverId);
    List<IncidentDTO> getDeliveryIncidents(UUID deliveryId);
    StatisticsDTO getDeliveryStatistics(UUID driverId);
}
