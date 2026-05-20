package com.cabinet.server.repository.jpa;

import com.cabinet.server.domain.Appointment;
import com.cabinet.server.domain.AppointmentRepository;
import com.cabinet.server.repository.entity.AppointmentEntity;
import com.cabinet.server.repository.mapper.AppointmentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaAppointmentRepository implements AppointmentRepository {
    private final SpringDataAppointmentRepository repository;

    public JpaAppointmentRepository(SpringDataAppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Appointment> findByDoctorId(Integer doctorId) {
        return repository.findByDoctor_Id(doctorId)
                .stream()
                .map(AppointmentMapper::toDomain)
                .toList();
    }

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = AppointmentMapper.toEntity(appointment);
        AppointmentEntity saved = repository.save(entity);

        return AppointmentMapper.toDomain(saved);
    }

    @Override
    public Appointment findById(Integer id) {
        AppointmentEntity entity = repository.findById(id).orElseThrow();

        return AppointmentMapper.toDomain(entity);
    }

    @Override
    public List<Appointment> findAll() {
        return repository.findAll()
                .stream()
                .map(AppointmentMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteAppointment(Integer id) {
        repository.deleteById(id);
    }
}