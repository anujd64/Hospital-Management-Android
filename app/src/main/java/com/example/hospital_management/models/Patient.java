package com.example.hospital_management.models;

import com.example.hospital_management.utils.Constants;

import java.util.List;

public class Patient extends Person {

    private List<String> symptoms;
    private int bedNo;
    private String assignedDoctorID;
    private String assignedDoctorName;

    private String fcmToken;

    @Override
    public String getFcmToken() {
        return fcmToken;
    }

    @Override
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    public String getAssignedDoctorName() {
        return assignedDoctorName;
    }

    public void setAssignedDoctorName(String assignedDoctorName) {
        this.assignedDoctorName = assignedDoctorName;
    }

    public int getBedNo() {
        return bedNo;
    }

    public void setBedNo(int bedNo) {
        this.bedNo = bedNo;
    }

    public String getAssignedDoctorID() {
        return assignedDoctorID;
    }

    public void setAssignedDoctorID(String assignedDoctorID) {
        this.assignedDoctorID = assignedDoctorID;
    }

    Patient(){}

    public Patient(String id,String firstname,String lastName,String emailAddress,String password,String mobile,String Address,List<String> symptoms, int bedNo, String assignedDoctorID, String assignedDoctorName,String fcmToken){
        this.setId(id);
        this.setUsertype(Constants.PATIENT);
        this.setFirstName(firstname);
        this.setLastName(lastName);
        this.setEmailAddress(emailAddress);
        this.setPassword(password);
        this.setMobileNumber(mobile);
        this.setAddress(Address);
        this.setSymptoms(symptoms);
        this.setBedNo(bedNo);
        this.setAssignedDoctorID(assignedDoctorID);
        this.setAssignedDoctorName(assignedDoctorName);
        this.setFcmToken(fcmToken);
    }

    public String getSymptomsString() {
        StringBuilder symptomsStr = new StringBuilder();
        for (String s : symptoms) {
            symptomsStr.append(s).append(", ") ;
        }
        return symptomsStr.toString();
    }
}
