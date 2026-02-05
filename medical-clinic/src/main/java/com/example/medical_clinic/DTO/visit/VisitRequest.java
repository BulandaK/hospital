package com.example.medical_clinic.DTO.visit;

import java.time.LocalDateTime;

public record VisitRequest(
LocalDateTime startTime,
LocalDateTime endTime,
Long doctorId
) {
}
