package tn.esprit.fleetflow.orderspackagesservice.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsResponse {

    private long totalOrders;
    private Map<String, Long> ordersByStatus;
    private long ordersLast30Days;
}