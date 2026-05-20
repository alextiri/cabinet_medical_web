package com.cabinet.server.controller;

import com.cabinet.server.domain.MedicalRecord;
import com.cabinet.server.service.MedicalRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/medical-records/doctor/{doctorId}")
    public List<MedicalRecord> getMedicalRecordsByDoctor(@PathVariable Integer doctorId) {
        return medicalRecordService.getMedicalRecordsByDoctor(doctorId);
    }

    @GetMapping("/medical-records")
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping("/medical-records/{id}")
    public MedicalRecord getMedicalRecord(@PathVariable Integer id) {
        return medicalRecordService.findById(id);
    }

    @GetMapping("/medical-records/patient/{patientId}")
    public MedicalRecord getByPatientId(@PathVariable Integer patientId) {
        return medicalRecordService.getByPatientId(patientId);
    }

    @PostMapping("/medical-records")
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.createMedicalRecord(medicalRecord);
    }
}