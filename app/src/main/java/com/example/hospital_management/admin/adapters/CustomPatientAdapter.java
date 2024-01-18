package com.example.hospital_management.admin.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospital_management.R;
import com.example.hospital_management.models.Patient;

import java.util.ArrayList;


public class CustomPatientAdapter extends RecyclerView.Adapter<CustomPatientAdapter.ViewHolder> {

    private ArrayList<Patient> list;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientName;
//        TextView patientId;
        TextView bedNumber;
        TextView assignedDoctor;
        Button delete;


        public ViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.patient_name);
//            patientId = (TextView) view.findViewById(R.id.patient_id);
            bedNumber = (TextView) view.findViewById(R.id.bed_no);
            assignedDoctor = (TextView) view.findViewById(R.id.assigned_doctor);
            delete = view.findViewById(R.id.delete_button);
        }
    }

    private final CustomPatientAdapter.AdapterClickListener adapterClickListener;

    public CustomPatientAdapter(ArrayList<Patient> list, Context context, AdapterClickListener adapterClickListener) {
        this.list=list;
        this.context=context;
        this.adapterClickListener = adapterClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_patient, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final int index=viewHolder.getAdapterPosition();

        String name=list.get(position).getFirstName()+" "+list.get(position).getLastName();
        String id=list.get(position).getId();
        int bedNumber=list.get(position).getBedNo();
        String assignedDoctor=list.get(position).getAssignedDoctorName();

        viewHolder.patientName.setText(name);
//        viewHolder.patientId.setText(id);
        viewHolder.bedNumber.setText(String.valueOf(bedNumber));
        viewHolder.assignedDoctor.setText(assignedDoctor);
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterClickListener.onItemClicked(position,id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface AdapterClickListener {
        void onItemClicked(int position, String patientId);
    }
}
