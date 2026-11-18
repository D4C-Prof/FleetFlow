package lockin.fleetflow.drivers.dto;


import lockin.fleetflow.drivers.domain.AssignmentHistory;
import lockin.fleetflow.drivers.domain.DriverAssignment;
import lockin.fleetflow.drivers.domain.DriverHiccup;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IMapper
{
    HiccupDTO toDTO(DriverHiccup driverHiccup);
    DriverHiccup toEntity(HiccupDTO driverHiccupDTO);
    List<HiccupDTO> hicToDTOList(List<DriverHiccup> list);

    AssignmentDTO toDTO(DriverAssignment driverAssignment);
    DriverAssignment toEntity(AssignmentDTO driverAssignmentDTO);
    List<AssignmentDTO> assToDTOList(List<DriverAssignment> list);

    HistoryDTO toDTO(AssignmentHistory assignmentHistory);
    AssignmentHistory toEntity(HistoryDTO assignmentHistoryDTO);
    List<HistoryDTO> hisToDTOList(List<AssignmentHistory> list);
}
