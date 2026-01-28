package com.example.medical_clinic.model;

import com.example.medical_clinic.DTO.patient.PatientUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String idCardNo;
    private String phoneNumber;
    private LocalDate birthday;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    @ToString.Exclude
    private User user;
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @ToString.Exclude
    Doctor doctor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return id != null &&
                id.equals(patient.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Patient update(PatientUpdateRequest updated) {
        this.setPhoneNumber(updated.phoneNumber());
        this.setBirthday(updated.birthday());
        if (this.user != null) {
            this.user.setFirstName(updated.firstName());
            this.user.setLastName(updated.lastName());
        }
        return this;
    }
}
