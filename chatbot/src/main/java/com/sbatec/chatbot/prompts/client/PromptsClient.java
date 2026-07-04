package com.sbatec.chatbot.prompts.client;

public interface PromptsClient {

    String TOOL_ADD_CLIENT = """
            Action : Enregistre un NOUVEAU client.
            
            Contexte :
            À utiliser uniquement si l'utilisateur demande explicitement la création ou l'ajout d’un client.
            
            Objectif :
            Extraire de manière STRICTE et STRUCTURÉE les informations fournies par l'utilisateur.
            
            Champs attendus dans l'objet Client :
            - socialReason : nom ou raison sociale du client
            - adresse :
                - numero : numéro de rue (ex: 1)
                - rue : nom de la rue SANS répétition (ex: Rue des Roses)
                - codePostal : code postal (ex: 75012)
                - localite : ville (ex: Paris)
                - pays : France
            - emails : liste contenant les adresses email
            
            Règles IMPORTANTES :
            - Ne JAMAIS dupliquer le nom de la rue
            - Toujours séparer le numéro de rue du nom de rue
            - Ne PAS concaténer plusieurs fois la même information
            - Respecter STRICTEMENT les informations fournies (ne pas inventer)
            - Nettoyer les doublons si présents dans la phrase utilisateur
            
            Exemple :
            Input utilisateur :
            "Créer ou ajouter un client Osiam, adresse 1 rue des roses 75012 Paris, email osiam@yahoo.fr"
            
            Résultat attendu :
            socialReason = "OSIAM"
            adresse.numero = "1"
            adresse.rue = "Rue des Roses"
            adresse.codePostal = "75012"
            adresse.localite = "Paris"
            adresse.pays= "France"
            emails = ["osiam@yahoo.fr"]
            
            Contrainte :
            - Ne jamais utiliser cet outil pour modifier un client existant
            """;

    String PARAM_ADD_CLIENT = """
                Objet Client STRICTEMENT structuré.
                Champs obligatoires :
                - socialReason : nom du client
                - adresseClient (OBLIGATOIRE si une adresse est fournie) :
                    - numero : numéro (ex: 1)
                    - rue : nom de rue (ex: Rue des Roses)
                    - codePostal : code postal (ex: 75012)
                    - localite : ville (ex: Paris)
                    - pays : France
            
                - emails :
                    - liste d'emails
            
                Règles STRICTES :
                - Si une adresse est mentionnée → créer obligatoirement un objet adresseClient
                - Ne jamais laisser adresseClient à null si des informations d’adresse existent
                - Ne pas fusionner les champs (ex: "1 rue..." doit être séparé)
                - Ne pas dupliquer les valeurs
            
                Exemple :
                Input :
                "Créer client Osiam, 1 rue des roses 75012 Paris, email osiam@yahoo.fr"
            
                Output attendu :
                {
                  "socialReason": "OSIAM",
                  "adresseClient": {
                    "numero": "1",
                    "rue": "Rue des Roses",
                    "codePostal": "75012",
                    "localite": "Paris"
                    "pays": "France"
                  },
                  "emails": [
                    {"email": "osiam@yahoo.fr"}
                  ]
                }
            """;

    String TOOL_UPDATE_CLIENT = """
            Action : Met à jour un client EXISTANT.
            
            Contexte :
            À utiliser uniquement si l'utilisateur demande explicitement la modification ou la mise à jour d’un client existant.
            
            Objectif :
            Extraire de manière STRICTE et STRUCTURÉE les informations fournies par l'utilisateur afin de mettre à jour un client déjà existant.
            
            Champs attendus dans l'objet Client :
            - id : identifiant du client à modifier (OBLIGATOIRE)
            - socialReason : nom ou raison sociale du client (si fourni)
            - adresse :
                - numero : numéro de rue (ex: 1)
                - rue : nom de la rue SANS répétition (ex: Rue des Roses)
                - codePostal : code postal (ex: 75012)
                - localite : ville (ex: Paris)
                - pays : France
            - emails : liste contenant les adresses email
            
            Règles IMPORTANTES :
            - L’id du client est OBLIGATOIRE pour toute mise à jour
            - Ne mettre à jour QUE les champs fournis par l’utilisateur
            - Ne PAS écraser les champs non mentionnés
            - Ne JAMAIS dupliquer le nom de la rue
            - Toujours séparer le numéro de rue du nom de rue
            - Ne PAS concaténer plusieurs fois la même information
            - Respecter STRICTEMENT les informations fournies (ne pas inventer)
            - Nettoyer les doublons si présents dans la phrase utilisateur
            
            Exemple :
            Input utilisateur :
            "Modifier le client 5, mettre à jour l'adresse 10 rue des lilas 75015 Paris et email contact@osiam.fr"
            
            Résultat attendu :
            id = 5
            adresse.numero = "10"
            adresse.rue = "Rue des Lilas"
            adresse.codePostal = "75015"
            adresse.localite = "Paris"
            adresse.pays = "France"
            emails = ["contact@osiam.fr"]
            
            Contrainte :
            - Ne jamais utiliser cet outil pour créer un nouveau client
            - Si aucun id n’est fourni, NE PAS exécuter l’action
            """;

