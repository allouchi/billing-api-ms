
FROM openjdk:21-oracle AS build
WORKDIR /app
COPY ../pom.xml ../pom.xml
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]