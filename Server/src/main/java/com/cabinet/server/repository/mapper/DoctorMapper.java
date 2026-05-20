package com.cabinet.server.repository.mapper;

import com.cabinet.server.domain.Doctor;
import com.cabinet.server.repository.entity.DoctorEntity;

public class DoctorMapper {
    public static Doctor toDomain(DoctorEntity entity) {
        Doctor doctor = new Doctor();

        doctor.setId(entity.getId());
        doctor.setFirstName(entity.getUser().getFirstName());
        doctor.setLastName(entity.getUser().getLastName());
        doctor.setSpecialization(entity.getSpecialization());
        doctor.setPhone(entity.getPhone());
        doctor.setOfficeHours(entity.getOfficeHours());

        return doctor;
    }

    public static DoctorEntity toEntity(Doctor doctor) {
        DoctorEntity entity = new DoctorEntity();

        entity.setId(doctor.getId());
        doctor.setFirstName(entity.getUser().getFirstName());
        doctor.setLastName(entity.getUser().getLastName());
        entity.setSpecialization(doctor.getSpecialization());
        entity.setPhone(doctor.getPhone());
        entity.setOfficeHours(doctor.getOfficeHours());

        return entity;
    }
}