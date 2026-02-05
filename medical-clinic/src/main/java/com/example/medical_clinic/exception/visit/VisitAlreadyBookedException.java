package com.example.medical_clinic.exception.visit;

import com.example.medical_clinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class VisitAlreadyBookedException extends MedicalClinicException {
    public VisitAlreadyBookedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
