package com.cabinet.server.controller;

import com.cabinet.server.domain.Consultation;
import com.cabinet.server.service.ConsultationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConsultationController {
    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @GetMapping("/consultations/patient/{patientId}")
    public List<Consultation> getConsultations(@PathVariable("patientId") Integer patientId) {
        return consultationService.getConsultations(patientId);
    }

    @GetMapping("/consultations")
    public List<Consultation> getAllConsultations() {
        return consultationService.getAllConsultations();
    }

    @GetMapping("/consultations/doctor/{doctorId}")
    public List<Consultation> getDoctorConsultations(@PathVariable Integer doctorId) {
        return consultationService.getDoctorConsultations(doctorId);
    }

    @PostMapping("/consultations")
    public Consultation createConsultation(@RequestBody Consultation consultation) {
        return consultationService.createConsultation(consultation);
    }
}