FROM ubuntu:latest
LABEL authors="Ali Herawi"
# Fetching latest version of Java
FROM openjdk:17

# Setting up work directory
WORKDIR /app

# Copy the jar file into our app
COPY ./target/spring-0.0.1-SNAPSHOT.jar /app

# Exposing port 5000
EXPOSE 5000
# Starting the application
CMD ["java", "-jar", "spring-0.0.1-SNAPSHOT.jar"]