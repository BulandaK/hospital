package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.ChangePasswordCommand;
import com.example.medical_clinic.DTO.patient.PatientDto;
import com.example.medical_clinic.DTO.patient.PatientUpdateRequest;
import com.example.medical_clinic.mapper.PatientMapper;
import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.DTO.patient.PatientCreateRequest;
import com.example.medical_clinic.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
@Slf4j
@Tag(name = "Patient Management", description = "Endpoints for managing patient profiles")
public class PatientController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping
    @Operation(
            summary = "Get all patients",
            description = "Retrieves a paginated list of all registered patients in the system."
    )
    public List<PatientDto> getAll(@ParameterObject Pageable pageable) {
        log.info("Getting all users");
        return patientService.getAll(pageable);
    }

    @GetMapping("/{email}")
    @Operation(
            summary = "Find patient by email",
            description = "Returns a single patient profile based on their unique email address.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Patient found successfully"),
                    @ApiResponse(responseCode = "404", description = "Patient not found with the provided email", content = @Content)
            }
    )
    public PatientDto getByEmail(
            @Parameter(description = "The unique email address of the patient", example = "kami@gmail.com")
            @PathVariable @NotBlank(message = "email can't be null") @Email String email) {
        Patient patient = patientService.getByEmail(email);
        log.info("Patient found with email {} {}", email, patient);
        return patientMapper.patientToDto(patient);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register a new patient",
            description = "Creates a new patient account and an associated user profile.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Patient created successfully"),
                    @ApiResponse(responseCode = "409", description = "Patient with this email already exists")
            }
    )
    public PatientDto create(@RequestBody @Valid PatientCreateRequest request) {
        log.info("Creating new patient with email: {}", request.email());
        Patient patient = patientService.add(request);
        log.info("Patient with email: {} has been created", patient.getEmail());
        return patientMapper.patientToDto(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete patient account",
            description = "Permanently removes the patient record from the database using their email.",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Patient not found with the provided email", content = @Content)
            }
    )
    public void delete(@PathVariable @NotBlank String email) {
        log.info("Deleting patient with email: {}", email);
        patientService.removeByEmail(email);
    }

    @PutMapping("/{email}")
    @Operation(
            summary = "Update patient details",
            description = "Updates editable fields like phone number and birthday for an existing patient.",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Patient not found with the provided email", content = @Content)
            }
    )
    public PatientDto update(
            @PathVariable @NotBlank String email,
            @RequestBody @Valid PatientUpdateRequest request) {
        log.info("Updating patient with email: {}", email);
        Patient patient = patientService.updateByEmail(email, request);
        log.info("Patient with email: {}  updated successfully", email);
        return patientMapper.patientToDto(patient);
    }

    @PatchMapping("/{email}")
    @Operation(
            summary = "Change password",
            description = "Updates the authentication password for the specified patient account.",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Patient not found with the provided email", content = @Content)
            }
    )
    public void updatePassword(
            @PathVariable @NotBlank String email,
            @RequestBody @Valid @NotNull ChangePasswordCommand password) {
        patientService.updatePassword(email, password.password());
        log.info("Patient with email: {}, changed password successfully", email);
    }
}
