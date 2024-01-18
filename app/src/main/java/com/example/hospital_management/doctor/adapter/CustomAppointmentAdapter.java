package com.example.hospital_management.doctor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hospital_management.R;
import com.example.hospital_management.models.Appointment;

import java.util.ArrayList;
import java.util.List;


public class CustomAppointmentAdapter extends RecyclerView.Adapter<CustomAppointmentAdapter.ViewHolder> {

    private ArrayList<Appointment> list;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTime;
        TextView patientName;
        TextView doctorName;
        TextView symptomsMentioned;

        Button cancelButton;


        public ViewHolder(View view) {
            super(view);
            dateTime= (TextView) view.findViewById(R.id.appointment_date_time);
            patientName = (TextView) view.findViewById(R.id.appointment_patient_name);
            doctorName = (TextView) view.findViewById(R.id.appointment_doctor_name);
            symptomsMentioned = (TextView) view.findViewById(R.id.appointment_symptoms_mentioned);
            cancelButton = view.findViewById(R.id.cancel_button);
        }
    }

    private final AppointmentAdapterClickListener adapterClickListener;

    public CustomAppointmentAdapter(ArrayList<Appointment> list, Context context, AppointmentAdapterClickListener adapterClickListener) {
        this.list=list;
        this.context=context;
        this.adapterClickListener = adapterClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_appointment, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final int index=viewHolder.getAdapterPosition();

        String dateTime=list.get(position).getDate()+"\n"+list.get(position).getTime();
        String patientName=list.get(position).getPatientName();
        String doctorId=list.get(position).getDoctorId();
        String doctorName=list.get(position).getDoctorName();
        StringBuilder symptoms = new StringBuilder();
        List<String> symptomList = list.get(position).getSymptoms();
        for (String s : symptomList) {
            symptoms.append(s).append(", ");
        }
        Log.d("Symptoms",symptoms.toString());

        viewHolder.dateTime.setText(dateTime);
        viewHolder.patientName.setText(patientName);
        viewHolder.doctorName.setText(doctorName);
        viewHolder.symptomsMentioned.setText(symptoms.toString());

        viewHolder.cancelButton.setOnClickListener(v -> {
            if (adapterClickListener != null && position != RecyclerView.NO_POSITION) {
                adapterClickListener.onItemClicked(position, doctorId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearData(){
        list.clear();
    }

    public interface AppointmentAdapterClickListener {
        void onItemClicked(int position, String doctorId);
    }
}
