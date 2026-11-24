package tn.esprit.fleetflow.orderspackagesservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.fleetflow.orderspackagesservice.domain.Package;

public interface PackageRepository extends JpaRepository<Package, Long> {

    Optional<Package> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);

    List<Package> findByOrderId(Long orderId);
}