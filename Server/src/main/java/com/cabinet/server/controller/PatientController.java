package com.cabinet.server.controller;

import com.cabinet.server.domain.Patient;
import com.cabinet.server.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    public List<Patient> getPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/patients/doctor/{doctorId}")
    public List<Patient> getDoctorPatients(@PathVariable Integer doctorId) {
        return patientService.getDoctorPatients(doctorId);
    }

    @PostMapping("/patients/{doctorId}")
    public Patient createPatient(@RequestBody Patient patient, @PathVariable Integer doctorId) {
        return patientService.createPatient(patient, doctorId);
    }

    @PutMapping("/patients/{id}")
    public Patient updatePatient(@PathVariable Integer id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @DeleteMapping("/patients/{id}")
    public void deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
    }
}