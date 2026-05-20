package com.cabinet.server.domain;

public class MedicalRecord {
    private Integer id;
    private Integer patientId;

    public MedicalRecord() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}