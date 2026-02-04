package com.example.medical_clinic.exception.visit;

import com.example.medical_clinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class InvalidVisitTimeException extends MedicalClinicException {
    public InvalidVisitTimeException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
