package com.example.hospital_management.patient.fragments;

import static com.example.hospital_management.utils.Constants.DATABASE_URL;
import static com.example.hospital_management.utils.Constants.DOCTOR;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospital_management.MainActivity;
import com.example.hospital_management.R;
import com.example.hospital_management.doctor.loginDoctor;
import com.example.hospital_management.models.Doctor;
import com.example.hospital_management.models.EmergencyData;
import com.example.hospital_management.network.EmergencyApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyHelpFragment extends Fragment implements View.OnClickListener {

    SharedPreferences preferences;
    String doctorToken, doctorId,patientId,patientName, emergencyReason;
    int bedNo;
    TextView notification_message;
    Button bpButton, unconsciousnessButton, ivButton;
    TextView bpText, unconsciousnessText, ivText;
    public EmergencyHelpFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_emergency_help, container, false);
        initWidgets(view);

        preferences = getContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        patientId = preferences.getString("userId","error");
        patientName = preferences.getString("userFirstName","error");
        bedNo = preferences.getInt("userBedNo", 0);
        doctorId = preferences.getString("userDoctorId","error");

        notification_message = view.findViewById(R.id.notification_message);

        return view;
    }

    private void initWidgets(View view) {
        bpButton = view.findViewById(R.id.bpButton);
        unconsciousnessButton = view.findViewById(R.id.unconsciousnessButton);
        ivButton = view.findViewById(R.id.ivButton);
        bpButton.setOnClickListener(this);
        unconsciousnessButton.setOnClickListener(this);
        ivButton.setOnClickListener(this);

        bpText = view.findViewById(R.id.bpText);
        unconsciousnessText = view.findViewById(R.id.unconsciousnessText);
        ivText = view.findViewById(R.id.ivText);
    }

    void sendPostRequest(){
        EmergencyData emergencyData = new EmergencyData(patientId,doctorId,patientName,bedNo,doctorToken, emergencyReason);
        // Set emergencyData fields accordingly

        EmergencyApiClient emergencyApiClient = new EmergencyApiClient();
        emergencyApiClient.sendEmergency(emergencyData, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("WTF", response.message().toString());
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(),"Notification sent to the doctor", Toast.LENGTH_LONG).show();
                    notification_message.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("WTF", t.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Could not send notification to the doctor", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void getDoctorToken(){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL+"/Doctors/");

        reference.child(doctorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor doctor=snapshot.getValue(Doctor.class);

                if(doctor==null){
                    Toast.makeText(getContext(), "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                }
                else{
                    doctorToken = doctor.getFcmToken();
                    sendPostRequest();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isBpExpanded = false;
    private boolean isUnconsciousnessExpanded = false;
    private boolean isIvExpanded = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bpButton:
                if(!isBpExpanded){
                    isBpExpanded = true ;
                    bpText.setVisibility(View.VISIBLE);
                    emergencyReason = "BP Shootout";
                    getDoctorToken();
                } else {
                    isBpExpanded = false;
                    bpText.setVisibility(View.GONE);
                }
                break;
            case R.id.unconsciousnessButton:
                if(!isUnconsciousnessExpanded){
                    isUnconsciousnessExpanded = true ;
                    unconsciousnessText.setVisibility(View.VISIBLE);
                    emergencyReason = "Patient Unconscious";
                    getDoctorToken();
                } else {
                    isUnconsciousnessExpanded = false;
                    unconsciousnessText.setVisibility(View.GONE);
                }
                break;
            case R.id.ivButton:
                if(!isIvExpanded){
                    isIvExpanded = true ;
                    ivText.setVisibility(View.VISIBLE);
                    emergencyReason = "IV Leakage";
                    getDoctorToken();
                } else {
                    isIvExpanded = false;
                    ivText.setVisibility(View.GONE);
                }
                break;

        }
    }

}