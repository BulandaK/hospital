package com.example.medical_clinic.DTO.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DoctorCreateRequest(
        String firstName,
        String lastName,
        @Email(message = "Email should be valid")
        String email,
        @Size(min = 4, message = "password should have 4 characters at least")
        String password,
        @NotNull(message = "Specialization is mandatory")
        String specialization
) {
}
