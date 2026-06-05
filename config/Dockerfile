FROM eclipse-temurin:21-jre
WORKDIR /app

# 1. On copie le JAR du serveur de configuration généré par Maven
COPY target/*.jar app.jar

# 2. IMPORTANT : On copie le dossier config-repo du projet vers /app/config-repo dans l'image
# Option Cas 1
COPY src/main/resources/config-repo ./config-repo

ENTRYPOINT ["java", "-jar", "app.jar"]