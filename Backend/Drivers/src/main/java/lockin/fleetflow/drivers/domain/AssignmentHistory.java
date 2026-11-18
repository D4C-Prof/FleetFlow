package lockin.fleetflow.drivers.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="AssignmentHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentHistory {
    @Id
    @GeneratedValue
    private long idAssHis;
    @Column(nullable = false)
    private LocalDateTime changeDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus previousStatus;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus newStatus;
    @Column(nullable = false)
    private long changedBy;
    @Column(nullable = true, length = 511)
    private String reason;
    @NonNull
    @ManyToOne(optional = false)
    private DriverAssignment assignment;
}
