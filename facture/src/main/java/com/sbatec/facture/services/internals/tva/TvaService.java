package com.sbatec.facture.services.internals.tva;

import com.sbatec.facture.dtos.Tva;
import com.sbatec.facture.dtos.TvaInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TvaService {

    void deleteById(Long id);

    Page<Tva> findByExerciseAndSiret(String exercise, String siret, Pageable pageable);

    Tva findById(Long id);

    void updateTva(Tva tva);

    Tva addTva(Tva tva);

    Page<Tva> findBySiret(String siret, Pageable pageable);

    TvaInfo findTvaInfoBySiret(String siret);

    TvaInfo findTvaInfoByExercice(String exercise, String siret);

    List<Tva> findByNumeroFacture(String numeroFacture);

    Page<Tva> searchTvas(String siret, String search, Pageable pageable);
}
