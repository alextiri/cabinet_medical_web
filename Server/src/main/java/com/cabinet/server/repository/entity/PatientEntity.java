package com.cabinet.server.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String cnp;
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String phone;
    private String email;
    private String address;

    @JsonIgnore
    @ManyToMany(mappedBy = "patients")
    private List<DoctorEntity> doctors;
}
