package com.example.medical_clinic.services;

import com.example.medical_clinic.DTO.PatientDto;
import com.example.medical_clinic.DTO.PatientUpdateRequest;
import com.example.medical_clinic.exception.PatientAlreadyExistsException;
import com.example.medical_clinic.exception.PatientNotFoundException;
import com.example.medical_clinic.mapper.PatientMapper;
import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.DTO.PatientCreateRequest;
import com.example.medical_clinic.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDto> getAll() {
        return patientRepository.findAll().stream()
                .map(patientMapper::patientToDto)
                .toList();
    }

    public Patient getByEmail(String email) {
        return patientRepository.getByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Pacjent o mailu " + email + " nie istnieje"));
    }

    public Patient add(PatientCreateRequest request) {

        if (patientRepository.existsByEmail(request.email())) {
            throw new PatientAlreadyExistsException("Pacjent juz istnieje!");
        }

        Patient patient = Patient.builder()
                .email(request.email())
                .password(request.password())
                .idCardNo(request.idCardNo())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .birthday(request.birthday())
                .build();

        patientRepository.save(patient);
        return patient;
    }

    @Transactional
    public void removeByEmail(String email) {
        if (!patientRepository.existsByEmail(email)) {
            throw new PatientNotFoundException("nie znaleziono pacjenta o mailu: " + email);
        }
        patientRepository.deleteByEmail(email);
    }

    @Transactional
    public Patient updateByEmail(String email, PatientUpdateRequest request) {
        Patient patient = getByEmail(email);
        return patient.update(request);
    }

    public void updatePassword(String email, String password) {
        Patient patient = getByEmail(email);
        patient.setPassword(password);
        patientRepository.save(patient);
    }
}