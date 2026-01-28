package com.example.medical_clinic.DTO.patient;

import java.time.LocalDate;

public record PatientCreateRequest(
        String email,
        String password,
        String idCardNo,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthday
) {
}
