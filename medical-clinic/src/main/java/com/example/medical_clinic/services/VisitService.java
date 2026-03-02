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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
                .orElseThrow(() -> {
                    log.warn("Doctor assignment failed: doctor with id: {} not found", request.doctorId());
                    return new DoctorNotFoundException("Doctor not found");
                });

        Visit visit = new Visit(null, request.startTime(), request.endTime(), doctor, null);
        return visitRepository.save(visit);
    }

    @Transactional
    public Visit addPatient(Long visitId, Long patientId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> {
                    log.warn("Visit assignment failed: Visit ID: {} not found", visitId);
                    return new VisitNotFoundException("Visit not found");
                });
        validatePatientDoubleBooking(visit);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    log.warn("Visit assignment failed: Patient ID: {} not found", patientId);
                    return new PatientNotFoundException("Patient not found");
                });

        visit.setPatient(patient);
        return visit;
    }

    private void validateTimeSlot(LocalDateTime date) {
        if (date.getMinute() % 15 != 0 || date.getSecond() != 0) {
            log.warn("Validation failed: Invalid time interval for visit. Received date: {}", date);
            throw new InvalidVisitTimeException("Wrong time interval for visit");
        }
        if (date.isBefore(LocalDateTime.now())) {
            log.warn("Validation failed: Attempt to book visit in the past. Received date: {}", date);
            throw new InvalidVisitDateException("Can't book visit in the past");
        }
    }

    private void verifyDoctorNoCollisions(Long doctorId, LocalDateTime start, LocalDateTime end) {
        if (visitRepository.existsOverlappingVisit(doctorId, start, end)) {
            log.warn("Doctor collision detected: doctorId {}, range {} - {}", doctorId, start, end);
            throw new VisitAlreadyExistsException("Doctor has another visit that overlaps with this time range");
        }
    }

    private void validatePatientDoubleBooking(Visit visit) {
        if (visit.getPatient() != null) {
            log.error("Attempt to book an already occupied visit ID: {} by patient ID: {}",
                    visit.getId(), visit.getPatient().getId());
            throw new VisitAlreadyBookedException("Visit is already booked by another patient");
        }
    }

    private void validateIfStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            log.warn("Invalid time range: Start {} is not before End {}", start, end);
            throw new InvalidVisitTimeException("Can't start time be after end time");
        }
    }
}
