package lockin.fleetflow.deliveries.service;

import lockin.fleetflow.deliveries.domain.Delivery;
import lockin.fleetflow.deliveries.domain.DeliveryRoute;
import lockin.fleetflow.deliveries.domain.RouteStatus;
import lockin.fleetflow.deliveries.dto.*;
import lockin.fleetflow.deliveries.exception.BusinessRuleException;
import lockin.fleetflow.deliveries.exception.ResourceNotFoundException;
import lockin.fleetflow.deliveries.repository.IDeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

// Class-level @Transactional: with open-in-view=false the lazy deliveries
// can only be read (and mapped to DTOs) inside a transaction.
@Service
@Transactional
@RequiredArgsConstructor
public class RouteServiceImpl implements IRouteService {

    private final IMapper map;
    private final IDeliveryRouteRepository routeRepo;

    @Override
    public RouteDTO createRoute(RouteDTO dto) {
        DeliveryRoute route = map.toEntity(dto);
        route.calculateDistance();
        return map.toDTO(routeRepo.save(route));
    }

    @Override
    @Transactional(readOnly = true)
    public RouteDTO findById(UUID id) {
        return map.toDTO(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteDTO> findAll() {
        return map.routesToDTOList(routeRepo.findAll());
    }

    @Override
    public RouteDTO updateRoute(UUID id, RouteDTO dto) {
        DeliveryRoute route = getOrThrow(id);
        route.updateDetails(dto.getOrigin(), dto.getScheduledDate());
        return map.toDTO(route);
    }

    @Override
    public void deleteRoute(UUID id) {
        DeliveryRoute route = getOrThrow(id);
        if (route.getStatus() != RouteStatus.PLANNED) {
            throw new BusinessRuleException("Only PLANNED routes can be deleted; cancel it instead");
        }
        routeRepo.delete(route);
    }

    @Override
    public DeliveryDTO addDelivery(UUID routeId, AddDeliveryRequest request) {
        DeliveryRoute route = getOrThrow(routeId);
        Delivery delivery = route.addDelivery(request.orderId(), request.deliveryAddress());
        routeRepo.flush(); // cascade-persist the new delivery so it gets its id
        return map.toDTO(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> getDeliveries(UUID routeId) {
        return map.deliveriesToDTOList(getOrThrow(routeId).getDeliveries());
    }

    @Override
    public void removeDelivery(UUID routeId, UUID deliveryId) {
        getOrThrow(routeId).removeDelivery(deliveryId);
    }

    @Override
    public RouteDTO optimizeDeliveries(UUID routeId) {
        DeliveryRoute route = getOrThrow(routeId);
        route.optimizeDeliveries();
        return map.toDTO(route);
    }

    @Override
    public RouteDTO assignRoute(UUID routeId, UUID driverId, UUID vehicleId) {
        // TODO phase (e): check driver (MS4) and vehicle (MS3) availability through the clients
        DeliveryRoute route = getOrThrow(routeId);
        route.assign(driverId, vehicleId);
        return map.toDTO(route);
    }

    @Override
    public RouteDTO startRoute(UUID routeId) {
        DeliveryRoute route = getOrThrow(routeId);
        route.start();
        return map.toDTO(route);
    }

    @Override
    public RouteDTO cancelRoute(UUID routeId) {
        DeliveryRoute route = getOrThrow(routeId);
        route.cancel();
        return map.toDTO(route);
    }

    @Override
    @Transactional(readOnly = true)
    public RouteProgress getRouteProgress(UUID routeId) {
        return RouteProgress.of(getOrThrow(routeId));
    }

    private DeliveryRoute getOrThrow(UUID id) {
        return routeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route", id));
    }
}
