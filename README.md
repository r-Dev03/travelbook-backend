# TravelBook Backend

**Spring Boot REST API for a vacation booking platform, with a transactional checkout.**

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

Customers browse vacation packages, add excursions, and check out. Each order is saved in a single transaction and gets a unique tracking number. This API serves the [TravelBook Frontend](https://github.com/r-Dev03/travelbook-frontend), an Angular client.

**Built on:** a starter template that supplied the MySQL schema (`create_and_populate_db.sql`), the Spring Data REST configuration (`RestDataConfig`), and the Angular client. The JPA entity mappings, repositories, checkout service and controller, and sample-data bootstrapping are implemented in this repository.

## Highlights

- REST API mapping 7 JPA entities to MySQL: vacations, excursions, carts, cart items, customers, divisions, and countries
- Transactional checkout that saves complete orders and issues a UUID tracking number
- Keeps both sides of the bidirectional Cart–CartItem relationship in sync, so every order is saved with all of its items
- Sample customers loaded at startup until the table holds six, so restarts add none

## How It Works

### Data model
```
Country ─< Division ─< Customer ─< Cart ─< CartItem >─ Vacation ─< Excursion
                                                │                    │
                                                └─── excursions >────┘ (many-to-many)
```
A **Cart** is one order. Each **CartItem** is one vacation in that order, plus the excursions chosen for it.

### Checkout
Most endpoints are generated directly from the repositories by Spring Data REST. Checkout is the one custom flow, because placing an order writes to several tables at once:

```
Angular client                       Spring Boot API                        MySQL
──────────────                       ───────────────                        ─────
Order confirmation view
  │  POST /api/checkout/purchase
  │  { customer, cart, cartItems }
  └───────────────────────────▶  CheckoutController
                                       │
                                       ▼
                                 CheckoutServiceImpl.placeOrder()  ┐
                                   1. generate UUID tracking #     │ one
                                   2. link each item ⇄ cart        │ @Transactional
                                   3. save cart + items ──────────▶│ carts, cart_items,
                                   4. save customer ──────────────▶│ excursion_cartitem,
                                       │                           ┘ customers
  shows tracking #  ◀──────────────────┘
                     { orderTrackingNumber }
```

If any save fails, every write in the bracket rolls back together.

Simplified from `CheckoutServiceImpl`:

```java
@Transactional
public PurchaseResponse placeOrder(Purchase purchase) {
    Cart cart = purchase.getCart();
    String orderTrackingNumber = UUID.randomUUID().toString();
    cart.setOrderTrackingNumber(orderTrackingNumber);
    cart.setStatus(StatusType.ordered);
    purchase.getCartItems().forEach(item -> {
        cart.add(item);       // parent → child
        item.setCart(cart);   // child → parent (owns the foreign key)
    });
    cartRepository.save(cart);
    customerRepository.save(purchase.getCustomer());
    return new PurchaseResponse(orderTrackingNumber);
}
```

- **`@Transactional`:** the cart, its items, and the customer are saved together. If any part fails, the whole order rolls back instead of leaving partial data.
- **Syncing both sides:** `Cart` and `CartItem` reference each other, and each side does a different job. The cart's item collection carries the cascade, so adding each item to it is what gets the items saved along with the cart. `CartItem` is the owning side that maps the `cart_id` foreign key, so each item also has to point back at its cart. Setting only one side either skips saving the items or leaves `cart_id` empty, which the schema rejects.

## API

All endpoints are prefixed with `/api`.

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vacations` | List vacation packages |
| GET | `/api/vacations/{id}/excursions` | Excursions for a vacation |
| GET | `/api/excursions` | List excursions |
| GET / POST | `/api/customers` | List or create customers |
| GET | `/api/divisions`, `/api/countries` | Location lookups |
| POST | `/api/checkout/purchase` | Place an order, returns `{ "orderTrackingNumber": "…" }` |

## Getting Started

**Prerequisites:** Java 21, MySQL 8

```bash
git clone https://github.com/r-Dev03/travelbook-backend.git
cd travelbook-backend

# Create the database, tables, sample data, and app user
mysql -u root -p < create_and_populate_db.sql

./mvnw spring-boot:run
```

The API runs at `http://localhost:8080/api`. The database connection is configured in `src/main/resources/application.properties` (user `ecommerceapp`, created by the SQL script).

## Tech Stack

Java 21 · Spring Boot · Spring Data JPA / Hibernate · Spring Data REST · MySQL · Lombok · Maven

## Known Limitations

- **No authentication.** The API trusts the client, so it isn't meant to be deployed publicly as-is.
- **No input validation on checkout.** The service saves whatever purchase payload it receives.
