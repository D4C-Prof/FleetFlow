package lockin.fleetflow.drivers.dto;

import jakarta.validation.constraints.NotNull;
import lockin.fleetflow.drivers.domain.AssignmentHistory;
import lockin.fleetflow.drivers.domain.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO
{
    private long idDriAss;
    @NotNull(message = "Driver is mandatory")
    private long idDriver;
    @NotNull(message = "Vehicle is mandatory")
    private long idVehicle;
    @NotNull(message = "Start date is mandatory")
    private LocalDateTime startDate;
    @NotNull(message = "End date is mandatory")
    private LocalDateTime endDate;
    @NotNull(message = "Assignment status is mandatory")
    private AssignmentStatus status;
    private List<AssignmentHistory> assHis;
}
