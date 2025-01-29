package com.example.pigfarmmanagementapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Cage;

import java.util.List;

public class CageAdapter extends RecyclerView.Adapter<CageAdapter.CageViewHolder> {

    private List<Cage> cageList;
    private OnCageClickListener listener;

    public CageAdapter(List<Cage> cageList, OnCageClickListener listener) {
        this.cageList = cageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cage_item, parent, false);
        return new CageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CageViewHolder holder, int position) {
        Cage cage = cageList.get(position);
        holder.bind(cage, listener);
        holder.tvCageName.setText("Cage Name: " + cage.getName());
        holder.tvCageStatus.setText("Cage Status: " + cage.getStatus());
        holder.tvCageId.setText("Cage Id: " + cage.getId());
    }

    @Override
    public int getItemCount() {
        return cageList.size();
    }

    public interface OnCageClickListener {
        void onCageClick(Cage cage);
    }

    static class CageViewHolder extends RecyclerView.ViewHolder {

        TextView tvCageName, tvCageStatus, tvCageId;

        public CageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCageName = itemView.findViewById(R.id.tvCageName);
            tvCageStatus = itemView.findViewById(R.id.tvCageStatus);
            tvCageId = itemView.findViewById(R.id.tvCageId);
        }

        void bind(Cage cage, OnCageClickListener listener) {
            tvCageName.setText(cage.getName());
            itemView.setOnClickListener(v -> listener.onCageClick(cage));
        }
    }
}
