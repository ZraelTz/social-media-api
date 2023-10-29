FROM openjdk:17
VOLUME /api-service
ARG JAR_FILE=target/api-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} api-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/api-service.jar"]
