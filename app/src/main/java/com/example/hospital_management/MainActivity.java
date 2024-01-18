package com.example.hospital_management;

import static com.example.hospital_management.utils.Constants.ADMIN;
import static com.example.hospital_management.utils.Constants.DATABASE_URL;
import static com.example.hospital_management.utils.Constants.DOCTOR;
import static com.example.hospital_management.utils.Constants.PATIENT;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospital_management.admin.ForgotPassword;
import com.example.hospital_management.admin.RegisterAdmin;
import com.example.hospital_management.admin.loginAdmin;
import com.example.hospital_management.doctor.loginDoctor;
import com.example.hospital_management.models.Admin;
import com.example.hospital_management.models.Doctor;
import com.example.hospital_management.models.Patient;
import com.example.hospital_management.patient.loginPatient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail,editTextPassword;
    private TextView textViewErrorLogin,textViewForgotPassword,textViewErrorSelectUser,textViewInvalidUser,textViewEmailVerification;
    private ProgressBar progressBar;
    private Button buttonLogin,buttonPatient,buttonDoctor, buttonAdmin;
    private RadioButton patientRadio,doctorRadio, adminRadio;

    private FirebaseAuth mAuth;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();

        mAuth=FirebaseAuth.getInstance();
        preferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

        loginIfCredentialsPresent();

        askNotificationPermission();
    }

    private void initWidgets() {
        editTextEmail= findViewById(R.id.loginPageEmailEditText);
        editTextPassword= findViewById(R.id.loginPagePasswordEditText);
        textViewErrorLogin= findViewById(R.id.textViewErrorLogin);
        textViewErrorSelectUser= findViewById(R.id.textViewSelectRadio);
        textViewInvalidUser= findViewById(R.id.textViewInvalidUser);
        textViewEmailVerification= findViewById(R.id.textViewEmailNotVerified);
        progressBar= findViewById(R.id.mainActivityIndeterminateProgressbar);

        patientRadio= findViewById(R.id.radioPatient);
        doctorRadio= findViewById(R.id.radioDoctor);
        adminRadio= findViewById(R.id.radioAdmin);

        buttonLogin= findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(this);

//        buttonPatient=(Button) findViewById(R.id.buttonPatient);
//        buttonPatient.setOnClickListener(this);
//
//        buttonDoctor=(Button) findViewById(R.id.buttonDoctor);
//        buttonDoctor.setOnClickListener(this);

        buttonAdmin= findViewById(R.id.buttonAdmin);
        buttonAdmin.setOnClickListener(this);

        textViewForgotPassword= findViewById(R.id.textViewForgotPassword);
        textViewForgotPassword.setOnClickListener(this);

        animationDrawable2 = (AnimationDrawable) buttonLogin.getBackground();
        animationDrawable2.setEnterFadeDuration(2000);
        animationDrawable2.setExitFadeDuration(4000);
        animationDrawable2.start();

        animationDrawable3 = (AnimationDrawable) buttonAdmin.getBackground();
        animationDrawable3.setEnterFadeDuration(2000);
        animationDrawable3.setExitFadeDuration(4000);
        animationDrawable3.start();
    }

    AnimationDrawable animationDrawable2;
    AnimationDrawable animationDrawable3;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        animationDrawable2.stop();
        animationDrawable3.stop();
    }

    private void loginIfCredentialsPresent() {
        if(mAuth.getCurrentUser()!=null){
            switch (getUserType()){
                case DOCTOR:
                    startActivity(new Intent(this,loginDoctor.class));
                    this.finish();
                    break;
                case PATIENT:
                    startActivity(new Intent(this,loginPatient.class));
                    this.finish();
                    break;
                case ADMIN:
                    startActivity(new Intent(this,loginAdmin.class));
                    this.finish();
                    break;
            }
        }
    }

    private int getUserType() {
        int userType = preferences.getInt("userType", 0);
        return userType;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.buttonPatient:
//                startActivity(new Intent(this, RegisterPatient.class));
//                break;
//
//            case R.id.buttonDoctor:
//                startActivity(new Intent(this, RegisterDoctor.class));
//                break;

            case R.id.buttonAdmin:
                startActivity(new Intent(this, RegisterAdmin.class));
                break;

            case R.id.login_button:
                textViewInvalidUser.setVisibility(View.GONE);
                textViewErrorLogin.setVisibility(View.GONE);
                textViewErrorSelectUser.setVisibility(View.GONE);
                textViewEmailVerification.setVisibility(View.INVISIBLE);
                loginUser();
                break;

            case R.id.textViewForgotPassword:
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
                break;
        }
    }

    void loginUser(){
        String email=editTextEmail.getText().toString();
        String password=editTextPassword.getText().toString();

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


        //If everything goes right
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            boolean flag=false;

                            if(patientRadio.isChecked()){
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL+"/Patients/");
                                FirebaseUser user=mAuth.getCurrentUser();
                                String uid=user.getUid();


                                 reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                         Patient patient=snapshot.getValue(Patient.class);
                                         progressBar.setVisibility(View.GONE);

                                         if(patient==null){
                                             textViewEmailVerification.setVisibility(View.INVISIBLE);    //Because loginButton is constrained to this layout
                                             textViewInvalidUser.setVisibility(View.VISIBLE);
                                         }
                                         else{

                                             getFCMToken(PATIENT,uid);

                                             SharedPreferences.Editor editor = preferences.edit();
                                             editor.putString("userID", uid);
                                             editor.putString("userFirstName", patient.getFirstName());
                                             editor.putString("userLastName", patient.getLastName());
                                             editor.putString("userDoctorId", patient.getAssignedDoctorID());
                                             editor.putInt("userBedNo", patient.getBedNo());
                                             editor.putInt("userType", PATIENT);
                                             editor.apply();

                                             if(true){
                                                 Intent intent=new Intent(MainActivity.this, loginPatient.class);
                                                 startActivity(intent);
                                                 MainActivity.this.finish();
                                             }
                                             else{
                                                 user.sendEmailVerification();
                                                 textViewEmailVerification.setVisibility(View.VISIBLE);
                                             }
                                         }
                                     }
                                     @Override
                                     public void onCancelled(@NonNull DatabaseError error) {
                                         progressBar.setVisibility(View.GONE);
                                         Toast.makeText(MainActivity.this, "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                                     }
                                 });
                                 flag=true;
                            }

                            if(doctorRadio.isChecked()){
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL+"/Doctors/");
                                FirebaseUser user=mAuth.getCurrentUser();
                                String uid=user.getUid();


                                reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Doctor doctor=snapshot.getValue(Doctor.class);
                                        progressBar.setVisibility(View.GONE);

                                        if(doctor==null){
                                            textViewEmailVerification.setVisibility(View.INVISIBLE);    //Because loginButton is constrained to this layout
                                            textViewInvalidUser.setVisibility(View.VISIBLE);
                                        }
                                        else{

                                            getFCMToken(DOCTOR,uid);

                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("userID", uid);
                                            editor.putString("userFirstName", doctor.getFirstName());
                                            editor.putString("userLastName", doctor.getLastName());
                                            editor.putInt("userType", DOCTOR);
                                            editor.apply();

                                            if(true){
                                                Intent intent=new Intent(MainActivity.this, loginDoctor.class);
                                                startActivity(intent);
                                                MainActivity.this.finish();
                                            }
                                            else{
                                                user.sendEmailVerification();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this, "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                flag=true;
                            }

                            if(adminRadio.isChecked()){
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL+"/Admins/");
                                FirebaseUser user=mAuth.getCurrentUser();
                                String uid=user.getUid();


                                reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Admin admin=snapshot.getValue(Admin.class);
                                        progressBar.setVisibility(View.GONE);

                                        if(admin==null){
                                            textViewEmailVerification.setVisibility(View.INVISIBLE);    //Because loginButton is constrained to this layout
                                            textViewInvalidUser.setVisibility(View.VISIBLE);
                                        }
                                        else{

                                            getFCMToken(ADMIN,uid);

                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("userID", uid);
                                            editor.putString("userFirstName", admin.getFirstName());
                                            editor.putString("userLastName", admin.getLastName());
                                            editor.putInt("userType", ADMIN);
                                            editor.apply();

                                            if(true){
                                                Intent intent=new Intent(MainActivity.this, loginAdmin.class);
                                                startActivity(intent);
                                                MainActivity.this.finish();
                                            }
                                            else{
                                                user.sendEmailVerification();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this, "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                flag=true;
                            }

                            //if nothing is selected
                            if(!flag){
                                progressBar.setVisibility(View.GONE);
                                textViewEmailVerification.setVisibility(View.INVISIBLE);    //Because loginButton is constrained to this layout
                                textViewErrorSelectUser.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            textViewEmailVerification.setVisibility(View.INVISIBLE);    //Because loginButton is constrained to this layout
                            textViewErrorLogin.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }


    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.

                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    String fcmToken;
    private void getFCMToken(int userType, String uid) {
        preferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        // Get the FCM token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fcmToken = task.getResult();
                        // Save FCM token in SharedPreferences
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("fcmToken", fcmToken);
                        editor.apply();
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });

        fcmToken = preferences.getString("fcmToken","sp");


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL);

        switch (userType){
            case PATIENT:
                myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL).child("Patients");
                break;
            case DOCTOR:
                myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL).child("Doctors");
                break;
            case ADMIN:
                myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL).child("Admins");
                break;

        }

        // Update the value
        myRef.child(uid).child("fcmToken").setValue(fcmToken, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(MainActivity.this,"Updated FCM token", Toast.LENGTH_LONG).show();
            }
        });

    }
}