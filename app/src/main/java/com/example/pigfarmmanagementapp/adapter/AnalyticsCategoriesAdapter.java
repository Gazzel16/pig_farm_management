package com.example.pigfarmmanagementapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.AnalyticsCategories;

import java.util.List;

public class AnalyticsCategoriesAdapter extends RecyclerView.Adapter<AnalyticsCategoriesAdapter.ViewHolder> {

    private Context context;
    private List<AnalyticsCategories> itemList;

    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(AnalyticsCategories category);
    }

    public AnalyticsCategoriesAdapter(Context context, List<AnalyticsCategories> items, OnItemClickListener listener) {
        this.context = context;
        this.itemList = items;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView icon;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            icon = itemView.findViewById(R.id.icon);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public AnalyticsCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.analytic_item_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnalyticsCategoriesAdapter.ViewHolder holder, int position) {
        AnalyticsCategories item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.icon.setImageResource(item.getIconResId());
        holder.progressBar.setProgress(item.getProgress());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
