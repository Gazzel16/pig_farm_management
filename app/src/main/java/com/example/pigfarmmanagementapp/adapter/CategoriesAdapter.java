package com.example.pigfarmmanagementapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Categories;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private List<Categories> categoriesList;
    private OnItemClickListener listener;

    public CategoriesAdapter(List<Categories> categoriesList, OnItemClickListener clickListener) {
        this.categoriesList = categoriesList;
        this.listener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Categories category);
    }
    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_item, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Categories category = categoriesList.get(position);
        holder.imageRes.setImageResource(category.getimageRes());
        holder.titleTextView.setText(category.getTitle());
        holder.descriptionTextView.setText(category.getDescription());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(category));
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView imageRes;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRes = itemView.findViewById(R.id.imageRes);
            titleTextView = itemView.findViewById(R.id.title);
            descriptionTextView = itemView.findViewById(R.id.description);
        }
    }
}
