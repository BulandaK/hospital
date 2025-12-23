package com.example.medical_clinic.services;

import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAll() {
        return patientRepository.getAll();
    }

    public Optional<Patient> getByEmail(String email) {
        return patientRepository.getByEmail(email);
    }

    public Optional<Patient> add(Patient patient) {

        if (userExists(patient.getEmail())) {
            System.out.println("LOG:użytkownik już istnieje");
            return Optional.empty();
        }

        patientRepository.add(patient);
        return Optional.of(patient);
    }

    public boolean removeByEmail(String email) {
        boolean isDeleted = patientRepository.deleteByEmail(email);
        if (!isDeleted) {
            System.out.println("LOG: nie znaleziono pacjenta o mailu: " + email);
        }
        return isDeleted;
    }

    public Optional<Patient> updateByEmail(String email, Patient updated) {
        return patientRepository.getByEmail(email)
                .map(existingPatient -> {
                    existingPatient.setPassword(updated.getPassword());
                    existingPatient.setIdCardNo(updated.getIdCardNo());
                    existingPatient.setFirstName(updated.getFirstName());
                    existingPatient.setLastName(updated.getLastName());
                    existingPatient.setPhoneNumber(updated.getPhoneNumber());
                    existingPatient.setBirthday(updated.getBirthday());
                    return existingPatient;
                });
    }

    private boolean userExists(String email) {
        return patientRepository.getByEmail(email).isPresent();
    }
}
