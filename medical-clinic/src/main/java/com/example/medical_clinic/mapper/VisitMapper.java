package com.example.medical_clinic.mapper;

import com.example.medical_clinic.DTO.visit.VisitDto;
import com.example.medical_clinic.DTO.visit.VisitRequest;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.model.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "doctorFullName", source = "doctor")
    @Mapping(target = "patientFullName", source = "patient")
    VisitDto toDto(Visit visit);

    default String mapDoctorLastName(Doctor doctor) {
        if (doctor == null || doctor.getUser() == null) {
            return null;
        }
        return doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName();
    }

    default String mapPatientLastName(Patient patient) {
        if (patient == null || patient.getUser() == null) {
            return null;
        }
        return patient.getUser().getFirstName() + " " + patient.getUser().getLastName();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Visit toEntity(VisitRequest request);
}
