package lockin.fleetflow.drivers.dto;

import jakarta.validation.constraints.NotNull;
import lockin.fleetflow.drivers.domain.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO
{
    private long idAssHis;
    @NotNull(message = "Change date is mandatory")
    private LocalDateTime changeDate;
    @NotNull(message = "Please enter previous status")
    private AssignmentStatus previousStatus;
    @NotNull(message = "Please enter new status")
    private AssignmentStatus newStatus;
    @NotNull(message = "Please mention Actor")
    private long changedBy;
    private String reason;
    private AssignmentDTO assignment;
}
