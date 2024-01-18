//package com.example.medi_consult.utils;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.example.medi_consult.R;
//import com.example.medi_consult.patient.adapters.CustomAppointmentAdapter;
//import com.example.medi_consult.models.Appointment;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class ViewAppointmentHistory extends AppCompatActivity {
//    CustomAppointmentAdapter adapter;
//    RecyclerView recyclerView;
//    ArrayList<Appointment> appointmentList=new ArrayList<>();
//
//
//    @SuppressLint("SuspiciousIndentation")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_appointment_history);
//
//        recyclerView=(RecyclerView) findViewById(R.id.appointments_recyclerView);
//
//
//        //Getting data:
//
//
//            //RetrievingDoctor's List
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/Appointments/");
//            FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/Appointments/")
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for(DataSnapshot data:snapshot.getChildren()){
//                                Appointment appointment=data.getValue(Appointment.class);
//                                appointmentList.add(appointment);
//                            }
//
////                            getValuesOfAllergies(rootRef);
//                            if(appointmentList.isEmpty()){
//                                Toast.makeText(ViewAppointmentHistory.this, "Sorry,No appointments are available", Toast.LENGTH_LONG).show();
//                            }
//                            else {
//                                adapter = new CustomAppointmentAdapter(appointmentList, ViewAppointmentHistory.this, adapterClickListener);
//                                recyclerView.setLayoutManager(new LinearLayoutManager(ViewAppointmentHistory.this));
//                                recyclerView.setAdapter(adapter);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Toast.makeText(ViewAppointmentHistory.this,"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//    }
//
////    private void getValuesOfAllergies(DatabaseReference rootRef) {
////
////        rootRef.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                for (DataSnapshot data : snapshot.getChildren()) {
////
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        })
////        DatabaseReference gameRef = rootRef.child("Allergies");
////        ValueEventListener valueEventListener = new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                for(DataSnapshot ds : dataSnapshot.getChildren()) {
////                    String url = ds.getValue(String.class);
////                    Log.d("TAG", url);
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////                Log.d(TAG, databaseError.getMessage());
////            }
////        };
////        gameRef.addListenerForSingleValueEvent(valueEventListener);
////    }
//}