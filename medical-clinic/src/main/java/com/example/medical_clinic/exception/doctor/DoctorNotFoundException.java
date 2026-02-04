package com.example.medical_clinic.exception.doctor;

import com.example.medical_clinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends MedicalClinicException {
    public DoctorNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}