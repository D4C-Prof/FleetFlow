package lockin.fleetflow.deliveries.dto;

public record StatisticsDTO(int completedDeliveries, int delayedDeliveries, int incidentsCount) {
}
