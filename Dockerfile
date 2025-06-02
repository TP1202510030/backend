FROM openjdk:22-jdk-slim
LABEL authors="alanegd"

ARG JAR_FILE=target/backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 3000

ENTRYPOINT ["java","-jar","/app.jar"]