# Stage 1: Build application using Java 21 JDK
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper and pom.xml first for efficient layer caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Download dependencies offline (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code and build package
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Create lightweight runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Security: Run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built artifact (war package) from builder stage
COPY --from=builder /app/target/*.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]
