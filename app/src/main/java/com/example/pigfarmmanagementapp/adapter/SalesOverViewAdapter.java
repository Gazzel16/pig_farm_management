package com.example.pigfarmmanagementapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Pig;

import java.util.List;

public class SalesOverViewAdapter extends RecyclerView.Adapter<SalesOverViewAdapter.PigSaleViewHolder> {

    private Context context;
    private List<Pig> pigSaleList;

    public SalesOverViewAdapter(Context context, List<Pig> pigSaleList) {
        this.context = context;
        this.pigSaleList = pigSaleList;
    }

    @NonNull
    @Override
    public PigSaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sales_overview_item, parent, false); // Replace with your XML filename
        return new PigSaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PigSaleViewHolder holder, int position) {
        Pig pigSale = pigSaleList.get(position);

        holder.buyerName.setText("Buyer: " + pigSale.getBuyerName());
        holder.buyerContact.setText("Contact: " + pigSale.getBuyerContact());
        holder.datePurchase.setText("Date Purchase: " + pigSale.getPurchaseDateTime());
        holder.pigBreed.setText("Pig Bought: " + pigSale.getBreed());
        holder.pigId.setText("Pig ID: " + pigSale.getId());
    }

    @Override
    public int getItemCount() {
        return pigSaleList.size();
    }

    public static class PigSaleViewHolder extends RecyclerView.ViewHolder {
        TextView buyerName, buyerContact, datePurchase, pigBreed, pigId;

        public PigSaleViewHolder(@NonNull View itemView) {
            super(itemView);
            buyerName = itemView.findViewById(R.id.buyerName);
            buyerContact = itemView.findViewById(R.id.buyerContact);
            datePurchase = itemView.findViewById(R.id.timeDateSold);
            pigBreed = itemView.findViewById(R.id.pigBreed);
            pigId = itemView.findViewById(R.id.pigId);
        }
    }
}

