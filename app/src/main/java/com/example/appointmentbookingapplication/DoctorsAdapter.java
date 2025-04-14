package com.example.appointmentbookingapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    private final List<Doctor> doctors;

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

        if (doctor != null) {
            holder.nameTextView.setText(doctor.getName());
            holder.specialityTextView.setText(doctor.getSpeciality());
            holder.ratingTextView.setText(String.valueOf(doctor.getRating()));
            holder.experienceTextView.setText(doctor.getExperience());
            holder.doctorImageView.setImageResource(doctor.getImageResId());

            // Handle View Profile button click
            holder.viewProfileButton.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("doctor_name", doctor.getName());
                intent.putExtra("speciality", doctor.getSpeciality());
                intent.putExtra("rating", doctor.getRating());
                intent.putExtra("experience", doctor.getExperience());
                intent.putExtra("image_res_id", doctor.getImageResId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return doctors != null ? doctors.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTextView;
        public final TextView specialityTextView;
        public final TextView ratingTextView;
        public final TextView experienceTextView;
        public final ImageView doctorImageView;
        public final Button viewProfileButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            specialityTextView = view.findViewById(R.id.specialityTextView);
            ratingTextView = view.findViewById(R.id.ratingTextView);
            experienceTextView = view.findViewById(R.id.experienceTextView);
            doctorImageView = view.findViewById(R.id.doctorImageView);
            viewProfileButton = view.findViewById(R.id.viewProfileButton);
        }
    }
}
