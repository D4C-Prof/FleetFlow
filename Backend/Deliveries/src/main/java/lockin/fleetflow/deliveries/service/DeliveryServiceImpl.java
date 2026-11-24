package lockin.fleetflow.deliveries.service;

import lockin.fleetflow.deliveries.domain.Delivery;
import lockin.fleetflow.deliveries.domain.DeliveryStatus;
import lockin.fleetflow.deliveries.domain.IncidentType;
import lockin.fleetflow.deliveries.dto.*;
import lockin.fleetflow.deliveries.exception.ForbiddenOperationException;
import lockin.fleetflow.deliveries.exception.ResourceNotFoundException;
import lockin.fleetflow.deliveries.repository.IDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements IDeliveryService {

    private final IMapper map;
    private final IDeliveryRepository deliveryRepo;

    @Override
    public DeliveryDTO updateDeliveryStatus(UUID deliveryId, UUID actingDriverId, DeliveryStatus status) {
        Delivery delivery = getOwnedBy(deliveryId, actingDriverId);
        delivery.advanceTo(status); // forward-only, route IN_PROGRESS, completes the route if last
        return map.toDTO(delivery);
    }

    @Override
    public IncidentDTO reportIncident(UUID deliveryId, UUID actingDriverId, IncidentType type, String description) {
        Delivery delivery = getOwnedBy(deliveryId, actingDriverId);
        delivery.reportIncident(type, description, actingDriverId);
        List<IncidentDTO> all = map.incidentsToDTOList(delivery.getIncidents());
        return all.get(all.size() - 1);
    }

    @Override
    @Transactional(readOnly = true)
    public RouteProgress getDeliveryProgress(UUID deliveryId) {
        return RouteProgress.of(getOrThrow(deliveryId).getRoute());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getDriverDeliveries(UUID driverId) {
        return map.deliveriesToDTOList(deliveryRepo.findByDriver(driverId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentDTO> getDeliveryIncidents(UUID deliveryId) {
        return map.incidentsToDTOList(getOrThrow(deliveryId).getIncidents());
    }

    // "delayed" = the delivery has at least one DELAY incident
    @Override
    @Transactional(readOnly = true)
    public StatisticsDTO getDeliveryStatistics(UUID driverId) {
        return new StatisticsDTO(
                (int) deliveryRepo.countByDriverAndStatus(driverId, DeliveryStatus.DELIVERED),
                (int) deliveryRepo.countWithIncidentType(driverId, IncidentType.DELAY),
                (int) deliveryRepo.countIncidents(driverId));
    }

    private Delivery getOrThrow(UUID id) {
        return deliveryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", id));
    }

    // only the driver assigned to the route may act on its deliveries
    private Delivery getOwnedBy(UUID deliveryId, UUID actingDriverId) {
        Delivery delivery = getOrThrow(deliveryId);
        UUID assigned = delivery.getRoute().getDriverId();
        if (assigned == null || !assigned.equals(actingDriverId)) {
            throw new ForbiddenOperationException("Only the driver assigned to this route can do this");
        }
        return delivery;
    }
}
