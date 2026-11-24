package lockin.fleetflow.deliveries.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/** Value object: an incident reported by the driver on a delivery. Immutable once created. */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Incident {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentType type;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    // the driver (MS6 user) who reported it; plain id, no cross-service foreign key
    private UUID reportedBy;

    public Incident(IncidentType type, String description, UUID reportedBy) {
        this.type = type;
        this.description = description;
        this.reportedBy = reportedBy;
        this.reportedAt = LocalDateTime.now();
    }
}
