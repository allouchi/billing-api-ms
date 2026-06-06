package com.sbatec.facture.services.internals.compte;

import com.sbatec.facture.dtos.Compte;
import com.sbatec.facture.exceptions.ErrorCatalog;
import com.sbatec.facture.exceptions.ServiceException;
import com.sbatec.facture.mappers.CompteMapper;
import com.sbatec.facture.mappers.OperationMapper;
import com.sbatec.facture.models.CompteEntity;
import com.sbatec.facture.models.OperationEntity;
import com.sbatec.facture.repository.compte.CompteJpaRepository;
import com.sbatec.facture.repository.operation.OperationJpaRepository;
import com.sbatec.facture.util.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class CompteServiceImpl implements CompteService {
    private static final String NDF = "NDF";
    private static final String DIVIDENDES = "DIV";
    private static final String DGFIP = "DGFIP";

    private static final String DIV = "DIV";
    private static final String TVA = "TVA";
    private static final String AUTRE = "AUTRE";

    CompteJpaRepository compteJpaRepository;
    CompteMapper compteMapper;
    OperationJpaRepository operationJpaRepository;
    OperationMapper operationMapper;

    @Override
    public Page<Compte> findBySiret(String siret, Pageable pageable) {
        Page<CompteEntity> operationEntities = compteJpaRepository.findBySiret(siret, pageable);
        return operationEntities.map(compteMapper::toDto);
    }

    @Override
    public Page<Compte> findBySiretAndExerciseAndTypeOperation(String siret, String exercice, String type, Pageable pageable) {
        Page<CompteEntity> operationEntities = compteJpaRepository.findBySiretAndExerciseAndTypeOperation(siret, exercice, type, pageable);
        return operationEntities.map(compteMapper::toDto);
    }

    @Override
    public Page<Compte> findBySiretAndExerciseAndTypeOperationAndMonthOperation(String siret, String exercice, String type, String monthOperation, Pageable pageable) {
        Page<CompteEntity> operationEntities = compteJpaRepository.findBySiretAndExerciseAndTypeOperationAndMonthOperation(siret, exercice, type, monthOperation, pageable);
        return operationEntities.map(compteMapper::toDto);
    }

    @Override
    public Page<Compte> findBySiretAndExercise(String siret, String exercice, Pageable pageable) {
        Page<CompteEntity> compteEntities = compteJpaRepository.findBySiretAndExercise(siret, exercice, pageable);
        return compteEntities.map(compteMapper::toDto);
    }

    @Override
    public Page<Compte> findBySiretAndTypeOperation(String siret, String type, Pageable pageable) {
        Page<CompteEntity> compteEntities = compteJpaRepository.findBySiretAndTypeOperation(siret, type, pageable);
        return compteEntities.map(compteMapper::toDto);
    }

    @Override
    public Compte addOperation(Compte compte) {
        CompteEntity compteEntity = compteJpaRepository.save(compteMapper.toEntity(compte));
        return compteMapper.toDto(compteEntity);
    }

    @Override
    public Page<Compte> findBySiretAndDateOperation(String siret, String dateOperation, Pageable pageable) {
        Page<CompteEntity> compteEntities = compteJpaRepository.findBySiretAndDateOperation(siret, dateOperation, pageable);
        return compteEntities.map(compteMapper::toDto);
    }

    @Override
    public Page<Compte> findBySiretAndMonthOperation(String siret, String month, Pageable pageable) {
        Page<CompteEntity> compteEntities = compteJpaRepository.findBySiretAndMonthOperation(siret, month, pageable);
        return compteEntities.map(compteMapper::toDto);
    }

    @Override
    public Page<Compte> findBySiretAndExerciseAndMonthOperation(String siret, String exercice, String monthOperation, Pageable pageableReq) {
        Page<CompteEntity> compteEntities = compteJpaRepository.findBySiretAndExerciseAndMonthOperation(siret, exercice, monthOperation, pageableReq);
        return compteEntities.map(compteMapper::toDto);
    }

    @Override
    public Page<Compte> findBySiretAndTypeOperationAndMonthOperation(String siret, String type, String monthOperation, Pageable pageable) {
        Page<CompteEntity> compteEntities = compteJpaRepository.findBySiretAndTypeOperationAndMonthOperation(siret, type, monthOperation, pageable);
        return compteEntities.map(compteMapper::toDto);
    }


    @Override
    public List<Compte> importOperations(String siret, String pathFile) throws IOException {
        List<Compte> comptes = new ArrayList<>();
        File folder = new File(pathFile);
        File[] files = folder.listFiles((file) -> file.isFile() && file.getName().toLowerCase().endsWith(".csv"));
        if (files == null || files.length == 0) {
            throw new ServiceException(ErrorCatalog.RESOURCE_NOT_FOUND, "Aucun nouveau fichier à importer !");
        }

        for (File file : files) {

            String fileCsvName = Utils.readCsvHeader(file.toPath());

            try (Stream<String> lines = Files.lines(file.toPath())) {

                lines.skip(7) // ignorer les 7 premières lignes
                        .filter(line -> line != null && !line.isBlank()) // ignorer lignes vides
                        .forEach(line -> {

                            String[] cols = line.split(";");
                            // sécurité : vérifier nombre de colonnes
                            if (cols.length < 3) {
                                System.err.println("Ligne ignorée car colonne manquante : " + line);
                                return;
                            }
                            Compte compte = new Compte();
                            compte.setSiret(siret);

                            // ---- Colonne 0 : date ----
                            String colDate = cols[0].trim();
                            if (colDate.length() >= 8) {
                                compte.setDateOperation(colDate);
                                // exercice = année
                                String exercice = colDate.substring(colDate.length() - 4);
                                compte.setExercise(exercice);
                                String monthOperation = colDate.substring(3, 5);
                                compte.setMonthOperation(monthOperation);
                            }

                            // ---- Colonne 1 : type / description ----
                            String colOp = cols[1].trim();
                            if (!colOp.isEmpty()) {
                                compte.setDescriptionOperation(colOp);
                                if (colOp.contains(NDF)) {
                                    compte.setTypeOperation(NDF);
                                } else if (colOp.contains(DIVIDENDES)) {
                                    compte.setTypeOperation(DIV);
                                } else if (colOp.contains(DGFIP)) {
                                    compte.setTypeOperation(TVA);
                                } else {
                                    compte.setTypeOperation(AUTRE);
                                }
                            }

                            // ---- Colonne 2 : montant ----
                            String colMontant = cols[2].trim();
                            if (!colMontant.isEmpty()) {
                                colMontant = colMontant.replace("€", "")
                                        .replace(",", ".")
                                        .trim();

                                try {
                                    compte.setMontantOperation(new BigDecimal(colMontant));
                                } catch (NumberFormatException e) {
                                    System.err.println("Montant invalide : " + colMontant);
                                }
                            }
                            comptes.add(compte);
                        });
            }

            if (fileCsvName != null && !fileCsvName.isEmpty()) {
                try {
                    String cible = pathFile + "/" + fileCsvName + ".imported";
                    Path sourcePath = file.toPath();
                    Path ciblePath = Paths.get(cible);
                    Files.move(sourcePath, ciblePath, StandardCopyOption.REPLACE_EXISTING);
                    log.info("Fichier renommé en : " + fileCsvName);
                } catch (IOException e) {
                    log.error("Problème lors du renommage du fichier : " + fileCsvName);
                }
            }
        }

        if (!comptes.isEmpty()) {
            for (Compte compte : comptes) {
                if (compte.getTypeOperation().equalsIgnoreCase(NDF) || compte.getTypeOperation().equalsIgnoreCase(DIVIDENDES)) {
                    OperationEntity operationEntity = new OperationEntity();
                    operationEntity.setDateOperation(compte.getDateOperation());
                    operationEntity.setTypeOperation(compte.getTypeOperation());
                    operationEntity.setExercise(compte.getExercise());
                    operationEntity.setMontantoperation(compte.getMontantOperation().negate());
                    operationEntity.setSiret(compte.getSiret());
                }
            }
        }
        comptes.stream().sorted(Comparator.comparing(Compte::getId).reversed());
        return comptes;
    }


    @Override
    public Page<Compte> searchOperations(String siret, String search, Pageable pageable) {

        Page<CompteEntity> compteEntities = null;
        boolean isNumeric = Utils.isNumeric(search);
        if (isNumeric) {
            String cleanedValue = search.replaceAll("\\s+", "").replace(",", ".");
            compteEntities = compteJpaRepository.searchOperationsByNumeric(siret, Float.valueOf(cleanedValue), pageable);
        } else {
            compteEntities = compteJpaRepository.searchOperations(siret, search, pageable);
        }
        return compteEntities.map(compteMapper::toDto);
    }

    @Override
    public List<Compte> findAll() throws IOException {
        List<CompteEntity> comptes = compteJpaRepository.findAll();
        return compteMapper.toDtoList(comptes);
    }

    @Override
    public Compte updateOperation(Compte compteDto) {
        CompteEntity compte = compteJpaRepository.findById(compteDto.getId()).orElseThrow(() -> new ServiceException(ErrorCatalog.RESOURCE_NOT_FOUND));
        compte.setDateOperation(compteDto.getDateOperation());
        compte.setTypeOperation(compteDto.getTypeOperation());
        compte.setDescriptionOperation(compteDto.getDescriptionOperation());
        compte.setMontantoperation(compteDto.getMontantOperation());
        compte.setSiret(compteDto.getSiret());
        compte = compteJpaRepository.save(compte);
        return compteMapper.toDto(compte);
    }
}
