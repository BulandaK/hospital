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
    public ResponseEntity<Patient> getByEmail(@PathVariable String email) {
        return patientSerivce.getByEmail(email)
                .map(patient -> ResponseEntity.status(HttpStatus.OK).body(patient))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @PostMapping("/create")
    public ResponseEntity<Patient> create(@RequestBody Patient patient) {
        return patientSerivce.add(patient)
                .map(patient1 -> ResponseEntity.status(HttpStatus.CREATED).body(patient1))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email) {
        boolean deletedPatient = patientSerivce.removeByEmail(email);

        if (deletedPatient) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<Patient> update(@PathVariable String email, @RequestBody Patient patient) {
        return patientSerivce.updateByEmail(email, patient)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
