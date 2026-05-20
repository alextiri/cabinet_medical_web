package com.cabinet.server.repository.jpa;

import com.cabinet.server.repository.entity.MedicalRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataMedicalRecordRepository extends JpaRepository<MedicalRecordEntity,Integer> {
    MedicalRecordEntity findByPatient_Id(Integer patientId);
    MedicalRecordEntity findByPatientId(Integer patientId);
}
