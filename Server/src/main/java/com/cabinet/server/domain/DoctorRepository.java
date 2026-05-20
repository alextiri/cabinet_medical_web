package com.cabinet.server.domain;

import java.util.List;

public interface DoctorRepository {
    List<Doctor> findAll();

    void addPatient(Integer doctorId, Integer patientId);
    Doctor save(Doctor doctor);
    Doctor findById(Integer id);
}