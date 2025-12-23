package com.example.medical_clinic.controller;

import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.services.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientSerivce;

    public PatientController(PatientService patientSerivce) {
        this.patientSerivce = patientSerivce;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Patient> getAll() {
        return patientSerivce.getAll();
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Patient getByEmail(@PathVariable String email) {
        return patientSerivce.getByEmail(email);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@RequestBody Patient patient) {
        return patientSerivce.add(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String email) {
        patientSerivce.removeByEmail(email);
    }

    @PutMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Patient update(@PathVariable String email, @RequestBody Patient patient) {
        return patientSerivce.updateByEmail(email, patient);
    }
}
