package com.example.d424vacationplanner.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424vacationplanner.R;
import com.example.d424vacationplanner.entities.Excursion;

import java.util.ArrayList;
import java.util.List;

// Code For Displaying With Recycler View for Excursions

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> excursions;

    public ExcursionAdapter(List<Excursion> excursions) {
        this.excursions = excursions;
    }

    // Listener variable
    private OnExcursionClickListener listener;

    // Listener interface
    public interface OnExcursionClickListener {
        void onExcursionClick(Excursion excursion);
    }

    // Setter method
    public void setOnExcursionClickListener(OnExcursionClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExcursionAdapter.ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single excursion item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionAdapter.ExcursionViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return excursions != null ? excursions.size() : 0;
    }

    // ViewHolder class, To create view holder objects. What you will see on the recycler view ?
    public static class ExcursionViewHolder extends RecyclerView.ViewHolder {
        TextView excursionName;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            excursionName = itemView.findViewById(R.id.textView);

        }
    }

    // Bind holder class after you fill in for view holder class. To fill the view holders with data.
    @Override
    public void onBindViewHolder(@NonNull ExcursionAdapter.ExcursionViewHolder holder, int position) {
        // Bind the data for this item
        Excursion excursion = excursions.get(position);
        holder.excursionName.setText(excursion.getExcursionTitle()); //from getters/setters from excursion entity

        // Adapter detects the click. Related to click listener on Vacation Activity file
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onExcursionClick(excursion);
            }
        });

    }

    // Notifies when list is modified, updated. List gets refreshed.
    public void setExcursions(List<Excursion> excursions) {
        if (excursions == null) {
            this.excursions = new ArrayList<>();
        } else {
            this.excursions = excursions;
        }
        notifyDataSetChanged();
    }

}
