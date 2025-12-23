package com.example.medical_clinic.repository;

import com.example.medical_clinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository {
    private final List<Patient> patients;

    public PatientRepository(List<Patient> patients) {
        this.patients = patients;
        patients.add(new Patient("jan@test.pl", "haslo123", "123", "Jan", "Kowalski", "555", LocalDate.now()));
    }

    public List<Patient> getAll() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> getByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    public void add(Patient patient) {
        patients.add(patient);
    }

    public boolean deleteByEmail(String email) {
        return patients.removeIf(patient -> patient.getEmail().equals(email));
    }

}
