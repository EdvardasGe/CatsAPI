# Cats API

A Spring Boot API for managing cat profiles, including pagination, sorting, and basic authentication.

## Requirements

- Docker
- Java 21
- PostgreSQL (for prod environment)

## Running the Application with Docker Compose

1. Ensure Docker is installed and running on your machine.
2. In the project root directory, run Docker Compose:

   ```bash
   docker-compose up --build

## Pagination and Sorting

The `GET /cats` endpoint supports pagination and sorting.

### Parameters
- `page` (int): The page number (0-indexed).
- `size` (int): The number of records per page.
- `sort` (string): Field by which to sort the results (e.g., `name`, `age`). To sort in descending order, prefix the field with a hyphen (e.g., `-age`).

### Example
```http
GET /cats?page=1&size=5&sort=age
```

## Swagger UI
http://localhost:8080/swagger-ui/index.html

## Secured Endpoints
The API uses basic authentication to restrict access to certain endpoints. Only authenticated users can create, update, or delete cats.

- **Access Credentials**:
    - Username: `user`
    - Password: `password`
- **How to Authenticate**:
    - When accessing protected endpoints (e.g., `/cats`), provide the username and password in the HTTP Basic Authentication header.

## Database Configuration

This project supports H2 and PostgreSQL databases. Use the `dev` profile for H2 (local testing) and `prod` for PostgreSQL.

**To switch profiles:**
- Open `application.properties`.
- Set `spring.profiles.active=dev` for H2 or `spring.profiles.active=prod` for PostgreSQL.
   