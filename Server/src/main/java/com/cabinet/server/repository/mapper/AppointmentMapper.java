package com.cabinet.server.repository.mapper;

import com.cabinet.server.domain.Appointment;
import com.cabinet.server.repository.entity.AppointmentEntity;
import com.cabinet.server.repository.entity.DoctorEntity;
import com.cabinet.server.repository.entity.PatientEntity;

public class AppointmentMapper {
    public static Appointment toDomain(AppointmentEntity entity) {
        Appointment appointment = new Appointment();

        appointment.setId(entity.getId());

        if (entity.getDoctor() != null) {
            appointment.setDoctorId(entity.getDoctor().getId());
        }

        if (entity.getPatient() != null) {
            appointment.setPatientId(entity.getPatient().getId());
        }

        appointment.setDate(entity.getDate());
        appointment.setStatus(entity.getStatus());

        return appointment;
    }

    public static AppointmentEntity toEntity(Appointment appointment) {
        AppointmentEntity entity = new AppointmentEntity();

        entity.setId(appointment.getId());

        if (appointment.getDoctorId() != null) {
            DoctorEntity doctor = new DoctorEntity();
            doctor.setId(appointment.getDoctorId());

            entity.setDoctor(doctor);
        }

        if (appointment.getPatientId() != null) {
            PatientEntity patient = new PatientEntity();
            patient.setId(appointment.getPatientId());

            entity.setPatient(patient);
        }

        entity.setDate(appointment.getDate());
        entity.setStatus(appointment.getStatus());

        return entity;
    }
}