package tn.esprit.fleetflow.orderspackagesservice.service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.fleetflow.orderspackagesservice.domain.OrderStatus;
import tn.esprit.fleetflow.orderspackagesservice.domain.Package;
import tn.esprit.fleetflow.orderspackagesservice.domain.PackageStatus;
import tn.esprit.fleetflow.orderspackagesservice.domain.ShipmentOrder;
import tn.esprit.fleetflow.orderspackagesservice.dto.OrderRequest;
import tn.esprit.fleetflow.orderspackagesservice.dto.OrderStatisticsResponse;
import tn.esprit.fleetflow.orderspackagesservice.dto.PackageRequest;
import tn.esprit.fleetflow.orderspackagesservice.repository.PackageRepository;
import tn.esprit.fleetflow.orderspackagesservice.repository.ShipmentOrderRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ShipmentOrderRepository orderRepository;
    private final PackageRepository packageRepository;

    public ShipmentOrder createOrder(OrderRequest request) {
        if (request.getPackages() == null || request.getPackages().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Une commande doit contenir au moins un colis");
        }

        ShipmentOrder order = ShipmentOrder.builder()
                .customerId(request.getCustomerId())
                .orderDate(LocalDateTime.now())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(OrderStatus.PENDING)
                .build();

        for (PackageRequest pkgReq : request.getPackages()) {
            order.addPackage(buildPackageFromRequest(pkgReq));
        }

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<ShipmentOrder> getAllOrders(Long customerId) {
        if (customerId != null) {
            return orderRepository.findByCustomerId(customerId);
        }
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ShipmentOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande introuvable : " + id));
    }

    public ShipmentOrder updateOrder(Long id, OrderRequest request) {
        ShipmentOrder order = getOrderById(id);

        if (order.getStatus() == OrderStatus.IN_TRANSIT || order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Impossible de modifier une commande au statut " + order.getStatus());
        }

        order.setCustomerId(request.getCustomerId());
        order.setOrigin(request.getOrigin());
        order.setDestination(request.getDestination());

        return orderRepository.save(order);
    }

    public ShipmentOrder cancelOrder(Long id) {
        ShipmentOrder order = getOrderById(id);

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "L'annulation n'est autorisée que pour les commandes PENDING ou CONFIRMED"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public Package addPackageToOrder(Long orderId, PackageRequest request) {
        ShipmentOrder order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Impossible d'ajouter un colis à cette commande");
        }

        Package pkg = buildPackageFromRequest(request);
        order.addPackage(pkg);
        orderRepository.save(order);

        return packageRepository.findByBarcode(pkg.getBarcode()).orElse(pkg);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> trackOrder(Long id) {
        ShipmentOrder order = getOrderById(id);
        Map<String, Object> tracking = new LinkedHashMap<>();
        tracking.put("orderId", order.getId());
        tracking.put("orderStatus", order.getStatus());
        tracking.put("origin", order.getOrigin());
        tracking.put("destination", order.getDestination());
        tracking.put("totalWeight", order.getTotalWeight());
        tracking.put("packages", order.getPackages());
        return tracking;
    }

    @Transactional(readOnly = true)
    public Package getPackageByBarcode(String barcode) {
        return packageRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colis introuvable pour le code-barres : " + barcode));
    }

    @Transactional(readOnly = true)
    public OrderStatisticsResponse getStatistics() {
        Map<String, Long> byStatus = new EnumMap<>(OrderStatus.class).keySet().stream()
                .collect(LinkedHashMap::new, (m, k) -> {}, Map::putAll);

        for (OrderStatus status : OrderStatus.values()) {
            byStatus.put(status.name(), orderRepository.countByStatus(status));
        }

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long last30Days = orderRepository.countByOrderDateBetween(thirtyDaysAgo, LocalDateTime.now());

        return OrderStatisticsResponse.builder()
                .totalOrders(orderRepository.count())
                .ordersByStatus(byStatus)
                .ordersLast30Days(last30Days)
                .build();
    }

    private Package buildPackageFromRequest(PackageRequest request) {
        String barcode;
        do {
            barcode = "PKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (packageRepository.existsByBarcode(barcode));

        return Package.builder()
                .barcode(barcode)
                .weight(request.getWeight())
                .length(request.getLength())
                .width(request.getWidth())
                .height(request.getHeight())
                .status(PackageStatus.REGISTERED)
                .build();
    }
}