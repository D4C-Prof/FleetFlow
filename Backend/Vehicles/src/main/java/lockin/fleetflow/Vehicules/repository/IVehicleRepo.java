package lockin.fleetflow.Vehicules.repository;

import lockin.fleetflow.Vehicules.domain.Vehicle;
import lockin.fleetflow.Vehicules.domain.VehicleStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IVehicleRepo extends MongoRepository<Vehicle, String> {

    Optional<Vehicle> findByRegistration(String registration);

    boolean existsByRegistration(String registration);
    List<Vehicle> findByBrand(String brand);
    List<Vehicle> findByStatus(VehicleStatus status);
}
