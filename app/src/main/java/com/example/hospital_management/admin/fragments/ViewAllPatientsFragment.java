package com.example.hospital_management.admin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hospital_management.R;
import com.example.hospital_management.admin.adapters.CustomPatientAdapter;
import com.example.hospital_management.models.Patient;
import com.example.hospital_management.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewAllPatientsFragment extends Fragment implements CustomPatientAdapter.AdapterClickListener {
    public ViewAllPatientsFragment() {}

    CustomPatientAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Patient> patientList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_all_patients, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.patient_rv);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/");

        reference.child("Patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Patient patient=data.getValue(Patient.class);
                    if(!patientList.contains(patient)){
                        patientList.add(patient);
                    }
                }
                if(patientList.isEmpty()){
                    Toast.makeText(getContext(), "Sorry,No patients are available at this moment", Toast.LENGTH_LONG).show();
                }
                else {
                    adapter = new CustomPatientAdapter(patientList, getContext(), ViewAllPatientsFragment.this);
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
    public void onItemClicked(int position,String patientId) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/");

        reference.child("Patients").child(patientId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(),"Patient Profile Deleted",Toast.LENGTH_LONG).show();
                patientList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
            }
        });
    }
}