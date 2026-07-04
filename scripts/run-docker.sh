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

echo "🛠 Build front image"
./scripts/docker/02-build-front.sh


echo "🚀 Lancement des services..."
docker compose up