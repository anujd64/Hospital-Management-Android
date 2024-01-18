//package com.example.hospital_management.utils;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.widget.Toast;
//
//import com.example.hospital_management.R;
//import com.example.hospital_management.admin.adapters.CustomDoctorAdapter;
//import com.example.hospital_management.models.Doctor;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class ViewDoctorsList extends AppCompatActivity {
//    CustomDoctorAdapter adapter;
//    RecyclerView recyclerView;
//    ArrayList <Doctor> doctorList=new ArrayList<>();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_doctors_list);
//
//        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
//
//
//        //Getting data:
//        DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/");
//
//        reference.child("Doctors").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot data:snapshot.getChildren()){
//                            Doctor doctor=data.getValue(Doctor.class);
//                            if(!doctorList.contains(doctor)){
//                                doctorList.add(doctor);
//                            }
//                        }
//                        if(doctorList.isEmpty()){
//                            Toast.makeText(ViewDoctorsList.this, "Sorry,No doctors are available at this moment", Toast.LENGTH_LONG).show();
//                        }
//                        else {
//                            adapter = new CustomDoctorAdapter(doctorList, ViewDoctorsList.this, adapterClickListener);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(ViewDoctorsList.this));
//                            recyclerView.setAdapter(adapter);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(ViewDoctorsList.this,error.getCode(), Toast.LENGTH_LONG).show();
//                    }
//                });
//
//    }
//}