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

This backend integrates with a separate Angular frontend to provide a complete booking experience.

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

## Project Context

This backend was built to replace a legacy 1990s system for a travel agency. The goal was to create a modern, maintainable REST API that integrates with a recently rebuilt Angular frontend, addressing technical debt and providing ongoing support.

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

**1. Create MySQL database and user:**
```sql
CREATE DATABASE `full-stack-ecommerce`;
CREATE USER 'ecommerceapp'@'localhost' IDENTIFIED BY 'ecommerceapp';
GRANT ALL PRIVILEGES ON `full-stack-ecommerce`.* TO 'ecommerceapp'@'localhost';
FLUSH PRIVILEGES;
```

**2. Create database schema:**

The application expects a pre-existing database schema with the following tables:
- `customers`, `countries`, `divisions`
- `vacations`, `excursions`
- `carts`, `cart_items`

**Note:** The schema is not auto-generated (`spring.jpa.hibernate.ddl-auto=none` in configuration). The database schema should be created manually or obtained from course materials before running the application.

**3. Verify database configuration:**

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
git clone https://github.com/yourusername/travelbook-backend.git
cd travelbook-backend
```

**2. Install dependencies:**
```bash
mvn clean install
```

**3. Run the application:**
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

### Input Validation

All customer inputs are validated using Bean Validation annotations:
```java
@Entity
public class Customer {
    @NotNull
    @Size(min = 1, message = "First name is required")
    private String firstName;
    
    @NotNull
    @Pattern(regexp = "^[0-9]{5}$", message = "Postal code must be 5 digits")
    private String postal_code;
    
    @NotNull
    @Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "Phone format: XXX-XXXX")
    private String phone;
}
```

### CORS Support

Cross-origin resource sharing enabled for frontend integration:
```java
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource
public interface VacationRepository extends JpaRepository<Vacation, Long> {
}
```

### Sample Data Loading

Five sample customers are programmatically added on application startup (without overwriting existing data):
```java
@Component
public class BootStrapData implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        if (customerRepository.count() == 0) {
            // Add 5 sample customers
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

This backend is designed to work with an Angular frontend located at `http://localhost:4200`.

**Frontend Integration:**
1. Angular app makes HTTP requests to REST endpoints
2. CORS is configured to allow Angular origin
3. Spring Data REST automatically exposes repositories
4. Custom checkout endpoint handles complex booking logic

**Not Included:**
- The Angular frontend code (maintained separately)
- Frontend modification is not part of this project scope

## Development Workflow

**Typical Development Cycle:**
- Create/modify JPA entity
- Generate repository interface
- Implement service layer if needed
- Add controller endpoint if beyond CRUD
- Test with Postman
- Verify database changes in MySQL Workbench

## Limitations & Trade-offs

**Current Limitations:**
- No authentication/authorization (assumes trusted frontend)
- Basic validation only (no complex business rules)
- No payment processing
- No email notifications
- Minimal error handling beyond validation
- Manual database schema setup required

**Design Trade-offs:**
- Used Spring Data REST for auto-generated endpoints (rapid development vs. control)
- Simple checkout flow (ease of implementation vs. flexibility)
- Direct entity exposure via REST (convenience vs. DTO pattern)

## Future Enhancements

**Security:**
- Add Spring Security with JWT authentication
- Implement role-based access control (admin vs. customer)
- Add API rate limiting

**Features:**
- Payment gateway integration (Stripe, PayPal)
- Email confirmation system
- Booking cancellation/modification
- Vacation package recommendations

**Technical Improvements:**
- Implement DTO pattern to decouple entities from API
- Add comprehensive unit and integration tests
- Set up CI/CD pipeline
- Add API documentation (Swagger/OpenAPI)
- Implement caching for frequently accessed data
- Automated database migration (Flyway/Liquibase)

**Scalability:**
- Database indexing optimization
- Read replicas for vacation/excursion queries
- Redis session management
- Microservices architecture (separate booking, catalog, customer services)

## Common Issues

**Issue: Application fails to start - "Table doesn't exist"**
- Solution: Ensure database schema is created before running the application. The schema is not auto-generated.

**Issue: CORS errors from Angular frontend**
- Solution: Verify `@CrossOrigin` annotation includes Angular dev server URL (`http://localhost:4200`)

**Issue: Validation errors not showing in response**
- Solution: Add `@Valid` annotation to controller method parameters

**Issue: Database connection refused**
- Solution: Verify MySQL is running and credentials in `application.properties` are correct

## License

MIT License - see LICENSE file for details

---

*A demonstration of Spring Boot backend development fundamentals including JPA, REST APIs, and database integration.*
