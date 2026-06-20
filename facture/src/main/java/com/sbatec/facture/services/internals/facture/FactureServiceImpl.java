package com.sbatec.facture.services.internals.facture;

import com.sbatec.facture.dtos.*;
import com.sbatec.facture.exceptions.ErrorCatalog;
import com.sbatec.facture.exceptions.ServiceException;
import com.sbatec.facture.mappers.FactureMapper;
import com.sbatec.facture.models.FactureEntity;
import com.sbatec.facture.models.TvaEntity;
import com.sbatec.facture.repository.facture.FactureJpaRepository;
import com.sbatec.facture.repository.tva.TvaJpaRepository;
import com.sbatec.facture.services.externals.ClientRestClient;
import com.sbatec.facture.services.externals.CompanyRestClient;
import com.sbatec.facture.services.externals.ConsultantRestClient;
import com.sbatec.facture.services.externals.PrestationRestClient;
import com.sbatec.facture.services.internals.edition.EditionReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@AllArgsConstructor
public class FactureServiceImpl implements FactureService {

    FactureJpaRepository factureJpaRepository;
    FactureMapper factureMapper;
    CompanyRestClient companyRestClient;
    ConsultantRestClient consultantRestClient;
    ClientRestClient clientRestClient;
    PrestationRestClient prestationRestClient;
    EditionReportService editionReportService;
    TvaJpaRepository tvaJpaRepository;

    /**
     * @param id
     * @return Facture
     */
    private Facture findFacture(Long id) {
        Optional<FactureEntity> factureEntity = factureJpaRepository.findById(id);
        if (factureEntity.isEmpty()) {
            throw new ServiceException(ErrorCatalog.RESOURCE_NOT_FOUND, "Facture introuvable");
        }
        return factureMapper.toDto(factureEntity.get());
    }

    /**
     *
     * @param id
     * @return Consultant
     */
    private Consultant findConsultant(String token, Long id) {
        Consultant consultant = consultantRestClient.findById(token, id);
        if (consultant == null) {
            throw new ServiceException(ErrorCatalog.RESOURCE_NOT_FOUND, "Consultant introuvable");
        }
        return consultant;
    }

    /**
     *
     * @param id
     * @return
     */
    private Client findClient(String token, Long id) {
        Client client = clientRestClient.findById(token, id);
        if (client == null) {
            throw new ServiceException(ErrorCatalog.RESOURCE_NOT_FOUND, "Client introuvable");
        }
        return client;
    }

    /**
     *
     * @param siret
     * @return
     */
    private Company findCompany(String token, String siret) {
        Company company = companyRestClient.findBySiret(token, siret);
        if (company == null) {
            throw new ServiceException(ErrorCatalog.RESOURCE_NOT_FOUND, "Company introuvable");
        }
        return company;
    }

    /**
     *
     * @param id prestation
     * @return prestation
     *
     */
    private Prestation findPrestation(String token, Long id) {
        return prestationRestClient.findById(token, id);
    }

    /**
     *
     *
     * @param prestationId
     * @return
     */
    private List<Facture> findFacturesHistory(Long prestationId) {
        return factureMapper.toDtoList(factureJpaRepository.findByprestationId(prestationId));
    }

    @Override
    public Facture addFacture(Facture facture, String pathRoot, String fileSuivi, String token) throws IOException, URISyntaxException {

        Company company = findCompany(token, facture.getSiret());
        Prestation prestation = findPrestation(token, facture.getPrestationId());
        Client client = findClient(token, prestation.getClientId());
        Consultant consultant = findConsultant(token, prestation.getConsultantId());
        List<Facture> facturesHistory = findFacturesHistory(prestation.getId());
        Facture factureEdit = editionReportService.buildFacture(prestation, facture, facturesHistory);
        String pathFile = editionReportService.buildPathFile(pathRoot, facture.getMoisFacture());
        Map<String, Object> dataPdf = editionReportService.buildParamsTemplate(company, prestation, consultant, client, factureEdit);
        FactureEntity factEntity = factureMapper.toEntity(factureEdit);
        FactureEntity newFacture = factureJpaRepository.save(factEntity);
        editionReportService.buildFacturePdFSaucer(dataPdf, pathFile);
        return factureMapper.toDto(newFacture);
    }

    @Override
    public void deleteFacture(Long factureId) {
        factureJpaRepository.deleteById(factureId);
    }

    @Override
    public Facture updateFacture(Facture facture, String rootPath, String fileSuiviName) {
        return null;
    }

    @Override
    public Facture updateFacture(Facture facture) {
        FactureEntity newFacture = factureJpaRepository.save(factureMapper.toEntity(facture));
        return factureMapper.toDto(newFacture);
    }


    @Override
    public Facture findById(Long id) {
        Optional<FactureEntity> factureEntity = factureJpaRepository.findById(id);
        if (factureEntity.isEmpty()) {
            throw new ServiceException(ErrorCatalog.RESOURCE_NOT_FOUND, "Facture introuvable");
        }
        return factureMapper.toDto(factureEntity.get());
    }

    @Override
    public Page<Facture> findAllBySiret(String siret, Pageable pageable) {
        Page<FactureEntity> factures = factureJpaRepository.findBySiret(siret, pageable);
        return factures.map(f -> factureMapper.toDto(f));
    }

