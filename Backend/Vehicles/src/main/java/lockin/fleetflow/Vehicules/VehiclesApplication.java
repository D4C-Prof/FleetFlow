package lockin.fleetflow.Vehicules;

import lockin.fleetflow.Vehicules.domain.Maintenance;
import lockin.fleetflow.Vehicules.domain.MaintenanceStatus;
import lockin.fleetflow.Vehicules.domain.MaintenanceType;
import lockin.fleetflow.Vehicules.domain.Vehicle;
import lockin.fleetflow.Vehicules.repository.IMaintenanceRepo;
import lockin.fleetflow.Vehicules.repository.IVehicleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.sql.Date;

import static lockin.fleetflow.Vehicules.domain.VehicleStatus.Available;

@SpringBootApplication
public class VehiclesApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehiclesApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase (IVehicleRepo vehicleRepo, IMaintenanceRepo maintenanceRepo) {
        return args -> {
            Vehicle vehicle = new Vehicle(
                    null,
                    "138 Tun 41925",
                    "Volkswagen",
                    "polo",
                    2003,
                    150000,
                    Available
            );

            Maintenance maintenance = new Maintenance(
                    null,
                    "6b0360ef14d22854183f10a5",
                    MaintenanceType.CURATIVE,
                    "Engine_Mis_fire",
                    Date.valueOf("2026-11-23"),
                    BigDecimal.valueOf(152.25),
                    MaintenanceStatus.IN_PROGRESS
            );

            maintenanceRepo.save(maintenance);
            vehicleRepo.save(vehicle);

        };
    }
}
