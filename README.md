# Help Desk Pro

A personal project built to practice modern Java and Spring Boot concepts through a full-stack Help Desk ticket management system.

> **Status:** 🚧 Personal project — actively evolving

---

# 🌐 Application

### Front-end
Hosted on **Vercel**

### Back-end
Hosted on **Render**

> This is the first public release of the project. The application is currently deployed on free cloud platforms for demonstration and learning purposes. Improvements to performance, scalability and infrastructure are planned next steps.

---

# 📌 About the Project

Help Desk Pro is a ticket management system designed to simulate a real-world customer support environment.

The application allows authenticated users to create and manage support tickets, while administrators have full control over ticket management and workflow.

The main goal of this project was to deepen my knowledge of modern backend development using the Spring ecosystem, applying concepts I've studied around security, caching, messaging and layered architecture.

---

# ✨ Features

## Authentication
- User registration
- Secure login with JWT
- Refresh Token authentication
- Logout
- Protected routes
- Role-Based Access Control (RBAC)

## Ticket Management
- Create support tickets
- Update tickets
- Change ticket status
- Set ticket priority
- Find ticket by ID
- Paginated ticket listing
- Delete tickets

## Security
- Spring Security
- JWT Authentication
- Role-based authorization
- Protected endpoints
- Password encryption using BCrypt

## Performance
- Redis Cache
- Spring Cache abstraction

## Messaging
- RabbitMQ integration
- Asynchronous communication between services

## Database
- PostgreSQL

## Testing
- JUnit 5
- Mockito

## API Documentation
- Swagger / OpenAPI

---

# 🛠️ Tech Stack

## Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Validation
- JWT
- PostgreSQL
- Redis
- RabbitMQ
- Flyway
- Maven
- Docker
- Docker Compose
- GitHub Actions
- Swagger / OpenAPI
- JUnit 5
- Mockito

## Frontend
- React
- Vite
- Axios
- React Router
- Tailwind CSS

---

# 🏗️ Architecture

The application follows a layered architecture to improve maintainability and separation of concerns.

Project structure includes:
- Controllers
- Services
- Repositories
- DTOs
- Entities
- Security
- Configuration
- Cache
- Messaging
- Exception Handling

---

# 🔐 Security

Security was one of the main areas of focus in this project.

Implemented features include:
- JWT Authentication
- Refresh Token flow
- Role-Based Authorization
- BCrypt password hashing
- Request validation
- Centralized exception handling
- CORS configuration
- Spring Security endpoint protection

---

# 📖 API Documentation

Interactive API documentation is available through Swagger.

After starting the backend, access:

```text
/swagger-ui/index.html
```

---

# ⚙️ CI/CD

This project uses **GitHub Actions** to automate build and test verification on every push and pull request.

Pipeline includes:
- Automated build with Maven
- Execution of unit tests (JUnit 5 + Mockito)
- Fail-fast checks before merging changes

---

# 🚀 Running the Project Locally

## Clone the repository
```bash
git clone <REPOSITORY_URL>
```

## Backend
```bash
cd help-desk-pro
```

Configure the required environment variables.

Then run:
```bash
docker compose up -d
```
or
```bash
mvn spring-boot:run
```

## Frontend
```bash
cd frontend

npm install

npm run dev
```

---

# ⚙️ Environment Variables

Example:
```env
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

JWT_SECRET=

REDIS_HOST=
REDIS_PASSWORD=

RABBITMQ_HOST=
RABBITMQ_USERNAME=
RABBITMQ_PASSWORD=

VITE_API_BASE_URL=
```

---

# 📸 Screenshots

Coming soon.

Suggested images:
- Login Screen
- Dashboard
- Ticket List
- Ticket Details
- Ticket Creation
- Swagger UI
- Database
- Running Docker Containers

---

# 🎯 Project Goals

This project was built to strengthen my knowledge of:
- RESTful API development
- Spring Boot
- Layered Architecture
- Spring Security
- JWT Authentication
- Redis
- RabbitMQ
- Docker
- CI/CD with GitHub Actions
- Cloud Deployment
- Unit Testing
- Backend Best Practices

---

# 🚀 Next Steps

- Migration to a more robust cloud infrastructure
- Monitoring and observability
- Integration testing
- File attachment support
- Real-time notifications
- Analytics dashboard
- UI/UX improvements
- Increased test coverage

---

# 👨‍💻 Author

Developed by **Gabriel Wederson Morais da Silva** as a personal project to improve my backend development skills and gain hands-on experience with modern Java technologies and software architecture.

Feel free to open an Issue or contribute with suggestions and improvements.

---

## 🔗 Links

- **Frontend:** https://help-desk-pro-orpin.vercel.app/
- **Backend API:** https://helpdeskpro-uo7q.onrender.com
- **Swagger UI:** https://helpdeskpro-uo7q.onrender.com/swagger-ui/index.html
