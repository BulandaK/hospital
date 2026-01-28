package com.example.medical_clinic.DTO.clinic;

public record ClinicRequest(
        String name,
        String city,
        String postalCode,
        String street,
        String buildingNumber
) {
}
