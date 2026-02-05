package com.example.medical_clinic.exception.clinic;

import com.example.medical_clinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class ClinicNotFoundException extends MedicalClinicException {
    public ClinicNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
