package com.cabinet.server.repository.jpa;

import com.cabinet.server.repository.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataAppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {
    List<AppointmentEntity> findByDoctor_Id(Integer doctorId);
}