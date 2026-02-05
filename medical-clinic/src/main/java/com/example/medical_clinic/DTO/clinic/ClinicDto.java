package com.example.medical_clinic.DTO.clinic;

public record ClinicDto(
        Long id,
        String name,
        String city,
        String postalCode,
        String street,
        String buildingNumber
) {
}
