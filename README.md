# TravelBook Backend

**Spring Boot REST API for Vacation Booking System**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

## Overview

TravelBook Backend is a Spring Boot REST API that provides backend functionality for a vacation booking platform. The project demonstrates fundamental Spring Boot development patterns including JPA entity modeling, repository interfaces, service layers, and RESTful controllers with MySQL database integration.

**Core Functionality:**
- Vacation package management
- Customer registration and management
- Excursion booking system
- Shopping cart and checkout processing
- Order tracking

This backend integrates with an Angular frontend ([TravelBook Frontend](https://github.com/r-Dev03/travelbook-frontend)) to provide a complete booking experience.

## Tech Stack

**Backend:**
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Data REST
- Maven

**Database:**
- MySQL 8.0
- Hibernate ORM

**Development Tools:**
- IntelliJ IDEA Ultimate Edition
- Lombok (reduce boilerplate code)
- MySQL Workbench

## Architecture

### Layered Architecture
```
┌─────────────────────────────────┐
│   Angular Frontend (separate)   │
└────────────┬────────────────────┘
             │ HTTP/REST
┌────────────▼────────────────────┐
│      Controllers Layer          │  ← REST endpoints
├─────────────────────────────────┤
│       Services Layer            │  ← Business logic
├─────────────────────────────────┤
│    Repository (DAO) Layer       │  ← Data access
├─────────────────────────────────┤
│      Entity/Model Layer         │  ← JPA entities
└────────────┬────────────────────┘
             │ JDBC
┌────────────▼────────────────────┐
│        MySQL Database           │
└─────────────────────────────────┘
```

## Data Model

### Key Entities

**Customer**
- Customer information (name, address, phone)
- Linked to Division (state/province) and Country
- One-to-many relationship with Carts

**Country**
- Geographic country (e.g., USA, Canada)
- One-to-many relationship with Divisions

**Division**
- State/province within a country
- One-to-many relationship with Customers

**Cart**
- Shopping cart for a customer
- Status type (enum: PENDING, COMPLETED, CANCELLED)
- Order tracking number
- Many-to-many relationship with CartItems

**Vacation**
- Vacation packages (destination, price, dates)
- Image URL, description
- Many-to-many relationship with Excursions

**Excursion**
- Activity/tour packages
- Price, dates, associated vacation

**CartItem**
- Join entity for Cart-Excursion relationship

## Setup & Installation

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+
- IntelliJ IDEA Ultimate (recommended)

### Database Setup

**1. Initialize the database using the provided SQL script:**

This project includes `create_and_populate_db.sql` which sets up the complete database.

**What the script does:**
- Drops and recreates the `full-stack-ecommerce` database
- Creates all tables (customers, countries, divisions, vacations, excursions, carts, cart_items)
- Inserts sample data (demo customer, vacation packages, excursions)
- Creates database user `ecommerceapp` with password `ecommerceapp`
- Grants necessary permissions

**To run the script:**

**Option A: MySQL Workbench (Recommended)**
1. Open MySQL Workbench
2. Connect to your local MySQL instance
3. Go to File > Open SQL Script
4. Select `create_and_populate_db.sql` from the project root
5. Click the lightning bolt icon to execute

**Option B: MySQL Command Line**
```bash
mysql -u root -p < create_and_populate_db.sql
```

**2. Verify database configuration:**

The application is pre-configured in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/full-stack-ecommerce
spring.datasource.username=ecommerceapp
spring.datasource.password=ecommerceapp
spring.jpa.hibernate.ddl-auto=none
spring.data.rest.base-path=/api
```

**Security Note:** For production deployment, change the database password to something secure.

### Build & Run

**1. Clone the repository:**
```bash
git clone https://github.com/r-Dev03/travelbook-backend.git
cd travelbook-backend
```

**2. Set up the database:**
```bash
# Run the SQL script as described above
mysql -u root -p < create_and_populate_db.sql
```

**3. Install dependencies:**
```bash
mvn clean install
```

**4. Run the application:**
```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

**Note:** All API endpoints are prefixed with `/api` (e.g., `http://localhost:8080/api/vacations`)

## API Endpoints

### Vacation Packages

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vacations` | List all vacation packages |
| GET | `/api/vacations/{id}` | Get vacation by ID |
| GET | `/api/vacations/search/findByVacationTitle` | Search vacations by title |

### Excursions

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/excursions` | List all excursions |
| GET | `/api/excursions/{id}` | Get excursion by ID |
| GET | `/api/excursions/search/findByVacation` | Find excursions for vacation |

### Customers

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | List all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| POST | `/api/customers` | Create new customer |

### Checkout

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/checkout/purchase` | Process vacation booking |

**Checkout Request Body:**
```json
{
  "customer": {
    "firstName": "John",
    "lastName": "Doe",
    "address": "123 Main St",
    "postal_code": "12345",
    "phone": "555-1234"
  },
  "cartItems": [
    {
      "vacation": {"id": 1},
      "excursions": [
        {"id": 10},
        {"id": 11}
      ]
    }
  ]
}
```

**Checkout Response:**
```json
{
  "orderTrackingNumber": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

## Project Structure
```
travelbook-backend/
├── mvnw                             # Maven wrapper (Unix)
├── mvnw.cmd                         # Maven wrapper (Windows)
├── pom.xml                          # Maven dependencies
├── create_and_populate_db.sql       # Database initialization script
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/example/demo/
    │   │       ├── DemoApplication.java         # Spring Boot entry point
    │   │       ├── BootStrapData/
    │   │       │   └── BootStrapData.java       # Sample data loader
    │   │       ├── config/
    │   │       │   └── RestDataConfig.java      # CORS & REST config
    │   │       ├── controllers/
    │   │       │   └── CheckoutController.java  # POST /api/checkout/purchase
    │   │       ├── dao/
    │   │       │   ├── CartItemRepository.java
    │   │       │   ├── CartRepository.java
    │   │       │   ├── CountryRepository.java
    │   │       │   ├── CustomerRepository.java
    │   │       │   ├── DivisionRepository.java
    │   │       │   ├── ExcursionRepository.java
    │   │       │   └── VacationRepository.java
    │   │       ├── entities/
    │   │       │   ├── Cart.java
    │   │       │   ├── CartItem.java
    │   │       │   ├── Country.java             # Geographic countries
    │   │       │   ├── Customer.java
    │   │       │   ├── Division.java            # States/provinces
    │   │       │   ├── Excursion.java
    │   │       │   ├── StatusType.java          # Cart status enum
    │   │       │   └── Vacation.java
    │   │       └── services/
    │   │           ├── CheckoutService.java     # Interface
    │   │           ├── CheckoutServiceImpl.java # Implementation
    │   │           ├── Purchase.java            # Request DTO
    │   │           └── PurchaseResponse.java    # Response DTO
    │   └── resources/
    │       └── application.properties           # Database config
    └── test/
        └── java/
            └── com/example/demo/
                └── DemoApplicationTests.java    # Basic test
```

## Key Features

### CORS Support

Cross-origin resource sharing enabled for frontend integration:
```java
@CrossOrigin("http://localhost:4200")
public interface VacationRepository extends JpaRepository<Vacation, Long> {
}
```

### Sample Data Loading

Sample customers are programmatically added on application startup:
```java
@Component
public class BootStrapData implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Set<Customer> customersToAdd = new HashSet<>(customerRepository.findAll());
        
        // Add up to 6 sample customers if not already present
        while (customersToAdd.size() < 6) {
            Customer customer = new Customer();
            // Set customer details...
            customerRepository.save(customer);
        }
    }
}
```

## Testing

### Manual Testing with Postman

**Create a vacation booking:**
```bash
POST http://localhost:8080/api/checkout/purchase
Content-Type: application/json

{
  "customer": {...},
  "cartItems": [...]
}
```

### Database Verification

Use MySQL Workbench to verify data persistence:
```sql
USE full-stack-ecommerce;
SELECT * FROM customers;
SELECT * FROM carts;
SELECT * FROM cart_items;
```

## Integration with Frontend

This backend is designed to work with the [TravelBook Frontend](https://github.com/r-Dev03/travelbook-frontend) Angular application.

**Frontend Integration:**
1. Angular app runs on `http://localhost:4200`
2. Makes HTTP requests to backend REST endpoints
3. CORS is configured to allow Angular origin
4. Spring Data REST automatically exposes repositories
5. Custom checkout endpoint handles complex booking logic

**To run the full stack:**
1. Start the backend: `mvn spring-boot:run`
2. Start the frontend: `ng serve` (in frontend repo)
3. Access the application at `http://localhost:4200`

## Development Workflow

**Typical Development Cycle:**
- Create/modify JPA entity
- Generate repository interface
- Implement service layer if needed
- Add controller endpoint if beyond CRUD
- Test with Postman
- Verify database changes in MySQL Workbench

## Limitations

- No authentication/authorization (assumes trusted frontend)
- Basic validation only (database-level constraints)
- No payment processing
- No email notifications
- Minimal error handling beyond validation
- Database reset script overwrites all data (use carefully)

## License

MIT License - see LICENSE file for details
