package tn.esprit.fleetflow.orderspackagesservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PackageRequest {

    @NotNull(message = "Le poids est obligatoire")
    @Positive(message = "Le poids doit être strictement positif")
    private Double weight;

    @NotNull(message = "La longueur est obligatoire")
    @Positive(message = "La longueur doit être strictement positive")
    private Double length;

    @NotNull(message = "La largeur est obligatoire")
    @Positive(message = "La largeur doit être strictement positive")
    private Double width;

    @NotNull(message = "La hauteur est obligatoire")
    @Positive(message = "La hauteur doit être strictement positive")
    private Double height;
}