package com.example.pigfarmmanagementapp.Chart;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.Chart.ChartUtils.ChartForPigSalesOverViewHelper;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.adapter.SalesOverViewAdapter;
import com.example.pigfarmmanagementapp.model.Pig;
import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PigsSalesOverviewActivity extends AppCompatActivity {

    BarChart barChart;
    List<Pig> pigList = new ArrayList<>();

    RecyclerView recyclerView;

    private SalesOverViewAdapter salesOverViewAdapter;
    List<Pig> pigSaleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pigs_sales_overview);

        barChart = findViewById(R.id.barChart);

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

        pigSaleOverView();
    }

    public void pigSaleOverView(){
        DatabaseReference pigRef = FirebaseDatabase.getInstance()
                .getReference("pigs");
        pigRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sold = 0;
                int notSold = 0;

                int malePigSold = 0;
                int femalePigSold = 0;

                int malePigNotSold = 0;
                int femalePigNotSold = 0;

                for (DataSnapshot cageSnap : snapshot.getChildren()){
                    for (DataSnapshot pigSnap : cageSnap.getChildren()){
                        Pig pig = pigSnap.getValue(Pig.class);

                        if (pig != null){
                            pigList.add(pig);

                            boolean salesOverViewStatus = pig.isPurchase();
                            if (!salesOverViewStatus){
                                notSold++;
                            }else {
                                sold++;
                            }

                            String gender = pig.getGender();
                            if (!salesOverViewStatus && gender.equalsIgnoreCase("Male")){
                                malePigSold++;
                            }else {
                                malePigNotSold++;
                            }

                            if (!salesOverViewStatus && gender.equalsIgnoreCase("Female")){
                                femalePigSold++;
                            }else {
                                femalePigNotSold++;
                            }
                        }

                    }
                }

                ChartForPigSalesOverViewHelper.salesOverView(barChart, sold, notSold, malePigSold, malePigNotSold, femalePigSold, femalePigNotSold);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}