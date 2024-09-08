FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} money-transfer-app.jar
ENTRYPOINT ["java", "-jar", "/money-transfer-app.jar"]