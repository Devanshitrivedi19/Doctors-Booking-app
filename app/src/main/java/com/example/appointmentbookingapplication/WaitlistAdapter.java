package com.example.appointmentbookingapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WaitlistAdapter extends RecyclerView.Adapter<WaitlistAdapter.ViewHolder> {

    private final List<Appointment> waitlist;

    public WaitlistAdapter(List<Appointment> waitlist) {
        this.waitlist = waitlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waitlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = waitlist.get(position);
        holder.patientName.setText(appointment.getPatientName());
        holder.doctorName.setText(appointment.getDoctorName());
        holder.date.setText(appointment.getAppointmentDate());
        holder.time.setText(appointment.getAppointmentTime());
    }

    @Override
    public int getItemCount() {
        return waitlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientName, doctorName, date, time;

        public ViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.textPatientName);
            doctorName = itemView.findViewById(R.id.textDoctorName);
            date = itemView.findViewById(R.id.textDate);
            time = itemView.findViewById(R.id.textTime);
        }
    }
}
