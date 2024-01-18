package com.example.hospital_management.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.hospital_management.doctor.fragments.DoctorHomeFragment;
import com.example.hospital_management.doctor.fragments.DoctorProfileFragment;
import com.example.hospital_management.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class loginDoctor extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        replaceFragment(new DoctorHomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_doctor);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId =  item.getItemId();

                if(menuItemId == R.id.doctor_home){
                    replaceFragment(new DoctorHomeFragment());
                    return true;
                }
                else if (menuItemId ==  R.id.doctor_profile) {
                    replaceFragment(new DoctorProfileFragment());
                    return true;
                }
//                else if (menuItemId ==  R.id.doctor_patients) {
//                    replaceFragment(new DoctorPatientsFragment());
//                    return true;
//                }
                return true;
            }
        });


    }

    private  void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    //    Button my_profile, view_patients_assigned, book_appointment, view_appointment_history, submit_appointment_form, load_fees, reset_form, sign_out;
//    TextView welcome_text, textview_name, textview_email, textview_address, textview_contact, textview_usertype, textview_degree, textview_specialization;
//    AutoCompleteTextView textview_doctorname, textview_fees;
//    RelativeLayout layout_myprofile, layout_bookappointment;
//    ProgressBar progressBar;
//    EditText edittext_date, edittext_time, edittext_allergies;
//
//    DatabaseReference reference;
//    FirebaseUser user;
//    String uid,doctorId;
//
//    //Doctor's List
//    ArrayList<String> doctor_name=new ArrayList<>();
//    ArrayList<Doctor> doctors=new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_doctor);
//
//        initWidgets();
//
//
//
//
//        progressBar.setVisibility(View.VISIBLE);
//
//        reference= FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/Doctors/");
//        user= FirebaseAuth.getInstance().getCurrentUser();
//
//        assert user != null;
//        uid=user.getUid();
//
//        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressBar.setVisibility(View.GONE);
//
//                Doctor doctor=snapshot.getValue(Doctor.class);
//                if(doctor==null){
//                    Toast.makeText(loginDoctor.this,"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    welcome_text.setText("Welcome, " + doctor.getFirstName());
//                    textview_name.setText(doctor.getFirstName() + " " + doctor.getLastName());
//                    textview_email.setText(doctor.getEmailAddress());
//                    textview_usertype.setText("Doctor");
//                    textview_degree.setText(doctor.getDegree());
//                    textview_specialization.setText(doctor.getSpecialization());
//                    textview_contact.setText(doctor.getMobileNumber());
//                    textview_address.setText(doctor.getAddress());
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(loginDoctor.this,"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void initWidgets() {
//        progressBar=findViewById(R.id.loginActivityIndeterminateProgressbar);
//
//        my_profile=findViewById(R.id.buttonMyProfile);
//        view_patients_assigned=findViewById(R.id.buttonViewPatients);
//        book_appointment=findViewById(R.id.buttonScheduleAppointment);
//        view_appointment_history=findViewById(R.id.buttonViewAppointmentHistory);
//        submit_appointment_form=findViewById(R.id.buttonViewSubmitAppointmentForm);
//        reset_form=findViewById(R.id.buttonViewResetAppointmentForm);
//        sign_out=findViewById(R.id.buttonViewSignOut);
//
//        welcome_text=findViewById(R.id.welcome_textView);
//        textview_name=findViewById(R.id.textViewName);
//        textview_email=findViewById(R.id.textViewEmail);
//        textview_address=findViewById(R.id.textViewAddress);
//        textview_contact=findViewById(R.id.textViewContact);
//        textview_usertype=findViewById(R.id.textViewUserType);
//        textview_degree=findViewById(R.id.textViewDegree);
//        textview_specialization=findViewById(R.id.textViewSpecialization);
//
//        layout_myprofile=findViewById(R.id.loginDoctor_myprofilelayout);
//        layout_bookappointment=findViewById(R.id.layoutScheduleAppointment);
//
//        textview_doctorname=findViewById(R.id.loginActivity_textview_doctorName);
//        textview_fees=findViewById(R.id.loginActivity_textview_FeeDetail);
//        edittext_date=findViewById(R.id.loginActivity_Date);
//        edittext_time=findViewById(R.id.loginActivity_Time);
//        edittext_allergies=findViewById(R.id.loginActivity_Allergy);
//        load_fees=findViewById(R.id.buttonLoadFees);
//
//        my_profile.setOnClickListener(loginDoctor.this);
//        view_patients_assigned.setOnClickListener(loginDoctor.this);
//        book_appointment.setOnClickListener(loginDoctor.this);
//        load_fees.setOnClickListener(loginDoctor.this);
//        submit_appointment_form.setOnClickListener(loginDoctor.this);
//        reset_form.setOnClickListener(loginDoctor.this);
//        textview_doctorname.setOnClickListener(loginDoctor.this);
//        sign_out.setOnClickListener(loginDoctor.this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.buttonMyProfile:
//                if(layout_myprofile.getVisibility()!=View.VISIBLE){
//                    layout_myprofile.setVisibility(View.VISIBLE);
//                    //Setting other one gone
//                    layout_bookappointment.setVisibility(View.GONE);
//                }
//                else{
//                    layout_myprofile.setVisibility(View.GONE);
//                }
//                break;
//
//            case R.id.buttonViewDoctors:
//                progressBar.setVisibility(View.VISIBLE);
//                Intent intent=new Intent(loginDoctor.this, ViewDoctorsList.class);
//                startActivity(intent);
//                progressBar.setVisibility(View.GONE);
//                break;
//
////            case R.id.buttonScheduleAppointment:
////                scheduleAppointment();
////                break;
////
//            case R.id.loginActivity_textview_doctorName:
//                textview_fees.setText("");
//                break;
////
////            case R.id.buttonLoadFees:
////                loadFees();
////                break;
////
////            case R.id.buttonViewSubmitAppointmentForm:
////                submitAppointment();
////                break;
////
////            case R.id.buttonViewResetAppointmentForm:
////                resetValues();
////                break;
//
//            case R.id.buttonViewSignOut:
//                signOutUser();
//        }
//
//    }
//
////    void scheduleAppointment(){
////        if(layout_bookappointment.getVisibility()!=View.VISIBLE){
////            //RetrievingDoctor's List
////            progressBar.setVisibility(View.VISIBLE);
////            FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/Doctors/")
////                    .addValueEventListener(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot snapshot) {
////                            for(DataSnapshot data:snapshot.getChildren()){
////                                Doctor doctor=data.getValue(Doctor.class);
////                                String name=doctor.getFirstName()+" "+doctor.getLastName();
////                                if(!doctor_name.contains(name)){
////                                    doctor_name.add(name);
////                                    doctors.add(doctor);
////                                }
////                            }
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError error) {
////                            Toast.makeText(loginDoctor.this,"Something went wrong, Try Again!", Toast.LENGTH_SHORT).show();
////                        }
////                    });
////
////            progressBar.setVisibility(View.GONE);
////            //Setting up adapter
////            ArrayAdapter<String> drNameAdapter=new ArrayAdapter<>(loginDoctor.this,R.layout.dropdown_item,doctor_name);
////            textview_doctorname.setAdapter(drNameAdapter);
////            progressBar.setVisibility(View.GONE);
////
////            //Setting layouts
////            layout_bookappointment.setVisibility(View.VISIBLE);
////        }
////        else{
////            layout_bookappointment.setVisibility(View.GONE);
////        }
////
////        //Setting other one gone
////        layout_myprofile.setVisibility(View.GONE);
////    }
//
//
//    void signOutUser(){
//        progressBar.setVisibility(View.VISIBLE);
//        FirebaseAuth.getInstance().signOut();
//        progressBar.setVisibility(View.GONE);
//
//        startActivity(getParentActivityIntent());
//    }


    //TODO

    //    void submitAppointment(){
//        String drName=textview_doctorname.getText().toString();
//        String fees=textview_fees.getText().toString();
//        String date= edittext_date.getText().toString();
//        String time=edittext_time.getText().toString();
//        String allergies=edittext_allergies.getText().toString();
//
//        if(drName.isEmpty()){
//            textview_doctorname.setError("Doctor name is required");
//            textview_doctorname.requestFocus();
//            return;
//        }
//
//        if(fees.isEmpty()){
//            textview_fees.setError("Error in loading fees, Try again later!");
//            textview_fees.requestFocus();
//            return;
//        }
//
//        if(date.isEmpty()){
//            edittext_date.setError("Date is required");
//            edittext_date.requestFocus();
//            return;
//        }
//
//        if(!checkValidDate(date)){
//            edittext_date.setError("Enter valid date");
//            edittext_date.requestFocus();
//            return;
//        }
//
//        if(time.isEmpty()){
//            edittext_time.setError("Time is required");
//            edittext_time.requestFocus();
//            return;
//        }
//
//
//        if(!checkValidTime(time)){
//            edittext_time.setError("Enter valid time");
//            edittext_time.requestFocus();
//            return;
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
//        String patientId=FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        ArrayList<String> allergiesList = null;
//        String[] arr = allergies.split(",");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Arrays.stream(arr).map(string ->
//                    allergiesList.add(string));
//        }
//        Appointment appointment=new Appointment(patientId,doctorId,date,time,allergiesList);
//        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.DATABASE_URL + "/Appointments/")
//                .child(patientId)
//                .setValue(appointment)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(loginDoctor.this,"Appointment Booked",Toast.LENGTH_LONG).show();
//                        }
//                        else{
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(loginDoctor.this,"Something went wrong, Try again!",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }

//   boolean checkValidDate(String s){
//        boolean flag=false;
//        if(s.length()!=10)
//            return false;
//        if(s.charAt(2)!='/' || s.charAt(5)!='/'){
//            return false;
//        }
//        for(int i=0;i<s.length();i++){
//            Character ch=s.charAt(i);
//            if((ch>=48 && ch<=57) || ch=='/'){
//                flag=true;
//            }
//            else{
//                return false;
//            }
//        }
//        return flag;
//    }
//
//    boolean checkValidTime(String s){
//        boolean flag=false;
//
//        if(s.length()!=8){
//            return false;
//        }
//        if(s.charAt(2)!=':' || s.charAt(5)!=' ' || !(s.charAt(6)=='A' || s.charAt(6)=='P') ||s.charAt(7)!='M'){
//            return false;
//        }
//
//        for(int i=0;i<5;i++){
//            if(i==2)
//                continue;
//
//            Character ch=s.charAt(i);
//            if((ch>=48 && ch<=57)){
//                flag=true;
//            }
//            else{
//                return false;
//            }
//        }
//        return flag;
//    }

//    void resetValues(){
//        textview_doctorname.setText("");
//        textview_fees.setText("");
//        edittext_date.setText("");
//        edittext_time.setText("");
//        edittext_allergies.setText("");
//    }
//
//    void loadFees(){
//        for(Doctor doctor: doctors){
//            String name=doctor.getFirstName()+" "+doctor.getLastName();
//            if(textview_doctorname.getText().toString().equals(name)){
//                Toast.makeText(loginDoctor.this,"Fees Applicable::"+doctor.getDoctorFees(),Toast.LENGTH_LONG);
//                textview_fees.setText(Integer.toString(doctor.getDoctorFees()));
//            }
//        }
//    }

}