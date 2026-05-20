package com.cabinet.server.repository.mapper;

import com.cabinet.server.domain.Patient;
import com.cabinet.server.repository.entity.PatientEntity;

public class PatientMapper {
    public static Patient toDomain(PatientEntity entity) {
        Patient patient = new Patient();

        patient.setId(entity.getId());
        patient.setFirstName(entity.getFirstName());
        patient.setLastName(entity.getLastName());
        patient.setCnp(entity.getCnp());
        patient.setGender(entity.getGender());
        patient.setBirthDate(entity.getBirthDate());
        patient.setPhone(entity.getPhone());
        patient.setEmail(entity.getEmail());
        patient.setAddress(entity.getAddress());

        return patient;
    }

    public static PatientEntity toEntity(Patient patient) {
        PatientEntity entity = new PatientEntity();

        entity.setId(patient.getId());
        entity.setFirstName(patient.getFirstName());
        entity.setLastName(patient.getLastName());
        entity.setCnp(patient.getCnp());
        entity.setGender(patient.getGender());
        entity.setBirthDate(patient.getBirthDate());
        entity.setPhone(patient.getPhone());
        entity.setEmail(patient.getEmail());
        entity.setAddress(patient.getAddress());

        return entity;
    }
}