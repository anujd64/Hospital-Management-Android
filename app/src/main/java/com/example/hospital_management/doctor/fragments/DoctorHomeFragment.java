package com.example.hospital_management.doctor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospital_management.R;
import com.example.hospital_management.doctor.adapter.CustomAppointmentAdapter;
import com.example.hospital_management.models.Appointment;
import com.example.hospital_management.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DoctorHomeFragment extends Fragment implements CustomAppointmentAdapter.AppointmentAdapterClickListener {



    TextView noAppointmentsTextView, greetingsTextView, upcomingHeading;
    RecyclerView appointmentsRV;
    CustomAppointmentAdapter adapter;
    SharedPreferences preferences;
    Button viewPatientsButton;

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        preferences = getContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);

        initWidgets(view);
        setWelcomeMessage();
        getAppointments();
        return view;
    }

    private void initWidgets(View view) {
        noAppointmentsTextView = view.findViewById(R.id.no_appointments);
        appointmentsRV = view.findViewById(R.id.appointments_rv);
        viewPatientsButton = view.findViewById(R.id.view_patients_button);
        greetingsTextView = view.findViewById(R.id.greetings_textView);
        upcomingHeading = view.findViewById(R.id.upcoming_heading);
        viewPatientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new DoctorPatientsFragment());
            }
        });
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
        String welcomeMessage = String.format(Locale.getDefault(), "%s,\n%s", greeting, "Dr. " + doctorName);
        greetingsTextView.setText(welcomeMessage);


    }

    ArrayList<Appointment> list = new ArrayList<>();
    private void getAppointments() {

//        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//        assert user != null;
//        String uid=user.getUid();

        String uid = preferences.getString("userID","User");


        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/");

        reference.child("Appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Appointment appointment=data.getValue(Appointment.class);
                    if(!list.contains(appointment) && appointment.getDoctorId().equals(uid)){
                        list.add(appointment);
                    }
                }
                if(list.isEmpty()){
                    upcomingHeading.setVisibility(View.GONE);
                    noAppointmentsTextView.setVisibility(View.VISIBLE);
                }
                else {
                    adapter = new CustomAppointmentAdapter(list, getContext(), DoctorHomeFragment.this::onItemClicked);
                    appointmentsRV.setLayoutManager(new LinearLayoutManager(getContext()));
                    appointmentsRV.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Use getSupportFragmentManager() for support fragments
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null); // Add the transaction to the back stack
        transaction.commit();
    }

    @Override
    public void onItemClicked(int position, String doctorId) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/");

        reference.child("Appointments").child(doctorId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(),"Appointment Canceled",Toast.LENGTH_LONG).show();
                list.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.clearData();
            adapter.notifyDataSetChanged();
        }    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter!=null){
            adapter.clearData();
            adapter.notifyDataSetChanged();
        }
    }
}