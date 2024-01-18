package com.example.hospital_management.doctor.fragments;

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
import com.example.hospital_management.models.Doctor;
import com.example.hospital_management.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfileFragment extends Fragment implements View.OnClickListener {


    Button sign_out;
    TextView welcome_text, textview_name, textview_email, textview_address, textview_contact,textview_degree, textview_specialization ;
    ProgressBar progressBar;
    DatabaseReference reference;
//    FirebaseUser user;
    String uid;

    SharedPreferences preferences;

    public DoctorProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        preferences = getContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        initWidget(view);
        return view;
    }

    private void initWidget(View view) {
        progressBar=view.findViewById(R.id.loginActivityIndeterminateProgressbar);

        sign_out=view.findViewById(R.id.buttonViewSignOut);

        welcome_text=view.findViewById(R.id.welcome_textView);
        textview_name=view.findViewById(R.id.textViewName);
        textview_email=view.findViewById(R.id.textViewEmail);
        textview_address=view.findViewById(R.id.textViewAddress);
        textview_contact=view.findViewById(R.id.textViewContact);
        textview_degree=view.findViewById(R.id.textViewDegree);
        textview_specialization=view.findViewById(R.id.textViewSpecialization);

        sign_out.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfile();
    }

    private void getProfile() {

        progressBar.setVisibility(View.VISIBLE);

        reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/Doctors/");
//        user= FirebaseAuth.getInstance().getCurrentUser();
//        assert user != null;
//        uid=user.getUid();

        uid = preferences.getString("userID", "NA");


        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);

                Doctor doctor=snapshot.getValue(Doctor.class);
                if(doctor==null){
                    Toast.makeText(getContext(),"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                }
                else{
                    welcome_text.setText("Welcome, " + doctor.getFirstName());
                    textview_name.setText(doctor.getFirstName() + " " + doctor.getLastName());
                    textview_email.setText(doctor.getEmailAddress());
                    textview_contact.setText(doctor.getMobileNumber());
                    textview_address.setText(doctor.getAddress());
                    textview_specialization.setText(doctor.getSpecialization());
                    textview_degree.setText(doctor.getDegree());
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