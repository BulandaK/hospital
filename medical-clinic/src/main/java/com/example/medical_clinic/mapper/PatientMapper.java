package com.example.medical_clinic.mapper;

import com.example.medical_clinic.DTO.patient.PatientDto;
import com.example.medical_clinic.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    PatientDto patientToDto(Patient patient);

    Patient DtoToPatient(PatientDto patientDTO);
}
