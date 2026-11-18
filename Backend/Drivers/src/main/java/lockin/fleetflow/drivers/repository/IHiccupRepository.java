package lockin.fleetflow.drivers.repository;

import lockin.fleetflow.drivers.domain.DriverHiccup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IHiccupRepository extends JpaRepository<DriverHiccup, Long> {
}
