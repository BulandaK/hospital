package com.example.medical_clinic.DTO.doctor;

public record DoctorUpdateRequest(String specialization,
                                  String email,
                                  String firstName,
                                  String lastName) {
}
