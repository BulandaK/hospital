package com.example.medical_clinic.DTO.clinic;

import jakarta.validation.constraints.NotBlank;

public record ClinicRequest(
        @NotBlank(message = "Clinic name is mandatory")
        String name,
        @NotBlank(message = "City is mandatory")
        String city,
        @NotBlank(message = "Postal code name is mandatory")
        String postalCode,
        @NotBlank(message = "Street is mandatory")
        String street,
        @NotBlank(message = "Building number is mandatory")
        String buildingNumber
) {
}
