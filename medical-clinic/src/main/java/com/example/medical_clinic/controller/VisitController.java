package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.visit.VisitDto;
import com.example.medical_clinic.DTO.visit.VisitRequest;
import com.example.medical_clinic.mapper.VisitMapper;
import com.example.medical_clinic.model.Visit;
import com.example.medical_clinic.services.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visits")
@Slf4j
@Tag(name = "Visit Management", description = "Endpoints for scheduling and managing medical appointments")
public class VisitController {
    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @GetMapping
    @Operation(
            summary = "Get all visits",
            description = "Retrieves a paginated list of all available and booked visits in the system."
    )
    public PageResponse<VisitDto> getAllVisits(@ParameterObject Pageable pageable) {
        log.info("Getting all visits");
        return visitService.getAllVisits(pageable);
    }

    @GetMapping("/{patientId}")
    @Operation(
            summary = "Get patient's visits",
            description = "Retrieves a list of all visits assigned to a specific patient.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of visits retrieved"),
                    @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
            }
    )
    public List<VisitDto> getAllVisitsByPatientId(
            @Parameter(description = "ID of the patient", example = "1")
            @PathVariable @NotBlank Long patientId) {
        log.info("Getting visit by patient with ID: {}",patientId);
        return visitService.getPatientVisits(patientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new visit slot",
            description = "Creates a new visit time slot for a doctor. Start and end times must be in 15-minute intervals (e.g., 10:00, 10:15).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Visit slot created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid time format or overlapping visit", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Doctor not found",content = @Content)
            }
    )
    public VisitDto create(@RequestBody @Valid VisitRequest request) {
        log.info("Creating visit, doctor ID: {}",request.doctorId());
        Visit visit = visitService.create(request);
        log.info("Visit created successfully, doctor ID: {}",request.doctorId());
        return visitMapper.toDto(visit);
    }

    @PatchMapping("/{id}/patient/{patientId}")
    @Operation(
            summary = "Book a visit slot",
            description = "Assigns a patient to an existing, empty visit slot.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Visit booked successfully"),
                    @ApiResponse(responseCode = "400", description = "Visit already booked or double booking error", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Visit or Patient not found", content = @Content)
            }
    )
    public VisitDto addPatient(
            @Parameter(description = "ID of the visit slot", example = "10") @PathVariable @NotBlank Long id,
            @Parameter(description = "ID of the patient booking the visit", example = "1") @PathVariable @NotBlank Long patientId) {
        log.info("Adding patient with ID: {}, to visit with id: {}",patientId,id);
        Visit visit = visitService.addPatient(id, patientId);
        log.info("Completed successfully: Adding patient with ID: {}, to visit with id: {}",patientId,id);
        return visitMapper.toDto(visit);
    }
}