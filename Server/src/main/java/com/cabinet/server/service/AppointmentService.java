package com.cabinet.server.service;

import com.cabinet.server.domain.Appointment;
import com.cabinet.server.domain.AppointmentRepository;
import com.cabinet.server.domain.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<Appointment> getAppointmentByDoctorId(Integer doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public Appointment createAppointment(Appointment appointment) {
        Appointment created = appointmentRepository.save(appointment);

        doctorRepository.addPatient(
                appointment.getDoctorId(),
                appointment.getPatientId()
        );

        return created;
    }

    public Appointment updateAppointment(Integer id, Appointment updatedAppointment) {
        Appointment appointment = appointmentRepository.findById(id);

        appointment.setDate(updatedAppointment.getDate());
        appointment.setStatus(updatedAppointment.getStatus());

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void deleteAppointment(Integer id) {
        appointmentRepository.deleteAppointment(id);
    }
}