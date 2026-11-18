package lockin.fleetflow.drivers.service;

import lockin.fleetflow.drivers.domain.DriverAssignment;
import lockin.fleetflow.drivers.dto.AssignmentDTO;
import lockin.fleetflow.drivers.dto.IMapper;
import lockin.fleetflow.drivers.exception.ResourceNotFoundException;
import lockin.fleetflow.drivers.repository.IAssignmentRepsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements IAssignmentService{
    private final IMapper map;
    private final IAssignmentRepsitory assRepo;

    @Override
    public AssignmentDTO create(AssignmentDTO assignmentDTO) {
        DriverAssignment driAss = map.toEntity(assignmentDTO);
        return map.toDTO(assRepo.save(driAss));
    }

    @Override
    public AssignmentDTO findById(Long idDriAss) {
        return map.toDTO(assRepo
                .findById(idDriAss)
                .orElseThrow(() -> new ResourceNotFoundException("Driver Assignment", idDriAss)));
    }

    @Override
    public List<AssignmentDTO> findAll() {return map.assToDTOList(assRepo.findAll());}

    @Override
    public AssignmentDTO update(long idDriAss, DriverAssignment driAss) {
        DriverAssignment old = assRepo
                .findById(idDriAss)
                .orElseThrow(() -> new ResourceNotFoundException("Driver Assignment", idDriAss));
        old.setIdDriver(driAss.getIdDriver());
        old.setIdVehicle(driAss.getIdVehicle());
        old.setStartDate(driAss.getStartDate());
        old.setEndDate(driAss.getEndDate());
        old.setStatus(driAss.getStatus());
        return map.toDTO(assRepo.save(old));
    }

    @Override
    public void deleteById(long id) {assRepo.deleteById(id);}
}
