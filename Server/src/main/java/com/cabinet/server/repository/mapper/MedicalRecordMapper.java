package com.cabinet.server.repository.mapper;

import com.cabinet.server.domain.MedicalRecord;
import com.cabinet.server.repository.entity.MedicalRecordEntity;
import com.cabinet.server.repository.entity.PatientEntity;

public class MedicalRecordMapper {
    public static MedicalRecord toDomain(MedicalRecordEntity entity) {
        MedicalRecord medicalRecord = new MedicalRecord();

        medicalRecord.setId(entity.getId());

        if (entity.getPatient() != null) {
            medicalRecord.setPatientId(entity.getPatient().getId());
        }

        return medicalRecord;
    }

    public static MedicalRecordEntity toEntity(MedicalRecord medicalRecord) {
        MedicalRecordEntity entity = new MedicalRecordEntity();

        entity.setId(medicalRecord.getId());

        if (medicalRecord.getPatientId() != null) {
            PatientEntity patient = new PatientEntity();
            patient.setId(medicalRecord.getPatientId());

            entity.setPatient(patient);
        }

        return entity;
    }
}