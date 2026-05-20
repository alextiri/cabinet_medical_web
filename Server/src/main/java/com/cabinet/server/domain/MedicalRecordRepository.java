package com.cabinet.server.domain;

import java.util.List;

public interface MedicalRecordRepository {
    List<MedicalRecord> findByDoctorId(Integer doctorId);
    List<MedicalRecord> findAll();

    MedicalRecord save(MedicalRecord medicalRecord);
    MedicalRecord findById(Integer id);
    MedicalRecord findByPatientId(Integer patientId);
}