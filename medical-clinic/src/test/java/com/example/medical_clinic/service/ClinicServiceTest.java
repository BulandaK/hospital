package com.example.medical_clinic.service;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.clinic.ClinicDto;
import com.example.medical_clinic.DTO.clinic.ClinicRequest;
import com.example.medical_clinic.mapper.ClinicMapper;
import com.example.medical_clinic.model.Clinic;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.repository.ClinicRepository;
import com.example.medical_clinic.repository.DoctorRepository;
import com.example.medical_clinic.services.ClinicService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ClinicServiceTest {
    ClinicRepository clinicRepository;
    DoctorRepository doctorRepository;
    ClinicMapper clinicMapper;
    ClinicService clinicService;

    @BeforeEach
    void setup() {
        this.clinicRepository = Mockito.mock(ClinicRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.clinicMapper = Mappers.getMapper(ClinicMapper.class);
        this.clinicService = new ClinicService(clinicRepository, doctorRepository, clinicMapper);
    }

    @Test
    void getAll_DataCorrect_ReturnPageOfClinicDto() {
        //given
        Clinic clinic1 = new Clinic(1L, "clinic one", "cracow", "32-125", "Długa", "19/4", null);
        Clinic clinic2 = new Clinic(2L, "clinic two", "cracow", "21-225", "Klasztorna", "23/1", null);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Clinic> clinics = List.of(clinic1, clinic2);
        Page<Clinic> clinicPage = new PageImpl<>(clinics);
        when(clinicRepository.findAll(pageRequest)).thenReturn(clinicPage);
        //when
        PageResponse<ClinicDto> result = clinicService.getAll(pageRequest);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals("clinic one", result.content().get(0).name()),
                () -> Assertions.assertEquals("clinic two", result.content().get(1).name()),
                () -> Assertions.assertEquals("cracow", result.content().get(0).city()),
                () -> Assertions.assertEquals("32-125", result.content().get(0).postalCode())
        );
    }

    @Test
    void getById_WhenDataCorrect_ThenReturnClinicEntity() {
        //given
        Long id = 1L;
        Clinic clinic = new Clinic(1L, "clinic one", "cracow", "32-125", "Długa", "19/4", null);
        when(clinicRepository.findById(id)).thenReturn(Optional.of(clinic));
        //when
        Clinic result = clinicService.getById(id);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(1l, result.getId()),
                () -> Assertions.assertEquals("clinic one", result.getName()),
                () -> Assertions.assertEquals("cracow", result.getCity())

        );
    }

    @Test
    void add_WhenDataCorrect_ThenReturnClinicEntity() {
        //given
        ClinicRequest clinicRequest = new ClinicRequest("clinic one", "Cracow", "32-125", "Długa", "19/4");
        Clinic clinic = new Clinic(1L, clinicRequest.name(), clinicRequest.city(), clinicRequest.postalCode(), clinicRequest.street(), clinicRequest.buildingNumber(), null);
        when(clinicRepository.save(any(Clinic.class))).thenReturn(clinic);
        //when
        Clinic result = clinicService.add(clinicRequest);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals("clinic one", result.getName()),
                () -> Assertions.assertEquals("Cracow", result.getCity()),
                () -> Assertions.assertEquals("32-125", result.getPostalCode()),
                () -> Assertions.assertEquals("Długa", result.getStreet())
        );
    }

    @Test
    void updateById_WhenDataCorrect_ThenReturnClinicEntity() {
        Long clinicId = 1L;
        ClinicRequest request = new ClinicRequest("clinic one","cracow","32-211","długa","19");
        Clinic existingClinic = new Clinic();
        existingClinic.setId(clinicId);
        when(clinicRepository.findById(clinicId)).thenReturn(Optional.of(existingClinic));
        // when
        Clinic result = clinicService.updateById(clinicId, request);
        // then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(clinicId, result.getId()),
                () -> Assertions.assertEquals(request.name(), result.getName()),
                () -> Assertions.assertEquals(request.city(), result.getCity()),
                () -> Assertions.assertEquals(request.postalCode(), result.getPostalCode())
        );
    }

    @Test
    void assignDoctor_WhenDataCorrect_ThenReturnClinicEntity() {
        //given
        Long clinicId = 1L;
        Long doctorId = 1L;
        Clinic clinic = new Clinic(1L, "clinic one", "cracow", "32-125", "Długa", "19/4", Collections.emptySet());
        Doctor doctor = new Doctor(1L, "cardiology", "jan@gmail.com", "password", null, null, null, null);
        when(clinicRepository.findById(clinicId)).thenReturn(Optional.of(clinic));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        //when
        Clinic result = clinicService.assignDoctor(1L, 1L);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertTrue(result.getDoctors().contains(doctor), "Doctor should be in set of clinic's doctors"),
                () -> Assertions.assertEquals(1, result.getDoctors().size())
        );
    }
}
