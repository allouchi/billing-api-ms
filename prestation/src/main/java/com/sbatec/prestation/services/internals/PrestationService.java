package com.sbatec.prestation.services.internals;

import com.sbatec.prestation.dtos.Prestation;

import java.util.List;


public interface PrestationService {

    Prestation addPrestation(String siret, Prestation prestation);

    void deletePrestation(Prestation prestation);

    Prestation updatePrestation(Prestation prestation);

    Prestation findById(Long id);

    List<Prestation> findBySiret(String siret, String token);

    void deleteById(Long id);

}
