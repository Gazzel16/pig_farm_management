package com.example.pigfarmmanagementapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Cage;
import com.example.pigfarmmanagementapp.model.Pig;

import java.util.List;

public class CageAdapter extends RecyclerView.Adapter<CageAdapter.CageViewHolder> {

    private List<Cage> cageList;
    private OnCageClickListener listener;
    private List<Pig> pigList;
    public CageAdapter(List<Cage> cageList, List<Pig> pigList, OnCageClickListener listener) {
        this.cageList = cageList;
        this.pigList = pigList; // Add this line
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

        int pigCount = 0;

        for (Pig pig : pigList){
            if (pig.getCageId().equals(cage.getId())){
                pigCount++;
            }
        }

        holder.tvPigsCount.setText("Pigs: " + pigCount);
    }

    @Override
    public int getItemCount() {
        return cageList.size();
    }

    public interface OnCageClickListener {
        void onCageClick(Cage cage);
    }

    static class CageViewHolder extends RecyclerView.ViewHolder {

        TextView tvCageName, tvCageStatus, tvCageId, tvPigsCount;

        public CageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCageName = itemView.findViewById(R.id.tvCageName);
            tvCageStatus = itemView.findViewById(R.id.tvCageStatus);
            tvCageId = itemView.findViewById(R.id.tvCageId);
            tvPigsCount = itemView.findViewById(R.id.tvPigsCount);
        }

        void bind(Cage cage, OnCageClickListener listener) {
            tvCageName.setText(cage.getName());
            itemView.setOnClickListener(v -> listener.onCageClick(cage));
        }
    }
}
