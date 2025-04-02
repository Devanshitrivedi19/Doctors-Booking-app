package com.example.appointmentbookingapplication;

// SpecialtiesAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SpecialtiesAdapter extends RecyclerView.Adapter<SpecialtiesAdapter.ViewHolder> {
    private List<String> specialties;

    public SpecialtiesAdapter(List<String> specialties) {
        this.specialties = specialties;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_speciality, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.specialtyTextView.setText(specialties.get(position));
    }

    @Override
    public int getItemCount() {
        return specialties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView specialtyTextView;

        public ViewHolder(View view) {
            super(view);
            specialtyTextView = view.findViewById(R.id.specialtyTextView);
        }
    }
}

