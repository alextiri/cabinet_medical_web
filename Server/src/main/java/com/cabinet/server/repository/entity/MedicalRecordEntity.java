package com.cabinet.server.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
public class MedicalRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @JsonIgnore
    @OneToMany(mappedBy = "medicalRecord")
    private List<ConsultationEntity> consultations;
}
