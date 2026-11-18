package lockin.fleetflow.drivers.repository;

import lockin.fleetflow.drivers.domain.DriverAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAssignmentRepsitory extends JpaRepository<DriverAssignment, Long> {
}
