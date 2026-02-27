package com.example.medical_clinic.DTO.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DoctorCreateRequest(
        String firstName,
        String lastName,
        @Email(message = "Email should be valid")
        String email,
        @Size(min = 4, message = "password should have 4 characters at least")
        String password,
        @NotBlank(message = "Specialization is mandatory")
        String specialization
) {
}
