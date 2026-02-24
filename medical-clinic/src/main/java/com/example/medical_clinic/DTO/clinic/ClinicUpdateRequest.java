package com.example.medical_clinic.DTO.clinic;

import jakarta.validation.constraints.NotBlank;

public record ClinicUpdateRequest(
        @NotBlank
        String name,
        @NotBlank
        String city,
        @NotBlank
        String postalCode,
        @NotBlank
        String street,
        @NotBlank
        String buildingNumber) {
}
