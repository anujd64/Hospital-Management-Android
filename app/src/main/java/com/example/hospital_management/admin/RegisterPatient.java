package com.example.hospital_management.admin;

import static com.example.hospital_management.utils.Constants.DATABASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hospital_management.R;
import com.example.hospital_management.models.Doctor;
import com.example.hospital_management.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterPatient extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextMobile, editTextAddress, editTextSymptoms, editTextBedNo, editTextAssignedDoctor;
    private ProgressBar progressBar;
    private Button register;

    private FirebaseAuth mAuth;

    AutoCompleteTextView textview_doctorname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);


        editTextFirstName = (EditText) findViewById(R.id.firstName_patient);
        editTextLastName = (EditText) findViewById(R.id.lastName_patient);
        editTextEmail = (EditText) findViewById(R.id.email_patient);
        editTextPassword = (EditText) findViewById(R.id.password_patient);
        editTextAddress = (EditText) findViewById(R.id.address_patient);
        editTextMobile = (EditText) findViewById(R.id.mobile_patient);
        editTextSymptoms=(EditText)  findViewById(R.id.symptomps_patient);
        editTextBedNo=(EditText)  findViewById(R.id.bedNo);
        textview_doctorname = findViewById(R.id.loginActivity_textview_doctorName);


        progressBar = (ProgressBar)  findViewById(R.id.registerPatientIndeterminateProgressbar);
        register = (Button)  findViewById(R.id.buttonRegisterPatient);

        mAuth= FirebaseAuth.getInstance();
        getDoctors();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPatient();
            }
        });
        textview_doctorname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        populateDoctorsList();
//                    }
//                });
//                thread.setPriority(Thread.MAX_PRIORITY);
//                thread.start();

            }
        });
    }

    void registerPatient() {

        List<String> symp=new ArrayList<>();

        //Inputs
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String symptoms=editTextSymptoms.getText().toString().trim();
        int bedNo=Integer.parseInt(editTextBedNo.getText().toString());


        String assignedDoctorName = textview_doctorname.getText().toString().trim();

        String assignedDoctorID = getAssignedDoctorsId(assignedDoctorName);

        //Validations
        if (firstName.isEmpty()) {
            editTextFirstName.setError("First Name is required");
            editTextFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            // A person may or may not have his last name.
            editTextLastName.setText("");
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email address");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<8){
            editTextPassword.setError("Password should be 8 characters long");
            editTextPassword.requestFocus();
            return;
        }

        if (mobile.isEmpty()) {
            editTextMobile.setError("Contact number is required");
            editTextMobile.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(mobile).matches() || mobile.length()!=10) {
            editTextMobile.setError("Invalid contact no");
            editTextMobile.requestFocus();
            return;
        }

        if(symptoms.isEmpty()){
            editTextSymptoms.setText("");
        }
        else{
            String[] arr=symptoms.split(",");
            for(int i=0;i< arr.length;i++){
                symp.add(arr[i].trim());
            }
        }

        if (address.isEmpty()) {
            editTextAddress.setError("Address is required");
            editTextAddress.requestFocus();
            return;
        }
        if(bedNo>10 || bedNo<1){
            editTextBedNo.setError("Enter a bedNo from 1 to 10");
            editTextBedNo.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Patient patient=new Patient(uid,firstName,editTextLastName.getText().toString(),email,password,mobile,address,symp, bedNo, assignedDoctorID, assignedDoctorName,"fcmToken");
                            FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL+"/Patients/")
                                    .child(uid)
                                    .setValue(patient)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterPatient.this, "Registration Successful", Toast.LENGTH_LONG).show();

                                                RegisterPatient.this.finish();
                                            } else {
                                                Toast.makeText(RegisterPatient.this, "Something went wrong, Try Again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterPatient.this,"Something went wrong, Try Again!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    ArrayList<String> doctor_name=new ArrayList<>();
    ArrayList<Doctor> doctors=new ArrayList<>();
    HashMap<String, String> doctorNameIdHashMap = new HashMap<>();
    private String getAssignedDoctorsId(String assignedDoctor) {
        return doctorNameIdHashMap.get(assignedDoctor);
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
                                doctorNameIdHashMap.put(name, doctor.getId());
                            }
//                                if (!doctorHashMap.containsKey(doctor.getId())){
//                                    doctorHashMap.put(doctor.getId(),doctor);
//                                    doctor_name.add(name);
//                                }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RegisterPatient.this,"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });

        progressBar.setVisibility(View.GONE);
        //Setting up adapter
        ArrayAdapter<String> drNameAdapter=new ArrayAdapter<>(this, R.layout.dropdown_item,doctor_name);
        textview_doctorname.setAdapter(drNameAdapter);
        progressBar.setVisibility(View.GONE);
    }

}