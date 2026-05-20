package com.cabinet.server.service;

import com.cabinet.server.domain.MedicalRecord;
import com.cabinet.server.domain.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord findById(Integer id) {
        return medicalRecordRepository.findById(id);
    }

    public MedicalRecord getByPatientId(Integer patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public List<MedicalRecord> getMedicalRecordsByDoctor(Integer doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId);
    }
}