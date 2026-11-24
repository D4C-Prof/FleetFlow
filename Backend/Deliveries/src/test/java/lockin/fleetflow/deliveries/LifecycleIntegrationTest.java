package lockin.fleetflow.deliveries;

import lockin.fleetflow.deliveries.domain.*;
import lockin.fleetflow.deliveries.dto.*;
import lockin.fleetflow.deliveries.exception.BusinessRuleException;
import lockin.fleetflow.deliveries.exception.ForbiddenOperationException;
import lockin.fleetflow.deliveries.repository.IDeliveryRouteRepository;
import lockin.fleetflow.deliveries.service.IDeliveryService;
import lockin.fleetflow.deliveries.service.IRouteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** Full lifecycle through the services against the real database (exercises JPA mappings and queries). */
@SpringBootTest
class LifecycleIntegrationTest {

    @Autowired IRouteService routes;
    @Autowired IDeliveryService deliveries;
    @Autowired IDeliveryRouteRepository routeRepo;

    private UUID routeId;

    @AfterEach
    void cleanUp() {
        if (routeId != null) {
            routeRepo.deleteById(routeId);
        }
    }

    private static Address addr(double lat, double lon) {
        return new Address("street", "city", "1000", lat, lon);
    }

    @Test
    void createPlanRunAndCompleteARoute() {
        RouteDTO dto = new RouteDTO();
        dto.setOrigin(addr(36.8065, 10.1815));
        dto.setScheduledDate(LocalDateTime.now().plusDays(1));
        routeId = routes.createRoute(dto).getId();

        DeliveryDTO sfax = routes.addDelivery(routeId, new AddDeliveryRequest(UUID.randomUUID(), addr(34.7406, 10.7603)));
        DeliveryDTO bizerte = routes.addDelivery(routeId, new AddDeliveryRequest(UUID.randomUUID(), addr(37.2744, 9.8739)));
        assertNotNull(sfax.id());
        assertEquals(2, routes.getDeliveries(routeId).size());

        RouteDTO optimized = routes.optimizeDeliveries(routeId);
        assertEquals(bizerte.id(), optimized.getDeliveries().get(0).id());
        assertTrue(optimized.getEstimatedDistance() > 0);

        UUID driver = UUID.randomUUID();
        assertThrows(BusinessRuleException.class, () -> routes.startRoute(routeId)); // nobody assigned
        routes.assignRoute(routeId, driver, UUID.randomUUID());
        assertEquals(RouteStatus.IN_PROGRESS, routes.startRoute(routeId).getStatus());

        // only the assigned driver may act
        assertThrows(ForbiddenOperationException.class,
                () -> deliveries.updateDeliveryStatus(bizerte.id(), UUID.randomUUID(), DeliveryStatus.IN_PROGRESS));
        // no skipping a step
        assertThrows(BusinessRuleException.class,
                () -> deliveries.updateDeliveryStatus(bizerte.id(), driver, DeliveryStatus.DELIVERED));

        deliveries.updateDeliveryStatus(bizerte.id(), driver, DeliveryStatus.IN_PROGRESS);
        IncidentDTO incident = deliveries.reportIncident(bizerte.id(), driver, IncidentType.DELAY, "traffic");
        assertEquals(driver, incident.reportedBy());
        deliveries.updateDeliveryStatus(bizerte.id(), driver, DeliveryStatus.DELIVERED);

        RouteProgress half = routes.getRouteProgress(routeId);
        assertEquals(50.0, half.percentComplete());
        assertEquals(RouteStatus.IN_PROGRESS, half.routeStatus());

        deliveries.updateDeliveryStatus(sfax.id(), driver, DeliveryStatus.IN_PROGRESS);
        deliveries.updateDeliveryStatus(sfax.id(), driver, DeliveryStatus.FAILED);

        RouteProgress done = deliveries.getDeliveryProgress(sfax.id());
        assertEquals(RouteStatus.COMPLETED, done.routeStatus());
        assertEquals(100.0, done.percentComplete());

        assertEquals(2, deliveries.getDriverDeliveries(driver).size());
        assertEquals(1, deliveries.getDeliveryIncidents(bizerte.id()).size());
        StatisticsDTO stats = deliveries.getDeliveryStatistics(driver);
        assertEquals(1, stats.completedDeliveries());
        assertEquals(1, stats.delayedDeliveries());
        assertEquals(1, stats.incidentsCount());

        // a completed route is read-only
        assertThrows(BusinessRuleException.class, () -> routes.cancelRoute(routeId));
        assertThrows(BusinessRuleException.class,
                () -> deliveries.reportIncident(sfax.id(), driver, IncidentType.OTHER, "late"));
    }
}
