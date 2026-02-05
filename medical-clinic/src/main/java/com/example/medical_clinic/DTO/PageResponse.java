package com.example.medical_clinic.DTO;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        Long totalElements,
        int totalPages
) {
}
