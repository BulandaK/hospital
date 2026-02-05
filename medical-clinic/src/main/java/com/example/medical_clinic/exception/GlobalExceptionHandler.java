package com.example.medical_clinic.exception;

import com.example.medical_clinic.DTO.ErrorResponse;
import com.example.medical_clinic.exception.clinic.ClinicNotFoundException;
import com.example.medical_clinic.exception.doctor.DoctorAlreadyExistsException;
import com.example.medical_clinic.exception.doctor.DoctorNotFoundException;
import com.example.medical_clinic.exception.patient.PatientAlreadyExistsException;
import com.example.medical_clinic.exception.patient.PatientNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicalClinicException.class)
    public ResponseEntity<ErrorResponse> handleMedicalClinicException(MedicalClinicException ex, HttpServletRequest request) {
        return createResponse(ex, request, ex.getHttpStatus());
    }

    private ResponseEntity<ErrorResponse> createResponse(Exception ex, HttpServletRequest request, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, status);
    }
}
