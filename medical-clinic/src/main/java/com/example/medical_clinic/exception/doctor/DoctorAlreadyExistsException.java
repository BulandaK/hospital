package com.example.medical_clinic.exception.doctor;

import com.example.medical_clinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;

public class DoctorAlreadyExistsException extends MedicalClinicException {
    public DoctorAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
