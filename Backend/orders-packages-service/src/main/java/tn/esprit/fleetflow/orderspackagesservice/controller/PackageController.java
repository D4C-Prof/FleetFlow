package tn.esprit.fleetflow.orderspackagesservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fleetflow.orderspackagesservice.domain.Package;
import tn.esprit.fleetflow.orderspackagesservice.service.OrderService;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final OrderService orderService;

    @GetMapping("/{barcode}")
    public ResponseEntity<Package> getPackageByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(orderService.getPackageByBarcode(barcode));
    }
}