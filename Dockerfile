FROM eclipse-temurin:17-jdk
LABEL maintainer="doston"

COPY build/libs/Techlearner-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
