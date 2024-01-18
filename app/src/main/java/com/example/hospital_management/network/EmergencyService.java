package com.example.hospital_management.network;

import com.example.hospital_management.models.EmergencyData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmergencyService {

    @POST("/api/emergency")
    Call<Void> sendEmergencyNotification(@Body EmergencyData emergencyData);
}
