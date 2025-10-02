# Multi-stage Dockerfile for Spring Boot Application
# Build stage
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Copy source code
COPY src ./src

# Build the application (skip tests as database is not available during build)
RUN chmod +x gradlew && ./gradlew build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built jar from build stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
