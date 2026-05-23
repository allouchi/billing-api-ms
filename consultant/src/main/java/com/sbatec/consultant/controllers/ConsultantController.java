package com.sbatec.consultant.controllers;

import com.sbatec.consultant.dtos.Consultant;
import com.sbatec.consultant.services.ConsultantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/consultants")
public class ConsultantController {

    ConsultantService consultantService;


    @GetMapping()
    public List<Consultant> getAllConsultants() {
        log.info("get all consultants");
        return consultantService.findAll();
    }

    @GetMapping("/batch")
    List<Consultant> findAllByIds(@RequestParam("ids") List<Long> ids) {
        log.info("Find batch of consultant for ids: {}", ids);
        if (ids == null || ids.isEmpty()) {
            return List.of(); // Évite un appel inutile au service si la liste est vide
        }
        return consultantService.findAllConsultantsByIds(ids);
    }

    @PostMapping(value = "/add")
    public Consultant addConsultant(@RequestBody Consultant consultantRequest
    ) {
        log.info("Create new consultant");
        return consultantService.addConsultant(consultantRequest);
    }

    @PutMapping(value = "/update")
    public Consultant updateConsultant(@RequestBody Consultant consultantRequest) {
        log.info("Update consultant by id {}:", consultantRequest.getId());
        return consultantService.updateConsultant(consultantRequest);
    }

    @GetMapping("/{id}")
    public Consultant findById(@PathVariable long id) {
        log.info("find consultant by id  {}:", id);
        return consultantService.findById(id);
    }


    @DeleteMapping(value = "/{id}")
    public void deleteConsultant(@PathVariable Long id) {
        log.info("delete consultant by id {}:", id);
        consultantService.deleteConsultant(id);
    }
}
