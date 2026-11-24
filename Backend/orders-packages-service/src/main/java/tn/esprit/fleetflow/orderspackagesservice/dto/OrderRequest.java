package tn.esprit.fleetflow.orderspackagesservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class OrderRequest {

    @NotNull(message = "L'identifiant client est obligatoire")
    private Long customerId;

    @NotBlank(message = "L'origine est obligatoire")
    private String origin;

    @NotBlank(message = "La destination est obligatoire")
    private String destination;

    @NotEmpty(message = "Une commande doit contenir au moins un colis")
    @Valid
    private List<PackageRequest> packages;
}