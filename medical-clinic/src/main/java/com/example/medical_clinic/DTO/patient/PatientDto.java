package com.example.medical_clinic.DTO.patient;

import java.time.LocalDate;

public record PatientDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthday) {
}
