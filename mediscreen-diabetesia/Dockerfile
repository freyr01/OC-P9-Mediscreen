FROM openjdk:8-jdk-alpine
LABEL maintainer="mathias.lauer.fr@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} mediscreen-diabetesia.jar
ENTRYPOINT ["java","-jar","/mediscreen-diabetesia.jar"]