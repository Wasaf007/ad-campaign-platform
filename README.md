# Ad Campaign Platform

A production-style Java microservice simulating a digital ad campaign management system, built to mirror the architecture used in high-throughput ad-tech platforms like Basis.

## Tech Stack

- Java 21
- Spring Boot 3.5
- JUnit 5 + Mockito
- PostgreSQL 15
- RabbitMQ 3
- Docker
- GitHub Actions CI/CD
- Maven

## Features

- REST API for ad campaign lifecycle management (create, read)
- PostgreSQL database integration for persistent campaign storage
- RabbitMQ messaging to simulate real-time auction event broadcasting
- Multithreaded auction processor using ExecutorService and AtomicInteger
- 12 automated tests covering unit, integration, and concurrency scenarios
- GitHub Actions CI/CD pipeline that runs all tests on every push

## Running Locally

### Prerequisites
- Java 21
- Docker Desktop
- Maven

### Start Dependencies

```bash
docker run -d --name postgres-basis \
  -e POSTGRES_USER=basis \
  -e POSTGRES_PASSWORD=basis123 \
  -e POSTGRES_DB=adcampaign \
  -p 5432:5432 postgres:15

docker run -d --name rabbitmq-basis \
  -e RABBITMQ_DEFAULT_USER=basis \
  -e RABBITMQ_DEFAULT_PASS=basis123 \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3-management
```

### Run Application

```bash
./mvnw spring-boot:run
```

### Run Tests

```bash
./mvnw test
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/campaigns | Create a new ad campaign |
| GET | /api/campaigns | Get all campaigns |
| GET | /api/campaigns/{id} | Get campaign by ID |

## CI/CD Pipeline

Every push to main or dev triggers the GitHub Actions pipeline which:
1. Spins up PostgreSQL and RabbitMQ in Docker
2. Builds the project with Maven
3. Runs all 12 JUnit tests
4. Reports results

## Author

Mohammed Sharif Akhter
[GitHub](https://github.com/Wasaf007)
