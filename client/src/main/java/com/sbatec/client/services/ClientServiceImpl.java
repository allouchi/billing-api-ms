package com.sbatec.client.services;

import com.sbatec.client.dtos.Adresse;
import com.sbatec.client.dtos.Client;
import com.sbatec.client.dtos.EmailClient;
import com.sbatec.client.mappers.ClientMapper;
import com.sbatec.client.models.ClientEntity;
import com.sbatec.client.repository.ClientJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    ClientJpaRepository clientJpaRepository;
    ClientMapper clientMapper;

    @Override
    public Client addClient(String socialReason, String numero, String rue, String codePostal, String ville, String pays, String emails) {
        Adresse adresse = Adresse.builder()
                .numero(numero)
                .rue(rue)
                .codePostal(codePostal)
                .localite(ville)
                .pays(pays)
                .build();

        String[] mails = emails.split(";");
        List<EmailClient> emailList = new ArrayList<>();
        for (String mail : mails) {
            EmailClient emailClient = new EmailClient();
            emailClient.setId(null);
            emailClient.setEmail(mail);
            emailList.add(emailClient);
        }
        Client client = Client.builder().
                socialReason(socialReason).
                adresseClient(adresse).emails(emailList).
                build();
        ClientEntity clientEntity = clientJpaRepository.save(clientMapper.toEntity(client));
        return clientMapper.toDto(clientEntity);
    }

    @Override
    public void deleteClientById(Long id) {
        clientJpaRepository.deleteById(id);
    }

    @Override
    public String deleteClientBySocialReason(String SocialReason) {
        return "";
    }

    @Override
    public Client updateClient(Long id, String socialReason, String numero, String rue, String cp, String ville, String pays, String emails) {
        return null;
    }

    @Override
    public List<Client> findAllClients() {
        List<ClientEntity> entities = clientJpaRepository.findAll();
        return clientMapper.toDtoList(entities);
    }

    @Override
    public Client findClientById(Long id) {
        Optional<ClientEntity> clientEntity = clientJpaRepository.findById(id);
        return clientEntity.map(entity -> clientMapper.toDto(entity)).orElse(null);
    }

    @Override
    public Client findClientBySocialReason(String SocialReason) {
        Optional<ClientEntity> clientEntity = clientJpaRepository.findBySocialReasonContainingIgnoreCase(SocialReason);
        return clientEntity.map(entity -> clientMapper.toDto(entity)).orElse(null);
    }

    @Override
    public List<Client> findAllClientsByIds(List<Long> ids) {
        List<ClientEntity> entities = clientJpaRepository.findAllById(ids);
        return clientMapper.toDtoList(entities);
    }
}
