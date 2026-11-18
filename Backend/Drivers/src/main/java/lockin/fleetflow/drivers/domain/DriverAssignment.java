package lockin.fleetflow.drivers.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="driverAssignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverAssignment
{
    @Id
    @GeneratedValue
    private long idDriAss;
    @Column(nullable = false)
    private long idDriver;
    @Column(nullable = false)
    private long idVehicle;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status;
    @OneToMany(mappedBy = "assignment")
    private List<AssignmentHistory> assHis = new ArrayList<AssignmentHistory>();
}
