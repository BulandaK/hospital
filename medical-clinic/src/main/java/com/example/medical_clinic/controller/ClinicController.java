package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.clinic.ClinicDto;
import com.example.medical_clinic.DTO.clinic.ClinicRequest;
import com.example.medical_clinic.mapper.ClinicMapper;
import com.example.medical_clinic.model.Clinic;
import com.example.medical_clinic.services.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clinics")
public class ClinicController {
    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;

    @GetMapping
    public PageResponse<ClinicDto> getAll(Pageable pageable) {
        return clinicService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ClinicDto getById(@PathVariable Long id) {
        Clinic clinic = clinicService.getById(id);
        return clinicMapper.toDto(clinic);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicDto create(@RequestBody ClinicRequest request) {
        Clinic clinic = clinicService.add(request);
        return clinicMapper.toDto(clinic);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clinicService.removeById(id);
    }

    @PutMapping("/{id}")
    public ClinicDto update(@PathVariable Long id, @RequestBody ClinicRequest request) {
        Clinic clinic = clinicService.updateById(id, request);
        return clinicMapper.toDto(clinic);
    }

    @PatchMapping("/{id}/doctor/{doctorId}")
    public ClinicDto assignDoctor(@PathVariable Long id, @PathVariable Long doctorId) {
        Clinic clinic = clinicService.assignDoctor(id, doctorId);
        return clinicMapper.toDto(clinic);
    }
}
