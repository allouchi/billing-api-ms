package com.sbatec.client.controllers;

import com.sbatec.client.dtos.Client;
import com.sbatec.client.dtos.EmailClient;
import com.sbatec.client.services.ClientService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientController {
    ClientService clientService;


    @GetMapping("/{id}")
    public Client findById(@PathVariable long id) {
        log.info("find client by id  {}:", id);
        return clientService.findClientById(id);
    }

    @GetMapping
    public List<Client> findClients() {
        log.info("get all clients");
        return clientService.findAllClients();
    }

    @GetMapping("/batch")
    List<Client> findAllByIds(@RequestParam("ids") List<Long> ids) {
        log.info("Find batch of clients for ids: {}", ids);
        if (ids == null || ids.isEmpty()) {
            return List.of(); // Évite un appel inutile au service si la liste est vide
        }
        return clientService.findAllClientsByIds(ids);
    }

    @PostMapping("/add")
    public Client addClient(@RequestBody Client clientRequest) {
        log.info("Create new client");
        var socialReason = clientRequest.getSocialReason();
        var numero = clientRequest.getAdresseClient().getNumero();
        var rue = clientRequest.getAdresseClient().getRue();
        var codePostal = clientRequest.getAdresseClient().getCodePostal();
        var ville = clientRequest.getAdresseClient().getLocalite();
        var pays = clientRequest.getAdresseClient().getPays();
        List<EmailClient> emails = clientRequest.getEmails();
        String mails = "";
        if (emails != null) {
            mails = emails.stream()
                    .map(EmailClient::getEmail) // On récupère l'adresse texte
                    .collect(Collectors.joining("; ")); // On joint avec "; "
        }
        return clientService.addClient(socialReason, numero, rue, codePostal, ville, pays, mails);
    }


    @PutMapping("/update")
    public Client updateClient(@RequestBody Client clientRequest) {
        log.info("update client by id  {}:", clientRequest.getId());
        Long id = clientRequest.getId();
        var socialReason = clientRequest.getSocialReason();
        var numero = clientRequest.getAdresseClient().getNumero();
        var rue = clientRequest.getAdresseClient().getRue();
        var codePostal = clientRequest.getAdresseClient().getCodePostal();
        var ville = clientRequest.getAdresseClient().getLocalite();
        var pays = clientRequest.getAdresseClient().getPays();
        List<EmailClient> emails = clientRequest.getEmails();

        String mails = emails.stream()
                .map(email -> {
                    String idStr = (email.getId() != null) ? email.getId().toString() : "NEW";
                    return idStr + "," + email.getEmail();
                }).collect(Collectors.joining(";"));

        return clientService.updateClient(id, socialReason, numero, rue, codePostal, ville, pays, mails);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable long id) {
        log.info("delete client by id  {}:", id);
        clientService.deleteClientById(id);
    }


}
