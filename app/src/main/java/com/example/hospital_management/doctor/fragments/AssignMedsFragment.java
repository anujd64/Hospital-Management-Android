package com.example.hospital_management.doctor.fragments;

import static com.example.hospital_management.utils.Constants.DATABASE_URL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospital_management.R;
import com.example.hospital_management.models.Medicine;
import com.example.hospital_management.models.Prescription;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AssignMedsFragment extends Fragment {

    private ArrayList<Medicine> medicines;
    private Button sendPrescription;
    private String patientID = "";

    public AssignMedsFragment() {
        // Required empty public constructor
    }

    public AssignMedsFragment(String patientID) {
        this.patientID=patientID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment_assign_meds, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medicines = new ArrayList<>();
        sendPrescription = view.findViewById(R.id.send_prescription_button);


        // Find all the checkboxes, buttons, and text views for each medicine
        for (int i = 1; i <= 6; i++) {
            int checkBoxId = getResources().getIdentifier("checkboxMedicine" + i, "id", getActivity().getPackageName());
            int btnDecreaseId = getResources().getIdentifier("btnDecrease" + i, "id", getActivity().getPackageName());
            int tvDosageId = getResources().getIdentifier("tvDosage" + i, "id", getActivity().getPackageName());
            int btnIncreaseId = getResources().getIdentifier("btnIncrease" + i, "id", getActivity().getPackageName());

            CheckBox checkBox = view.findViewById(checkBoxId);
            Button btnDecrease = view.findViewById(btnDecreaseId);
            TextView tvDosage = view.findViewById(tvDosageId);
            Button btnIncrease = view.findViewById(btnIncreaseId);

            // Set initial quantity to 0 for each medicine
            final Medicine medicine = new Medicine(checkBox.getText().toString(), 0);
            medicines.add(medicine);

            // Handle decrease button click
            btnDecrease.setOnClickListener(v -> {
                int dosage = medicine.getDosage();
                if (dosage > 0) {
                    dosage--;
                    medicine.setDosage(dosage);
                    tvDosage.setText(String.valueOf(dosage));
                }
            });

            // Handle increase button click
            btnIncrease.setOnClickListener(v -> {
                int dosage = medicine.getDosage();
                dosage++;
                medicine.setDosage(dosage);
                tvDosage.setText(String.valueOf(dosage));
            });

            // Handle checkbox click
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Do something when checkbox is checked or unchecked
                // For example, you can enable/disable dosage buttons based on checkbox state
                btnDecrease.setEnabled(isChecked);
                btnIncrease.setEnabled(isChecked);
            });
        }
        sendPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrescription();
            }
        });
    }

//    private void removeFragment() {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.popBackStack();
//    }

    void savePrescription() {

        String doctorID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Prescription prescription = new Prescription(doctorID, patientID, medicines);

        FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL + "/Prescriptions/")
                .child(patientID)
                .setValue(prescription)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Prescription Sent Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Something went wrong, Try Again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

