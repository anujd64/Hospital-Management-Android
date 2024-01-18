package com.example.hospital_management.patient.fragments;

import static com.example.hospital_management.utils.Constants.DATABASE_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospital_management.R;
import com.example.hospital_management.models.Medicine;
import com.example.hospital_management.models.Prescription;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PatientHomeFragment extends Fragment implements View.OnClickListener {

    public PatientHomeFragment() {
        // Required empty public constructor
    }

    TextView prescriptionTextView, greetingsTextView;
    LinearLayout prescriptionLayout;
    Button emergencyButton, bookAppointmentButton;
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_home, container, false);
        preferences = getContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        prescriptionTextView = view.findViewById(R.id.prescription);
        prescriptionLayout = view.findViewById(R.id.prescriptionLayout);
        emergencyButton = view.findViewById(R.id.emergency_button);
        bookAppointmentButton = view.findViewById(R.id.book_appointment_button);
        greetingsTextView = view.findViewById(R.id.greetings_textView);
        bookAppointmentButton.setOnClickListener(this);
        emergencyButton.setOnClickListener(this);

        getPrescription();
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

        String patientName = preferences.getString("userFirstName","User");

        String welcomeMessage = String.format(Locale.getDefault(), "%s,\n%s", greeting,patientName);
        greetingsTextView.setText(welcomeMessage);


    }
    void getPrescription(){

        String patientID= FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl(DATABASE_URL+"/Prescriptions/")
                .child(patientID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Prescription prescription = snapshot.getValue(Prescription.class);
                if(prescription==null){
//                    Toast.makeText(getContext(),"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                }
                else{
                    prescriptionLayout.setVisibility(View.VISIBLE);
                    ArrayList<Medicine> meds = prescription.getMeds();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Medicine m : meds) {
                        if (m.getDosage()>=1){
                            stringBuilder.append("Take ").append(m.getName()).append(" ").append(m.getDosage()).append(" times a day").append("\n\n");
                        }
                    }
                    prescriptionTextView.setText(stringBuilder.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.book_appointment_button) {
            addFragment(new BookAppointmentFragment());
        } else if (viewId == R.id.emergency_button) {
            addFragment(new EmergencyHelpFragment());
        }

    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Use getSupportFragmentManager() for support fragments
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null); // Add the transaction to the back stack
        transaction.commit();
    }
}