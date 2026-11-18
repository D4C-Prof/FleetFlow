package lockin.fleetflow.drivers.service;

import lockin.fleetflow.drivers.domain.AssignmentHistory;
import lockin.fleetflow.drivers.domain.DriverAssignment;
import lockin.fleetflow.drivers.dto.HistoryDTO;
import lockin.fleetflow.drivers.dto.IMapper;
import lockin.fleetflow.drivers.exception.ResourceNotFoundException;
import lockin.fleetflow.drivers.repository.IAssignmentRepsitory;
import lockin.fleetflow.drivers.repository.IHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements IHistoryService{
    private final IHistoryRepository hisRepo;
    private final IAssignmentRepsitory assRepo;
    private final IMapper map;

    @Override
    public HistoryDTO create(HistoryDTO historyDTO) {
        AssignmentHistory assHis = map.toEntity(historyDTO);
        return map.toDTO(hisRepo.save(assHis));
    }

    @Override
    public HistoryDTO findById(Long idAssHis) {
        return map.toDTO(hisRepo
                .findById(idAssHis)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment History", idAssHis)));
    }

    @Override
    public List<HistoryDTO> findAll() {return map.hisToDTOList(hisRepo.findAll());}

    @Override
    public HistoryDTO update(long idAssHis, AssignmentHistory assHis) {
        AssignmentHistory old = hisRepo.findById(idAssHis)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment History", idAssHis));
        old.setChangeDate(assHis.getChangeDate());
        old.setPreviousStatus(assHis.getPreviousStatus());
        old.setNewStatus(assHis.getNewStatus());
        old.setChangedBy(assHis.getChangedBy());
        old.setReason(assHis.getReason());
        old.setAssignment(assHis.getAssignment());
        return map.toDTO(hisRepo.save(old));
    }

    @Override
    public void deleteById(long id) {
        hisRepo.deleteById(id);
    }

    @Override
    public HistoryDTO addHistoryToAssignment(long idAssHis, long idDriAss) {
        AssignmentHistory assHis = hisRepo.findById(idAssHis)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment History", idAssHis));
        DriverAssignment driAss = assRepo.findById(idAssHis)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment History", idAssHis));
        assHis.setAssignment(driAss);
        assHis.getAssignment().setAssHis(null);
        return map.toDTO(hisRepo.save(assHis));
    }
}
