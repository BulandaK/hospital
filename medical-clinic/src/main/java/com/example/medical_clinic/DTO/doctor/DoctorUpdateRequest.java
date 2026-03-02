package com.example.medical_clinic.DTO.doctor;

import jakarta.validation.constraints.Email;

public record DoctorUpdateRequest(
        String specialization,
        @Email(message = "Email should be valid")
        String email,
        String firstName,
        String lastName) {
}
