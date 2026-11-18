package lockin.fleetflow.drivers.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="driverHiccup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverHiccup
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDriHic;
    @Column(nullable = false)
    private Hiccup hiccup;
    @Column(nullable = false)
    private long idDriver;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Column(nullable = true, length = 511)
    private String reason;
}
