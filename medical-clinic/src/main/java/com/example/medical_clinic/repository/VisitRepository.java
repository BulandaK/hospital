package com.example.medical_clinic.repository;

import com.example.medical_clinic.model.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findAllByPatientId(Long patientId);

    @Query("SELECT v FROM Visit v WHERE " +
            "(CAST(:doctorId AS long) IS NULL OR v.doctor.id = :doctorId) AND " +
            "(CAST(:startRange AS LocalDateTime) IS NULL OR v.startTime >= :startRange) AND " +
            "(CAST(:endRange AS LocalDateTime) IS NULL OR v.startTime <= :endRange) AND " +
            "(:spec IS NULL OR v.doctor.specialization = :spec) AND " +
            "(CAST(:now AS LocalDateTime) IS NULL OR v.patient IS NULL) AND " +
            "(CAST(:now AS LocalDateTime) IS NULL OR v.startTime >= :now)")
    Page<Visit> findAvailableVisits(
            Pageable pageable,
            @Param("doctorId") Long doctorId,
            @Param("startRange") LocalDateTime startRange,
            @Param("endRange") LocalDateTime endRange,
            @Param("spec") String spec,
            @Param("now") LocalDateTime now
    );

    List<Visit> findByDoctorSpecializationAndStartTimeBetween(
            String specialization,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("SELECT COUNT(v) > 0 FROM Visit v " +
            "WHERE v.doctor.id = :doctorId " +
            "AND :newStart < v.endTime " +
            "AND :newEnd > v.startTime")
    boolean existsOverlappingVisit(@Param("doctorId") Long doctorId,
                                   @Param("newStart") LocalDateTime newStart,
                                   @Param("newEnd") LocalDateTime newEnd);
}
