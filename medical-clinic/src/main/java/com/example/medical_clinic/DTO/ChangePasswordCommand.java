package com.example.medical_clinic.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ChangePasswordCommand(
        @Size(min = 4, message = "password should be at least 4 characters long")
        String password) {
}
