package com.example.hospital_management.models;

import java.util.ArrayList;

public class Appointment {
    private String patientId;
    private String doctorId;

    private String patientName;

    private String doctorName;

    private String date;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    private String time;
    private ArrayList<String> symptoms=new ArrayList<>();

    Appointment(){}

    public Appointment(String patientId, String doctorId,String patientName,String doctorName, String date, String time, ArrayList<String> symptoms) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.setSymptoms(symptoms);
        this.setDoctorName(doctorName);
        this.setPatientName(patientName);
    }
    public ArrayList<String> getSymptoms() {
        return this.symptoms;
    }

    public void setSymptoms(ArrayList<String> allergies) { this.symptoms =symptoms; }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
