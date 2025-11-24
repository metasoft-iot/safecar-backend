# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.9.9-eclipse-temurin-25 AS builder
WORKDIR /workspace

# Only copy what is needed for the build
COPY pom.xml .
COPY src ./src

# Produce the runnable JAR (tests skipped for faster image builds)
RUN mvn -B -DskipTests clean package


# Runtime stage
FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

# Non-root user for better security
RUN addgroup --system spring && adduser --system --ingroup spring spring

# Copy the built artifact
COPY --from=builder /workspace/target/backend-*.jar /app/app.jar

EXPOSE 8080
USER spring

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
