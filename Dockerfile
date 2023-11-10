FROM openjdk:17-jdk-slim
LABEL authors="ygmelnikov"

ARG JAR_VERSION="0.0.6"
ARG JAR_FILE="build/libs/otus-highload-$JAR_VERSION.jar"
COPY $JAR_FILE "/opt/app.jar"
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
EXPOSE 8080