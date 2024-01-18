package com.example.hospital_management.patient.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospital_management.MainActivity;
import com.example.hospital_management.R;
import com.example.hospital_management.models.Patient;
import com.example.hospital_management.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientProfileFragment extends Fragment implements View.OnClickListener {


    Button sign_out;
    TextView welcome_text, textview_name, textview_email, textview_address, textview_contact, textview_bed, textview_assigned_doctor, textview_symptoms ;
    ProgressBar progressBar;
    DatabaseReference reference;
//    FirebaseUser user;
    String uid;

    SharedPreferences preferences;

    public PatientProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = getContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view) {

        progressBar=view.findViewById(R.id.loginActivityIndeterminateProgressbar);

        sign_out=view.findViewById(R.id.buttonViewSignOut);

        welcome_text=view.findViewById(R.id.welcome_textView);
        textview_name=view.findViewById(R.id.textViewName);
        textview_email=view.findViewById(R.id.textViewEmail);
        textview_address=view.findViewById(R.id.textViewAddress);
        textview_contact=view.findViewById(R.id.textViewContact);
        textview_bed=view.findViewById(R.id.textViewBedNumber);
        textview_assigned_doctor=view.findViewById(R.id.textAssignedDoctor);
        textview_symptoms=view.findViewById(R.id.textViewSymptoms);

        sign_out.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      getProfile();
    }

    private void getProfile() {
        progressBar.setVisibility(View.VISIBLE);


        reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/Patients/");
//        user= FirebaseAuth.getInstance().getCurrentUser();
//        assert user != null;
//        uid=user.getUid();
        uid = preferences.getString("userID", "NA");


        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);

                Patient patient=snapshot.getValue(Patient.class);
                if(patient==null){
                    Toast.makeText(getContext(),"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                }
                else{
                    welcome_text.setText("Welcome, " + patient.getFirstName());
                    textview_name.setText(patient.getFirstName() + " " + patient.getLastName());
                    textview_email.setText(patient.getEmailAddress());
                    textview_contact.setText(patient.getMobileNumber());
                    textview_address.setText(patient.getAddress());
                    textview_bed.setText(String.valueOf(patient.getBedNo()));
                    textview_assigned_doctor.setText(patient.getAssignedDoctorName());
                    textview_symptoms.setText(patient.getSymptomsString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void signOutUser(){
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().signOut();
        progressBar.setVisibility(View.GONE);

        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonViewSignOut) {
            signOutUser();
            getActivity().finish();
        }
    }

}