package com.example.medical_clinic.model;

import com.example.medical_clinic.DTO.clinic.ClinicUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clinics")
@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String postalCode;
    private String street;
    private String buildingNumber;

    @ManyToMany
    @JoinTable(
            name = "clinic_doctors",
            joinColumns = @JoinColumn(name = "clinic_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Doctor> doctors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clinic)) return false;
        Clinic clinic = (Clinic) o;
        return id != null &&
                id.equals(clinic.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Clinic update(ClinicUpdateRequest updated) {
        this.setName(updated.name());
        this.setCity(updated.city());
        this.setPostalCode(updated.postalCode());
        this.setStreet(updated.street());
        this.setBuildingNumber(updated.buildingNumber());
        return this;
    }

}
