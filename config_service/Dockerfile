FROM --platform=linux/amd64 openjdk:17-jdk-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/config_service-1.0-SNAPSHOT.jar
ARG CONFIG_FILE_PATH=src/main/resources/config-repo/
ARG APP_YML=src/main/resources/application.yml
ARG AWS_YML=src/main/resources/application-aws.yml

COPY ${JAR_FILE} app.jar
COPY ${CONFIG_FILE_PATH} config-repo/
COPY ${APP_YML} application.yml
COPY ${AWS_YML} application-aws.yml

ENV SPRING_PROFILES_ACTIVE=aws,native

ENTRYPOINT ["java", "-jar", "app.jar"]