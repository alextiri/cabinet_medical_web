package com.cabinet.server.service;

import com.cabinet.server.domain.Doctor;
import com.cabinet.server.domain.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctor(Integer id) {
        return doctorRepository.findById(id);
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Integer id, Doctor updatedDoctor) {
        Doctor doctor = doctorRepository.findById(id);
        doctor.setOfficeHours(updatedDoctor.getOfficeHours());

        return doctorRepository.save(doctor);
    }
}