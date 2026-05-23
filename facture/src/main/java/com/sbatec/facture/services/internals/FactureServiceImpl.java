package com.sbatec.facture.services.internals;

import com.sbatec.facture.dtos.*;
import com.sbatec.facture.exceptions.ErrorCatalog;
import com.sbatec.facture.exceptions.ServiceException;
import com.sbatec.facture.mappers.FactureMapper;
import com.sbatec.facture.models.FactureEntity;
import com.sbatec.facture.repository.FactureJpaRepository;
import com.sbatec.facture.services.externals.ClientRestClient;
import com.sbatec.facture.services.externals.CompanyRestClient;
import com.sbatec.facture.services.externals.ConsultantRestClient;
import com.sbatec.facture.services.externals.PrestationRestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    /**
     *
     * @param id
     * @return Consultant
     */
    private Consultant findConsultant(Long id) {
        Consultant consultant = consultantRestClient.findBySiret(id);
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
    private Client findClientById(Long id) {
        Client client = clientRestClient.findById(id);
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
    private Company findCompany(String siret) {
        Company company = companyRestClient.findBySiret(siret);
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
    private Prestation findPrestation(Long id) {
        return prestationRestClient.findById(id);
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
    public Facture addFacture(Facture facture, String pathRoot, String fileSuivi) throws IOException, URISyntaxException {

        Company company = findCompany(facture.getSiret());
        Prestation prestation = findPrestation(facture.getPrestationId());
        Client client = findClientById(prestation.getClientId());
        Consultant consultant = findConsultant(prestation.getConsultantId());
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

    }

    @Override
    public Facture updateFacture(Facture facture, String rootPath, String fileSuiviName) {
        return null;
    }

    @Override
    public Facture updateFacture(Facture facture) {
        return null;
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
        return null;
    }

    @Override
    public Page<Facture> findBySiretAndExercice(String siret, String exercice, Pageable pageable) {
        return null;
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
        return List.of();
    }

    @Override
    public Page<Facture> searchFactures(String siret, String search, Pageable pageable) {
        return null;
    }

    @Override
    public DataPDF buildPdf(Long id, String pathRoot) throws IOException, URISyntaxException {
        return null;
    }
}
