#!/bin/bash

echo "📦 Build Angular frontend..."

# aller dans le projet front
cd ../bill-front-ng || exit 1

# Appelle directement le script dev configuré dans le package.json
npm run build:dev

echo "🐳 Build Docker image..."

# retour dossier compose
cd -

# build image Docker
docker build -t front-service ../bill-front-ng

echo "✅ Front Docker image built successfully"