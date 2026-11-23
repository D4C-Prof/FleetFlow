package lockin.fleetflow.deliveries.dto;

import lockin.fleetflow.deliveries.domain.Delivery;
import lockin.fleetflow.deliveries.domain.DeliveryRoute;
import lockin.fleetflow.deliveries.domain.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IMapper {
    RouteDTO toDTO(DeliveryRoute route);

    // deliveries are never set from a route body: they have their own endpoints
    @Mapping(target = "deliveries", ignore = true)
    DeliveryRoute toEntity(RouteDTO dto);

    List<RouteDTO> routesToDTOList(List<DeliveryRoute> list);

    @Mapping(source = "route.id", target = "routeId")
    DeliveryDTO toDTO(Delivery delivery);
    List<DeliveryDTO> deliveriesToDTOList(List<Delivery> list);

    IncidentDTO toDTO(Incident incident);
    List<IncidentDTO> incidentsToDTOList(List<Incident> list);
}
