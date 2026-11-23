package lockin.fleetflow.deliveries.domain;

import lockin.fleetflow.deliveries.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryRouteTest {

    private static Address addr(double lat, double lon) {
        return new Address("street", "city", "1000", lat, lon);
    }

    private DeliveryRoute plannedRoute() {
        DeliveryRoute r = new DeliveryRoute();
        r.updateDetails(addr(36.8065, 10.1815), LocalDateTime.now().plusDays(1)); // Tunis
        return r;
    }

    private void start(DeliveryRoute r) {
        ReflectionTestUtils.setField(r, "status", RouteStatus.IN_PROGRESS); // start() comes in a later phase
    }

    @Test
    void haversineTunisToSfaxIsAbout240Km() {
        double km = addr(36.8065, 10.1815).distanceTo(addr(34.7406, 10.7603));
        assertEquals(240, km, 10);
    }

    @Test
    void addDeliveryAppendsAndComputesDistance() {
        DeliveryRoute r = plannedRoute();
        r.addDelivery(UUID.randomUUID(), addr(34.7406, 10.7603));
        assertEquals(1, r.getDeliveries().size());
        assertTrue(r.getEstimatedDistance() > 200);
    }

    @Test
    void duplicateOrderIsRejected() {
        DeliveryRoute r = plannedRoute();
        UUID order = UUID.randomUUID();
        r.addDelivery(order, addr(35, 10));
        assertThrows(BusinessRuleException.class, () -> r.addDelivery(order, addr(35, 10)));
    }

    @Test
    void cannotModifyNonPlannedRoute() {
        DeliveryRoute r = plannedRoute();
        start(r);
        assertThrows(BusinessRuleException.class, () -> r.addDelivery(UUID.randomUUID(), addr(35, 10)));
        assertThrows(BusinessRuleException.class, () -> r.updateDetails(addr(1, 1), LocalDateTime.now()));
    }

    @Test
    void statusOnlyMovesForward() {
        DeliveryRoute r = plannedRoute();
        Delivery d = r.addDelivery(UUID.randomUUID(), addr(35, 10));
        start(r);
        assertThrows(BusinessRuleException.class, () -> d.advanceTo(DeliveryStatus.DELIVERED)); // skips IN_PROGRESS
        d.advanceTo(DeliveryStatus.IN_PROGRESS);
        assertThrows(BusinessRuleException.class, () -> d.advanceTo(DeliveryStatus.PENDING)); // backwards
    }

    @Test
    void routeCompletesWhenLastDeliveryIsDoneThenIsReadOnly() {
        DeliveryRoute r = plannedRoute();
        Delivery d1 = r.addDelivery(UUID.randomUUID(), addr(35, 10));
        Delivery d2 = r.addDelivery(UUID.randomUUID(), addr(34, 10));
        start(r);
        d1.advanceTo(DeliveryStatus.IN_PROGRESS);
        d1.advanceTo(DeliveryStatus.DELIVERED);
        assertEquals(RouteStatus.IN_PROGRESS, r.getStatus());
        d2.advanceTo(DeliveryStatus.IN_PROGRESS);
        d2.advanceTo(DeliveryStatus.FAILED);
        assertEquals(RouteStatus.COMPLETED, r.getStatus());
        assertThrows(BusinessRuleException.class, () -> d2.reportIncident(IncidentType.OTHER, "x"));
    }

    @Test
    void removeDeliveryRenumbersSequence() {
        DeliveryRoute r = plannedRoute();
        Delivery d1 = r.addDelivery(UUID.randomUUID(), addr(35, 10));
        Delivery d2 = r.addDelivery(UUID.randomUUID(), addr(34, 10));
        ReflectionTestUtils.setField(d1, "id", UUID.randomUUID());
        r.removeDelivery(d1.getId());
        assertEquals(0, d2.getSequence());
    }
}
