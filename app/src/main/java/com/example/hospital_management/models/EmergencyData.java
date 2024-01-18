package com.example.hospital_management.models;

public class EmergencyData {

    public EmergencyData(String patientId, String doctorId, String patientName, int bedNo, String deviceToken,String emergencyReason) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.bedNo = bedNo;
        this.deviceToken = deviceToken;
        this.emergencyReason = emergencyReason;
    }

    private String emergencyReason;

    public String getEmergencyReason() {
        return emergencyReason;
    }

    public void setEmergencyReason(String emergencyReason) {
        this.emergencyReason = emergencyReason;
    }

    private String patientId;
    private String doctorId;
    private String patientName;
    private int bedNo;
    private String deviceToken;

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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getBedNo() {
        return bedNo;
    }

    public void setBedNo(int bedNo) {
        this.bedNo = bedNo;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