    @Override
    public Page<Facture> findBySiretAndExercice(String siret, String exercice, Pageable pageable) {
        Page<FactureEntity> entities = factureJpaRepository.findBySiretAndExercice(siret, exercice, pageable);
        Page<Facture> factures = entities.map(factureMapper::toDto);

        for (Facture facture : factures.getContent()) {
            BigDecimal montantTva = BigDecimal.ZERO;
            List<TvaEntity> tvas = tvaJpaRepository.findByNumeroFacture(facture.getNumeroFacture());
            if (tvas != null) {
                for (TvaEntity tva : tvas) {
                    montantTva = montantTva.add(tva.getMontantPayment());
                }
                facture.setMontantTvaPaye(montantTva);
            }
        }
        return factures;
    }

    @Override
    public List<Facture> findAllBySiret(String siret) {
        List<FactureEntity> facturesEntity = factureJpaRepository.findBySiret(siret);
        return factureMapper.toDtoList(facturesEntity);
    }

    @Override
    public List<Facture> findBySiretAndExercice(String siret, String exercice) {
        return List.of();
    }

    @Override
    public List<Facture> findAll() {
        List<FactureEntity> factures = factureJpaRepository.findAll();
        return factureMapper.toDtoList(factures);
    }

    @Override
    public Page<Facture> searchFactures(String siret, String search, Pageable pageable) {
        Page<FactureEntity> factures = factureJpaRepository.searchFactures(siret, search, pageable);
        return factures.map(f -> factureMapper.toDto(f));
    }


    @Override
    public DataPDF buildPdf(Long id, String pathRoot, String token) throws IOException, URISyntaxException {
        // 1. Récupération locale (synchrone et rapide)
        Facture facture = findFacture(id);

        // 2. On lance immédiatement les deux premiers appels distants en parallèle
        CompletableFuture<Company> companyFuture = CompletableFuture.supplyAsync(() -> findCompany(token, facture.getSiret()))
                .exceptionally(ex -> {
                    log.error("Erreur récupération Company distante pour la facture {}", id, ex);
                    return null;
                });

// On combine l'appel Prestation avec le résultat de Company pour valider sa présence
        CompletableFuture<Prestation> prestationFuture = CompletableFuture.supplyAsync(() -> findPrestation(token, facture.getPrestationId()))
                .thenCombineAsync(companyFuture, (prestation, company) -> {
                    // SI COMPANY EST NULL, ON LÈVE L'EXCEPTION ICI
                    if (company == null) {
                        throw new ServiceException(ErrorCatalog.SERVICE_ERROR, "Impossible de charger la prestation car la Company est manquante.");
                    }
                    return prestation;
                })
                .exceptionally(ex -> {
                    // Attrape l'erreur de findPrestation OU le ServiceException levé juste au-dessus
                    log.error("Erreur ou annulation de la Prestation pour la facture {} : {}", id, ex.getMessage());
                    return null;
                });

        // 3. On enchaîne le Client et le Consultant dès que la Prestation répond (sans bloquer le thread)
        CompletableFuture<Consultant> consultantFuture = prestationFuture.thenApplyAsync(prestation -> {
            if (prestation == null)
                return null; // Sécurité : si la prestation a échoué (ou si company était null), on n'appelle pas le service distant
            return findConsultant(token, prestation.getConsultantId());
        }).exceptionally(ex -> {
            log.error("Erreur récupération Consultant distant pour la facture {}", id, ex);
            return null;
        });

        CompletableFuture<Client> clientFuture = prestationFuture.thenApplyAsync(prestation -> {
            if (prestation == null) return null; // Sécurité identique
            return findClient(token, prestation.getClientId());
        }).exceptionally(ex -> {
            log.error("Erreur récupération Client distant pour la facture {}", id, ex);
            return null;
        });

        try {
            // 4. Barrière de synchronisation UNIQUE
            // On attend la réponse de la Company et des appels qui découlaient de la prestation
            CompletableFuture.allOf(companyFuture, consultantFuture, clientFuture).join();

            // Récupération instantanée des résultats
            Company company = companyFuture.join();
            Consultant consultant = consultantFuture.join();
            Client client = clientFuture.join();
            Prestation prestation = prestationFuture.join();

            // Si la prestation (ou la compagnie) a échoué en cours de route, vous pouvez lever l'exception globale ici pour le PDF
            if (company == null || prestation == null || consultant == null || client == null) {
                throw new ServiceException(ErrorCatalog.PDF_ERROR, "Données incomplètes pour générer le PDF");
            }

            Map<String, Object> dataPdf = editionReportService.buildParamsTemplate(company, prestation, consultant, client, facture);
            byte[] encodedBytes = editionReportService.buildFacturePdFSaucer(dataPdf, pathRoot);
            String fileName = (String) dataPdf.get("fileName");
            return DataPDF.builder().facture(facture).
                    fileContent(encodedBytes)
                    .contentBase64(Base64.getEncoder().encodeToString(encodedBytes))
                    .fileName(fileName).build();

        } catch (Exception e) {
            throw new ServiceException(ErrorCatalog.PDF_ERROR, "Erreur création fichier pdf");
        }
    }
}
