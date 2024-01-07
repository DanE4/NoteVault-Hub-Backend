# Use a base image that supports multiple architectures
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

# Use a base image that supports ARM64 architecture
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/HomeworkHelp-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
