# Use Java 17
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the Spring Boot JAR
COPY target/student-management-0.0.1-SNAPSHOT.jar app.jar

# Expose the same port as in application.properties
EXPOSE 8089

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
