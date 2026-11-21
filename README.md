# Task Management Spec Kit

A responsive task-management dashboard with a React frontend and a Java 21
Spring Boot REST API. The backend stores data in H2 and seeds five example
tasks on startup when the task table is empty.

## Requirements

- Java 21
- Maven
- Node.js and npm

## Run locally

Start the backend:

```powershell
cd backend
mvn spring-boot:run
```

The REST API runs at `http://localhost:8080`. OpenAPI documentation is available
at `http://localhost:8080/swagger-ui.html`.

In another terminal, start the frontend:

```powershell
cd frontend
npm ci
npm run dev
```

Open the Vite URL printed in the terminal. The dashboard defaults to six tasks
per page and supports card and table views, search, filters, sorting, and task
management.

## Test and build

Backend tests and the configured JaCoCo coverage gate:

```powershell
cd backend
mvn clean verify
```

Frontend tests and production build:

```powershell
cd frontend
npm ci
npm test
npm run build
```
