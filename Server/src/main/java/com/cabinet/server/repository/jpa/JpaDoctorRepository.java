package com.cabinet.server.repository.jpa;

import com.cabinet.server.domain.Doctor;
import com.cabinet.server.domain.DoctorRepository;
import com.cabinet.server.repository.entity.DoctorEntity;
import com.cabinet.server.repository.entity.PatientEntity;
import com.cabinet.server.repository.mapper.DoctorMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaDoctorRepository implements DoctorRepository {
    private final SpringDataDoctorRepository repository;
    private final SpringDataPatientRepository patientRepository;

    public JpaDoctorRepository(SpringDataDoctorRepository repository, SpringDataPatientRepository patientRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Doctor> findAll() {
        return repository.findAll()
                .stream()
                .map(DoctorMapper::toDomain)
                .toList();
    }

    @Override
    public Doctor save(Doctor doctor) {
        DoctorEntity entity;

        if (doctor.getId() != null) {
            entity = repository.findById(doctor.getId()).orElse(new DoctorEntity());
        } else {
            entity = new DoctorEntity();
        }

        entity.setSpecialization(doctor.getSpecialization());
        entity.setPhone(doctor.getPhone());
        entity.setOfficeHours(doctor.getOfficeHours());

        DoctorEntity saved = repository.save(entity);

        return DoctorMapper.toDomain(saved);
    }

    @Override
    public Doctor findById(Integer id) {
        DoctorEntity entity = repository.findById(id).orElseThrow();

        return DoctorMapper.toDomain(entity);
    }

    @Override
    public void addPatient(Integer doctorId, Integer patientId) {
        DoctorEntity doctor = repository.findById(doctorId).orElseThrow();
        PatientEntity patient = patientRepository.findById(patientId).orElseThrow();

        boolean exists = doctor.getPatients()
                .stream()
                .anyMatch(p ->
                        p.getId().equals(patientId));

        if (!exists) {
            doctor.getPatients().add(patient);
            repository.save(doctor);
        }
    }
}