package com.cabinet.server.repository.jpa;

import com.cabinet.server.repository.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataPatientRepository extends JpaRepository<PatientEntity, Integer> {
    List<PatientEntity> findPatientsByDoctors_Id(Integer doctorId);
}