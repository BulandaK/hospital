FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

MAINTAINER kg

COPY target/medical-clinic-0.0.1-SNAPSHOT.jar medical-clinic-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","medical-clinic-0.0.1-SNAPSHOT.jar"]