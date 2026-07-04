package com.sbatec.client.services;

import com.sbatec.client.models.ClientEntity;
import com.sbatec.client.repository.ClientJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IAClientServiceImpl implements IAClientService {

    ClientJpaRepository clientJpaRepository;

    @Override
    @Tool(
            name = "findAllClients",
            description = """
                    Action : Liste l'intégralité du carnet client (Nom et ID uniquement).
                    Contexte : À utiliser si l'utilisateur demande 'Qui sont nos clients ?', 'Donne-moi la liste complète' ou 'Affiche tout'.
                    """
    )
    public String findAllClients() { // 👈 On retourne un String au lieu de List<Client>
        List<ClientEntity> entities = clientJpaRepository.findAll();
        if (entities.isEmpty()) {
            return "Aucun client trouvé dans la base de données.";
        }

        StringBuilder sb = new StringBuilder("Liste des clients trouvés :\n");
        for (ClientEntity entity : entities) {
            sb.append(String.format("- ID: %d, Raison Sociale: %s\n", entity.getId(), entity.getSocialReason()));
        }
        return sb.toString();
    }
}
