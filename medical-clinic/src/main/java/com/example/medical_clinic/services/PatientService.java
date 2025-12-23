package com.example.medical_clinic.services;

import com.example.medical_clinic.exception.PatientAlreadyExistsException;
import com.example.medical_clinic.exception.PatientNotFoundException;
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

    public Patient getByEmail(String email) {
        return patientRepository.getByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Pacjent o mailu " + email + " nie istnieje"));
    }

    public Patient add(Patient patient) {
        if (userExists(patient.getEmail())) {
            throw new PatientAlreadyExistsException("Pacjent juz istnieje!");
        }

        patientRepository.add(patient);
        return patient;
    }

    public void removeByEmail(String email) {
        if (!patientRepository.deleteByEmail(email)) {
            throw new PatientNotFoundException("nie znaleziono pacjenta o mailu: " + email);
        }
    }

    public Patient updateByEmail(String email, Patient updated) {
        Patient source = getByEmail(email);

        source.setPassword(updated.getPassword());
        source.setIdCardNo(updated.getIdCardNo());
        source.setFirstName(updated.getFirstName());
        source.setLastName(updated.getLastName());
        source.setPhoneNumber(updated.getPhoneNumber());
        source.setBirthday(updated.getBirthday());

        return source;
    }

    private boolean userExists(String email) {
        return patientRepository.getByEmail(email).isPresent();
    }
}
