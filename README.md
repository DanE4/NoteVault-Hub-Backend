# "NoteVault Hub" Website's Backend

## Overview

The backend of the **NoteVault Hub website** is developed in **Java**, leveraging the **Spring Boot** framework. It
functions as the underlying infrastructure for a versatile platform dedicated to fostering both student-student and
student-tutor interactions. This comprehensive platform not only facilitates question and answer interactions but also
empowers users to seamlessly share notes and study materials. Students can pose questions, tutors can provide answers,
and users can collaborate by sharing valuable study resources. To ensure robust security, scalability, and
straightforward deployment, the system integrates a range of cutting-edge technologies and tools.

## Technologies Used

- **Java:** The primary programming language for backend development.
- **Spring Boot:** A powerful framework for building Java-based enterprise applications.
- **PostgreSQL:** The database of choice for storing application data.
- [X] **JWT (JSON Web Token):** Used for authentication and authorization purposes.
- [X] **Spring Security:** Ensures secure authentication and authorization mechanisms.
- [X] **Spring Data JPA:** Simplifies data access and manipulation using the Java Persistence API.
- [X] **Spring Web:** Facilitates the development of web applications.
- [ ] **Spring Cloud (future):** Planned for microservices architecture in the future.
- [X] **Docker:** Utilized for the containerization of the application.
- [X] **Docker Compose:** Orchestrates the deployment of containers.
- [ ] **WebSockets:** Implemented for chat functionality; future plans for extending to other features like posts.
- [X] **Caching:** Implemented across all services for improved performance.
- [ ] **Validation and Sanitization:** Utilizing `@Valid` for validation, ensuring input integrity, and considering
  sanitization.
- [ ] **Rate Limiting:** Implemented to control the rate of incoming requests, preventing abuse or accidental resource
  overuse.
- [ ] **Email Implementation:** Integrated for sending notifications, updates, or verification emails.
- [ ] **File Management:** Handling file compression, potential encryption, and overall file-related operations.
- [X] **Unit Testing:** Thorough testing of individual components to ensure their functionality in isolation.
- [ ] **Integration Testing:** Ensuring seamless interaction between various components of the system.
- [X] **Swagger Documentation :** Enhanced Swagger documentation for more in-depth insights into API endpoints.

### After main features are implemented

- [ ] **Logging Shippability:** Evaluate if logging mechanisms are ready for efficient shipping and analysis.
- [ ] **OAuth2:** Implement OAuth2 for enhanced security and user authentication.

## Note on Credentials

Sensitive information, such as credentials, is managed carefully. The `.env` file and its example, if pushed to a public
repository, will not contain any confidential data. Proper credentials will be set securely in the production
environment.

## How to set up locally

1. Clone the repository.
2. Open the project in your preferred IDE.
3. Run the following command in the terminal:

   ```bash
   docker build -t notevault_backend:latest .
   docker-compose up -d
   ```
4. Run the application.
5. Access the Swagger documentation at `http://localhost:8080/swagger-ui/index.html#/

### Prerequisites

- Java 21
- Docker
- PostgreSQL
- Maven
- IDE (IntelliJ IDEA recommended)

## Current Database Structure

![](https://github.com/DanE4/NoteVault-Hub-Backend/blob/master/DB_structure.png)
