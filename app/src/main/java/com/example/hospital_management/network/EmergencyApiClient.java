package com.example.hospital_management.network;

import static com.example.hospital_management.utils.Constants.FCM_SERVER_URL;

import com.example.hospital_management.models.EmergencyData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmergencyApiClient {

    private final EmergencyService emergencyService;

    public EmergencyApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FCM_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        emergencyService = retrofit.create(EmergencyService.class);
    }

    public void sendEmergency(EmergencyData emergencyData, final Callback<Void> callback) {
        Call<Void> call = emergencyService.sendEmergencyNotification(emergencyData);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    // Handle unsuccessful response
                    callback.onFailure(call, new Throwable("Failed to send emergency notification."));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
