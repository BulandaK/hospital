package com.example.medical_clinic.services;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.doctor.DoctorCreateRequest;
import com.example.medical_clinic.DTO.doctor.DoctorDto;
import com.example.medical_clinic.DTO.doctor.DoctorUpdateRequest;
import com.example.medical_clinic.exception.doctor.DoctorAlreadyExistsException;
import com.example.medical_clinic.exception.doctor.DoctorNotFoundException;
import com.example.medical_clinic.mapper.DoctorMapper;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.model.User;
import com.example.medical_clinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public PageResponse<DoctorDto> getAll(Pageable pageable) {
        Page<Doctor> page = doctorRepository.findAll(pageable);
        List<DoctorDto> content = page.stream()
                .map(doctorMapper::toDto)
                .toList();
        return new PageResponse<>(content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    public Doctor getByEmail(String email) {
        return doctorRepository.getByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Can't find doctor with email: {}", email);
                    return new DoctorNotFoundException("doctor with mail " + email + " not found");
                });
    }

    public Doctor getById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Can't find doctor with id: {}", id);
                    return new DoctorNotFoundException("doctor with id" + id + " not found");
                });
    }

    @Transactional
    public Doctor add(DoctorCreateRequest request) {
        if (doctorRepository.existsByEmail(request.email())) {
            log.error("Can't create doctor with this email: {}, it already exists", request.email());
            throw new DoctorAlreadyExistsException("doctor already exists!");
        }
        User user = new User(null, request.firstName(), request.lastName(), null, null);
        Doctor doctor = new Doctor(null, request.email(), request.password(), request.specialization(), user, null, null, null);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void removeByEmail(String email) {
        if (!doctorRepository.existsByEmail(email)) {
            log.error("Can't delete doctor, doctor with email: {} not found", email);
            throw new DoctorNotFoundException("Doctor with this email not found");
        }
        doctorRepository.deleteByEmail(email);
    }

    @Transactional
    public Doctor updateByEmail(String email, DoctorUpdateRequest request) {
        Doctor doctor = getByEmail(email);
        return doctor.update(request);
    }
}
