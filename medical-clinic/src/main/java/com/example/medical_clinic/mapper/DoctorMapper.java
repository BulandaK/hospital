package com.example.medical_clinic.mapper;

import com.example.medical_clinic.DTO.doctor.DoctorDto;
import com.example.medical_clinic.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    DoctorDto toDto(Doctor doctor);

    Doctor dtoToDoctor(DoctorDto dto);
}
