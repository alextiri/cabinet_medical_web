package com.cabinet.server.service;

import com.cabinet.server.domain.MedicalRecord;
import com.cabinet.server.domain.Patient;
import com.cabinet.server.domain.MedicalRecordRepository;
import com.cabinet.server.domain.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PatientService(PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository) {
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<Patient> getDoctorPatients(Integer doctorId) {
        return patientRepository.findPatientsByDoctorId(doctorId);
    }

    public Patient createPatient(Patient patient, Integer doctorId) {
        Patient savedPatient = patientRepository.save(patient);

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatientId(savedPatient.getId());
        medicalRecordRepository.save(medicalRecord);

        patientRepository.addPatientToDoctor(savedPatient.getId(), doctorId);

        return savedPatient;
    }

    public Patient updatePatient(Integer id, Patient updatedPatient) {
        Patient patient = patientRepository.findById(id);

        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setCnp(updatedPatient.getCnp());
        patient.setGender(updatedPatient.getGender());
        patient.setBirthDate(updatedPatient.getBirthDate());
        patient.setPhone(updatedPatient.getPhone());
        patient.setEmail(updatedPatient.getEmail());
        patient.setAddress(updatedPatient.getAddress());

        return patientRepository.save(patient);
    }

    public void deletePatient(Integer id) {
        patientRepository.deletePatient(id);
    }
}