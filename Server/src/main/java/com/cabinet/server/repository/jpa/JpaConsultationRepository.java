package com.cabinet.server.repository.jpa;

import com.cabinet.server.domain.Consultation;
import com.cabinet.server.domain.ConsultationRepository;
import com.cabinet.server.repository.entity.ConsultationEntity;
import com.cabinet.server.repository.mapper.ConsultationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaConsultationRepository implements ConsultationRepository {
    private final SpringDataConsultationRepository repository;

    public JpaConsultationRepository(SpringDataConsultationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Consultation> findByPatientId(Integer patientId) {
        return repository.findByMedicalRecord_Patient_Id(patientId)
                .stream()
                .map(ConsultationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Consultation> findByDoctorId(Integer doctorId) {
        return repository.findByDoctorId(doctorId)
                .stream()
                .map(ConsultationMapper::toDomain)
                .toList();
    }

    @Override
    public Consultation save(Consultation consultation) {
        ConsultationEntity entity = ConsultationMapper.toEntity(consultation);
        ConsultationEntity saved = repository.save(entity);

        return ConsultationMapper.toDomain(saved);
    }

    @Override
    public List<Consultation> findAll() {
        return repository.findAll()
                .stream()
                .map(ConsultationMapper::toDomain)
                .toList();
    }
}