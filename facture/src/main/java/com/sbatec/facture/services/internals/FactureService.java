package com.sbatec.facture.services.internals;

import com.sbatec.facture.dtos.DataPDF;
import com.sbatec.facture.dtos.Facture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public interface FactureService {
    Facture addFacture(Facture facture,
                       String pathRoot, String fileSuivi) throws IOException, URISyntaxException;

    void deleteFacture(Long factureId);

    Facture updateFacture(Facture facture, String rootPath, String fileSuiviName);

    Facture updateFacture(Facture facture);

    Facture findById(Long id);

    Page<Facture> findAllBySiret(String siret, Pageable pageable);

    Page<Facture> findBySiretAndExercice(String siret, String exercice, Pageable pageable);

    List<Facture> findAllBySiret(String siret);

    List<Facture> findBySiretAndExercice(String siret, String exercice);

    List<Facture> findAll();

    Page<Facture> searchFactures(String siret, String search, Pageable pageable);

    DataPDF buildPdf(Long id,
                     String pathRoot) throws IOException, URISyntaxException;
}
