package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.ChangePasswordCommand;
import com.example.medical_clinic.DTO.patient.PatientDto;
import com.example.medical_clinic.DTO.patient.PatientUpdateRequest;
import com.example.medical_clinic.mapper.PatientMapper;
import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.DTO.patient.PatientCreateRequest;
import com.example.medical_clinic.services.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping
    public List<PatientDto> getAll(Pageable pageable) {
        return patientService.getAll(pageable);
    }

    @GetMapping("/{email}")
    public PatientDto getByEmail(@PathVariable String email) {
        Patient patient = patientService.getByEmail(email);
        return patientMapper.patientToDto(patient);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto create(@RequestBody PatientCreateRequest request) {
        Patient patient = patientService.add(request);
        return patientMapper.patientToDto(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String email) {
        patientService.removeByEmail(email);
    }

    @PutMapping("/{email}")
    public PatientDto update(@PathVariable String email, @RequestBody PatientUpdateRequest request) {
        Patient patient = patientService.updateByEmail(email, request);
        return patientMapper.patientToDto(patient);
    }

    @PatchMapping("/{email}")
    public void updatePassword(@PathVariable String email, @RequestBody ChangePasswordCommand password) {
        patientService.updatePassword(email, password.password());
    }
}
