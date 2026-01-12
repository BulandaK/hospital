package com.example.medical_clinic.repository;

import com.example.medical_clinic.model.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> getByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Patient p SET p.password = :password WHERE p.email = :email")
//    void updatePassword(String email, String password);

}
