package com.sbatec.prestation.services.internals;

import com.sbatec.prestation.dtos.Client;
import com.sbatec.prestation.dtos.Consultant;
import com.sbatec.prestation.dtos.Prestation;
import com.sbatec.prestation.exceptions.ErrorCatalog;
import com.sbatec.prestation.mappers.PrestationMapper;
import com.sbatec.prestation.models.PrestationEntity;
import com.sbatec.prestation.repository.PrestationJpaRepository;
import com.sbatec.prestation.services.externals.ClientRestClient;
import com.sbatec.prestation.services.externals.ConsultantRestClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrestationServiceImpl implements PrestationService {

    PrestationJpaRepository prestationJpaRepository;
    PrestationMapper prestationMapper;
    ClientRestClient clientRestClient;
    ConsultantRestClient consultantRestClient;


    @Override
    public Prestation addPrestation(String siret, Prestation prestation) {
        PrestationEntity prestationEntity = prestationMapper.toEntity(prestation);
        prestationEntity = prestationJpaRepository.save(prestationEntity);
        return prestationMapper.toDto(prestationEntity);
    }

    @Override
    public void deletePrestation(Prestation prestation) {
        prestationJpaRepository.delete(prestationMapper.toEntity(prestation));
    }

    @Override
    public Prestation updatePrestation(Prestation prestation) {
        Optional.ofNullable(prestation).orElseThrow(() -> new ServiceException(ErrorCatalog.BAD_DATA_ARGUMENT.getMessage()));
        PrestationEntity prestationEntity = prestationJpaRepository.save(prestationMapper.toEntity(prestation));
        return prestationMapper.toDto(prestationEntity);
    }

    @Override
    public Prestation findById(Long id) {
        Optional<PrestationEntity> prestationEntity = prestationJpaRepository.findById(id);
        return prestationEntity.map(prestationMapper::toDto).orElse(null);

    }

    @Override
    public List<Prestation> findBySiret(String siret) {
        List<PrestationEntity> prestationEntities = prestationJpaRepository.findBySiret(siret);
        List<Prestation> prestations = prestationMapper.toDtoList(prestationEntities);

        if (prestations == null || prestations.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. Collecte des IDs uniques (inchangé)
        List<Long> clientIds = prestations.stream().map(Prestation::getClientId).distinct().toList();
        List<Long> consultantIds = prestations.stream().map(Prestation::getConsultantId).distinct().toList();

        // 2. Déclenchement des appels réseau EN PARALLÈLE
        // Tâche asynchrone pour les Clients
        CompletableFuture<Map<Long, Client>> clientsFuture = CompletableFuture.supplyAsync(() ->
                clientRestClient.findAllByIds(clientIds).stream()
                        .collect(Collectors.toMap(Client::getId, client -> client))
        ).exceptionally(ex -> {
            // En cas d'erreur globale non gérée par le CircuitBreaker, on évite le crash en renvoyant une map vide
            return Collections.emptyMap();
        });

        // Tâche asynchrone pour les Consultants
        CompletableFuture<Map<Long, Consultant>> consultantsFuture = CompletableFuture.supplyAsync(() ->
                consultantRestClient.findAllByIds(consultantIds).stream()
                        .collect(Collectors.toMap(Consultant::getId, consultant -> consultant))
        ).exceptionally(ex -> Collections.emptyMap());

        // 3. Barrière de synchronisation : on attend que les deux tâches soient terminées
        CompletableFuture.allOf(clientsFuture, consultantsFuture).join();

        // 4. Récupération des résultats calculés en tâche de fond
        Map<Long, Client> clientMap = clientsFuture.join();
        Map<Long, Consultant> consultantMap = consultantsFuture.join();

        // 5. Association en mémoire (Ultra rapide)
        prestations.forEach(prestation -> {
            prestation.setClient(clientMap.get(prestation.getClientId()));
            prestation.setConsultant(consultantMap.get(prestation.getConsultantId()));
        });
        return prestations;
    }

    @Override
    public void deleteById(Long id) {
        prestationJpaRepository.deleteById(id);
    }
}
