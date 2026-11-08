# FleetFlow 🚚

### Integrated Supply Chain & Fleet Management Platform




\


---

## 📌 Overview

**FleetFlow** is an integrated platform designed to combine supply chain management and fleet management into a unified solution.

The platform aims to optimize and streamline business operations by centralizing the management of procurement, inventory, transportation, and deliveries.

By connecting supply chain activities with fleet operations, FleetFlow seeks to improve operational efficiency, enhance resource utilization, and provide better visibility across the entire logistics process.

## 🎯 Project Objectives

- **Procurement Management:** Organize purchasing operations and supplier relationships.
- **Inventory Management:** Monitor stock levels and track inventory movements.
- **Fleet Management:** Manage vehicles, their availability, and operational information.
- **Transportation Management:** Organize transport operations and optimize resource allocation.
- **Delivery Management:** Monitor deliveries and track their progress.
- **Centralized Management:** Bring supply chain and fleet operations together in one platform.

## 🏗️ Project Architecture

FleetFlow follows a frontend/backend architecture:

- **Frontend:** Angular application responsible for the user interface and user interactions.
- **Backend:** Spring Boot application providing RESTful APIs and business logic.
- **Database:** MySQL for persistent data storage.

### Repository Structure

```text
FleetFlow/
│
├── Backend/                     # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── ...
│
├── Frontend/                    # Angular application
│   ├── src/
│   │   ├── app/
│   │   ├── ...
│   ├── public/
│   ├── angular.json
│   ├── package.json
│   └── ...
│
├── Documentation/
│   ├── Presentation/            # Project presentations
│   └── Diagrams/                # UML and architecture diagrams
│
├── .gitignore
└── README.md
```

## 🛠️ Technology Stack

| Component          | Technology        |
| ------------------ | ----------------- |
| Backend            | Java, Spring Boot |
| Backend Build Tool | Maven             |
| Frontend           | Angular           |
| Frontend Language  | TypeScript        |
| Styling            | SCSS              |
| Database           | MySQL             |
| API Architecture   | REST              |
| Version Control    | Git & GitHub      |
| IDE                | IntelliJ IDEA     |

## ⚙️ Prerequisites

Make sure the following tools are installed:

- Java Development Kit (JDK) 17 or compatible project version
- Maven (or use the included Maven Wrapper)
- Node.js (compatible with your Angular version)
- npm
- Angular CLI
- MySQL Server
- Git

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/D4C-Prof/FleetFlow.git
cd FleetFlow
```

### 2. Backend Setup (Spring Boot)

Navigate to the backend directory:

```bash
cd Backend
```

Configure your MySQL database connection in:

`src/main/resources/application.properties`

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fleetflow_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Set the `DB_USERNAME` and `DB_PASSWORD` environment variables before starting the backend.

Run the application using the Maven Wrapper:

**Windows:**

```bash
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

The backend will be available at:

`http://localhost:8080`

### 3. Frontend Setup (Angular)

Open another terminal and navigate to the frontend directory:

```bash
cd Frontend
```

Install dependencies:

```bash
npm install
```

Start the Angular development server:

```bash
ng serve
```

The frontend will be available at:

`http://localhost:4200`

## 🔗 Application Communication

The Angular frontend communicates with the Spring Boot backend through RESTful HTTP requests.

```text
        User
         |
         v
   Angular Frontend
   localhost:4200
         |
         | HTTP / REST API
         v
   Spring Boot Backend
   localhost:8080
         |
         v
       MySQL
```

## 📚 Documentation

Project documentation and supporting materials are organized in the `Documentation/` directory.

- **Presentation:** Project presentations and related materials.
- **Diagrams:** UML diagrams, database diagrams, and system architecture documentation.

## 🌱 Development Workflow

The project uses Git for version control and collaborative development.

Suggested branch naming convention:

- `main` — Stable project version.
- `develop` — Integration branch for ongoing development.
- `feature/<feature-name>` — New features.
- `fix/<issue-name>` — Bug fixes.

Example:

```bash
git checkout -b feature/inventory-management
```

## 👥 Contributors

- Ahmed Fendi
- Chiheb Bouali
- Anas Ben Esghaier
- Anas Chammakhi
- Baha Ben Kilani

## 📄 License

This project is developed for academic purposes.

---

**FleetFlow — Connecting Supply Chain and Fleet Operations.**
