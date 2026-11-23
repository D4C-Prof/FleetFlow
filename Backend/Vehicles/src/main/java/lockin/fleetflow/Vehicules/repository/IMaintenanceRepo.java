package lockin.fleetflow.Vehicules.repository;

import lockin.fleetflow.Vehicules.domain.Maintenance;
import lockin.fleetflow.Vehicules.domain.MaintenanceStatus;
import lockin.fleetflow.Vehicules.domain.MaintenanceType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IMaintenanceRepo extends MongoRepository<Maintenance, String> {
    
    List<Maintenance> findByVehicleId(String vehicleId);

    List<Maintenance> findByVehicleIdOrderByMaintenanceDateDesc(String vehicleId);
    List<Maintenance> findByStatus(MaintenanceStatus maintenance);
    List<Maintenance> findByType(MaintenanceType maintenanceType);
    
   
}
