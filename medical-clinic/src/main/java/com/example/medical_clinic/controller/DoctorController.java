package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.doctor.DoctorCreateRequest;
import com.example.medical_clinic.DTO.doctor.DoctorDto;
import com.example.medical_clinic.DTO.doctor.DoctorUpdateRequest;
import com.example.medical_clinic.mapper.DoctorMapper;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.services.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @GetMapping
    public PageResponse<DoctorDto> getAll(Pageable pageable) {
        return doctorService.getAll(pageable);
    }

    @GetMapping("/{email}")
    public DoctorDto getByEmail(@PathVariable String email) {
        Doctor doctor = doctorService.getByEmail(email);
        return doctorMapper.toDto(doctor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto create(@RequestBody DoctorCreateRequest request) {
        Doctor doctor = doctorService.add(request);
        return doctorMapper.toDto(doctor);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String email) {
        doctorService.removeByEmail(email);
    }

    @PutMapping("/{email}")
    public DoctorDto update(@PathVariable String email, @RequestBody DoctorUpdateRequest request) {
        Doctor doctor = doctorService.updateByEmail(email, request);
        return doctorMapper.toDto(doctor);
    }
}
