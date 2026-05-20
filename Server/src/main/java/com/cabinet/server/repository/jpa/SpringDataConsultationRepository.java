package com.cabinet.server.repository.jpa;

import com.cabinet.server.repository.entity.ConsultationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataConsultationRepository extends JpaRepository<ConsultationEntity,Integer> {
    List<ConsultationEntity> findByMedicalRecord_Patient_Id(Integer patientId);
    List<ConsultationEntity> findByDoctorId(Integer doctorId);
}
