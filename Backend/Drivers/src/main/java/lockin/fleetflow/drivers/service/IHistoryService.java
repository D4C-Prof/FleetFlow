package lockin.fleetflow.drivers.service;

import lockin.fleetflow.drivers.domain.AssignmentHistory;
import lockin.fleetflow.drivers.dto.HistoryDTO;

import java.util.List;

public interface IHistoryService {
    HistoryDTO create(HistoryDTO historyDTO);
    HistoryDTO findById(Long idAssHis);
    List<HistoryDTO> findAll();
    HistoryDTO update(long idAssHis, AssignmentHistory assignmentHistory);
    void deleteById(long id);
    HistoryDTO addHistoryToAssignment(long idAssHis, long idDriAss);
}
