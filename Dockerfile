# Use a base image with Java and OpenJDK
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot JAR file to the container
COPY build/libs/*.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
