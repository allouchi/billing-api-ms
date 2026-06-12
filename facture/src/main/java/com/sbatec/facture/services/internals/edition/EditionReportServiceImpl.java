package com.sbatec.facture.services.internals.edition;


import com.sbatec.facture.dtos.*;
import com.sbatec.facture.util.FactureStatus;
import com.sbatec.facture.util.TaxType;
import com.sbatec.facture.util.Utils;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class EditionReportServiceImpl implements EditionReportService {

    private static final String TIRET = "-";
    private static final String TYPE_FILE = ".pdf";
    private static final String IBAN = "IBAN : ";
    private static final String BIC = "BIC    : ";
    private static final String FACTURE_LIBELLE = "Facture";
    private static final String ESPACE_BLANC = " ";
    private static final String UNDERSCORE = "_";

    private final JavaMailSender factureSender;

    // 2. Ajoute le constructeur explicitement ici
    public EditionReportServiceImpl(JavaMailSender factureSender) {
        this.factureSender = factureSender;
    }


    public static String extractFullName(String email) {

        if (email == null || !email.contains("@")) {
            return "Inconnu";
        }

        // 1. Extraire la partie avant le @
        String localPart = email.split("@")[0];

        // 2. Découper selon les séparateurs courants : . ou - ou _
        // Le regex [._-] signifie : "n'importe lequel de ces caractères"
        String[] parts = localPart.split("[._-]");

        String prenom = "Inconnu";
        String nom = "";

        if (parts.length >= 1) {
            prenom = capitalize(parts[0]);
        }

        if (parts.length >= 2) {
            // Si on a plusieurs parties (ex: jean.pierre.dupont),
            // on peut joindre le reste pour le nom
            StringBuilder nomBuilder = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                nomBuilder.append(capitalize(parts[i])).append(" ");
            }
            nom = nomBuilder.toString().trim();
        }
        return prenom + " " + nom;
    }

    // Petite fonction utilitaire pour mettre la première lettre en majuscule
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    @Override
    public String factureSender(List<EmailClient> mailsTo, String from, String bcc, String subject, String htmlContent, byte[] attachmentBytes, String fileName) {

        try {
            MimeMessage message = factureSender.createMimeMessage();
            // Utilisation du helper avec encodage UTF-8
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setSubject(subject);

            // Affiche "Mustapha Aliane <votre@mail.com>" chez le destinataire
            String fullName = extractFullName(from);
            helper.setFrom(from, fullName);

            helper.setText("", htmlContent);
            helper.addAttachment(fileName, new ByteArrayResource(attachmentBytes));

            // DEMANDE DE CONFIRMATION DE LECTURE
            message.setHeader("Disposition-Notification-To", from);

            for (EmailClient mailTo : mailsTo) {
                log.info("✅ La {} est en cours d'envoi pour {}", fileName, mailTo.getEmail());
                helper.setTo(mailTo.getEmail());

                if (bcc != null && !bcc.isBlank()) {
                    helper.addBcc(bcc);
                }
                factureSender.send(message);
            }
        } catch (Exception e) {
            log.error("Mail send error cause {}", e.getMessage());
            throw new RuntimeException("Mail send error " + e.getMessage());
        }
        log.info("✅ La {} a été envoyée avec succès", fileName);
        return "Facture envoyée avec succès";
    }

    @Override
    public String buildPathFile(String pathRoot, String moisNumeric) {

        String filePath1 = null;

        try {
            String moisFacture = Utils.getMonthNumber(moisNumeric);
            final DateTimeFormatter formaterDate = DateTimeFormatter.ofPattern("yyyy");
            LocalDate dateJour = LocalDate.now();
            String annee = formaterDate.format(dateJour);
            String factureClient = pathRoot + File.separator + annee + File.separator + moisNumeric + TIRET + moisFacture
                    + File.separator + "Facture Client";
            Path pathFactureClient = Paths.get(factureClient);
            String releve = pathRoot + File.separator + annee + File.separator + moisNumeric + TIRET + moisFacture
                    + File.separator + "Relevé de compte";
            Path pathReleve = Paths.get(releve);
            String charge = pathRoot + File.separator + annee + File.separator + moisNumeric + TIRET + moisFacture
                    + File.separator + "Charges";
            Path pathCharge = Paths.get(charge);
            filePath1 = Files.createDirectories(pathFactureClient).toString();
            Files.createDirectories(pathReleve);
            Files.createDirectories(pathCharge);
        } catch (IOException e) {
            log.error("Error lors de la creation du repertoire :" + e.getMessage());
        }
        return filePath1;
    }

    @Override
    public Facture buildFacture(Prestation prestation, Facture facture, List<Facture> factureHistory) {
        String moisId = facture.getMoisFacture();
        String moisFacture = Utils.convertMoisFacture(String.valueOf(facture.getMoisFacture()));
        BigDecimal tarifHT = prestation.getTarifHT();
        BigDecimal prixTotalHT = tarifHT.multiply(facture.getQuantite());
        BigDecimal pourcentage = new BigDecimal("0.2");
        BigDecimal tva = pourcentage.multiply(prixTotalHT);
        facture.setPrixTotalHT(prixTotalHT);
        facture.setTarifHT(prestation.getTarifHT());
        facture.setPrixTotalTTC(prixTotalHT.add(tva));
        facture.setMontantNetTVA(facture.getMontantTVA().subtract(BigDecimal.valueOf(30)));
        facture.setMontantTVA(tva);
        facture.setDateFacturation(Utils.calculDateFacturation(moisId));
        facture.setDateEcheance(Utils.calculerDateEcheance(prestation, moisId));
        facture.setFactureStatus(FactureStatus.NON.getCode());
        facture.setStatusDesc(FactureStatus.NON.getDescription());
        facture.setFraisRetard(BigDecimal.valueOf(0));
        facture.setNbJourRetard(0L);
        facture.setNumeroCommande(prestation.getNumeroCommande());
        facture.setClientPrestation(prestation.getClientPrestation());
        facture.setPrestationId(prestation.getId());
        facture.setMoisFacture(moisFacture);
        if (facture.getTaxType() != null && facture.getTaxType().equalsIgnoreCase(TaxType.IS.getCode())) {
            String exercice = facture.getDateFacturation().split("/")[2];
            facture.setExercice(exercice);
        } else {
            facture.setExercice(null);
        }
        String numeroFacture = updateNumeroFacture(facture.getClientPrestation().toLowerCase(),
                factureHistory, moisId);
        facture.setNumeroFacture(numeroFacture);
        return facture;

    }

    /**
     * @param company
     * @param prestation
     * @param facture
     * @return
     */
    @Override
    public Map<String, Object> buildParamsTemplate(Company company, Prestation prestation, Consultant consultant, Client client, Facture facture) {
        // infos company
        String rsCompany = company.getSocialReason();
        String companyStatus = company.getStatus();
        Adresse adresseCompany = company.getAdresse();
        String adresse1Company = adresseCompany.getNumero() + ESPACE_BLANC + adresseCompany.getRue();
        String adresse2Company = adresseCompany.getCodePostal() + ESPACE_BLANC + adresseCompany.getLocalite() + ESPACE_BLANC
                + adresseCompany.getPays();
        String numeroRcs = company.getRcsName();
        String numeroSiret = company.getSiret().trim();
        String numeroApe = company.getCodeApe();
        String numeroTva = company.getNumeroTva().trim();
        String numeroBic = BIC + company.getNumeroBic();
        String numeroIban = company.getNumeroIban();

        String ibanAffichage = IBAN + numeroIban.substring(0, 4) + ESPACE_BLANC
                + numeroIban.substring(4, 8) + ESPACE_BLANC + numeroIban.substring(8, 12) + ESPACE_BLANC
                + numeroIban.substring(12, 16) + ESPACE_BLANC + numeroIban.substring(16, 20) + ESPACE_BLANC
                + numeroIban.substring(20, 24) + ESPACE_BLANC + numeroIban.substring(24, 27);

        String siretAffichage = null;
        String tvaAffichage = null;

        siretAffichage =
                numeroSiret.substring(0, 3) + ESPACE_BLANC + numeroSiret.substring(3, 6) + ESPACE_BLANC
                        + numeroSiret.substring(6, 9) + ESPACE_BLANC + numeroSiret.substring(9, 14);

        tvaAffichage = numeroTva.trim().substring(0, 2) + ESPACE_BLANC
                + numeroTva.trim().substring(2, 5) + ESPACE_BLANC + numeroTva.trim().substring(5, 8)
                + ESPACE_BLANC + numeroTva.trim().substring(8, 11) + ESPACE_BLANC
                + numeroTva.trim().substring(11, 13);


        // infos factures
        String dateFacturation = facture.getDateFacturation();
        String numeroFacture = facture.getNumeroFacture();
        BigDecimal montantHT = facture.getPrixTotalHT();
        BigDecimal montantTTC = facture.getPrixTotalTTC();
        BigDecimal montantTva = facture.getMontantTVA();
        BigDecimal quantite = facture.getQuantite();
        String moisPrestation = facture.getMoisFacture();
        String communeDateEdition = adresseCompany.getLocalite() + ", le " + dateFacturation;

        // infos prestation
        BigDecimal tarifHT = prestation.getTarifHT();
        String numeroCommande = prestation.getNumeroCommande();
        long delaiPaiement = prestation.getDelaiPaiement();
        String clientPrestation = prestation.getClientPrestation();
        String designation = prestation.getDesignation();
        String consultantFonction = consultant.getFonction();
        String consultantIdentite = consultant.getFirstName() + ESPACE_BLANC
                + consultant.getLastName().toUpperCase();

        LocalDate dateActuelle = LocalDate.now();
        int strDateJour = dateActuelle.getYear();

        String designationLigne1 =
                "Prestation pour" + ESPACE_BLANC + clientPrestation.toUpperCase();
        String designationLigne2 = moisPrestation + ESPACE_BLANC + strDateJour + ESPACE_BLANC + "par" + ESPACE_BLANC + consultantIdentite;

        // infos client
        Adresse adresseClient = client.getAdresseClient();
        String adresse1Client = adresseClient.getNumero() + ESPACE_BLANC + adresseClient.getRue();
        String adresse2Client = adresseClient.getCodePostal() + ESPACE_BLANC + adresseClient.getLocalite()
                + ESPACE_BLANC + adresseClient.getPays();

        String rsClient = client.getSocialReason();

        String moisFacture = Utils.buildMoisFacture(facture.getMoisFacture());
        String fileName =
                FACTURE_LIBELLE + UNDERSCORE + formatString(rsCompany) + UNDERSCORE + formatString(rsClient)
                        + UNDERSCORE + moisFacture + UNDERSCORE + numeroFacture.split("-")[1] + TYPE_FILE;

        // - Parametres envoyes au rapport
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("rs_company", rsCompany);
        parameters.put("statut_company", companyStatus);
        parameters.put("adresse1_company", adresse1Company);
        parameters.put("adresse2_company", adresse2Company);
        parameters.put("numero_rcs", numeroRcs);
        parameters.put("numero_siret", siretAffichage);
        parameters.put("numero_tva", tvaAffichage);
        parameters.put("numero_ape", numeroApe);
        parameters.put("code_iban", ibanAffichage);
        parameters.put("code_bic", numeroBic);
        parameters.put("date_facturation", dateFacturation);
        parameters.put("rs_client", rsClient);
        parameters.put("adresse1_client", adresse1Client);
        parameters.put("adresse2_client", adresse2Client);
        parameters.put("mois_facture", moisPrestation);
        parameters.put("numero_commande", numeroCommande);
        parameters.put("quantite", String.format("%.2f", quantite));
        parameters.put("montantHT", String.format("%.2f", montantHT));
        parameters.put("montantTTC", String.format("%.2f", montantTTC));
        parameters.put("montantTva", String.format("%.2f", montantTva));
        parameters.put("tarifHT", String.format("%.2f", tarifHT));
        parameters.put("numero_facture", numeroFacture);
        parameters.put("commune_company", communeDateEdition);
        parameters.put("designation", designation);
        parameters.put("delai_paiement", delaiPaiement);
        parameters.put("client_prestation", clientPrestation);
        parameters.put("designation_ligne1", designationLigne1);
        parameters.put("designation_ligne2", designationLigne2);
        parameters.put("fonction_consultant", consultantFonction);
        parameters.put("fileName", fileName);
        return parameters;
    }

    @Override
    public byte[] buildFacturePdFSaucer(Map<String, Object> parameters,
                                        String pathParam) throws IOException {
        Map<String, Resource> mapResources = Utils.loadFilesResources();
        Resource htmlTemplate = mapResources.get("Html");
        Resource logoFile = mapResources.get("Logo");

        String rsCompany = (String) parameters.get("rs_company");
        String adresse1Company = (String) parameters.get("adresse1_company");
        String adresse2Company = (String) parameters.get("adresse2_company");
        String status = (String) parameters.get("statut_company");
        String numeroRcs = (String) parameters.get("numero_rcs");
        String siretAffichage = (String) parameters.get("numero_siret");
        String tvaAffichage = (String) parameters.get("numero_tva");
        String numeroApe = (String) parameters.get("numero_ape");
        String ibanAffichage = (String) parameters.get("code_iban");
        String numeroBic = (String) parameters.get("code_bic");
        String dateFacturation = (String) parameters.get("date_facturation");
        String rsClient = (String) parameters.get("rs_client");
        String adresse1Client = (String) parameters.get("adresse1_client");
        String adresse2Client = (String) parameters.get("adresse2_client");
        String moisFacture = (String) parameters.get("mois_facture");
        String numeroCommande = (String) parameters.get("numero_commande");
        String quantite = (String) parameters.get("quantite");
        String montantHT = (String) parameters.get("montantHT");
        String montantTTC = (String) parameters.get("montantTTC");
        String montantTva = (String) parameters.get("montantTva");
        String tarifHT = (String) parameters.get("tarifHT");
        String numeroFacture = (String) parameters.get("numero_facture");
        String communeCompany = (String) parameters.get("commune_company");
        String designation = (String) parameters.get("designation");
        long delaiPaiement = (Long) parameters.get("delai_paiement");
        String clientPrestation = (String) parameters.get("client_prestation");
        String designationLigne1 = (String) parameters.get("designation_ligne1");
        String designationLigne2 = (String) parameters.get("designation_ligne2");
        String consultantFonction = (String) parameters.get("fonction_consultant");

        log.info("********************* Début génération fichier pdf *********************");

        String template = new String(htmlTemplate.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        LocalDate dateActuelle = LocalDate.now();
        int strDateJour = dateActuelle.getYear();

        byte[] imageBytes;
        try (InputStream is = logoFile.getInputStream()) {
            imageBytes = is.readAllBytes();
        }

        String logoPath = Base64.getEncoder().encodeToString(imageBytes);

        String html = template
                .replace("${rsCompany}", rsCompany)
                .replace("${statutCompany}", status)
                .replace("${adresse1Company}", adresse1Company)
                .replace("${adresse2Company}", adresse2Company)
                .replace("${numeroRcs}", numeroRcs)
                .replace("${numeroSiret}", siretAffichage)
                .replace("${numeroTva}", tvaAffichage)
                .replace("${numeroApe}", numeroApe)
                .replace("${codeIban}", ibanAffichage)
                .replace("${codeBic}", numeroBic)
                .replace("${dateFacturation}", dateFacturation)
                .replace("${rsClient}", rsClient.toUpperCase())
                .replace("${moisFacture}", moisFacture)
                .replace("${numeroCommande}", numeroCommande)
                .replace("${quantite}", String.valueOf(quantite))
                .replace("${montantHT}", String.valueOf(montantHT))
                .replace("${montantTTC}", String.valueOf(montantTTC))
                .replace("${montantTva}", String.valueOf(montantTva))
                .replace("${tarifHT}", String.valueOf(tarifHT))
                .replace("${numeroFacture}", numeroFacture)
                .replace("${communeCompany}", communeCompany)
                .replace("${designation}", designation)
                .replace("${delaiPaiement}", String.valueOf(delaiPaiement))
                .replace("${clientPrestation}", clientPrestation)
                .replace("${designationLigne1}", designationLigne1)
                .replace("${designationLigne2}", designationLigne2)
                .replace("${fonctionConsultant}", consultantFonction)
                .replace("${exercice}", String.valueOf(strDateJour))
                .replace("${adresse1Client}", adresse1Client)
                .replace("${adresse2Client}", adresse2Client)
                .replace("${logoPath}", logoPath);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(out);
        if (pathParam != null) {
            String outputFileName = (String) parameters.get("fileName");
            String outputPath = pathParam + File.separator + outputFileName;
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                out.writeTo(fos);
                fos.flush();
                log.info("✅ Fichier PDF écrit sur le disque in path {} : ", outputPath);
            } catch (IOException e) {
                log.error("❌ Erreur lors de l'écriture du fichier PDF : {}", e.getMessage());
            }
        }
        log.info("********************* Fin génération fichier pdf  *********************");
        return out.toByteArray();
    }

    /**
     *
     * @param s
     * @return
     */
    private String formatString(String s) {
        String format = s.split(" ")[0];
        String start = format.substring(0, 1);
        format = start.toUpperCase() + format.substring(1).toLowerCase();
        return format;
    }

    private String updateNumeroFacture(String rsClient, List<Facture> factures,
                                       String moisId) {

        Set<Integer> numeros = new HashSet<>();

        String numeroFacture = null;
        if (factures == null || factures.isEmpty()) {
            return Utils.buildNumeroFacture("1000", moisId);
        }
        numeros.add(1000);

        for (Facture facture : factures) {
            String client = facture.getClientPrestation();
            if (client != null && client.toLowerCase().equals(rsClient)) {
                numeroFacture = facture.getNumeroFacture();
                String[] endNumero = numeroFacture.split("-");
                Integer numero = Integer.parseInt(endNumero[1]);
                numeros.add(numero);
            }
        }
        int max = Collections.max(numeros);
        return Utils.buildNumeroFacture(String.valueOf(max + 1), moisId);
    }
}
