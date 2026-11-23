package lockin.fleetflow.Vehicules.web.mapper;

import lockin.fleetflow.Vehicules.domain.Maintenance;
import lockin.fleetflow.Vehicules.web.dto.MaintenanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IMaintenanceMapper {
    MaintenanceDTO toDTO(Maintenance maintenance);
    Maintenance toEntity(MaintenanceDTO maintenanceDTO);
    List<MaintenanceDTO> toDTOList(List<Maintenance> maintenances);
}
