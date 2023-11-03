This is Homework Help website's backend, written in Java, using Spring Boot.

The application is using a PostgreSQL database, and is using JWT for authentication,

also, the app makes use of Spring Security, Spring Data JPA, Spring Web, and will use Spring Cloud for the microservices in the future.

The application is using Docker for containerization, and is using Docker Compose for the orchestration of the containers.

The main purpose of the system to be able to provide a platform for students to ask questions, and for tutors to 
answer them, the users would earn points for answering questions, and would be able to spend those points to ask 
questions, or in the future if the site collaborates with companies or other sites, they would be able to spend 
those as a discount on the other site.

Swagger Documentation is accessible at http://localhost:8080/swagger-ui.html

With authentication enabled, the credentials that are seen in .env are not used in prod, those are only for local testing environment.

The final/confidental credentials will be set properly at the end, so .env and .env-example, if the backend is pushed somewhere, so it will not contain any sensitive data.