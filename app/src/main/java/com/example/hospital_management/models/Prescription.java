package com.example.hospital_management.models;

import java.util.ArrayList;

public class Prescription {
    private String doctorId;
    private String patientId;
    private ArrayList<Medicine> meds;

    public Prescription(){}

    public Prescription(String doctorId, String patientId, ArrayList<Medicine> meds) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.meds = meds;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public ArrayList<Medicine> getMeds() {
        return meds;
    }

    public void setMeds(ArrayList<Medicine> meds) {
        this.meds = meds;
    }
}
