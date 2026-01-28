package com.example.medical_clinic.services;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.doctor.DoctorCreateRequest;
import com.example.medical_clinic.DTO.doctor.DoctorDto;
import com.example.medical_clinic.DTO.doctor.DoctorUpdateRequest;
import com.example.medical_clinic.exception.DoctorAlreadyExistsException;
import com.example.medical_clinic.exception.DoctorNotFoundException;
import com.example.medical_clinic.exception.PatientAlreadyExistsException;
import com.example.medical_clinic.mapper.DoctorMapper;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.model.User;
import com.example.medical_clinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
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
                .orElseThrow(() -> new DoctorAlreadyExistsException("doctor with mail " + email + " already exists"));
    }

    @Transactional
    public Doctor add(DoctorCreateRequest request) {

        if (doctorRepository.existsByEmail(request.email())) {
            throw new PatientAlreadyExistsException("patient already exists!");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());


        Doctor doctor = new Doctor();
        doctor.setEmail(request.email());
        doctor.setPassword(request.password());
        doctor.setSpecialization(request.specialization());
        doctor.setUser(user);


        return doctorRepository.save(doctor);
    }

    @Transactional
    public void removeByEmail(String email) {
        if (!doctorRepository.existsByEmail(email)) {
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
