# =========================================================================
# SCRIPT DE COMPILATION DES IMAGES DANS MINIKUBE
# =========================================================================

# Force la console PowerShell à décoder correctement l'UTF-8 (Accents et Émojis)
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

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

echo "Minikube est bien actif. Poursuite du script..."
echo "`n=== 2. Connexion au moteur Docker de Minikube ==="
minikube docker-env --shell powershell | Invoke-Expression
echo "Connecté au démon Docker de Minikube."

# 2. Définition des microservices à compiler (Dossier : Nom de l'image)
$services = @{
    "discovery" = "discovery-service:latest"
    #"config" = "config-service:latest"
    "gateway" = "gateway-service:latest"
    "authent" = "authent-service:latest"
    "client" = "client-service:latest"
    "consultant" = "consultant-service:latest"
    "company" = "company-service:latest"
    "prestation" = "prestation-service:latest"
    "facture" = "facture-service:latest"
    "../bill-front-ng" = "front-service:latest"  # Chemin relatif vers votre dossier Frontend
}

echo "`n=== 3. Début de la phase de nettoyage et compilation ==="

# 3. Unique boucle de traitement automatique
foreach ($folder in $services.Keys)
{
    $imageName = $services[$folder]

    echo ""
    echo "========================================================="
    echo " Traitement de : ${folder} -> Image: ${imageName}"
    echo "========================================================="

    # CAS PARTICULIER : Si c'est le dossier du frontend Angular
    if ($folder -eq "../bill-front-ng")
    {
        if (Test-Path .\$folder)
        {
            Push-Location .\$folder
            echo "Construction du Frontend (Docker Build)..."
            docker build --no-cache -t $imageName .
            Pop-Location
        }
        else
        {
            #Write-Warning "Le dossier Frontend .\$folder n'existe pas. Passage au suivant."
        }
        continue # On passe au service suivant, pas de Maven ici
    }

    # CAS GENERAL : Pour tous les microservices Java (Spring Boot)
    if (Test-Path .\$folder)
    {
        Push-Location .\$folder

        # On force le nettoyage complet et la recompilation du JAR localement
        echo "Exécution de mvn clean package (sans les tests)..."
        mvn clean package -DskipTests

        if ($LASTEXITCODE -eq 0)
        {
            # On construit l'image Docker avec le flag --no-cache en lui injectant le nouveau JAR tout propre
            echo "Construction de l'image Docker dans Minikube..."
            docker build --no-cache -t $imageName .
        }
        else
        {
            Write-Error "Erreur lors du mvn clean package pour $folder. L'image Docker n'a pas été créée."
        }
        Pop-Location
    }
    else
    {
        Write-Warning "Le dossier .\$folder n'existe pas. Passage au suivant."
    }
}

echo ""
echo "========================================================="
echo " === Félicitations ! Toutes les images sont prêtes ===  "
echo "========================================================="
# Affiche la liste finale pour vérifier que tout est bien là
docker images | Select-String -Pattern "service|front"