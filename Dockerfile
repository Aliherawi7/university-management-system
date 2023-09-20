FROM ubuntu:latest
LABEL authors="Ali Herawi"
# Fetching latest version of Java
FROM openjdk

# Setting up work directory
WORKDIR /app

# Copy the jar file into our app
COPY ./target/portal-api-0.0.1-SNAPSHOT.jar /app
COPY upload /app/upload

# Exposing port 5000
EXPOSE 5000
# Starting the application
CMD ["java", "-jar", "portal-api-0.0.1-SNAPSHOT.jar"]