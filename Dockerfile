FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

COPY build/libs/UserService-1.0-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]