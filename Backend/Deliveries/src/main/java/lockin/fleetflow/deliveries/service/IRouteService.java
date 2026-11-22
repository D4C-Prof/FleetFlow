package lockin.fleetflow.deliveries.service;

import lockin.fleetflow.deliveries.dto.RouteDTO;
import java.util.List;
import java.util.UUID;

public interface IRouteService {
    RouteDTO createRoute(RouteDTO dto);
    RouteDTO findById(UUID id);
    List<RouteDTO> findAll();
    RouteDTO updateRoute(UUID id, RouteDTO dto);
    void deleteRoute(UUID id);
}