# Étape 1 : Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. On copie le POM parent à la racine de /app
COPY pom.xml /app/pom.xml

# 2. On se place dans le sous-dossier du module "client"
WORKDIR /app/client
COPY client/pom.xml .

COPY client/src ./src

# 3. On lance le build. Maven trouvera le parent dans "../pom.xml"
RUN mvn clean package -DskipTests

# Étape 2 : Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/client/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]