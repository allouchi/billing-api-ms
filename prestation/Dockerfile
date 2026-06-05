FROM eclipse-temurin:21-jre
WORKDIR /app

# On prend le JAR qui a été proprement généré par le script PowerShell juste avant
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]