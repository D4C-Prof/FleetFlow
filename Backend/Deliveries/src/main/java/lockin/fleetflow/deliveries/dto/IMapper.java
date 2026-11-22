package lockin.fleetflow.deliveries.dto;

import lockin.fleetflow.deliveries.domain.DeliveryRoute;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IMapper {
    RouteDTO toDTO(DeliveryRoute route);
    DeliveryRoute toEntity(RouteDTO dto);
    List<RouteDTO> routesToDTOList(List<DeliveryRoute> list);
}