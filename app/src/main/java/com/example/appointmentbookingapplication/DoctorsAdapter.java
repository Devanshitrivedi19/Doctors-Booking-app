package com.example.appointmentbookingapplication;

// DoctorsAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {
    private List<Doctor> doctors;

    public DoctorsAdapter(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.nameTextView.setText(doctor.getName());
        holder.specialityTextView.setText(doctor.getSpeciality());
        holder.ratingTextView.setText(String.valueOf(doctor.getRating()));
        holder.experienceTextView.setText(doctor.getExperience());
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, specialityTextView, ratingTextView, experienceTextView;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            specialityTextView = view.findViewById(R.id.specialityTextView);
            ratingTextView = view.findViewById(R.id.ratingTextView);
            experienceTextView = view.findViewById(R.id.experienceTextView);
        }
    }
}