package tn.esprit.fleetflow.orderspackagesservice.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fleetflow.orderspackagesservice.domain.Package;
import tn.esprit.fleetflow.orderspackagesservice.domain.ShipmentOrder;
import tn.esprit.fleetflow.orderspackagesservice.dto.OrderRequest;
import tn.esprit.fleetflow.orderspackagesservice.dto.OrderStatisticsResponse;
import tn.esprit.fleetflow.orderspackagesservice.dto.PackageRequest;
import tn.esprit.fleetflow.orderspackagesservice.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ShipmentOrder> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<ShipmentOrder>> getAllOrders(@RequestParam(required = false) Long customerId) {
        return ResponseEntity.ok(orderService.getAllOrders(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentOrder> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ShipmentOrder> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @PostMapping("/{id}/packages")
    public ResponseEntity<Package> addPackage(@PathVariable Long id, @Valid @RequestBody PackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addPackageToOrder(id, request));
    }

    @GetMapping("/{id}/tracking")
    public ResponseEntity trackOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.trackOrder(id));
    }

    @GetMapping("/statistics")
    public ResponseEntity<OrderStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(orderService.getStatistics());
    }
}