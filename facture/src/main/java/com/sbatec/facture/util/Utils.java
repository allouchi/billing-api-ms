package com.sbatec.facture.util;


import com.sbatec.facture.dtos.Consultant;
import com.sbatec.facture.dtos.Facture;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Utils {

    private static final String TIRET = "-";
    private static final String SLATSH = "/";
    private static final Map<String, String> mapMois = new HashMap<>();
    static Map<String, File> map = null;

    static {
        mapMois.put("01", "Janvier");
        mapMois.put("02", "Février");
        mapMois.put("03", "Mars");
        mapMois.put("04", "Avril");
        mapMois.put("05", "Mai");
        mapMois.put("06", "Juin");
        mapMois.put("07", "Juillet");
        mapMois.put("08", "Août");
        mapMois.put("09", "Septembre");
        mapMois.put("10", "Octobre");
        mapMois.put("11", "Novembre");
        mapMois.put("12", "Décembre");
    }


    private Utils() {
    }

    /**
     * @param consultant
     * @return
     */
    public static Consultant formatConsulantName(Consultant consultant) {

        consultant.setLastName(consultant.getLastName().toUpperCase());
        String firstName = consultant.getFirstName().substring(0, 1).toUpperCase()
                + consultant.getFirstName().substring(1);
        consultant.setFirstName(firstName);
        return consultant;
    }

    /**
     * @param moisId
     * @return
     */
    public static String convertMoisFacture(String moisId) {
        String mois = "";
        if (moisId != null && moisId.length() == 1) {
            moisId = "0" + moisId;
        }
        if (mapMois.containsKey(moisId)) {
            mois = mapMois.get(moisId);
        }
        return mois;
    }


    /**
     * @param mois
     * @return
     */
    public static String buildMoisFacture(String mois) {

        String[] moisId = new String[1];
        mapMois.forEach((key, value) ->
        {
            if (value.equalsIgnoreCase(mois)) {
                moisId[0] = key;
            }
        });

        int anneeCourante = LocalDate.now().getYear();
        LocalDate date = LocalDate.of(anneeCourante, Integer.parseInt(moisId[0]), 1);
        String[] stringDate = date.toString().split("-");
        return stringDate[0] + stringDate[1];
    }

    /**
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static Map<String, Resource> loadFilesResources() throws IOException {
        Map<String, Resource> map = new HashMap<>();
        String suiviFacturation = "data/suivi-facturation.xls";
        String templateHtml = "data/factureTemplate.html";
        String logo = "assets/images/background.jpeg";

        Resource excelFile = new ClassPathResource(suiviFacturation);
        Resource htmlFile = new ClassPathResource(templateHtml);
        Resource logoFile = new ClassPathResource(logo);

        map.put("Suivi", excelFile);
        map.put("Html", htmlFile);
        map.put("Logo", logoFile);
        return map;
    }

    /**
     * @param dateToConvert
     * @return
     */
    public static LocalDate convertStringToDate(String dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateToConvert, formatter);
    }

    /**
     * @param facture
     * @return
     */
    public static long calculerNbJourRetard(Facture facture) {
        if (facture == null) {
            return 0;
        }

        LocalDate dateEcheance = convertStringToDate(facture.getDateEcheance());
        LocalDate dateJour = LocalDate.now();
        if (Period.between(dateEcheance, dateJour).getDays() > 0) {
            return ChronoUnit.DAYS.between(dateEcheance, dateJour);
        }
        return 0;
    }

    /**
     * @param facture
     * @param joursRetard
     * @return
     */
    public static float calculerFraisRetard(Facture facture, long joursRetard) {
        if (facture == null) {
            return 0;
        }
        float div = (float) joursRetard / 365;
        return 1 * ((0.1f * facture.getPrixTotalHT() * div) + 40);
    }

    /**
     *
     * @param moisFacture
     * @return
     */
    public static String getMonthNumber(String moisFacture) {
        String[] moisId = new String[1];

        mapMois.forEach((key, value) ->
        {
            if (key.equalsIgnoreCase(moisFacture)) {
                moisId[0] = value;
            }
        });
        return moisId[0];
    }

    /**
     * @param prestation
     * @param moisFacture
     * @return
     */
    public static String calculerDateEcheance(com.sbatec.facture.dtos.Prestation prestation, String moisFacture) {

        if (prestation == null || moisFacture == null || moisFacture.isEmpty()) {
            return null;
        }

        long delai = prestation.getDelaiPaiement();
        LocalDate dateActuelle = LocalDate.now();
        String dateFacture = dateActuelle.getYear() + TIRET + moisFacture + TIRET + "01";
        LocalDate date = LocalDate.parse(dateFacture);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        LocalDate dateEcheance = endOfMonth.plusDays(delai);
        return convertToDateFromLocalDate(dateEcheance);
    }

    /**
     * @param dateToConvert
     * @return
     */
    public static String convertToDateFromLocalDate(LocalDate dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        final DateTimeFormatter formaterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formaterDate.format(dateToConvert);
    }

    /**
     * @param moisFacture mois facture
     * @return date
     */
    public static String calculDateFacturation(String moisFacture) {
        String dateFacture = null;
        if (moisFacture == null || moisFacture.isEmpty()) {
            return null;
        }
        int mois = Integer.parseInt(moisFacture);
        LocalDate dateActuelle = LocalDate.now();
        LocalDate initial = LocalDate.of(dateActuelle.getYear(), mois, 01);
        LocalDate endOfMonth = initial.withDayOfMonth(initial.lengthOfMonth());
        if (moisFacture.equals("02")) {
            dateFacture = endOfMonth.getDayOfMonth() + SLATSH + moisFacture + SLATSH + dateActuelle.getYear();
        } else {
            dateFacture =
                    endOfMonth.getMonth().maxLength() + SLATSH + moisFacture + SLATSH + dateActuelle.getYear();
        }
        return dateFacture;
    }

    /**
     * @param dateToConvert
     * @return
     */
    public static String convertFromDomainToEntityDate(String dateToConvert) {
        if (dateToConvert == null || dateToConvert.isEmpty()) {
            return null;
        }
        String[] tab = dateToConvert.split("-");
        return tab[2] + "/" + tab[1] + "/" + tab[0];
    }


    /**
     * @param endNumero
     * @param moisFacture
     * @return
     */
    public static String buildNumeroFacture(String endNumero, String moisFacture) {

        LocalDate dateActuelle = LocalDate.now();
        String dateFacture = dateActuelle.getYear() + TIRET + moisFacture + TIRET + "01";
        LocalDate date = LocalDate.parse(dateFacture);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        String dateConvert = endOfMonth.toString().replace("-", "");
        return dateConvert + "-" + endNumero;
    }

    /**
     * @param pathComplet
     * @param rootPath
     * @return
     */
    public static String buildPath(String pathComplet, String rootPath) {
        if (pathComplet != null && rootPath != null) {
            int lg = rootPath.length();
            return pathComplet.substring(lg);
        }
        return null;
    }


    public static String buildMessageBody(com.sbatec.facture.dtos.Facture facture) {

        String moisFacture = facture.getMoisFacture().toLowerCase();
        String numeroFacture = facture.getNumeroFacture();
        String client = facture.getClientPrestation();
        int anneeCourante = LocalDate.now().getYear();
        float nbJoursFacture = facture.getQuantite();

        return """
                <!DOCTYPE html>
                <html lang="fr">
                <head>
                    <meta charset="UTF-8">
                    <title>Facture</title>
                </head>
                
                <body style="margin:0; padding:0; background-color:#f5f5f5; font-family:Arial, sans-serif;">                
                <table width="100%%" cellpadding="0" cellspacing="0" role="presentation"
                       style="background-color:#f5f5f5; padding:20px;">
                    <tr>
                        <td align="center">                
                            <table width="100%%" cellpadding="0" cellspacing="0" role="presentation"
                                   style="max-width:650px; background-color:#ffffff;
                                          border:1px solid #e0e0e0; border-radius:6px;">
                                <tr>
                                    <td style="padding:25px; font-size:14px; color:#333;">                
                                        <p style="margin:0 0 15px 0;">Bonjour,</p>                
                                        <p style="margin:0 0 20px 0;">
                                            Veuillez trouver en pièce jointe la facture correspondant au mois de
                                            <strong>%s %s</strong>.
                                        </p>                
                                        <table width="100%%" cellpadding="0" cellspacing="0" role="presentation"
                                               style="background-color:#fafafa;
                                                      border-left:4px solid #4CAF50;
                                                      margin-bottom:20px;">
                                            <tr>
                                                <td style="padding:15px;">                
                                                    <p style="margin:0 0 12px 0; font-weight:bold; text-decoration:underline;">
                                                        Détails de la facture :
                                                    </p>
                                                    <ul style="margin:0; padding-left:20px;">
                                                        <li><strong>Numéro :</strong> %s</li>
                                                        <li><strong>Client :</strong> %s</li>
                                                        <li><strong>Période concernée :</strong> %s %s</li>
                                                        <li><strong>Nombre de jours facturés :</strong> %s</li>
                                                    </ul>                
                                                </td>
                                            </tr>
                                        </table>            
                                        <p style="margin:20px 0 10px 0;">
                                            Pour toute question, n’hésitez pas à me contacter.
                                        </p>            
                                        <p style="margin:0 0 15px 0;">Cordialement,</p>            
                                        <hr style="border:none; border-top:1px solid #ddd; margin:15px 0;">            
                                        <p style="margin:0; font-size:13px; color:#555;">
                                            <strong>SBATEC Consulting</strong><br>
                                            Email : mustapha.aliane@free.fr<br>
                                            Téléphone : +33 6 51 28 00 71
                                        </p>            
                                    </td>
                                </tr>
                            </table>                
                        </td>
                    </tr>
                </table>                            
                </body>
                </html>
                """.formatted(
                moisFacture,
                anneeCourante,
                numeroFacture,
                client,
                moisFacture,
                anneeCourante,
                nbJoursFacture
        );
    }

    /**
     * @param annee
     * @return
     */
    private static Set<LocalDate> getJoursFeries(int annee) {
        Set<LocalDate> feries = new HashSet<>();

        // Jours fériés fixes
        feries.add(LocalDate.of(annee, 1, 1));   // Jour de l'an
        feries.add(LocalDate.of(annee, 5, 1));   // Fête du Travail
        feries.add(LocalDate.of(annee, 5, 8));   // Victoire 1945
        feries.add(LocalDate.of(annee, 7, 14));  // Fête nationale
        feries.add(LocalDate.of(annee, 8, 15));  // Assomption
        feries.add(LocalDate.of(annee, 11, 1));  // Toussaint
        feries.add(LocalDate.of(annee, 11, 11)); // Armistice
        feries.add(LocalDate.of(annee, 12, 25)); // Noël

        // Jours fériés mobiles
        LocalDate paques = calculPaques(annee);
        feries.add(paques.plusDays(1));   // Lundi de Pâques
        feries.add(paques.plusDays(39));  // Ascension
        feries.add(paques.plusDays(50));  // Lundi de Pentecôte
        return feries;
    }

    /**
     * Calcul de la date de Pâques (algorithme de Butcher-Meeus)
     */
    private static LocalDate calculPaques(int annee) {
        int a = annee % 19;
        int b = annee / 100;
        int c = annee % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int mois = (h + l - 7 * m + 114) / 31;
        int jour = ((h + l - 7 * m + 114) % 31) + 1;

        return LocalDate.of(annee, mois, jour);
    }


    /**
     * @param annee
     * @param mois
     * @return
     */
    public static int calculerJoursOuvres(int annee, int mois) {
        YearMonth yearMonth = YearMonth.of(annee, mois);
        Set<LocalDate> joursFeries = getJoursFeries(annee);

        int joursOuvres = 0;

        for (int jour = 1; jour <= yearMonth.lengthOfMonth(); jour++) {
            LocalDate date = yearMonth.atDay(jour);

            boolean weekend =
                    date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                            date.getDayOfWeek() == DayOfWeek.SUNDAY;

            boolean ferie = joursFeries.contains(date);

            if (!weekend && !ferie) {
                joursOuvres++;
            }
        }
        return joursOuvres;
    }

    /**
     * @param path
     * @return extention file
     * @throws IOException
     */
    public static String readCsvHeader(Path path) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String nameCsvFile = null;

        List<String> lines = Files.readAllLines(path);
        boolean headerFound = false;

        for (String line : lines) {
            if (line.startsWith("Opérations imputées")) {
                headerFound = true;

                String value = line.split(";")[1];
                String[] parts = value.replace("du ", "").split(" au ");

                LocalDate start = LocalDate.parse(parts[0], formatter);
                LocalDate end = LocalDate.parse(parts[1], formatter);

                if (start != null && end != null) {
                    String startHeader = start.toString().replace("-", "");
                    String endHeader = end.toString().replace("-", "");

                    DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyyMMdd");
                    DateTimeFormatter output = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                    LocalDate date1 = LocalDate.parse(startHeader, input);
                    String dateStart = date1.format(output);

                    LocalDate date2 = LocalDate.parse(endHeader, input);
                    String dateEnd = date2.format(output);
                    nameCsvFile = "Opérations du " + dateStart + " au " + dateEnd + ".csv";
                }
            }
            if (headerFound) {
                return nameCsvFile;
            }
        }
        return null;
    }

    /**
     * @param str
     * @return
     */

    public static boolean isNumeric(String strValue) {
        if (strValue == null || strValue.isEmpty()) return false;

        try {
            String cleanedValue = strValue.replaceAll("\\s+", "").replace(",", ".");
            Double.parseDouble(cleanedValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
