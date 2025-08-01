package com.example.pigfarmmanagementapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.adapter.SalesOverViewAdapter;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PigPurchaseHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private SalesOverViewAdapter salesOverViewAdapter;
    List<Pig> pigSaleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pig_purchase_history);

        DatabaseReference salesRef = FirebaseDatabase.getInstance().getReference("pigs");

        salesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pigSaleList.clear(); // Clear old data
                for (DataSnapshot cageSnap : snapshot.getChildren()) {
                    for (DataSnapshot pigSnap : cageSnap.getChildren()) {
                        Pig pig = pigSnap.getValue(Pig.class);
                        if (pig != null && pig.isPurchase()) {
                            pigSaleList.add(pig);
                        }
                    }
                }
                salesOverViewAdapter.notifyDataSetChanged(); // Update the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        salesOverViewAdapter = new SalesOverViewAdapter(this, pigSaleList);
        recyclerView.setAdapter(salesOverViewAdapter);
    }
}