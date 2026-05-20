package com.cabinet.server.controller;

import com.cabinet.server.domain.Doctor;
import com.cabinet.server.service.DoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/doctors/{id}")
    public Doctor getDoctor(@PathVariable Integer id) {
        return doctorService.getDoctor(id);
    }

    @PostMapping("/doctors")
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    @PutMapping("/doctors/{id}")
    public Doctor updateDoctor(@PathVariable Integer id, @RequestBody Doctor doctor) {
        System.out.println(doctor.getOfficeHours());
        return doctorService.updateDoctor(id, doctor);
    }
}