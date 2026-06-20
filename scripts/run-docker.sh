#!/bin/bash

set -e

export JAVA_HOME="/c/Program Files/Java/jdk-21.0.11"
export PATH="$JAVA_HOME/bin:$PATH"

echo "JAVA_HOME=$JAVA_HOME"

if [ -z "$JAVA_HOME" ]; then
  echo "JAVA_HOME is not set"
  exit 1
fi

echo "🧹 Nettoyage Docker (optionnel)..."
./scripts/docker/01-clean.sh

echo "🛠 Build Maven multi-module..."
./scripts/docker/01-build-maven.sh

echo "🐳 Build images Docker..."

# Les parenthèses permettent d'entrer et de sortir du dossier proprement
(cd ./discovery && docker build -t discovery-service .)
(cd ./gateway && docker build -t gateway-service .)
(cd ./authent && docker build -t authent-service .)
(cd ./company && docker build -t company-service .)
(cd ./consultant && docker build -t consultant-service .)
(cd ./client && docker build -t client-service .)
(cd ./prestation && docker build -t prestation-service .)
(cd ./config && docker build -t config-service .)

echo "🚀 Lancement des services..."
docker compose up