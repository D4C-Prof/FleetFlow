package lockin.fleetflow.deliveries.dto;

import lockin.fleetflow.deliveries.domain.IncidentType;

import java.time.LocalDateTime;
import java.util.UUID;

public record IncidentDTO(IncidentType type, String description, LocalDateTime reportedAt, UUID reportedBy) {
}
