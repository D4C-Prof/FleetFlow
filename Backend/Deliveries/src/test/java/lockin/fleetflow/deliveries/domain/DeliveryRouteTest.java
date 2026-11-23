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

    private void assignAndStart(DeliveryRoute r) {
        r.assign(UUID.randomUUID(), UUID.randomUUID());
        r.start();
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
        r.addDelivery(UUID.randomUUID(), addr(35, 10));
        assignAndStart(r);
        assertThrows(BusinessRuleException.class, () -> r.addDelivery(UUID.randomUUID(), addr(35, 10)));
        assertThrows(BusinessRuleException.class, () -> r.updateDetails(addr(1, 1), LocalDateTime.now()));
        assertThrows(BusinessRuleException.class, r::optimizeDeliveries);
        assertThrows(BusinessRuleException.class, () -> r.assign(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void startNeedsDeliveriesDriverAndVehicle() {
        DeliveryRoute r = plannedRoute();
        r.assign(UUID.randomUUID(), UUID.randomUUID());
        assertThrows(BusinessRuleException.class, r::start); // no delivery

        r.addDelivery(UUID.randomUUID(), addr(35, 10));
        DeliveryRoute noDriver = plannedRoute();
        noDriver.addDelivery(UUID.randomUUID(), addr(35, 10));
        assertThrows(BusinessRuleException.class, noDriver::start); // nobody assigned

        r.start();
        assertEquals(RouteStatus.IN_PROGRESS, r.getStatus());
        assertThrows(BusinessRuleException.class, r::start); // already started
    }

    @Test
    void assignRequiresBothIds() {
        DeliveryRoute r = plannedRoute();
        assertThrows(BusinessRuleException.class, () -> r.assign(null, UUID.randomUUID()));
        assertThrows(BusinessRuleException.class, () -> r.assign(UUID.randomUUID(), null));
    }

    @Test
    void cancelWorksFromPlannedAndInProgressButNotAfter() {
        DeliveryRoute planned = plannedRoute();
        planned.cancel();
        assertEquals(RouteStatus.CANCELLED, planned.getStatus());
        assertThrows(BusinessRuleException.class, planned::cancel);

        DeliveryRoute running = plannedRoute();
        running.addDelivery(UUID.randomUUID(), addr(35, 10));
        assignAndStart(running);
        running.cancel();
        assertEquals(RouteStatus.CANCELLED, running.getStatus());
    }

    @Test
    void statusOnlyMovesForward() {
        DeliveryRoute r = plannedRoute();
        Delivery d = r.addDelivery(UUID.randomUUID(), addr(35, 10));
        assignAndStart(r);
        assertThrows(BusinessRuleException.class, () -> d.advanceTo(DeliveryStatus.DELIVERED)); // skips IN_PROGRESS
        d.advanceTo(DeliveryStatus.IN_PROGRESS);
        assertThrows(BusinessRuleException.class, () -> d.advanceTo(DeliveryStatus.PENDING)); // backwards
    }

    @Test
    void deliveryCannotMoveWhileRouteIsPlanned() {
        DeliveryRoute r = plannedRoute();
        Delivery d = r.addDelivery(UUID.randomUUID(), addr(35, 10));
        assertThrows(BusinessRuleException.class, () -> d.advanceTo(DeliveryStatus.IN_PROGRESS));
    }

    @Test
    void routeCompletesWhenLastDeliveryIsDoneThenIsReadOnly() {
        DeliveryRoute r = plannedRoute();
        Delivery d1 = r.addDelivery(UUID.randomUUID(), addr(35, 10));
        Delivery d2 = r.addDelivery(UUID.randomUUID(), addr(34, 10));
        assignAndStart(r);
        d1.advanceTo(DeliveryStatus.IN_PROGRESS);
        d1.markDelivered();
        assertEquals(RouteStatus.IN_PROGRESS, r.getStatus());
        assertNotNull(d1.getDeliveredAt());
        d2.advanceTo(DeliveryStatus.IN_PROGRESS);
        d2.markFailed();
        assertEquals(RouteStatus.COMPLETED, r.getStatus());
        assertThrows(BusinessRuleException.class,
                () -> d2.reportIncident(IncidentType.OTHER, "x", UUID.randomUUID()));
        assertThrows(BusinessRuleException.class, r::cancel);
    }

    @Test
    void arrivalAndIncidentsAreRecorded() {
        DeliveryRoute r = plannedRoute();
        Delivery d = r.addDelivery(UUID.randomUUID(), addr(35, 10));
        assignAndStart(r);
        assertThrows(BusinessRuleException.class, d::recordArrival); // still PENDING
        d.advanceTo(DeliveryStatus.IN_PROGRESS);
        d.recordArrival();
        assertNotNull(d.getArrivalTime());
        assertEquals(DeliveryStatus.IN_PROGRESS, d.getStatus());

        UUID driver = UUID.randomUUID();
        d.reportIncident(IncidentType.DELAY, "traffic", driver);
        assertEquals(1, d.getIncidents().size());
        assertEquals(driver, d.getIncidents().get(0).getReportedBy());
    }

    @Test
    void optimizeOrdersStopsByNearestNeighbour() {
        DeliveryRoute r = plannedRoute(); // origin: Tunis
        Delivery sfax = r.addDelivery(UUID.randomUUID(), addr(34.7406, 10.7603));
        Delivery bizerte = r.addDelivery(UUID.randomUUID(), addr(37.2744, 9.8739));
        Delivery sousse = r.addDelivery(UUID.randomUUID(), addr(35.8256, 10.6369));
        double before = r.getEstimatedDistance();

        r.optimizeDeliveries();

        assertEquals(bizerte, r.getDeliveries().get(0)); // closest to Tunis
        assertEquals(sousse, r.getDeliveries().get(1));
        assertEquals(sfax, r.getDeliveries().get(2));
        assertEquals(0, bizerte.getSequenceNumber());
        assertEquals(2, sfax.getSequenceNumber());
        assertTrue(r.getEstimatedDistance() < before);
    }

    @Test
    void removeDeliveryRenumbersSequence() {
        DeliveryRoute r = plannedRoute();
        Delivery d1 = r.addDelivery(UUID.randomUUID(), addr(35, 10));
        Delivery d2 = r.addDelivery(UUID.randomUUID(), addr(34, 10));
        ReflectionTestUtils.setField(d1, "id", UUID.randomUUID());
        r.removeDelivery(d1.getId());
        assertEquals(0, d2.getSequenceNumber());
        assertEquals(1, r.getDeliveries().size());
    }
}
