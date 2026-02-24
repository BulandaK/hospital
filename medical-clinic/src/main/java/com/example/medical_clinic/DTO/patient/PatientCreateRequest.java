package com.example.medical_clinic.DTO.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientCreateRequest(
        @Email(message = "Email should be valid")
        String email,
        @Size(min = 4, message = "password should be at least 4 characters long")
        String password,
        String idCardNo,
        String firstName,
        String lastName,
        @NotBlank
        String phoneNumber,
        LocalDate birthday
) {
}
