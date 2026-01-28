package com.example.medical_clinic.exception;

public class ClinicNotFoundException extends RuntimeException {
    public ClinicNotFoundException(String message) {
        super(message);
    }
}
