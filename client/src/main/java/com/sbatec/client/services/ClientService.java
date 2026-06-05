package com.sbatec.client.services;

import com.sbatec.client.dtos.Client;


import java.util.List;

public interface ClientService {
    Client addClient(String socialReason,
                     String numero,
                     String rue,
                     String cp,
                     String ville,
                     String pays,
                     String emails);

    void deleteClientById(Long id);

    void deleteClientBySocialReason(String SocialReason);

    Client updateClient(Long id,
                        String socialReason,
                        String numero,
                        String rue,
                        String cp,
                        String ville,
                        String pays,
                        String emails);

    List<Client> findAllClients();

    Client findClientById(Long id);

    Client findClientBySocialReason(String SocialReason);

    List<Client> findAllClientsByIds(List<Long> ids);

}
