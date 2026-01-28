package com.example.medical_clinic.services;

import com.example.medical_clinic.DTO.patient.PatientDto;
import com.example.medical_clinic.DTO.patient.PatientUpdateRequest;
import com.example.medical_clinic.exception.PatientAlreadyExistsException;
import com.example.medical_clinic.exception.PatientNotFoundException;
import com.example.medical_clinic.mapper.PatientMapper;
import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.DTO.patient.PatientCreateRequest;
import com.example.medical_clinic.model.User;
import com.example.medical_clinic.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
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
                .orElseThrow(() -> new PatientNotFoundException("patient with mail " + email + " already exists"));
    }

    @Transactional
    public Patient add(PatientCreateRequest request) {

        if (patientRepository.existsByEmail(request.email())) {
            throw new PatientAlreadyExistsException("patient already exists!");
        }
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        Patient patient = new Patient(null, request.email(), request.password(), request.idCardNo(), request.phoneNumber(), request.birthday(), user, null);
        patientRepository.save(patient);
        return patient;
    }

    @Transactional
    public void removeByEmail(String email) {
        if (!patientRepository.existsByEmail(email)) {
            throw new PatientNotFoundException("not found patient with: " + email);
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