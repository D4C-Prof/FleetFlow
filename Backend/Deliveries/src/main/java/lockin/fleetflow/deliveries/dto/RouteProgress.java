package lockin.fleetflow.deliveries.dto;

import lockin.fleetflow.deliveries.domain.Delivery;
import lockin.fleetflow.deliveries.domain.DeliveryRoute;
import lockin.fleetflow.deliveries.domain.DeliveryStatus;
import lockin.fleetflow.deliveries.domain.RouteStatus;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

/** percentComplete = finished deliveries (DELIVERED, FAILED, RESCHEDULED) / total * 100. */
public record RouteProgress(
        UUID routeId,
        RouteStatus routeStatus,
        int totalDeliveries,
        Map<DeliveryStatus, Long> countByStatus,
        double percentComplete) {

    /** Must be called inside a transaction (reads the lazy deliveries list). */
    public static RouteProgress of(DeliveryRoute route) {
        Map<DeliveryStatus, Long> counts = new EnumMap<>(DeliveryStatus.class);
        for (DeliveryStatus s : DeliveryStatus.values()) {
            counts.put(s, 0L);
        }
        long finished = 0;
        for (Delivery d : route.getDeliveries()) {
            counts.merge(d.getStatus(), 1L, Long::sum);
            if (d.getStatus().isFinished()) {
                finished++;
            }
        }
        int total = route.getDeliveries().size();
        double percent = total == 0 ? 0 : Math.round(finished * 1000.0 / total) / 10.0;
        return new RouteProgress(route.getId(), route.getStatus(), total, counts, percent);
    }
}
