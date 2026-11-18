package lockin.fleetflow.drivers.dto;


import lockin.fleetflow.drivers.domain.Hiccup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HiccupDTO
{
    private long idDriHic;
    @NotNull(message = "HiccupType is mandatory")
    private Hiccup hiccup;
    @NotNull(message = "Driver is mandatory")
    private long idDriver;
    @NotNull(message = "Start date is mandatory")
    private LocalDateTime startDate;
    @NotNull(message = "End date is mandatory")
    private LocalDateTime endDate;
    private String reason;
}
