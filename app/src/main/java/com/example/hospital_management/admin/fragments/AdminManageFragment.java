package com.example.hospital_management.admin.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hospital_management.admin.RegisterDoctor;
import com.example.hospital_management.admin.RegisterPatient;
import com.example.hospital_management.R;

import java.util.Calendar;
import java.util.Locale;


public class AdminManageFragment extends Fragment implements View.OnClickListener{


    TextView greetingsTextView;
    Button buttonPatient,buttonDoctor,buttonAllPatients,buttonAllDoctors;

    SharedPreferences preferences;

    public AdminManageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_manage, container, false);
        preferences = getContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        initWidgets(view);
        setWelcomeMessage();


        return view;
    }

    private void setWelcomeMessage() {

        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (timeOfDay >= 0 && timeOfDay < 12) {
            greeting = "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }

        // Get the doctor's name
        String doctorName = preferences.getString("userFirstName","User");

        // Set the text in the TextView
        String welcomeMessage = String.format(Locale.getDefault(), "%s,\n%s", greeting, doctorName);
        greetingsTextView.setText(welcomeMessage);


    }

    private void initWidgets(View view) {
        buttonPatient = view.findViewById(R.id.buttonPatient);
        buttonDoctor = view.findViewById(R.id.buttonDoctor);
        buttonAllPatients = view.findViewById(R.id.buttonAllPatient);
        buttonAllDoctors = view.findViewById(R.id.buttonAllDoctor);
        greetingsTextView = view.findViewById(R.id.greetings_textView);


        buttonPatient.setOnClickListener(this);
        buttonDoctor.setOnClickListener(this);
        buttonAllPatients.setOnClickListener(this);
        buttonAllDoctors.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonPatient:
                startActivity(new Intent(getActivity(), RegisterPatient.class));
                break;

            case R.id.buttonDoctor:
                startActivity(new Intent(getActivity(), RegisterDoctor.class));
                break;

//            case R.id.buttonAllPatient:
//                startActivity(new Intent(getActivity(), ViewPatientList.class));
//                break;
            case R.id.buttonAllDoctor:
                Fragment viewAllDoctorsFragment = new ViewAllDoctorsFragment();
                addFragment(viewAllDoctorsFragment);
                break;

            case R.id.buttonAllPatient:
                Fragment viewAllPatientsFragment = new ViewAllPatientsFragment();
                addFragment(viewAllPatientsFragment);
                break;


        }

    }

    private  void addFragment (Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Use getSupportFragmentManager() for support fragments
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null); // Add the transaction to the back stack
        transaction.commit();
    }
}