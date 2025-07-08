package com.example.pigfarmmanagementapp.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.PigStatusAdvice;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PigStatusAdviceAdapter extends RecyclerView.Adapter<PigStatusAdviceAdapter.ViewHolder> {

    private List<PigStatusAdvice> statusList;


    public PigStatusAdviceAdapter(List<PigStatusAdvice> statusList) {
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pig_status_advice_item, parent, false); // This is your XML layout name
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PigStatusAdvice status = statusList.get(position);
        holder.tempResult.setText("Temperature: " + status.getTemperature() + "° C");
        holder.humidResult.setText("Humidity: " + status.getHumidity() + "%");
        holder.stressLevel.setText("Stress Level: " + status.getStressLevel());
        holder.advisory.setText("Advisory: " + status.getAdvisory());

    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tempResult, humidResult, stressLevel, advisory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tempResult = itemView.findViewById(R.id.tempResult);
            humidResult = itemView.findViewById(R.id.humidResult);
            stressLevel = itemView.findViewById(R.id.stressLevel);
            advisory = itemView.findViewById(R.id.advisory);
        }
    }
}

