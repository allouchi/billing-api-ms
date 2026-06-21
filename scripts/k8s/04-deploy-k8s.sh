#!/bin/bash

set +e

# Crée une ConfigMap à partir des variables classiques
kubectl create configmap variables-config --from-env-file=.env --dry-run=client -o yaml | kubectl apply -f -


# ==============================================================================
# FONCTION DE VÉRIFICATION ET DÉPLOIEMENT POUR LES SERVICES
# ==============================================================================
deploy_if_not_up() {
    local service_name=$1
    local yaml_path=$2

    echo "🔍 Vérification du statut de : $service_name..."
    local available_replicas=$(kubectl get deployment "$service_name" -o jsonpath='{.status.availableReplicas}' 2>/dev/null)

    if [ -z "$available_replicas" ] || [ "$available_replicas" -eq 0 ]; then
        echo "⚠️  $service_name n'est pas actif. Déploiement en cours..."
        kubectl apply -f "$yaml_path"
        kubectl rollout status deployment/$service_name --timeout=120s
        echo "✅ $service_name a été déployé."
    else
        echo "🟢 $service_name est déjà UP (Réplicas: $available_replicas). Étape ignorée."
    fi
    echo "------------------------------------------------"
}

# ==============================================================================
# ÉTAPE 1 : DÉPLOIEMENT PRIORITAIRE DE LA BASE DE DONNÉES
# ==============================================================================
echo "📦 [ÉTAPE 1] Déploiement et initialisation de la Base de Données..."
kubectl apply -f k8s/billing-db/

echo "⏳ Attente que le conteneur de la base de données soit prêt (Running)..."
# Cette commande bloque le script tant que les pods avec le label lié à la DB ne sont pas prêts
# Note : Adaptez 'app=billing-db' selon le label exact défini dans votre fichier YAML de la DB
kubectl wait --for=condition=ready pod -l app=billing-db --timeout=120s

if [ $? -ne 0 ]; then
    echo "❌ Erreur : La base de données n'a pas démarré à temps. Arrêt du script."
    exit 1
fi

echo "🛢️  La base de données est prête ! Lancement des microservices..."
echo "------------------------------------------------"

# ==============================================================================
# ÉTAPE 2 : EXÉCUTION DU RESTE DU PIPELINE (Ordre logique de dépendance)
# ==============================================================================
echo "🚀 [ÉTAPE 2] Analyse et déploiement des microservices..."

# On déploie Eureka en premier car tous les autres services s'y enregistrent
deploy_if_not_up "discovery-service" "k8s/discovery/"

# On déploie le service d'authentification (nécessaire pour la Gateway et les microservices)
deploy_if_not_up "authent-service" "k8s/authent/"

# On déploie la Gateway
deploy_if_not_up "gateway-service" "k8s/gateway/"

# Déploiement des microservices fonctionnels
deploy_if_not_up "company-service" "k8s/company/"
deploy_if_not_up "prestation-service" "k8s/prestation/"
deploy_if_not_up "client-service" "k8s/client/"
deploy_if_not_up "consultant-service" "k8s/consultant/"
deploy_if_not_up "facture-service" "k8s/facture/"

# Déploiement du Front en dernier
deploy_if_not_up "front-service" "k8s/front/"

echo "🎉 Analyse terminée. L'infrastructure est stable et opérationnelle !"