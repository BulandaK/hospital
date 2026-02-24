package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.doctor.DoctorCreateRequest;
import com.example.medical_clinic.DTO.doctor.DoctorDto;
import com.example.medical_clinic.DTO.doctor.DoctorUpdateRequest;
import com.example.medical_clinic.mapper.DoctorMapper;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.services.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
@Slf4j
@Tag(name = "Doctor Management", description = "Endpoints for managing doctors profiles")
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @GetMapping
    @Operation(
            summary = "Get all doctors",
            description = "Retrieves a paginated list of all registered medical clinics."
    )
    public PageResponse<DoctorDto> getAll(@ParameterObject Pageable pageable) {
        log.info("Getting all doctors");
        return doctorService.getAll(pageable);
    }

    @GetMapping("/{email}")
    @Operation(
            summary = "Get doctor by email",
            description = "Returns detailed information about a doctor using its email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctor found successfully"),
                    @ApiResponse(responseCode = "404", description = "Doctor not found with provided email", content = @Content)
            }
    )
    public DoctorDto getByEmail(@PathVariable @NotBlank String email) {
        Doctor doctor = doctorService.getByEmail(email);
        log.info("Doctor found with email: {}", email);
        return doctorMapper.toDto(doctor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new Doctor",
            description = "Register a new Doctor in system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Doctor created successfully"),
                    @ApiResponse(responseCode = "409", description = "Doctor already exists", content = @Content)
            }
    )
    public DoctorDto create(@RequestBody @Valid DoctorCreateRequest request) {
        log.info("Creating doctor with email: {}", request.email());
        Doctor doctor = doctorService.add(request);
        log.info("Doctor with email: {}, has been created", request.email());
        return doctorMapper.toDto(doctor);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete doctor",
            description = "Permanently removes a clinic from the system based on its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Doctor deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Doctor not found with provided email", content = @Content)
            }
    )
    public void delete(@PathVariable @NotBlank String email) {
        log.info("Deleting doctor with email: {}", email);
        doctorService.removeByEmail(email);
        log.info("Doctor with email: {}, removed successfully", email);
    }

    @PutMapping("/{email}")
    @Operation(
            summary = "Update doctor",
            description = "Updates the information of an existing doctor.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "doctor updated successfully"),
                    @ApiResponse(responseCode = "404", description = "doctor not found with the provided email", content = @Content)
            }
    )
    public DoctorDto update(@PathVariable @NotBlank String email,
                            @RequestBody @Valid DoctorUpdateRequest request) {
        log.info("Updating doctor with email: {}",email);
        Doctor doctor = doctorService.updateByEmail(email, request);
        log.info("Doctor with email: {}, successfully updated",email);
        return doctorMapper.toDto(doctor);
    }
}
