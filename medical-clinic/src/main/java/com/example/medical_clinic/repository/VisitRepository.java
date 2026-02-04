package com.example.medical_clinic.repository;

import com.example.medical_clinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit,Long> {

    List<Visit> findAllByPatientId(Long patientId);
    @Query("SELECT COUNT(v) > 0 FROM Visit v " +
            "WHERE v.doctor.id = :doctorId " +
            "AND :newStart < v.endTime " +
            "AND :newEnd > v.startTime")
    boolean existsOverlappingVisit(@Param("doctorId") Long doctorId,
                                   @Param("newStart") LocalDateTime newStart,
                                   @Param("newEnd") LocalDateTime newEnd);
}
