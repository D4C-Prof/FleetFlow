# Orders & Packages Service (MS1)

Microservice responsible for managing shipment orders (`shipment_orders`) and packages (`packages`) in the **FleetFlow** project.

## Stack & Environment
- **Java**: 17 (Amazon Corretto)
- **Framework**: Spring Boot `4.2.0`
- **Database**: PostgreSQL `17` (database: `orders_db`)
- **Port**: `8081`
- **API Documentation**: `springdoc-openapi-starter-webmvc-ui` (`3.0.3`)

## What Was Set Up & Verified
- Configured the PostgreSQL (`orders_db`) connection and JPA/Hibernate schema generation (`shipment_orders`, `packages`).
- Fixed compilation issues in `OrderController` (tracking endpoint mapping) and resolved Maven synchronization.
- Enabled **Swagger UI**, accessible at:
    - `http://localhost:8081/swagger-ui/index.html` (or `/swagger-ui.html`)
- Verified endpoints via Swagger UI:
    - `POST /api/orders`: create an order with packages and auto-generate a barcode (`PKG-...`).
    - `GET /api/orders`: fetch orders from PostgreSQL.
    - `GET /api/orders/{id}/tracking`: track order and package status.
    - `GET /api/orders/statistics`: view order statistics.
    - `POST /api/orders/{id}/packages`: add a package to an existing order.

## Notes for the Team
- **Eureka Discovery (`localhost:8761`)**: if the Eureka server is not running locally yet, the Eureka client logs connection errors on startup without preventing the service from starting on port `8081`.
- Make sure your local database configuration (`orders_db`) is set up before running the service.