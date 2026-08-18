ARG MSVC_NAME=msvc-cursos

FROM eclipse-temurin:17-jdk-alpine AS builder
LABEL authors="Ronald"

ARG MSVC_NAME

WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw  .
COPY ./$MSVC_NAME/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
ARG MSVC_NAME

WORKDIR /app
RUN mkdir ./logs

ARG TARGET_FOLDER=/app/$MSVC_NAME/target
COPY --from=builder $TARGET_FOLDER/msvc-cursos-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8002
ENV PORT=$PORT_APP

EXPOSE $PORT

CMD ["java", "-jar", "msvc-cursos-0.0.1-SNAPSHOT.jar"]
#Este es a nivel de contendor, es decir, cuando se ejecute el contenedor, este comando se ejecutará.