FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/TakaPay-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Added space below
ENTRYPOINT ["java", "-jar", "app.jar"]