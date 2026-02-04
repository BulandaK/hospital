package com.example.medical_clinic.DTO.visit;

import java.time.LocalDateTime;

public record VisitDto(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long doctorId,
        String doctorFullName,
        Long patientId,
        String patientFullName
) {}
