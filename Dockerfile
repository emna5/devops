# ===== Build Stage =====
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven files first (for caching)
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests if you want)
RUN mvn clean package -DskipTests

# ===== Runtime Stage =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/student-management-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8089

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
