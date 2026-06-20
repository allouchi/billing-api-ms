#!/bin/bash

set -euo pipefail

echo "=================================================="
echo "🐳 Building Docker images (local mode)"
echo "=================================================="

# Liste de tes microservices Java (correspondant aux noms de tes dossiers)
services=(
  discovery
  gateway
  #config
  authent
  consultant
  client
  company
  prestation
  facture
)

# Boucle de build pour les services Java
for s in "${services[@]}"; do
  echo ""
  echo "🔨 Building ${s}-service-image"
  # Utilisation du suffixe '-image' pour correspondre à tes fichiers Kubernetes Deployment
  docker build -t "${s}-service-image:latest" "./${s}"
done

echo ""
echo "🌐 Building front-service-image"
# Build du dossier Angular externe
docker build -t front-service-image:latest ../bill-front-ng

echo ""
echo "=================================================="
echo "📦 Loading images into Minikube"
echo "=================================================="

# Injection automatique des images Java dans le cache de Minikube
for s in "${services[@]}"; do
  echo "📥 Loading ${s}-service-image into Minikube..."
  minikube image load "${s}-service-image:latest"
done

# Injection du Front Angular
echo "📥 Loading front-service-image into Minikube..."
minikube image load front-service-image:latest

echo ""
echo "=================================================="
echo "🎉 ALL IMAGES BUILT AND LOADED SUCCESSFULLY"
echo "=================================================="