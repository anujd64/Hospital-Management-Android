package com.example.hospital_management.models;

import com.example.hospital_management.utils.Constants;

public class Admin extends Person{

    Admin(){}

    private String fcmToken;

    @Override
    public String getFcmToken() {
        return fcmToken;
    }

    @Override
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Admin(String id,String firstname,String lastName,String emailAddress,String password,String mobile,String address,String fcmToken){
        this.setId(id);
        this.setUsertype(Constants.ADMIN);
        this.firstName=firstname;
        this.lastName=lastName;
        this.emailAddress=emailAddress;
        this.password=password;
        this.mobileNumber=mobile;
        this.address=address;
        this.setFcmToken(fcmToken);
    }

}
