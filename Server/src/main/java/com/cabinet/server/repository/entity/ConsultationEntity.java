package com.cabinet.server.repository.entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
@Getter
@Setter
public class ConsultationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecordEntity medicalRecord;

    @Column(name = "doctor_id")
    private Integer doctorId;

    private LocalDateTime date;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private String observations;
}
