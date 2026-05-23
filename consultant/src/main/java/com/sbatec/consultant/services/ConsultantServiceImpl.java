package com.sbatec.consultant.services;

import com.sbatec.consultant.dtos.Consultant;
import com.sbatec.consultant.mappers.ConsultantMapper;
import com.sbatec.consultant.models.ConsultantEntity;
import com.sbatec.consultant.repository.ConsultantJpaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsultantServiceImpl implements ConsultantService {

    ConsultantJpaRepository consultantJpaRepository;
    ConsultantMapper consultantMapper;

    @Override
    public Consultant addConsultant(Consultant consultant) {
        return null;
    }

    @Override
    public void deleteConsultant(Long id) {
        consultantJpaRepository.deleteById(id);
    }

    @Override
    public Consultant updateConsultant(Consultant consultant) {
        ConsultantEntity consultantEntity = consultantJpaRepository.save(consultantMapper.toEntity(consultant));
        return consultantMapper.toDto(consultantEntity);
    }

    @Override
    public List<Consultant> findAll() {
        return consultantMapper.toDtoList(consultantJpaRepository.findAll());
    }

    @Override
    public Consultant findById(Long id) {
        Optional<ConsultantEntity> consultantEntity = consultantJpaRepository.findById(id);
        return consultantEntity.map(consultantMapper::toDto).orElse(null);
    }

    @Override
    public List<Consultant> findAllConsultantsByIds(List<Long> ids) {
        List<ConsultantEntity> entities = consultantJpaRepository.findAllById(ids);
        return consultantMapper.toDtoList(entities);
    }
}
