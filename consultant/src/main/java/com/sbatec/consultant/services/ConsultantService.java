package com.sbatec.consultant.services;

import com.sbatec.consultant.dtos.Consultant;

import java.util.List;

public interface ConsultantService {

    Consultant addConsultant(Consultant consultant);

    void deleteConsultant(Long id);

    Consultant updateConsultant(Consultant consultant);


    List<Consultant> findAll();

    Consultant findById(Long id);

    List<Consultant> findAllConsultantsByIds(List<Long> ids);
}
