ARG MSVC_NAME=msvc-usuarios

FROM eclipse-temurin:17-jdk-alpine AS builder
#Builder: JDK → compila.
LABEL authors="Ronald"

ARG MSVC_NAME

WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw  .
COPY ./$MSVC_NAME/pom.xml .

#Docker congela este estado (con la carpeta .m2 llena de librerías) en una capa de caché.
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline

COPY ./$MSVC_NAME/src ./src

#Como este paso cambia cada vez que programás algo nuevo, Docker invalida la caché a partir de aquí. no descarga de internet
RUN ./mvnw clean package -DskipTests
#Aquí se genera el compilador para pasarlo limpia a la imagen 2, se genera con el main codigo fuente.

#Aquí elimina la carpeta de cachpe de maven .m2 y solo se queda con el JAR compilado, para que la imagen final sea más liviana.
FROM eclipse-temurin:17-jre-alpine
#Runtime: JRE → ejecuta el .jar.
ARG MSVC_NAME

WORKDIR /app
RUN mkdir ./logs

ARG TARGET_FOLDER=/app/$MSVC_NAME/target
COPY --from=builder $TARGET_FOLDER/msvc-usuarios-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8001
ENV PORT=$PORT_APP

EXPOSE $PORT

CMD ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"] -> mas  estricto, no permite cambiar el comando de entrada al ejecutar el contenedor.
#Este es a nivel de contendor, es decir, cuando se ejecute el contenedor, este comando se ejecutará.





#NUMEROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO 1 ==========================================================
#FROM eclipse-temurin:17-jre-alpine
#
#LABEL authors="Ronald"
#
#WORKDIR /app
#
#COPY target/msvc-usuarios-0.0.1-SNAPSHOT.jar msvc-usuarios-0.0.1-SNAPSHOT.jar
#
#EXPOSE 8001
#
#ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]


#NUMEROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO 2 ==========================================================
#FROM eclipse-temurin:17-jre-alpine
#
#LABEL authors="Ronald"
#
#WORKDIR /app/msvc-usuarios
#
#COPY ./pom.xml /app
#COPY ./msvc-usuarios .
#
#RUN ./mvn clean package -DskipTests
#
#EXPOSE 8001
#
#ENTRYPOINT ["java", "-jar", "./target/msvc-usuarios-0.0.1-SNAPSHOT.jar"]


#NUMEROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO 3 ==========================================================
#FROM eclipse-temurin:17-jre-alpine
#
#LABEL authors="Ronald"
#
#WORKDIR /app/msvc-usuarios
#
#COPY ./pom.xml /app
#COPY ./msvc-usuarios./.mvn ./.mvn
#COPY ./msvc-usuarios/mvnw  ./mvnw
#COPY ./msvc-usuarios/pom.xml .
#
#RUN ./mvn clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
##RUN ./mvn dependency:go-offline
#
#COPY ./msvc-usuarios/src ./src
#
#RUN ./mvn clean package -DskipTests
#
#EXPOSE 8001
#
#ENTRYPOINT ["java", "-jar", "./target/msvc-usuarios-0.0.1-SNAPSHOT.jar"]



#NUMEROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO 4 ==========================================================
#FROM eclipse-temurin:17-jdk-alpine AS builder
##Builder: JDK → compila.
#LABEL authors="Ronald"
#
#WORKDIR /app/msvc-usuarios
#
#COPY ./pom.xml /app
#COPY ./msvc-usuarios/.mvn ./.mvn
#COPY ./msvc-usuarios/mvnw  .
#COPY ./msvc-usuarios/pom.xml .
#
##Docker congela este estado (con la carpeta .m2 llena de librerías) en una capa de caché.
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
##RUN ./mvnw dependency:go-offline
#
#COPY ./msvc-usuarios/src ./src
#
##Como este paso cambia cada vez que programás algo nuevo, Docker invalida la caché a partir de aquí. no descarga de internet
#RUN ./mvnw clean package -DskipTests
##Aquí se genera el compilador para pasarlo limpia a la imagen 2, se genera con el main codigo fuente.
#
##Aquí elimina la carpeta de cachpe de maven .m2 y solo se queda con el JAR compilado, para que la imagen final sea más liviana.
#FROM eclipse-temurin:17-jre-alpine
##Runtime: JRE → ejecuta el .jar.
#WORKDIR /app
#RUN mkdir ./logs
#
#COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar .
#
#EXPOSE 8001
#
#CMD ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]
##ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"] -> mas  estricto, no permite cambiar el comando de entrada al ejecutar el contenedor.
##Este es a nivel de contendor, es decir, cuando se ejecute el contenedor, este comando se ejecutará.