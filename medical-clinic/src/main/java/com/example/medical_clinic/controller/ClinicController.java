package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.clinic.ClinicDto;
import com.example.medical_clinic.DTO.clinic.ClinicRequest;
import com.example.medical_clinic.DTO.clinic.ClinicUpdateRequest;
import com.example.medical_clinic.mapper.ClinicMapper;
import com.example.medical_clinic.model.Clinic;
import com.example.medical_clinic.services.ClinicService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/clinics")
@Slf4j
@Tag(name = "Clinic Management", description = "Endpoints for managing medical facilities and staff assignments")
public class ClinicController {
    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;

    @GetMapping
    @Operation(
            summary = "Get all clinics",
            description = "Retrieves a paginated list of all registered medical clinics."
    )
    public PageResponse<ClinicDto> getAll(@ParameterObject Pageable pageable) {
        log.info("Getting all clinics");
        return clinicService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get clinic by ID",
            description = "Returns detailed information about a specific clinic using its numeric identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Clinic found successfully"),
                    @ApiResponse(responseCode = "404", description = "Clinic not found with the provided ID", content = @Content)
            }
    )
    public ClinicDto getById(
            @Parameter(description = "The unique ID of the clinic", example = "1")
            @PathVariable @NotBlank Long id) {
        log.info("Getting clinic with id: {}",id);
        Clinic clinic = clinicService.getById(id);
        return clinicMapper.toDto(clinic);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new clinic",
            description = "Registers a new medical facility in the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Clinic created successfully"),
                    @ApiResponse(responseCode = "409", description = "Clinic already exists", content = @Content)
            }
    )
    public ClinicDto create(@RequestBody @Valid ClinicRequest request) {
        log.info("Creating clinic: {}",request.name());
        Clinic clinic = clinicService.add(request);
        log.info("Clinic: {} created successfully",request.name());
        return clinicMapper.toDto(clinic);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete clinic",
            description = "Permanently removes a clinic from the system based on its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Clinic deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Clinic not found with the provided ID", content = @Content)
            }
    )
    public void delete(@PathVariable @NotBlank Long id) {
        log.info("Deleting clinic with id: {}",id);
        clinicService.removeById(id);
        log.info("Clinic with id: {}, deleted successfully",id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update clinic",
            description = "Updates the information of an existing clinic facility.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Clinic updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Clinic not found with the provided ID", content = @Content)
            }
    )
    public ClinicDto update(
            @PathVariable @NotBlank Long id,
            @RequestBody @Valid ClinicUpdateRequest request) {
        log.info("Updating clinic with id: {}",id);
        Clinic clinic = clinicService.updateById(id, request);
        log.info("Clinic with id: {}, updated successfully",id);
        return clinicMapper.toDto(clinic);
    }

    @PatchMapping("/{id}/doctor/{doctorId}")
    @Operation(
            summary = "Assign doctor to clinic",
            description = "Establishes a connection between a doctor and a specific medical clinic.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctor assigned successfully"),
                    @ApiResponse(responseCode = "404", description = "Clinic or Doctor not found", content = @Content)
            }
    )
    public ClinicDto assignDoctor(
            @Parameter(description = "ID of the clinic facility", example = "1") @PathVariable Long id,
            @Parameter(description = "ID of the doctor to be assigned", example = "5") @PathVariable Long doctorId) {
        log.info("Assigning doctor with id: {} to clinic with id: {}",doctorId,id);
        Clinic clinic = clinicService.assignDoctor(id, doctorId);
        log.info("Completed: assigning doctor with id: {} to clinic with id: {}",doctorId,id);
        return clinicMapper.toDto(clinic);
    }
}