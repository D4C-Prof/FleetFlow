package lockin.fleetflow.deliveries.service;

import lockin.fleetflow.deliveries.domain.DeliveryRoute;
import lockin.fleetflow.deliveries.domain.RouteStatus;
import lockin.fleetflow.deliveries.dto.IMapper;
import lockin.fleetflow.deliveries.dto.RouteDTO;
import lockin.fleetflow.deliveries.exception.BusinessRuleException;
import lockin.fleetflow.deliveries.exception.ResourceNotFoundException;
import lockin.fleetflow.deliveries.repository.IDeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements IRouteService {

    private final IMapper map;
    private final IDeliveryRouteRepository routeRepo;

    @Override
    @Transactional
    public RouteDTO createRoute(RouteDTO dto) {
        return map.toDTO(routeRepo.save(map.toEntity(dto)));
    }

    @Override
    public RouteDTO findById(UUID id) {
        return map.toDTO(getOrThrow(id));
    }

    @Override
    public List<RouteDTO> findAll() {
        return map.routesToDTOList(routeRepo.findAll());
    }

    @Override
    @Transactional
    public RouteDTO updateRoute(UUID id, RouteDTO dto) {
        DeliveryRoute route = getOrThrow(id);
        route.updateDetails(dto.getOrigin(), dto.getScheduledDate());
        return map.toDTO(routeRepo.save(route));
    }

    @Override
    @Transactional
    public void deleteRoute(UUID id) {
        DeliveryRoute route = getOrThrow(id);
        if (route.getStatus() != RouteStatus.PLANNED) {
            throw new BusinessRuleException("Only PLANNED routes can be deleted; cancel it instead");
        }
        routeRepo.delete(route);
    }

    private DeliveryRoute getOrThrow(UUID id) {
        return routeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route", id));
    }
}