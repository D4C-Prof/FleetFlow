package lockin.fleetflow.Vehicules.services;

import lockin.fleetflow.Vehicules.domain.VehicleStatus;
import lockin.fleetflow.Vehicules.web.dto.VehicleDTO;

import java.util.List;

public interface IVehicleService {
    VehicleDTO create(VehicleDTO vehicleDTO);
    VehicleDTO findById(String id);
    List<VehicleDTO> findAll();
    VehicleDTO update(String id, VehicleDTO vehicleDTO);
    void deleteById(String id);
    VehicleDTO findByRegistration(String registration);
    List<VehicleDTO> findByBrand(String brand);
    List<VehicleDTO> findByStatus(VehicleStatus status);
}
