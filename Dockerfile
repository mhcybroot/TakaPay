# --- Stage 1: Build the Application ---
FROM gradle:8.5-jdk21-alpine AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
# Skip tests to speed up the build, remove -x test if you want them to run
RUN ./gradlew bootJar --no-daemon -x test

# --- Stage 2: Run the Application ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy only the built JAR from the 'build' stage
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]