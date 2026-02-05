package com.example.medical_clinic.DTO.doctor;

public record DoctorCreateRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String specialization
) {
}
