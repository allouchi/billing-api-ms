#!/bin/bash
set -e

echo "=== 1. Vérification du statut de Minikube ==="
if minikube status | grep -q "Running"; then
    echo "▶ Minikube est déjà en cours d'exécution."
else
    echo "🛑 Minikube n'est pas démarré. Démarrage en cours..."
    minikube start --container-runtime=docker --cpus=4 --memory=8192 # 💡 Conseil : Passe à 8Go si tu as 5+ microservices Spring
fi

echo "=== 2. Synchronisation de la ConfigMap ==="
# On s'assure que le fichier .env existe avant de planter
if [ -f .env ]; then
    kubectl create configmap variables-config --from-env-file=.env --dry-run=client -o yaml | kubectl apply -f -
else
    echo "⚠️ Attention : Aucun fichier .env trouvé à la racine !"
fi

echo "=== 3. Nettoyage du Docker de Minikube ==="
# On exécute le prune directement "à l'intérieur" sans polluer le terminal actuel
minikube ssh "docker system prune -af"

echo "=== 4. Lancement de Skaffold ==="
# Lance skaffold normalement. Pas besoin de désactiver le cache à chaque fois,
# sauf si tu as des comportements bizarres, car le cache fait gagner beaucoup de temps.
skaffold dev --cleanup=true --no-prune=false