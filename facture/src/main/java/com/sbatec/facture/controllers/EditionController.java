package com.sbatec.facture.controllers;


import com.sbatec.facture.config.ApplicationProperties;
import com.sbatec.facture.dtos.*;
import com.sbatec.facture.services.externals.ClientRestClient;
import com.sbatec.facture.services.externals.PrestationRestClient;
import com.sbatec.facture.services.internals.edition.EditionReportService;
import com.sbatec.facture.services.internals.facture.FactureService;
import com.sbatec.facture.util.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class EditionController {

    EditionReportService editionReportService;
    ApplicationProperties applicationProperties;
    PrestationRestClient prestationRestClient;
    FactureService factureService;
    ClientRestClient clientRestClient;


    @GetMapping(value = "/editions/download/{id}")
    public ResponseEntity<DataPDF> downloadPdf(@PathVariable Long id) throws IOException, URISyntaxException {
        DataPDF pdfData = factureService.buildPdf(id, null);
        return new ResponseEntity<>(pdfData, HttpStatus.OK);
    }

    @GetMapping(value = "/editions/mail/{idFacture}")
    public ResponseEntity<List<EmailClient>> getClientMails(@PathVariable Long idFacture) {
        Facture facture = factureService.findById(idFacture);
        Prestation prestation = prestationRestClient.findById(facture.getPrestationId());
        Client client = clientRestClient.findById(prestation.getClientId());
        List<EmailClient> emails = client.getEmails();
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }


    @PostMapping(value = "/editions/sendMail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMail(@PathVariable Long id, @RequestBody List<EmailClient> mailsTo) throws IOException, URISyntaxException {
        DataPDF pdfData = factureService.buildPdf(id, null);
        String mailFrom = applicationProperties.getMailFrom();
        String bcc = applicationProperties.getMailBcc();


        try {
            String messageBody = Utils.buildMessageBody(pdfData.getFacture());
            String anneeFacture = pdfData.getFacture().getNumeroFacture().substring(0, 4);
            String lettreMois = pdfData.getFacture().getMoisFacture().substring(0, 1).toUpperCase();
            String subject = "Facture du mois de " + lettreMois + pdfData.getFacture().getMoisFacture().substring(1).toLowerCase() + " " + anneeFacture;
            String response = editionReportService.factureSender(mailsTo, mailFrom, bcc, subject, messageBody, pdfData.getFileContent(), pdfData.getFileName());
            Facture facture = factureService.findById(id);
            facture.setSended(true);
            factureService.updateFacture(facture);
            String json = "{ \"message\": \"" + response + "\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (Exception e) {
            // Renvoyer un status KO
            String message = "Échec d'envoi de la facture";
            String json = "{ \"message\": \"" + message + "\" }";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(json);
        }
    }


    @GetMapping(value = "/editions/workingDays/{year}/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> calculOpenDays(@PathVariable Integer year, @PathVariable Integer month) {
        int nbDays = Utils.calculerJoursOuvres(year, month);
        return new ResponseEntity<>(nbDays, HttpStatus.OK);
    }
}
