# =========================================================================
# SCRIPT DE DEPLOIEMENT ORDONNE DANS KUBERNETES (TIMINGS BLINDES) - V3
# =========================================================================

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$ErrorActionPreference = "Stop"
$k8sFolder = "$PSScriptRoot/k8s"

echo "========================================================="
echo " EXECUTION DU DEPLOIEMENT ORDONNE K8S"
echo "========================================================="

# --- ÉTAPE OPTIONNELLE : Nettoyage pour forcer la prise en compte du nouveau ConfigMap ---
echo "`n=== NETTOYAGE SECURITE (Optionnel) ==="
echo "Suppression de l'ancien déploiement MySQL pour forcer le re-sourcing du init.sql..."
kubectl delete deployment billing-db --ignore-not-found=true
# Si vous observez que les bases ne se créent toujours pas, décommentez la ligne suivante (⚠️ écrase les données locales) :
# kubectl delete pvc --all --ignore-not-found=true

# --- ETAPE 1 : Base de données ---
echo "`n=== ETAPE 4.1 : Lancement de la Base de donnees ==="

$cmFile = "$k8sFolder/billing-db-cm0-configmap.yaml"
if (Test-Path $cmFile)
{
    echo "Mise a jour de la ConfigMap pour l'initialisation SQL (init.sql)..."
    kubectl apply -f $cmFile
}
else
{
    Write-Error "Fichier ConfigMap introuvable : $cmFile !"
    return # 🟢 CORRECTION : Évite de fermer brutalement la console PowerShell
}

$dbFile = "$k8sFolder/billing-db.yaml"
if (Test-Path $dbFile)
{
    echo "Lancement du conteneur MySQL (billing-db)..."
    kubectl apply -f $dbFile
}
else
{
    Write-Error "Fichier billing-db.yaml introuvable !"
    return # 🟢 CORRECTION : Évite de fermer brutalement la console PowerShell
}

# Attente intelligente de la mise à disposition de MySQL
echo "Attente que le Pod MySQL soit 'Ready'..."
kubectl wait --for=condition=ready pod -l io.kompose.service=billing-db --timeout=90s

echo "Pause de 15 secondes pour laisser à MySQL le temps d'exécuter entièrement le script init.sql..."
Start-Sleep -Seconds 15


# --- ETAPE 2 : Serveurs Infrastructure Spring Cloud ---
echo "`n=== ETAPE 4.2 : Lancement des serveurs d'infrastructure ==="

echo "Lancement du Discovery Server..."
if (Test-Path "$k8sFolder/discovery.yaml")
{
    kubectl apply -f "$k8sFolder/discovery.yaml"
}
else
{
    kubectl apply -f "$k8sFolder/discovery-deployment.yaml" --ignore-not-found=true
}

Start-Sleep -Seconds 10

echo "Lancement du Config Server..."
if (Test-Path "$k8sFolder/config.yaml")
{
    #kubectl apply -f "$k8sFolder/config.yaml"
}
else
{
    #kubectl apply -f "$k8sFolder/config-deployment.yaml" --ignore-not-found=true
}

# --- ETAPE 3 : Passerelle (Gateway) ---
echo "`n=== ETAPE 4.3 : Lancement de la Gateway ==="
if (Test-Path "$k8sFolder/gateway.yaml")
{
    kubectl apply -f "$k8sFolder/gateway.yaml"
}
else
{
    kubectl apply -f "$k8sFolder/gateway-deployment.yaml" --ignore-not-found=true
}

Start-Sleep -Seconds 5


# --- ETAPE 4 : Microservices Métiers ---
echo "`n=== ETAPE 4.4 : Lancement des microservices metiers ==="
# ⚠️ ATTENTION : Vérifiez si votre fichier s'appelle 'authent.yaml' ou 'authent-service.yaml' et ajustez la liste ci-dessous
$metierServices = @("authent", "client", "company", "consultant", "facture", "prestation")

foreach ($svc in $metierServices)
{
    $targetFile = "$k8sFolder/$svc.yaml"
    if (Test-Path $targetFile)
    {
        echo "Deployement du service unique : $svc"
        kubectl apply -f $targetFile
    }
    else
    {
        $fallbackFile = "$k8sFolder/$svc-deployment.yaml"
        if (Test-Path $fallbackFile)
        {
            echo "Deployement du service (fallback) : $svc"
            kubectl apply -f $fallbackFile
        }
        else
        {
            Write-Warning "Impossible de trouver le fichier YAML pour le service : $svc"
        }
    }
}

# --- ETAPE 5 : Application Graphique (Frontend) ---
echo "`n=== ETAPE 4.5 : Lancement du Frontend ==="
if (Test-Path "$k8sFolder/front.yaml")
{
    kubectl apply -f "$k8sFolder/front.yaml"
}
else
{
    kubectl apply -f "$k8sFolder/front-deployment.yaml" --ignore-not-found=true
}

echo "Attente du démarrage global des Pods..."
Start-Sleep -Seconds 15

echo "`n========================================================="
echo " Deploiement termine ! Verification du statut des Pods :"
echo "========================================================="
kubectl get pods

# --- ETAPE 6 : Tunnels d'accès automatiques en arrière-plan ---
echo "`n========================================================="
echo " Démarrage des tunnels réseau..."
echo "========================================================="

# 🟢 CORRECTION FRONTEND : On cible le deployment/front pour mapper directement le port 80 du conteneur sans passer par le mauvais port du service.
#echo "Ouverture du tunnel Frontend sur http://localhost:8282"
#Start-Job -ScriptBlock { kubectl port-forward deployment/front 8282:80 } | Out-Null
#Start-Sleep -Seconds 2

# 🟢 CORRECTION GATEWAY : On utilise le vrai nom 'gateway-service' sur le port 8181 attendu par votre Angular
#echo "Ouverture du tunnel Gateway sur http://localhost:8181"
#Start-Job -ScriptBlock { kubectl port-forward service/gateway-service 8181:8181 } | Out-Null
#Start-Sleep -Seconds 3

# Ouverture automatique du navigateur sur la page de login
#echo "Lancement du navigateur..."
#Start-Process "http://localhost:8282"