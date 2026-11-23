package lockin.fleetflow.Vehicules.services;

import lockin.fleetflow.Vehicules.domain.MaintenanceStatus;
import lockin.fleetflow.Vehicules.domain.MaintenanceType;
import lockin.fleetflow.Vehicules.web.dto.MaintenanceDTO;

import java.util.List;

public interface IMaintenanceService {
    MaintenanceDTO create(MaintenanceDTO maintenanceDTO);
    MaintenanceDTO findById(String id);
    List<MaintenanceDTO> findAll();
    MaintenanceDTO update(String id, MaintenanceDTO maintenanceDTO);
    void deleteById(String id);
    List<MaintenanceDTO> findByVehicleId(String vehicleId);
    List<MaintenanceDTO> findByStatus(MaintenanceStatus status);
    List<MaintenanceDTO> findByType(MaintenanceType type);
}
