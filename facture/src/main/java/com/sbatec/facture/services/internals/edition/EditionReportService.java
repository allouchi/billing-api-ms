package com.sbatec.facture.services.internals.edition;

import com.sbatec.facture.dtos.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface EditionReportService {

    Facture buildFacture(Prestation prestation, Facture facture, List<Facture> factureHistory);

    Map<String, Object> buildParamsTemplate(Company company, Prestation prestation, Consultant consultant, Client client, Facture facture);

    byte[] buildFacturePdFSaucer(Map<String, Object> paramJasper,
                                 String path) throws IOException, URISyntaxException;

    String buildPathFile(String pathRoot, String moisNumeric);

    String factureSender(List<EmailClient> mailsTo, String from, String bcc, String subject, String body, byte[] fileByte, String fileName);

}
