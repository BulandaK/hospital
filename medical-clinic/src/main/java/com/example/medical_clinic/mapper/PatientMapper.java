package com.example.medical_clinic.mapper;

import com.example.medical_clinic.DTO.PatientDto;
import com.example.medical_clinic.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDto patientToDto(Patient patient);

    Patient DtoToPatient(PatientDto patientDTO);
}
