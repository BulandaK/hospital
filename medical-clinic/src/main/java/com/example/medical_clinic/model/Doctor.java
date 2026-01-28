package com.example.medical_clinic.model;


import com.example.medical_clinic.DTO.doctor.DoctorUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctors")
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String specialization;
    private String email;
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
    @OneToMany(mappedBy = "doctor")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Patient> patients;
    @ManyToMany(mappedBy = "doctors")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Clinic> clinics;

    public Doctor update(DoctorUpdateRequest updated) {
        this.setSpecialization(updated.specialization());
        this.setEmail(updated.email());
        if (this.user != null) {
            this.user.setFirstName(updated.firstName());
            this.user.setLastName(updated.lastName());
        }
        return this;
    }
}
