package com.cabinet.server.domain;

import java.util.List;

public interface ConsultationRepository {
    List<Consultation> findByPatientId(Integer patientId);
    List<Consultation> findByDoctorId(Integer doctorId);
    List<Consultation> findAll();

    Consultation save(Consultation consultation);
}