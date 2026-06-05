# =========================================================================
# SCRIPT DE RESTRUCTURATION ET REMISE A NEUF COMPLETE DU CLUSTER K8S
# =========================================================================

# Securite : On force l'arret du script en cas d'erreur critique
$ErrorActionPreference = "Stop"

# Determination intelligente de la racine du projet
$currentDir = $PSScriptRoot

# Si le script est execute depuis le sous-dossier 'k8s', on remonte d'un niveau
if ($currentDir -like "*\k8s" -or $currentDir -like "*/k8s")
{
    $rootDir = Split-Path -Path $currentDir -Parent
    $k8sFolder = $currentDir
}
else
{
    $rootDir = $currentDir
    $k8sFolder = "$rootDir/k8s"
}

echo "========================================================="
echo " START : REMISE A NEUF COMPLETE DE L'APPLICATION"
echo " Racine detectee : $rootDir"
echo " Dossier K8s     : $k8sFolder"
echo "========================================================="

# --- ETAPE 1 : Nettoyage de Kubernetes ---
echo "`n=== ETAPE 1 : Suppression des ressources Kubernetes ==="
if (Test-Path $k8sFolder)
{
    Write-Host "Suppression des Pods, Services et Deployments..." -ForegroundColor Cyan

    # On applique la suppression sur le dossier contenant les YAML
    kubectl delete -f $k8sFolder --ignore-not-found=true

    # On attend de maniere securisee que Kubernetes ait fini de detruire les Pods
    Write-Host "Attente de la destruction complete des Pods..." -ForegroundColor Yellow

    $podsRunning = $true
    while ($podsRunning)
    {
        try
        {
            # On demande du JSON (-o json). Si c'est vide, l'objet retourné aura 0 éléments.
            # Cela évite le message textuel "No resources found" qui fait crasher PowerShell.
            $podCount = (kubectl get pods -o json | ConvertFrom-Json).items.Count

            if ($podCount -eq 0 -or $null -eq $podCount)
            {
                $podsRunning = $false
            }
            else
            {
                Start-Sleep -Seconds 3
                Write-Host "." -NoNewline
            }
        }
        catch
        {
            # Si jamais kubectl renvoie quand même une erreur "No resources", on considère que c'est vide
            $podsRunning = $false
        }
    }
    Write-Host "`n✔ Kubernetes est totalement vide." -ForegroundColor Green
}
else
{
    Write-Warning "Le dossier $k8sFolder n'existe pas. Impossible de purger Kubernetes."
}


# --- ETAPE 2 : Nettoyage des anciennes images Docker ---
echo "`n=== ETAPE 2 : Nettoyage du moteur Docker de Minikube ==="
$cleanScript = "$rootDir/clean-images.ps1"

if (Test-Path $cleanScript)
{
    Write-Host "Lancement du script de purge des images..." -ForegroundColor Cyan
    & $cleanScript
}
else
{
    Write-Warning "Le script $cleanScript est introuvable. Etape ignoree."
}


# --- ETAPE 3 : Recompilation Java (Maven) et Docker Build ---
echo "`n=== ETAPE 3 : Recompilation complete (Mvn Clean + Docker Build) ==="
$buildScript = "$rootDir/build-images.ps1"

if (Test-Path $buildScript)
{
    Write-Host "Lancement de la compilation de tous les microservices..." -ForegroundColor Cyan
    & $buildScript
}
else
{
    Write-Error "Le script de build $buildScript est introuvable ! Arret du processus."
    exit
}


# --- ETAPE 4 : Redeploiement ordonne ---
echo "`n=== ETAPE 4 : Redeploiement ordonne dans le cluster ==="
$deployScript = "$rootDir/deploy-ordered.ps1"

if (Test-Path $deployScript)
{
    Write-Host "Application des fichiers de configuration YAML..." -ForegroundColor Cyan
    & $deployScript
}
else
{
    Write-Error "Le script de deploiement $deployScript est introuvable !"
    exit
}

echo "`n========================================================="
echo " ✔ SCRIPT TERMINE AVEC SUCCES ! TOUT EST EN LIGNE ✔"
echo "========================================================="