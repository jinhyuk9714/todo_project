# Use a base image with Java and OpenJDK
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# Run Maven to build the application
RUN apt-get update && apt-get install -y maven
RUN mvn clean package


# Copy the built JAR file to the container
COPY target/*.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
