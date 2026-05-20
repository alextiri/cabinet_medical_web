package com.cabinet.server.repository.jpa;

import com.cabinet.server.domain.Patient;
import com.cabinet.server.domain.PatientRepository;
import com.cabinet.server.repository.entity.DoctorEntity;
import com.cabinet.server.repository.entity.PatientEntity;
import com.cabinet.server.repository.mapper.PatientMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JpaPatientRepository implements PatientRepository {
    private final SpringDataPatientRepository repository;
    private final SpringDataDoctorRepository doctorRepository;

    public JpaPatientRepository(SpringDataPatientRepository repository, SpringDataDoctorRepository doctorRepository) {
        this.repository = repository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Patient> findAll() {
        return repository.findAll()
                .stream()
                .map(PatientMapper::toDomain)
                .toList();
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = PatientMapper.toEntity(patient);
        PatientEntity saved = repository.save(entity);

        return PatientMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void deletePatient(Integer id) {
        PatientEntity patient = repository.findById(id).orElseThrow();

        for (DoctorEntity doctor : patient.getDoctors()) {
            doctor.getPatients().remove(patient);
        }

        patient.getDoctors().clear();
        repository.delete(patient);
    }

    @Override
    public Patient findById(Integer id) {
        PatientEntity entity = repository.findById(id).orElseThrow();

        return PatientMapper.toDomain(entity);
    }

    @Override
    public List<Patient> findPatientsByDoctorId(Integer doctorId) {
        return repository.findPatientsByDoctors_Id(doctorId)
                .stream()
                .map(PatientMapper::toDomain)
                .toList();
    }

    @Override
    public void addPatientToDoctor(Integer patientId, Integer doctorId) {
        PatientEntity patient = repository
                .findById(patientId)
                .orElseThrow();

        DoctorEntity doctor = doctorRepository
                .findById(doctorId)
                .orElseThrow();

        if (doctor.getPatients() == null) {
            doctor.setPatients(new ArrayList<>());
        }

        boolean exists = doctor.getPatients()
                .stream()
                .anyMatch(p -> p.getId().equals(patientId));

        if (!exists) {
            doctor.getPatients().add(patient);
        }

        doctorRepository.save(doctor);
    }
}