package com.example.appointmentbookingapplication;

// SpecialtiesAdapter.java
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class SpecialtiesAdapter extends RecyclerView.Adapter<SpecialtiesAdapter.ViewHolder>
{
    private List<String> specialties;
    private OnSpecialtyClickListener listener;
    private int selectedPosition = -1;
    public interface OnSpecialtyClickListener {
        void onSpecialtyClick(String specialty);
    }
    public SpecialtiesAdapter(List<String> specialties, OnSpecialtyClickListener listener) {
        this.specialties = specialties;
        this.listener = listener;
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
        String specialty = specialties.get(position);
        holder.specialtyTextView.setText(specialty);
// Update selection state
        if (selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.selected_specialty_background);
            holder.specialtyTextView.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.specialty_background);
            holder.specialtyTextView.setTextColor(Color.BLACK);
        }
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
// Update selection
                int previousSelected = selectedPosition;
                selectedPosition = adapterPosition;
// Notify changes
                if (previousSelected != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelected);
                }
                notifyItemChanged(selectedPosition);
                if (listener != null) {
                    listener.onSpecialtyClick(specialties.get(adapterPosition));
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return specialties.size();
    }
    public void clearSelection() {
        int previousSelected = selectedPosition;
        selectedPosition = -1;
        if (previousSelected != RecyclerView.NO_POSITION) {
            notifyItemChanged(previousSelected);
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView specialtyTextView;
        public ViewHolder(View view) {
            super(view);
            specialtyTextView = view.findViewById(R.id.specialtyTextView);
        }
    }
}
