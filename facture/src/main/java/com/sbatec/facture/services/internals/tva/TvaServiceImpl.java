package com.sbatec.facture.services.internals.tva;

import com.sbatec.facture.dtos.Tva;
import com.sbatec.facture.dtos.TvaInfo;
import com.sbatec.facture.exceptions.TvaNotFoundException;
import com.sbatec.facture.mappers.TvaMapper;
import com.sbatec.facture.models.FactureEntity;
import com.sbatec.facture.models.TvaEntity;
import com.sbatec.facture.repository.facture.FactureJpaRepository;
import com.sbatec.facture.repository.tva.TvaJpaRepository;
import com.sbatec.facture.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TvaServiceImpl implements TvaService {
    static final String TOUS = "Tous";
    TvaJpaRepository tvaJpaRepository;
    FactureJpaRepository factureJpaRepository;
    TvaMapper tvaMapper;

    @Override
    public Page<Tva> findByExerciseAndSiret(String exercise, String siret, Pageable pageable) {

        Page<TvaEntity> tvaEntities;
        if (exercise.equalsIgnoreCase("Tous")) {
            tvaEntities = tvaJpaRepository.findBySiret(siret, pageable);
        } else {
            tvaEntities = tvaJpaRepository.findByExerciseAndSiret(exercise, siret, pageable);
        }

        if (Objects.isNull(tvaEntities)) {
            throw new TvaNotFoundException(String.format("La Tva exercice %s n'existe pas", exercise));
        }

        Page<Tva> tvas = tvaEntities.map(tvaMapper::toDto);

        for (Tva tva : tvas.getContent()) {
            FactureEntity facture = factureJpaRepository.findByNumeroFacture(tva.getNumeroFacture());
            if (facture != null) {
                tva.setMontantTTC(facture.getPrixTotalTTC());
                if (facture.getMontantTVA() != null) {
                    tva.setMontantTvaFacture(facture.getMontantTVA());
                }
                tva.setDateEncaissement(facture.getDateEncaissement());
            }
        }
        return tvas;
    }

    @Override
    public void deleteById(Long id) {
        tvaJpaRepository.deleteById(id);
    }

    @Override
    public Tva addTva(Tva tva) {
        String datePayment = Utils.convertFromDomainToEntityDate(tva.getDatePayment());
        tva.setDatePayment(datePayment);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(datePayment, formatter);
        tva.setDatePaymentSorted(date);

        FactureEntity facture = factureJpaRepository.findByNumeroFacture(tva.getNumeroFacture());
        if (facture != null) {
            tva.setMontantTvaFacture(facture.getMontantTVA());
        }
        TvaEntity entity = tvaJpaRepository.save(tvaMapper.toEntity(tva));
        return tvaMapper.toDto(entity);
    }

    /**
     * @param tva
     */
    @Override
    public void updateTva(Tva tva) {
        String datePayment = Utils.convertFromDomainToEntityDate(tva.getDatePayment());
        tva.setDatePayment(datePayment);
        FactureEntity facture = factureJpaRepository.findByNumeroFacture(tva.getNumeroFacture());
        if (facture != null) {
            tva.setMontantTvaFacture(facture.getMontantTVA());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(tva.getDatePayment(), formatter);
        tva.setDatePaymentSorted(date);

        TvaEntity tvaEntity = tvaMapper.toEntity(tva);
        tvaJpaRepository.saveAndFlush(tvaEntity);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Tva findById(Long id) {
        Optional<TvaEntity> o = tvaJpaRepository.findById(id);
        o.orElseThrow(
                () -> new TvaNotFoundException(String.format("La Tva numéro %s n'existe pas", id)));
        return tvaMapper.toDto(o.get());
    }

    /**
     * @param siret
     * @return
     */
    @Override
    public Page<Tva> findBySiret(String siret, Pageable pageable) {
        Page<TvaEntity> entities = tvaJpaRepository.findBySiret(siret, pageable);
        return entities.map(tvaMapper::toDto);
    }

    /**
     * @param siret
     * @param exercise
     * @param facturesFiltred
     * @return
     */
    TvaInfo buildTvaInfo(String siret, String exercise, List<FactureEntity> facturesFiltred) {

        List<TvaEntity> listeTvaPayee;
        TvaInfo info = new TvaInfo();

        if (Objects.isNull(facturesFiltred)) {
            return info;
        }
        BigDecimal totalTvaPaye = BigDecimal.ZERO;
        BigDecimal montantTvaFacture = BigDecimal.ZERO;

// 1. Correction du calcul de la TVA Nette (Soustraction de 30 avec .subtract())
        BigDecimal totalTvaNet = facturesFiltred.stream()
                .map(e -> e.getMontantTVA() != null ? e.getMontantTVA().subtract(new BigDecimal("30")) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

// 2. Somme des Prix TTC (Pas besoin de valueOf car c'est déjà un BigDecimal)
        BigDecimal totalTTC = facturesFiltred.stream()
                .map(e -> e.getPrixTotalTTC() != null ? e.getPrixTotalTTC() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

// 3. Somme du CA HT (Pas besoin de valueOf car c'est déjà un BigDecimal)
        BigDecimal totalCAHorsTaxe = facturesFiltred.stream()
                .map(e -> e.getPrixTotalHT() != null ? e.getPrixTotalHT() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (exercise.equalsIgnoreCase(TOUS)) {
            listeTvaPayee = tvaJpaRepository.findBySiret(siret);
        } else {
            listeTvaPayee = tvaJpaRepository.findByExerciseAndSiret(exercise, siret);
        }
        if (listeTvaPayee != null) {
            totalTvaPaye = listeTvaPayee.stream().map(TvaEntity::getMontantPayment).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            montantTvaFacture = listeTvaPayee.stream().map(TvaEntity::getMontantTvaFacture).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        info.setTotalTvaPaye(totalTvaPaye);
        info.setMontantTvaFacture(montantTvaFacture);
        info.setTotalTvaNet(totalTvaNet);
        info.setTotalTvaRestant(montantTvaFacture.subtract(totalTvaPaye));
        info.setTotalTTC(totalTTC);
        info.setTotalCAHorsTaxe(totalCAHorsTaxe);
        return info;
    }

    /**
     * @param siret
     * @return
     */
    @Override
    public TvaInfo findTvaInfoBySiret(String siret) {
        List<FactureEntity> factures = factureJpaRepository.findBySiretAndDateEncaissementNotNull(siret);
        return buildTvaInfo(siret, "Tous", factures);
    }


    @Override
    public TvaInfo findTvaInfoByExercice(String exercise, String siret) {
        List<FactureEntity> factures = factureJpaRepository.findBySiretAndExerciceTvaInfo(siret, exercise);
        return buildTvaInfo(siret, exercise, factures);
    }

    @Override
    public List<Tva> findByNumeroFacture(String numeroFacture) {
        List<TvaEntity> tvas = tvaJpaRepository.findByNumeroFacture(numeroFacture);
        return tvaMapper.toDtoList(tvas);
    }

    @Override
    public Page<Tva> searchTvas(String siret, String search, Pageable pageable) {

        Page<TvaEntity> tvaEntities = null;
        boolean isNumeric = Utils.isNumeric(search);
        if (isNumeric) {
            String cleanedValue = search.replaceAll("\\s+", "").replace(",", ".");
            tvaEntities = tvaJpaRepository.searchTvasByNumeric(siret, Float.valueOf(cleanedValue), pageable);
        } else {
            tvaEntities = tvaJpaRepository.searchTvas(siret, search, pageable);
        }
        return tvaEntities.map(tvaMapper::toDto);
    }
}