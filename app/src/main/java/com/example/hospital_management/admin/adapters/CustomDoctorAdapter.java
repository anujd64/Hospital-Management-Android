package com.example.hospital_management.admin.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hospital_management.R;
import com.example.hospital_management.models.Doctor;

import java.util.ArrayList;


public class CustomDoctorAdapter extends RecyclerView.Adapter<CustomDoctorAdapter.ViewHolder> {

    private ArrayList<Doctor> list;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView drName;
        TextView drDegree;
        TextView drSpecialization;
        Button delete;

        public ViewHolder(View view) {
            super(view);
            drName = (TextView) view.findViewById(R.id.doctoritem_textViewName);
            drDegree = (TextView) view.findViewById(R.id.doctoritem_textViewDegree);
            drSpecialization = (TextView) view.findViewById(R.id.doctoritem_textViewSpecialization);
            delete = view.findViewById(R.id.delete_button);
        }
    }

    private final CustomDoctorAdapter.AdapterClickListener adapterClickListener;

    public CustomDoctorAdapter(ArrayList<Doctor> list, Context context, AdapterClickListener adapterClickListener) {
        this.list=list;
        this.context=context;
        this.adapterClickListener = adapterClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_doctor, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final int index=viewHolder.getAdapterPosition();

        String id = list.get(position).getId();
        String name=list.get(position).getFirstName()+" "+list.get(position).getLastName();
        String degree=list.get(position).getDegree();
        String specialization=list.get(position).getSpecialization();

        viewHolder.drName.setText(name);
        viewHolder.drDegree.setText(degree);
        viewHolder.drSpecialization.setText(specialization);

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterClickListener.onItemClicked(position, id);
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
