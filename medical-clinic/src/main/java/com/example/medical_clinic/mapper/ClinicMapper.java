package com.example.medical_clinic.mapper;

import com.example.medical_clinic.DTO.clinic.ClinicDto;
import com.example.medical_clinic.DTO.clinic.ClinicRequest;
import com.example.medical_clinic.model.Clinic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
    ClinicDto toDto(Clinic clinic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    Clinic toEntity(ClinicRequest request);
}
