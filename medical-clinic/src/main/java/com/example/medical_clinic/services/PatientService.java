package com.example.medical_clinic.services;

import com.example.medical_clinic.DTO.patient.PatientDto;
import com.example.medical_clinic.DTO.patient.PatientUpdateRequest;
import com.example.medical_clinic.exception.patient.PatientAlreadyExistsException;
import com.example.medical_clinic.exception.patient.PatientNotFoundException;
import com.example.medical_clinic.mapper.PatientMapper;
import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.DTO.patient.PatientCreateRequest;
import com.example.medical_clinic.model.User;
import com.example.medical_clinic.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
@Slf4j
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDto> getAll(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .stream()
                .map(patientMapper::patientToDto)
                .toList();
    }

    public Patient getByEmail(String email) {
        return patientRepository.getByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Patient search field: email {} not found", email);
                    return new PatientNotFoundException("patient with mail " + email + " already exists");
                });
    }

    @Transactional
    public Patient add(PatientCreateRequest request) {
        if (patientRepository.existsByEmail(request.email())) {
            log.error("PatientAlreadyExistsException has been thrown for email:{}", request.email());
            throw new PatientAlreadyExistsException("patient already exists!");
        }
        User user = new User(null, request.firstName(), request.lastName(), null, null);
        Patient patient = new Patient(null, request.email(), request.password(), request.idCardNo(), request.phoneNumber(), request.birthday(), user, null, null);
        patientRepository.save(patient);
        return patient;
    }

    @Transactional
    public void removeByEmail(String email) {
        if (!patientRepository.existsByEmail(email)) {
            log.error("Can't delete patient with email: {} that doesn't exists", email);
            throw new PatientNotFoundException("not found patient with: " + email);
        }
        patientRepository.deleteByEmail(email);
    }

    @Transactional
    public Patient updateByEmail(String email, PatientUpdateRequest request) {
        Patient patient = getByEmail(email);
        return patient.update(request);
    }

    @Transactional
    public void updatePassword(String email, String password) {
        Patient patient = getByEmail(email);
        patient.setPassword(password);
        patientRepository.save(patient);
    }
}