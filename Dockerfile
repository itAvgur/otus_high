FROM openjdk:17-jdk-slim
LABEL authors="ygmelnikov"

ARG JAR_VERSION="0.0.1-SNAPSHOT"
ARG JAR_FILE="build/libs/otus-highload-$JAR_VERSION.jar"
COPY $JAR_FILE "/opt/app.jar"
RUN apt-get update && apt-get install -y iputils-ping
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
EXPOSE 8080