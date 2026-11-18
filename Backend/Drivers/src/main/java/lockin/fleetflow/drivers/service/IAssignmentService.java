package lockin.fleetflow.drivers.service;

import lockin.fleetflow.drivers.domain.DriverAssignment;
import lockin.fleetflow.drivers.dto.AssignmentDTO;

import java.util.List;

public interface IAssignmentService {
    AssignmentDTO create(AssignmentDTO assignmentDTO);
    AssignmentDTO findById(Long idDriAss);
    List<AssignmentDTO> findAll();
    AssignmentDTO update(long idDriAss, DriverAssignment driverAssignment);
    void deleteById(long id);
}
