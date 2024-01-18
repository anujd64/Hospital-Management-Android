package com.example.hospital_management.patient.fragments;

import static com.example.hospital_management.utils.Constants.DATABASE_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hospital_management.R;
import com.example.hospital_management.models.Appointment;
import com.example.hospital_management.models.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BookAppointmentFragment extends Fragment implements View.OnClickListener {

    ArrayList<String> doctor_name=new ArrayList<>();
    ArrayList<Doctor> doctors=new ArrayList<>();
    HashMap<String, String> doctorIdNameHashMap = new HashMap<>();
    EditText edittext_date, edittext_time, edittext_allergies;
    Button my_profile, view_doctors, book_appointment, view_appointment_history, submit_appointment_form, load_fees, reset_form;
    AutoCompleteTextView textview_doctor_name, textview_fees;
    ProgressBar progressBar;

    public BookAppointmentFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_book_appointment, container, false);
        preferences = getContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        initWidgets(view);
        getDoctors();
        return view;
    }

    private void initWidgets(View view) {
        progressBar=view.findViewById(R.id.loginActivityIndeterminateProgressbar);

        submit_appointment_form=view.findViewById(R.id.buttonViewSubmitAppointmentForm);
        reset_form=view.findViewById(R.id.buttonViewResetAppointmentForm);

        textview_doctor_name=view.findViewById(R.id.loginActivity_textview_doctorName);
        textview_fees=view.findViewById(R.id.loginActivity_textview_FeeDetail);
        edittext_date=view.findViewById(R.id.loginActivity_Date);
        edittext_time=view.findViewById(R.id.loginActivity_Time);
        edittext_allergies=view.findViewById(R.id.loginActivity_Allergy);
        load_fees=view.findViewById(R.id.buttonLoadFees);

        load_fees.setOnClickListener(this);
        submit_appointment_form.setOnClickListener(this);
        reset_form.setOnClickListener(this);

    }

    void getDoctors(){
            //RetrievingDoctor's List
            progressBar.setVisibility(View.VISIBLE);
            FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL + "/Doctors/")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot data:snapshot.getChildren()){
                                Doctor doctor=data.getValue(Doctor.class);
                                String name=doctor.getFirstName()+" "+doctor.getLastName();
                                if(!doctor_name.contains(name)){
                                    doctor_name.add(name);
                                    doctors.add(doctor);
                                    doctorIdNameHashMap.put(doctor.getId(),name);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(),"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    });

            progressBar.setVisibility(View.GONE);
            //Setting up adapter
            ArrayAdapter<String> drNameAdapter=new ArrayAdapter<>(getContext(), R.layout.dropdown_item,doctor_name);
            textview_doctor_name.setAdapter(drNameAdapter);
            progressBar.setVisibility(View.GONE);
    }

    void submitAppointment(){
        String drName=textview_doctor_name.getText().toString();
        String fees=textview_fees.getText().toString();
        String date= edittext_date.getText().toString();
        String time=edittext_time.getText().toString();
        String allergies=edittext_allergies.getText().toString();

        if(drName.isEmpty()){
            textview_doctor_name.setError("Doctor name is required");
            textview_doctor_name.requestFocus();
            return;
        }

        if(fees.isEmpty()){
            textview_fees.setError("Error in loading fees, Try again later!");
            textview_fees.requestFocus();
            return;
        }

        if(date.isEmpty()){
            edittext_date.setError("Date is required");
            edittext_date.requestFocus();
            return;
        }

        if(!checkValidDate(date)){
            edittext_date.setError("Enter valid date");
            edittext_date.requestFocus();
            return;
        }

        if(time.isEmpty()){
            edittext_time.setError("Time is required");
            edittext_time.requestFocus();
            return;
        }


        if(!checkValidTime(time)){
            edittext_time.setError("Enter valid time");
            edittext_time.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
//        String patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String patientId = preferences.getString("userID","user");
        String doctorId = "";
        for (Doctor d : doctors) {
            if ((d.getFirstName()+" "+d.getLastName()).equals(drName)){
                doctorId = d.getId(); 
            }
        }
        String patientName = getPatientName();
        String doctorName = getDoctorName(doctorId);
        
        ArrayList<String> symptomsList = null;
        String[] arr = allergies.split(",");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Arrays.stream(arr).map(string ->
                    symptomsList.add(string));
        }
        Appointment appointment=new Appointment(patientId,doctorId,patientName,doctorName,date,time,symptomsList);
        FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL + "/Appointments/")
                .child(doctorId)
                .setValue(appointment)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(),"Appointment Booked",Toast.LENGTH_LONG).show();
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(),"Something went wrong, Try again!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private String getDoctorName(String doctorId) {
        return doctorIdNameHashMap.get(doctorId);
    }

    String patientName = "";
    SharedPreferences preferences;
    private String getPatientName() {
        preferences = getContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        String name = preferences.getString("userFirstName", "FirstName")+" "+preferences.getString("userLastName", "LastName");
        return name;
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/Patients/");
//        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//
//        assert user != null;
//        String uid=user.getUid();
//
//        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressBar.setVisibility(View.GONE);
//
//                Patient patient=snapshot.getValue(Patient.class);
//                if(patient==null){
//                    Toast.makeText(getContext(),"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    patientName = patient.getFirstName()+" "+patient.getLastName();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getContext(),"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return patientName;
    }

    boolean checkValidDate(String s){
        boolean flag=false;
        if(s.length()!=10)
            return false;
        if(s.charAt(2)!='/' || s.charAt(5)!='/'){
            return false;
        }
        for(int i=0;i<s.length();i++){
            Character ch=s.charAt(i);
            if((ch>=48 && ch<=57) || ch=='/'){
                flag=true;
            }
            else{
                return false;
            }
        }
        return flag;
    }

    boolean checkValidTime(String s){
        boolean flag=false;

        if(s.length()!=8){
            return false;
        }
        if(s.charAt(2)!=':' || s.charAt(5)!=' ' || !(s.charAt(6)=='A' || s.charAt(6)=='P') ||s.charAt(7)!='M'){
            return false;
        }

        for(int i=0;i<5;i++){
            if(i==2)
                continue;

            char ch=s.charAt(i);
            if((ch>=48 && ch<=57)){
                flag=true;
            }
            else{
                return false;
            }
        }
        return flag;
    }

    void resetValues(){
        textview_doctor_name.setText("");
        textview_fees.setText("");
        edittext_date.setText("");
        edittext_time.setText("");
        edittext_allergies.setText("");
    }

    void loadFees(){

        for(Doctor doctor: doctors){
            String name=doctor.getFirstName()+" "+doctor.getLastName();
            if(textview_doctor_name.getText().toString().equals(name)){
                Toast.makeText(getContext(),"Fees Applicable::"+doctor.getDoctorFees(),Toast.LENGTH_LONG).show();
                textview_fees.setText(Integer.toString(doctor.getDoctorFees()));
            }
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.loginActivity_textview_doctorName) {
            textview_fees.setText("");
        } else if (viewId == R.id.buttonLoadFees) {
            loadFees();
        } else if (viewId == R.id.buttonViewSubmitAppointmentForm) {
            submitAppointment();
        } else if (viewId == R.id.buttonViewResetAppointmentForm) {
            resetValues();
        }

    }
}