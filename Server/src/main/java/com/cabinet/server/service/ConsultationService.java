package com.cabinet.server.service;

import com.cabinet.server.domain.Consultation;
import com.cabinet.server.domain.ConsultationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationService {
    private final ConsultationRepository consultationRepository;

    public ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    public List<Consultation> getConsultations(Integer patientId) {
        return consultationRepository.findByPatientId(patientId);
    }

    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    public List<Consultation> getDoctorConsultations(Integer doctorId) {
        return consultationRepository.findByDoctorId(doctorId);
    }

    public Consultation createConsultation(Consultation consultation) {
        return consultationRepository.save(consultation);
    }
}