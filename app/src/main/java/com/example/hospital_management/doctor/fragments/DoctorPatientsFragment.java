package com.example.hospital_management.doctor.fragments;

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
import android.widget.Toast;

import com.example.hospital_management.R;
import com.example.hospital_management.doctor.adapter.CustomPatientForDoctorAdapter;
import com.example.hospital_management.models.Patient;
import com.example.hospital_management.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorPatientsFragment extends Fragment implements CustomPatientForDoctorAdapter.AdapterClickListener{
    public DoctorPatientsFragment() {
        // Required empty public constructor
    }

    CustomPatientForDoctorAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Patient> patientList = new ArrayList<>();

    FirebaseUser user;
    String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        

        return inflater.inflate(R.layout.fragment_doctor_patients, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.patient_rv);


        user= FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        uid=user.getUid();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/");

        reference.child("Patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Patient patient=data.getValue(Patient.class);
                    if(patient!=null){
                        if(!patientList.contains(patient) && patient.getAssignedDoctorID().equals(uid)){
                            patientList.add(patient);
                        }
                    }
                }
                if(patientList.isEmpty()){
                    Toast.makeText(getContext(), "Sorry,No patients are available at this moment", Toast.LENGTH_LONG).show();
                }
                else {
                    adapter = new CustomPatientForDoctorAdapter(patientList, getContext(),DoctorPatientsFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClicked(int position, String patientId) {
        AssignMedsFragment fragment = new AssignMedsFragment(patientId);
        replaceFragment(fragment);
    }
    private  void replaceFragment (Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Use getSupportFragmentManager() for support fragments
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null); // Add the transaction to the back stack
        transaction.commit();

//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.container, fragment);
//        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter!=null){
            adapter.clearData();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.clearData();
            adapter.notifyDataSetChanged();
        }
    }
}