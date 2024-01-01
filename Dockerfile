FROM bellsoft/liberica-openjdk-alpine:latest
WORKDIR /app

RUN apk add --no-cache maven
COPY pom.xml .
RUN mvn dependency:go-offline


COPY src ./src

RUN mvn package -DskipTests
