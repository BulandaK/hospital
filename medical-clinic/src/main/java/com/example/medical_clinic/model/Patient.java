package com.example.medical_clinic.model;

import com.example.medical_clinic.DTO.PatientUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
@Entity
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;

    public Patient update(PatientUpdateRequest updated) {
        this.setFirstName(updated.firstName());
        this.setLastName(updated.lastName());
        this.setPhoneNumber(updated.phoneNumber());
        this.setBirthday(updated.birthday());
        return this;
    }
}
