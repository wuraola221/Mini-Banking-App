# Mini Banking App

A secure RESTful banking backend built with **Java, Spring Boot, Spring Security, and PostgreSQL**.

The project demonstrates practical backend and fintech concepts including authentication, transaction processing, database consistency, concurrency control, and transaction history.

## Features

- User registration and login
- JWT authentication with Spring Security
- BCrypt password hashing
- Automatic account number generation
- Deposit and withdrawal processing
- Account-to-account transfers
- Transaction history and unique transaction references
- Balance validation and insufficient-funds handling
- Optimistic locking with JPA `@Version` for concurrency protection
- Transaction management with `@Transactional`
- Global exception handling
- Environment-based configuration for sensitive credentials

## Tech Stack

- **Backend:** Java, Spring Boot
- **Security:** Spring Security, JWT, BCrypt
- **Database:** PostgreSQL
- **Persistence:** Spring Data JPA, Hibernate
- **Build Tool:** Maven
- **Version Control:** Git & GitHub

## Architecture

The application follows a layered architecture:

Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
PostgreSQL


Core relationship:

User  →  Account  →  Transactions
        (1:1)         (1:N)


## Core API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth` | Register user |
| POST | `/api/auth/login` | Login and receive JWT |
| POST | `/api/accounts/deposit` | Deposit funds |
| POST | `/api/accounts/withdrawal` | Withdraw funds |
| POST | `/api/accounts/transfer` | Transfer funds |

Protected account operations require a valid JWT.

## Transaction Processing

Each financial operation records:

- Transaction type and status
- Amount
- Balance before and after
- Unique transaction reference
- Timestamp
- Transfer reference where applicable

Transfers create separate `TRANSFER_OUT` and `TRANSFER_IN` records while updating both accounts within a transactional operation.

## Running Locally

Clone the repository:

```bash
git clone https://github.com/wuraola221/Mini-Banking-App
cd Mini-Banking-App
```

Configure your PostgreSQL and JWT environment variables, then run:

```bash
./mvnw spring-boot:run
```

The application runs by default on:

```text
http://localhost:8080
```


## Author

**Oyemade Wuraola Hikimat**  
Java Backend Developer
