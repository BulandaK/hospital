package com.example.medical_clinic.service;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.visit.VisitDto;
import com.example.medical_clinic.DTO.visit.VisitRequest;
import com.example.medical_clinic.mapper.VisitMapper;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.model.Visit;
import com.example.medical_clinic.repository.DoctorRepository;
import com.example.medical_clinic.repository.PatientRepository;
import com.example.medical_clinic.repository.VisitRepository;
import com.example.medical_clinic.services.VisitService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class VisitServiceTest {
    VisitRepository visitRepository;
    VisitMapper visitMapper;
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
    VisitService visitService;

    @BeforeEach
    void setup() {
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        this.visitService = new VisitService(visitRepository, visitMapper, patientRepository, doctorRepository);
    }

    @Test
    void getAllVisits_WhenDataCorrect_ThenReturnPageVisitDto() {
        // given
        Pageable pageable = PageRequest.of(0, 5);
        Visit visit1 = new Visit();
        visit1.setId(1L);
        Visit visit2 = new Visit();
        visit2.setId(2L);
        List<Visit> visits = List.of(visit1, visit2);
        Page<Visit> visitPage = new PageImpl<>(visits, pageable, visits.size());
        when(visitRepository.findAll(pageable)).thenReturn(visitPage);
        // when
        PageResponse<VisitDto> result = visitService.getAllVisits(pageable);
        // then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(2, result.content().size())
        );
    }

    @Test
    void getPatientVisits_WhenDataCorrect_ThenReturnListVisitDto() {
        // given
        Long patientId = 1L;
        Visit visit1 = new Visit();
        visit1.setId(101L);
        Visit visit2 = new Visit();
        visit2.setId(102L);
        List<Visit> patientVisits = List.of(visit1, visit2);
        when(visitRepository.findAllByPatientId(patientId)).thenReturn(patientVisits);
        // when
        List<VisitDto> result = visitService.getPatientVisits(patientId);
        // then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(2, result.size()),
                () -> Assertions.assertEquals(101L, result.get(0).id()),
                () -> Assertions.assertEquals(102L, result.get(1).id())
        );
    }

    @Test
    void create_WhenDataCorrect_ThenReturnVisitEntity() {
        //given
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        VisitRequest request = new VisitRequest(LocalDateTime.of(2055, 2, 20, 10, 0), LocalDateTime.of(2055, 2, 20, 11, 0), doctor.getId());
        Visit visit = new Visit(1L, request.startTime(), request.endTime(), doctor, null);
        when(doctorRepository.findById(request.doctorId())).thenReturn(Optional.of(doctor));
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);
        //when
        Visit result = visitService.create(request);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(doctor, result.getDoctor()),
                () -> Assertions.assertNull(result.getPatient()),
                () -> Assertions.assertEquals(request.startTime(), result.getStartTime()),
                () -> Assertions.assertEquals(request.endTime(), result.getEndTime())
        );
    }

}
