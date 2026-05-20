package com.cabinet.server.domain;

import java.util.List;

public interface PatientRepository {
    List<Patient> findAll();
    List<Patient> findPatientsByDoctorId(Integer doctorId);

    Patient save(Patient patient);
    Patient findById(Integer id);
    void addPatientToDoctor(Integer patientId, Integer doctorId);
    void deletePatient(Integer id);
}