package com.cabinet.server.repository.jpa;

import com.cabinet.server.repository.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpringDataDoctorRepository extends JpaRepository<DoctorEntity,Integer> { }
