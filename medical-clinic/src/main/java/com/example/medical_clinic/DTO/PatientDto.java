package com.example.medical_clinic.DTO;

import java.time.LocalDate;

public record PatientDto(String email,
                         String firstName,
                         String lastName,
                         String phoneNumber,
                         LocalDate birthday) {
}
