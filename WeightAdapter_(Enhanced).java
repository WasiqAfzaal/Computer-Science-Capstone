package com.WasiqAfzaal.Mobile2App;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightViewHolder> {

    public interface OnWeightActionListener {
        void onEdit(WeightEntry entry);
        void onDelete(WeightEntry entry);
    }

    private final List<WeightEntry> data;
    private final List<WeightEntry> filteredData; // secondary list for filtering
    private final OnWeightActionListener listener;

    public WeightAdapter(List<WeightEntry> data, OnWeightActionListener listener) {
        this.data = data;
        this.filteredData = new ArrayList<>(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weight_row, parent, false);
        return new WeightViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightViewHolder holder, int position) {
        WeightEntry entry = filteredData.get(position);

        holder.textRowDate.setText(entry.date);
        holder.textRowWeight.setText(String.valueOf(entry.weight));

        holder.buttonRowEdit.setOnClickListener(v -> listener.onEdit(entry));
        holder.buttonRowDelete.setOnClickListener(v -> listener.onDelete(entry));
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    // Updated for CS 499: Enhancement Two: Algorithms and Data Structures - 03-26-2026

    // Filters weight entries based on a search query (date or weight)
    public void filter(String query) {
        filteredData.clear();

        if (query == null || query.trim().isEmpty()) {
            filteredData.addAll(data);
        } else {
            String lowerQuery = query.toLowerCase();

            for (WeightEntry entry : data) {
                // Check if date or weight matches query
                if (entry.date.toLowerCase().contains(lowerQuery) ||
                        String.valueOf(entry.weight).contains(lowerQuery)) {
                    filteredData.add(entry);
                }
            }
        }

        notifyDataSetChanged();
    }

    static class WeightViewHolder extends RecyclerView.ViewHolder {
        TextView textRowDate, textRowWeight;
        Button buttonRowEdit, buttonRowDelete;

        public WeightViewHolder(@NonNull View itemView) {
            super(itemView);
            textRowDate = itemView.findViewById(R.id.textRowDate);
            textRowWeight = itemView.findViewById(R.id.textRowWeight);
            buttonRowEdit = itemView.findViewById(R.id.buttonRowEdit);
            buttonRowDelete = itemView.findViewById(R.id.buttonRowDelete);
        }
    }
}