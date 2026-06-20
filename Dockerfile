FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# 1. Copier uniquement les POM (cache dépendances)
COPY pom.xml .
COPY */pom.xml ./

RUN mvn -B -q -e -DskipTests dependency:go-offline

# 2. Copier le reste du code
COPY . .

# 3. Build
RUN mvn clean install -DskipTests