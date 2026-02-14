package com.example.d424vacationplanner.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424vacationplanner.R;
import com.example.d424vacationplanner.entities.Vacation;

import java.util.ArrayList;
import java.util.List;

// Code For Displaying With Recycler View for Vacations

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacations;

    public VacationAdapter(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    // Listener variable
    private OnVacationClickListener listener;

    // Listener interface
    public interface OnVacationClickListener {
        void onVacationClick(Vacation vacation);
    }

    // Setter method
    public void setOnVacationClickListener(OnVacationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single vacation item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return vacations != null ? vacations.size() : 0;
    }


    // ViewHolder class, To create view holder objects. What you will see on the recycler view ?
    public static class VacationViewHolder extends RecyclerView.ViewHolder {
        TextView vacationName;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationName = itemView.findViewById(R.id.textView);

        }
    }


    // Bind holder class after you fill in for view holder class. To fill the view holders with data.
    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        // Bind the data for this item
        Vacation vacation = vacations.get(position);
        holder.vacationName.setText(vacation.getVacationTitle()); //from getters/setters from vacation entity

        // Adapter detects the click. Related to click listener on Vacation Activity file
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVacationClick(vacation);
            }
        });

    }


    // Notifies when list is modified, updated. List gets refreshed on recycler view.
    public void setVacations(List<Vacation> vacations) {
        if (vacations == null) {
            this.vacations = new ArrayList<>();
        } else {
            this.vacations = vacations;
        }
        notifyDataSetChanged();
    }

}
