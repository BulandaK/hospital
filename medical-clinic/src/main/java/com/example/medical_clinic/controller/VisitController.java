package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.visit.VisitDto;
import com.example.medical_clinic.DTO.visit.VisitRequest;
import com.example.medical_clinic.mapper.VisitMapper;
import com.example.medical_clinic.model.Visit;
import com.example.medical_clinic.services.VisitService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/visits")
public class VisitController {
    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @GetMapping
    public PageResponse<VisitDto> getAllVisits(Pageable pageable) {
        return visitService.getAllVisits(pageable);
    }

    @GetMapping("/{patientId}")
    public List<VisitDto> getAllVisitsByPatientId(@PathVariable Long patientId) {
        return visitService.getPatientVisits(patientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitDto create(@RequestBody VisitRequest request) {
        Visit visit = visitService.create(request);
        return visitMapper.toDto(visit);
    }

    @PatchMapping("/{id}/patient/{patientId}")
    public VisitDto addPatient(@PathVariable Long id, @PathVariable Long patientId) {
        Visit visit = visitService.addPatient(id, patientId);
        return visitMapper.toDto(visit);
    }


}
