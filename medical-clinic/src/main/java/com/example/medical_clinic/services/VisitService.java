package com.example.medical_clinic.services;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.visit.VisitDto;
import com.example.medical_clinic.DTO.visit.VisitRequest;
import com.example.medical_clinic.exception.doctor.DoctorNotFoundException;
import com.example.medical_clinic.exception.patient.PatientNotFoundException;
import com.example.medical_clinic.exception.visit.*;
import com.example.medical_clinic.mapper.VisitMapper;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.model.Visit;
import com.example.medical_clinic.repository.DoctorRepository;
import com.example.medical_clinic.repository.PatientRepository;
import com.example.medical_clinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PageResponse<VisitDto> getAllVisits(Pageable pageable) {
        Page<Visit> page = visitRepository.findAll(pageable);
        List<VisitDto> content = page.stream()
                .map(visitMapper::toDto)
                .toList();

        return new PageResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public List<VisitDto> getPatientVisits(Long patientId) {
        return visitRepository.findAllByPatientId(patientId).stream()
                .map(visitMapper::toDto)
                .toList();
    }

    @Transactional
    public Visit create(VisitRequest request) {
        validateTimeSlot(request.startTime());
        validateTimeSlot(request.endTime());
        validateIfStartBeforeEnd(request.startTime(), request.endTime());
        verifyDoctorNoCollisions(request.doctorId(), request.startTime(), request.endTime());

        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        Visit visit = new Visit(null, request.startTime(), request.endTime(), doctor, null);
        return visitRepository.save(visit);
    }

    @Transactional
    public Visit addPatient(Long visitId, Long patientId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        validatePatientDoubleBooking(visit);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        visit.setPatient(patient);
        return visit;
    }

    private void validateTimeSlot(LocalDateTime date) {
        if (date.getMinute() % 15 != 0 || date.getSecond() != 0) {
            throw new InvalidVisitTimeException("Wrong time interval for visit");
        }
        if (date.isBefore(LocalDateTime.now())) {
            throw new InvalidVisitDateException("Can't book visit in the past");
        }
    }

    private void verifyDoctorNoCollisions(Long doctorId, LocalDateTime start, LocalDateTime end) {
        if (visitRepository.existsOverlappingVisit(doctorId, start, end)) {
            throw new VisitAlreadyExistsException("Doctor has another visit that overlaps with this time range");
        }
    }

    private void validatePatientDoubleBooking(Visit visit) {
        if (visit.getPatient() != null) {
            throw new VisitAlreadyBookedException("Visit is already booked by another patient");
        }
    }

    private void validateIfStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new InvalidVisitTimeException("Can't start time be after end time");
        }
    }
}
