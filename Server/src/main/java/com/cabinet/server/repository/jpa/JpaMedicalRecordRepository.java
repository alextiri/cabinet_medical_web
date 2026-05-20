package com.cabinet.server.repository.jpa;

import com.cabinet.server.domain.MedicalRecord;
import com.cabinet.server.domain.MedicalRecordRepository;
import com.cabinet.server.repository.entity.MedicalRecordEntity;
import com.cabinet.server.repository.mapper.MedicalRecordMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaMedicalRecordRepository implements MedicalRecordRepository {
    private final SpringDataMedicalRecordRepository repository;
    private final SpringDataPatientRepository patientRepository;

    public JpaMedicalRecordRepository(SpringDataMedicalRecordRepository repository, SpringDataPatientRepository patientRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<MedicalRecord> findByDoctorId(Integer doctorId) {
        return patientRepository.findPatientsByDoctors_Id(doctorId)
                .stream()
                .map(patient -> repository.findByPatient_Id(patient.getId()))
                .map(MedicalRecordMapper::toDomain)
                .toList();
    }

    @Override
    public List<MedicalRecord> findAll() {
        return repository.findAll()
                .stream()
                .map(MedicalRecordMapper::toDomain)
                .toList();
    }

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        MedicalRecordEntity entity = MedicalRecordMapper.toEntity(medicalRecord);
        MedicalRecordEntity saved = repository.save(entity);

        return MedicalRecordMapper.toDomain(saved);
    }

    @Override
    public MedicalRecord findById(Integer id) {
        MedicalRecordEntity entity = repository.findById(id).orElseThrow();

        return MedicalRecordMapper.toDomain(entity);
    }

    @Override
    public MedicalRecord findByPatientId(Integer patientId) {
        MedicalRecordEntity entity = repository.findByPatientId(patientId);

        if (entity == null) {
            return null;
        }

        return MedicalRecordMapper.toDomain(entity);
    }
}