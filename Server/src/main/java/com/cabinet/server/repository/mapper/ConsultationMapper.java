package com.cabinet.server.repository.mapper;

import com.cabinet.server.domain.Consultation;
import com.cabinet.server.repository.entity.ConsultationEntity;
import com.cabinet.server.repository.entity.MedicalRecordEntity;

public class ConsultationMapper {
    public static Consultation toDomain(ConsultationEntity entity) {
        Consultation consultation = new Consultation();

        consultation.setId(entity.getId());
        consultation.setDoctorId(entity.getDoctorId());

        if (entity.getMedicalRecord() != null) {
            consultation.setMedicalRecordId(
                    entity.getMedicalRecord().getId()
            );
        }

        consultation.setDate(entity.getDate());
        consultation.setSymptoms(entity.getSymptoms());
        consultation.setDiagnosis(entity.getDiagnosis());
        consultation.setTreatment(entity.getTreatment());
        consultation.setObservations(entity.getObservations());

        return consultation;
    }

    public static ConsultationEntity toEntity(Consultation consultation) {
        ConsultationEntity entity = new ConsultationEntity();

        entity.setId(consultation.getId());
        entity.setDoctorId(consultation.getDoctorId());

        if (consultation.getMedicalRecordId() != null) {
            MedicalRecordEntity medicalRecord = new MedicalRecordEntity();
            medicalRecord.setId(consultation.getMedicalRecordId());

            entity.setMedicalRecord(medicalRecord);
        }

        entity.setDate(consultation.getDate());
        entity.setSymptoms(consultation.getSymptoms());
        entity.setDiagnosis(consultation.getDiagnosis());
        entity.setTreatment(consultation.getTreatment());
        entity.setObservations(consultation.getObservations());

        return entity;
    }
}