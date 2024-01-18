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
import com.example.hospital_management.admin.adapters.CustomDoctorAdapter;
import com.example.hospital_management.models.Doctor;
import com.example.hospital_management.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllDoctorsFragment extends Fragment implements CustomDoctorAdapter.AdapterClickListener {



    public ViewAllDoctorsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment_view_all_doctors, container, false);
    }

    ArrayList<Doctor> doctorList = new ArrayList<>();
    CustomDoctorAdapter adapter;
    RecyclerView recyclerView;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/");

        reference.child("Doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Doctor doctor=data.getValue(Doctor.class);
                    if(!doctorList.contains(doctor)){
                        doctorList.add(doctor);
                    }
                }
                if(doctorList.isEmpty()){
                    Toast.makeText(getContext(), "Sorry,No doctors are available at this moment", Toast.LENGTH_LONG).show();
                }
                else {
                    adapter = new CustomDoctorAdapter(doctorList, getContext(), ViewAllDoctorsFragment.this);
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
    public void onItemClicked(int position, String doctorId) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/");

        reference.child("Doctors").child(doctorId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(),"Doctor Profile Deleted",Toast.LENGTH_LONG).show();
                doctorList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();

            }
        });
    }
}