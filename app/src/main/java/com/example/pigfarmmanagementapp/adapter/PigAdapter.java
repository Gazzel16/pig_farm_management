package com.example.pigfarmmanagementapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Pig;

import java.util.ArrayList;
import java.util.List;

public class PigAdapter extends RecyclerView.Adapter<PigAdapter.PigViewHolder> implements Filterable {

    private List<Pig> pigList;
    private List<Pig> pigListFull;

    public PigAdapter(List<Pig> pigList) {
        this.pigList = pigList;
        this.pigListFull = new ArrayList<>(pigList); // Make a copy for filtering
    }

    @NonNull
    @Override
    public PigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pig, parent, false);
        return new PigViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PigViewHolder holder, int position) {
        Pig pig = pigList.get(position);

        // Bind the breed, weight, and status to the respective TextViews
        holder.tvPigBreed.setText("Breed: " + pig.getBreed());
        holder.tvPigWeight.setText("Weight: " + pig.getWeight() + " kg");
        holder.tvPigStatus.setText("Status: " + pig.vaccinationStatus());
    }

    @Override
    public int getItemCount() {
        return pigList.size();
    }

    static class PigViewHolder extends RecyclerView.ViewHolder {
        TextView tvPigBreed, tvPigWeight, tvPigStatus;

        PigViewHolder(View itemView) {
            super(itemView);
            tvPigBreed = itemView.findViewById(R.id.tvPigBreed);
            tvPigWeight = itemView.findViewById(R.id.tvPigWeight);
            tvPigStatus = itemView.findViewById(R.id.tvPigStatus);
        }
    }

    @Override
    public Filter getFilter() {
        return pigFilter;
    }

    private Filter pigFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Pig> filteredList = new ArrayList<>();
            String filterPattern = constraint.toString().toLowerCase().trim();
            Log.d("FilterDebug", "Filtering for: " + filterPattern);  // Log the filter input

            // If the filter is empty, show all pigs
            if (filterPattern.isEmpty()) {
                filteredList.addAll(pigListFull);  // All pigs in the full list
            } else {
                // Filter pigs by breed
                for (Pig pig : pigListFull) {
                    if (pig.getBreed().toLowerCase().contains(filterPattern)) {
                        filteredList.add(pig);
                    }
                }
            }

            // Set up the results to return
            FilterResults results = new FilterResults();
            results.values = filteredList;
            Log.d("FilterDebug", "Filtered list size: " + filteredList.size());  // Log the filtered list size
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                pigList.clear();
                pigList.addAll((List<Pig>) results.values);  // Add the filtered pigs to the adapter list
                notifyDataSetChanged();  // Notify the adapter to update the UI
            }
        }
    };


}
