package lockin.fleetflow.drivers.repository;

import lockin.fleetflow.drivers.domain.AssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IHistoryRepository extends JpaRepository<AssignmentHistory, Long> {
}