    String PARAM_UPDATE_CLIENT = """
            Objet client contenant les nouvelles données ET l'identifiant technique (ID) ou raison social (reasonSocial) obligatoire.
            """;

    String TOOL_DELETE_CLIENT = """
            Action : Suppression IRRÉVERSIBLE d'un client par son ID ou sa reasonSocial.
            
            ⚠️ RÈGLE CRITIQUE :
            - CE TOOL NE DOIT JAMAIS ÊTRE APPELÉ DIRECTEMENT.
            
            WORKFLOW OBLIGATOIRE :
            1. Rechercher le client (findClientById ou findClientBySocialReason)
            2. Afficher les informations du client (sans ID)
            3. Demander explicitement confirmation à l'utilisateur
            4. Attendre une réponse explicite : "oui", "confirmer", "valider"
            5. UNIQUEMENT APRÈS confirmation → appeler ce tool
            
            INTERDICTIONS ABSOLUES :
            - Ne jamais appeler ce tool sans confirmation explicite
            - Ne jamais deviner ou inventer un ID ou reasonSocial
            - Ne jamais supprimer sur une simple intention ("supprime X") sans validation utilisateur
            
            VALIDATION OBLIGATOIRE :
            - Si la confirmation n’est pas clairement exprimée → NE PAS appeler ce tool
            
            SOURCE DE L’ID :
            - L’ID ou reasonSocial doit obligatoirement provenir d’un tool précédent (find*)
            - Interdiction d’inventer ou d’estimer un ID ou un reasonSocial
            
            CAS D’ERREUR :
            - Si pas de confirmation → demander confirmation
            - Si ID ou reasonSocial absent → ne pas appeler → demander précision
            """;

    String PARAM_DELETE_CLIENT = """
            Identifiant id ou reasonSocial du client à supprimer.
            
            Contraintes STRICTES :
            - Doit provenir d’un résultat de tool (findClientById ou findClientBySocialReason)
            - Ne jamais être inventé ou déduit
            - Ne jamais être fourni directement par l’utilisateur sans vérification préalable
            
            Validation :
            - Si l’ID ou reasonSocial n’est pas confirmé par une étape précédente → refuser l’appel du tool
            """;

    String TOOL_FIND_CLIENT_ID = """
            Action : Récupère la fiche détaillée d'un client par son ID technique.
                    Contexte : Utiliser uniquement si l'ID numérique (ex: 104) est explicitement connu. 
                    Si l'utilisateur donne un nom (ex: 'Total'), utilisez 'findClientBySocialReason'.
            """;

    String PARAM_FIND_CLIENT_ID = """
            L'identifiant numérique unique stocké en base (ex: 42).
            """;


    String TOOL_FIND_CLIENT_REASON = """
            Action : Trouve un client via raison sociale (Raison Sociale).
                    Contexte : C'est la méthode de recherche privilégiée lors d'une conversation naturelle.
                    Note : Gère les noms partiels. Si l'utilisateur dit 'Sbatec', ce tool retournera la fiche complète de 'Sbatec'.
            """;

    String PARAM_FIND_CLIENT_REASON = """
            Le nom/socialRaeson ou une partie du nom/socialRaeson de l'entreprise (ex: 'Sbatec', 'Google').
            """;


}
