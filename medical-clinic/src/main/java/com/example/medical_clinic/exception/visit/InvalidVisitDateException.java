package com.example.medical_clinic.exception.visit;

import com.example.medical_clinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class InvalidVisitDateException extends MedicalClinicException {
    public InvalidVisitDateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
