package com.example.medical_clinic.service;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.doctor.DoctorCreateRequest;
import com.example.medical_clinic.DTO.doctor.DoctorDto;
import com.example.medical_clinic.DTO.doctor.DoctorUpdateRequest;
import com.example.medical_clinic.mapper.DoctorMapper;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.model.User;
import com.example.medical_clinic.repository.DoctorRepository;
import com.example.medical_clinic.services.DoctorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {
    DoctorRepository doctorRepository;
    DoctorMapper doctorMapper;
    DoctorService doctorService;

    @BeforeEach
    void setup() {
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.doctorService = new DoctorService(doctorRepository, doctorMapper);
    }

    @Test
    void getAll_DataCorrect_ReturnListOfDoctorDtos() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Doctor doctor1 = new Doctor(1L, "cardiology", "jan@gmail.com", "abc", null, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
        Doctor doctor2 = new Doctor(2L, "proctology", "damian@gmail.com", "def", null, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
        List<Doctor> doctorsList = List.of(doctor1, doctor2);
        Page<Doctor> doctorPage = new PageImpl<>(doctorsList);
        when(doctorRepository.findAll(pageRequest)).thenReturn(doctorPage);
        //when
        PageResponse<DoctorDto> result = doctorService.getAll(pageRequest);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(2, result.content().size()),
                () -> Assertions.assertEquals(1L, result.content().get(0).id()),
                () -> Assertions.assertEquals(2L, result.content().get(1).id()),
                () -> Assertions.assertEquals("cardiology", result.content().get(0).specialization()),
                () -> Assertions.assertEquals("jan@gmail.com", result.content().get(0).email()),
                () -> Assertions.assertEquals("proctology", result.content().get(1).specialization()),
                () -> Assertions.assertEquals("damian@gmail.com", result.content().get(1).email())
        );
    }

    @Test
    void getByEmail_DataCorrect_ReturnDoctorEntity() {
        //given
        String email = "jan@gmail.com";
        Doctor doctor = new Doctor(1L, "cardiology", "jan@gmail.com", "abc", null, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
        when(doctorRepository.getByEmail(email)).thenReturn(Optional.of(doctor));
        //when
        Doctor result = doctorService.getByEmail(email);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(1L, result.getId()),
                () -> Assertions.assertEquals("jan@gmail.com", result.getEmail())
        );
    }

    @Test
    void getById_DataCorrect_ReturnDoctorEntity() {
        //given
        Long id = 1L;
        Doctor doctor = new Doctor(1L, "cardiology", "jan@gmail.com", "abc", null, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        //when
        Doctor result = doctorService.getById(id);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(1L, result.getId()),
                () -> Assertions.assertEquals("jan@gmail.com", result.getEmail())
        );
    }

    @Test
    void add_DataCorrect_ReturnDoctorEntity() {
        //given
        DoctorCreateRequest request = new DoctorCreateRequest("Jan", "Kowalczyk", "jankowalczyk@gmail.com", "password", "cardiology");
        when(doctorRepository.existsByEmail(request.email())).thenReturn(false);
        User user = new User(1L, request.firstName(), request.lastName(), null, null);
        Doctor doctor = new Doctor(1L, request.specialization(), request.email(), request.password(), user, null, null, null);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        //when
        Doctor result = doctorService.add(request);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(1L, result.getId()),
                () -> Assertions.assertEquals("jankowalczyk@gmail.com", result.getEmail()),
                () -> Assertions.assertEquals("Jan", result.getUser().getFirstName()),
                () -> Assertions.assertEquals("Kowalczyk", result.getUser().getLastName())

        );
    }

    @Test
    void updateEmail_DataCorrect_ReturnDoctorEntity() {
        //given
        String email = "jankowalczyk@gmail.com";
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest("cardiology",email,"Jan","Kowalczyk");
        User user = new User(1L,"Kamil","Bulanda",null,null);
        Doctor doctor = new Doctor(1L,"proctology",email,"password",user,null,null,null);
        when(doctorRepository.getByEmail(email)).thenReturn(Optional.of(doctor));
        //when
        Doctor result = doctorService.updateByEmail(email,updateRequest);
        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(updateRequest.specialization(),result.getSpecialization()),
                () -> Assertions.assertEquals(updateRequest.firstName(),result.getUser().getFirstName()),
                () -> Assertions.assertEquals(updateRequest.lastName(),result.getUser().getLastName())
        );
    }
}
