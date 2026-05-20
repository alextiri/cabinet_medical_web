package com.cabinet.server.domain;

import java.util.List;

public interface AppointmentRepository {
    List<Appointment> findByDoctorId(Integer doctorId);
    List<Appointment> findAll();

    void deleteAppointment(Integer id);
    Appointment findById(Integer id);
    Appointment save(Appointment appointment);
}