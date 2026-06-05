# 1. Test du statut de Minikube
Write-Host "=== Vérification du statut de Minikube ===" -ForegroundColor Cyan

$minikubeStatus = minikube status --format "{{.Host}}" 2> $null

if ($minikubeStatus -ne "Running")
{
    Write-Host ""
    Write-Warning "Attention : Minikube n'est pas démarré (Statut actuel : '$minikubeStatus')."

    $choix = Read-Host "Voulez-vous démarrer Minikube maintenant ? (O/N)"
    if ($choix -match "^[OoYy]")
    {
        Write-Host "Démarrage de Minikube en cours (Allocation : 4 CPUs / 6144 Mo)..." -ForegroundColor Yellow

        # Lancement avec une valeur de mémoire compatible (6144 au lieu de 8192)
        minikube delete
        # Petite pause de sécurité de 5 secondes pour laisser Docker respirer
        Start-Sleep -Seconds 5
        minikube start --cpus=4 --memory=6144

        # SÉCURITÉ : Si la commande précédente a échoué ($? renvoie False)
        if (-not $?)
        {
            Write-Error "Le démarrage de Minikube a échoué. Arrêt du script pour éviter d'autres erreurs."
            exit
        }
    }
    else
    {
        Write-Warning "Arrêt du script : Minikube doit être démarré pour nettoyer ou générer les images."
        exit
    }
}

# 2. Connexion obligatoire au moteur Docker de Minikube
Write-Host ""
Write-Host "=== Connexion au moteur Docker de Minikube ===" -ForegroundColor Cyan
minikube docker-env --shell powershell | Invoke-Expression

# 3. Définition des images locales à supprimer (Nettoyées selon votre architecture)
$services = @{
    "discovery" = "discovery-service:latest"
    "config" = "config-service:latest"
    "gateway" = "gateway-service:latest"
    "authent" = "authent-service:latest"
    "client" = "client-service:latest"
    "consultant" = "consultant-service:latest"
    "company" = "company-service:latest"
    "prestation" = "prestation-service:latest"
    "facture" = "facture-service:latest"
    "../billing-front" = "front-service:latest"
    # Note : billing-db n'est pas là car l'image mysql:8.0 officielle ne doit pas être supprimée
}

Write-Host ""
Write-Host "=== DÉBUT DU NETTOYAGE DES IMAGES APPLICATIVES ===" -ForegroundColor Magenta

# 4. Boucle de suppression automatique
foreach ($folder in $services.Keys)
{
    $imageName = $services[$folder]

    # On vérifie d'abord si l'image existe dans Minikube pour éviter les messages d'erreur inutiles
    $imageExists = docker images -q $imageName

    if ($imageExists)
    {
        Write-Host "Suppression de l'image : $imageName ..." -ForegroundColor Yellow
        # Le flag -f (force) permet de détruire l'image même si un conteneur arrêté l'utilisait
        docker rmi -f $imageName
    }
    else
    {
        Write-Host "L'image $imageName n'existe pas dans Minikube. Passage au suivant." -ForegroundColor DarkGray
    }
}

Write-Host ""
Write-Host "=== Nettoyage terminé ! Statut actuel des images dans Minikube : ===" -ForegroundColor Green
docker images