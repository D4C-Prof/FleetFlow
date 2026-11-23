package lockin.fleetflow.Vehicules.web.mapper;

import lockin.fleetflow.Vehicules.domain.Vehicle;
import lockin.fleetflow.Vehicules.web.dto.VehicleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IVehicleMapper {
    VehicleDTO toDTO(Vehicle vehicle);
    Vehicle toEntity(VehicleDTO vehicleDTO);
    List<VehicleDTO> toDTOList(List<Vehicle> vehicles);
}
