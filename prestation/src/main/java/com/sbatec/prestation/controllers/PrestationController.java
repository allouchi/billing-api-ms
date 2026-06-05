package com.sbatec.prestation.controllers;

import com.sbatec.prestation.dtos.Prestation;
import com.sbatec.prestation.services.internals.PrestationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/prestations")
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrestationController {

    PrestationService prestationService;

    /**
     *
     * @param siret
     * @return
     */
    @GetMapping(value = "/{siret}")
    public List<Prestation> findBySiret(@PathVariable String siret) {
        log.info("get all prestations");
        return prestationService.findBySiret(siret);
    }

    /**
     *
     * @param prestation
     * @param siret
     * @return
     */
    @PostMapping(value = "/{siret}")
    public Prestation addPrestation(@RequestBody Prestation prestation,
                                    @PathVariable String siret) {
        log.info("Create new Prestation");
        return prestationService.addPrestation(siret, prestation);
    }

    /**
     *
     * @param prestation
     * @return
     */
    @PutMapping()
    public Prestation updatePrestation(@RequestBody Prestation prestation) {
        log.info("Create or update Prestation");
        return prestationService.updatePrestation(prestation);
    }

    /**
     *
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    public void deletePrestation(@PathVariable long id) {
        log.info("delete prestation by id : {}", id);
        prestationService.deleteById(id);
    }
}
