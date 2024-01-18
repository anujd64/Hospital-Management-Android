package com.example.hospital_management.admin;

import static com.example.hospital_management.utils.Constants.DATABASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hospital_management.R;
import com.example.hospital_management.models.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;


public class RegisterAdmin extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextMobile, editTextAddress;
    private ProgressBar progressBar;
    private Button register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);


        editTextFirstName = (EditText) findViewById(R.id.firstName_admin);
        editTextLastName = (EditText) findViewById(R.id.lastName_admin);
        editTextEmail = (EditText) findViewById(R.id.email_admin);
        editTextPassword = (EditText) findViewById(R.id.password_admin);
        editTextAddress = (EditText) findViewById(R.id.address_admin);
        editTextMobile = (EditText) findViewById(R.id.mobile_admin);

        progressBar = (ProgressBar)  findViewById(R.id.registeradminIndeterminateProgressbar);
        register = (Button)  findViewById(R.id.buttonRegisteradmin);

        mAuth= FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeradmin();
            }
        });
    }

    void registeradmin() {

        List<String> symp=new ArrayList<>();

        //Inputs
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

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

        if (address.isEmpty()) {
            editTextAddress.setError("Address is required");
            editTextAddress.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Admin admin=new Admin(uid,firstName,editTextLastName.getText().toString(),email,password,mobile,address,"FCMToken");

                            FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL+"/Admins/")
                                    .child(uid)
                                    .setValue(admin)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterAdmin.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                                RegisterAdmin.this.finish();
                                            } else {
                                                Toast.makeText(RegisterAdmin.this, "Something went wrong, Try Again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterAdmin.this,"Something went wrong, Try Again!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}