package com.sbatec.facture.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ToString
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    String pathFilePdf;
    String pathLibelleFacture;
    String pathLibelleCharges;
    String pathLibelleReleve;
    String mailFrom;
    String mailBcc;
    String smtpUserName;
    String smtpPassword;
    String smtpPort;
    String SmtpHost;
    String fichierSuiviFactures;
    String fichierImportCsv;
}
