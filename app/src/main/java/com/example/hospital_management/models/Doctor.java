package com.example.hospital_management.models;

import com.example.hospital_management.utils.Constants;

public class Doctor extends Person {
    private int doctorFees;
    private String degree;
    private String specialization;

    private String fcmToken;

    @Override
    public String getFcmToken() {
        return fcmToken;
    }

    @Override
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    Doctor(){}

    public Doctor(String id,String firstname,String lastName,String emailAddress,String password,String mobile,String Address,String doctorFees,String degree,String specialization,String fcmToken){
        this.setId(id);
        this.setUsertype(Constants.DOCTOR);
        this.firstName=firstname;
        this.lastName=lastName;
        this.emailAddress=emailAddress;
        this.password=password;
        this.mobileNumber=mobile;
        this.address=emailAddress;
        this.doctorFees=Integer.valueOf(doctorFees);
        this.degree=degree;
        this.specialization=specialization;
        this.setFcmToken(fcmToken);
    }

    public int getDoctorFees() {
        return doctorFees;
    }

    public void setDoctorFees(int doctorFees) {
        this.doctorFees = doctorFees;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
