package com.example.hospital_management.doctor.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hospital_management.R;
import com.example.hospital_management.models.Patient;

import java.util.ArrayList;


public class CustomPatientForDoctorAdapter extends RecyclerView.Adapter<CustomPatientForDoctorAdapter.ViewHolder> {

    private ArrayList<Patient> list;
    Context context;

    public void clearData() {
        list.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientName;
        TextView patientId;
        TextView bedNumber;
//        TextView assignedDoctor;
        TextView symptoms;
        Button assignMedicine;

        public ViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.patient_name);
//            patientId = (TextView) view.findViewById(R.id.patient_id);
            bedNumber = (TextView) view.findViewById(R.id.bed_no);
            symptoms = (TextView) view.findViewById(R.id.symptoms);
//            assignedDoctor = (TextView) view.findViewById(R.id.assigned_doctor);
            assignMedicine = view.findViewById(R.id.assign_medicine_button);
        }
    }

//    public CustomPatientForDoctorAdapter(ArrayList<Patient> list, Context context) {
//        this.list=list;
//        this.context=context;
//    }

    private final AdapterClickListener adapterClickListener;

    public CustomPatientForDoctorAdapter(ArrayList<Patient> list, Context context,AdapterClickListener listener) {
        this.adapterClickListener = listener;
        this.list=list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_patient_for_doctor, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final int index=viewHolder.getAdapterPosition();

        String name=list.get(position).getFirstName()+" "+list.get(position).getLastName();
        String id=list.get(position).getId();
        int bedNumber=list.get(position).getBedNo();
        String symptoms = list.get(position).getSymptomsString();

        viewHolder.patientName.setText(name);
        viewHolder.bedNumber.setText(String.valueOf(bedNumber));
        viewHolder.symptoms.setText(symptoms);


        viewHolder.assignMedicine.setOnClickListener(v -> {
            if (adapterClickListener != null && position != RecyclerView.NO_POSITION) {
                adapterClickListener.onItemClicked(position, id);
            }
        });

//        String assignedDoctor=list.get(position).getAssignedDoctorID();
//        viewHolder.assignedDoctor.setText(assignedDoctor);
//        viewHolder.patientId.setText(id);
    }


    //

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface AdapterClickListener {
        void onItemClicked(int position, String patientId);
    }
}
