package com.example.medical_clinic.exception.clinic;

import com.example.medical_clinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class ClinicAlreadyExistsException extends MedicalClinicException {
    public ClinicAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
