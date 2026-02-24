package com.example.medical_clinic.services;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.clinic.ClinicDto;
import com.example.medical_clinic.DTO.clinic.ClinicRequest;
import com.example.medical_clinic.DTO.clinic.ClinicUpdateRequest;
import com.example.medical_clinic.exception.clinic.ClinicNotFoundException;
import com.example.medical_clinic.exception.doctor.DoctorNotFoundException;
import com.example.medical_clinic.mapper.ClinicMapper;
import com.example.medical_clinic.model.Clinic;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.repository.ClinicRepository;
import com.example.medical_clinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicService {

    private final ClinicRepository clinicRepository;
    private final DoctorRepository doctorRepository;
    private final ClinicMapper clinicMapper;

    public PageResponse<ClinicDto> getAll(Pageable pageable) {
        Page<Clinic> clinicPage = clinicRepository.findAll(pageable);
        List<ClinicDto> content = clinicPage.getContent().stream()
                .map(clinicMapper::toDto)
                .toList();

        return new PageResponse<>(content,
                clinicPage.getNumber(),
                clinicPage.getSize(),
                clinicPage.getTotalElements(),
                clinicPage.getTotalPages()
        );
    }

    public Clinic getById(Long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Clinic not found with id: " + id);
                    return new ClinicNotFoundException("Clinic not found with id: " + id);
                });
    }

    @Transactional
    public Clinic add(ClinicRequest request) {
        Clinic clinic = new Clinic(null,request.name(),request.city(), request.postalCode(), request.street(), request.buildingNumber(),null);
        return clinicRepository.save(clinic);
    }

    @Transactional
    public void removeById(Long id) {
        if (!clinicRepository.existsById(id)) {
            log.error("Cannot delete. Clinic not found with id: {}", id);
            throw new ClinicNotFoundException("Cannot delete. Clinic not found with id: " + id);
        }
        clinicRepository.deleteById(id);
    }

    @Transactional
    public Clinic updateById(Long id, ClinicUpdateRequest request) {
        Clinic clinic = getById(id);
        clinic.update(request);
        return clinic;
    }

    @Transactional
    public Clinic assignDoctor(Long clinicId, Long doctorId) {
        Clinic clinic = getById(clinicId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> {
                    log.error("Doctor not found with id: {}", doctorId);
                    return new DoctorNotFoundException("Doctor not found with id: " + doctorId);
                });
        clinic.getDoctors().add(doctor);
        return clinic;
    }
}