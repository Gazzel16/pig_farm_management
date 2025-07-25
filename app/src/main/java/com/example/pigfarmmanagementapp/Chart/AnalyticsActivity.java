package com.example.pigfarmmanagementapp.Chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Cage;
import com.example.pigfarmmanagementapp.model.Pig;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analytics);

        DatabaseReference dbRefPigs = FirebaseDatabase.getInstance()
                .getReference("pigs");

        dbRefPigs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalPigs = 0;
                int vaccinated = 0;
                int ill = 0;
                int male = 0;
                int female = 0;

                for (DataSnapshot cageSnap : snapshot.getChildren()) {
                    for (DataSnapshot pigSnap : cageSnap.getChildren()) {
                        Pig pig = pigSnap.getValue(Pig.class);
                        if (pig != null) {
                            totalPigs++;

                            String status = pig.getVaccinationStatus();
                            if (status != null && !status.equals("Select Vaccines") && !status.equalsIgnoreCase("none") && !status.trim().isEmpty()) {
                                vaccinated++;
                            }


                            if (pig.getPigIllness() != null &&
                                    !pig.getPigIllness().trim().isEmpty() &&
                                    !"none".equalsIgnoreCase(pig.getPigIllness().trim())) {
                                ill++;
                            }

                            if ("Male".equalsIgnoreCase(pig.getGender())) {
                                male++;
                            } else if ("Female".equalsIgnoreCase(pig.getGender())) {
                                female++;
                            }
                        }
                    }
                }

                updateChart(totalPigs, vaccinated, ill, male, female);

                Log.d("Analytics", "Total pigs: " + totalPigs +
                        ", Vaccinated: " + vaccinated +
                        ", Ill: " + ill +
                        ", Male: " + male +
                        ", Female: " + female);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AnalyticsActivity.this, "Failed to load pigs", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void updateChart(int totalPigs, int vaccinated, int ill, int male, int female) {
        BarChart barChart = findViewById(R.id.groupedBarChart);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, totalPigs));     // Light Blue
        entries.add(new BarEntry(1, vaccinated));    // Green
        entries.add(new BarEntry(2, ill));           // Red
        entries.add(new BarEntry(3, male));          // Blue
        entries.add(new BarEntry(4, female));        // Pink

        BarDataSet dataSet = new BarDataSet(entries, "Pig Stats");
        dataSet.setColors(new int[]{
                Color.parseColor("#ADD8E6"), // Light Blue - Total Pigs
                Color.GREEN,                 // Green - Vaccinated
                Color.RED,                   // Red - Ill
                Color.BLUE,                  // Blue - Male
                Color.parseColor("#FFC0CB")  // Pink - Female
        });

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.invalidate();

        String[] labels = {"Total Pigs", "Vaccinated", "Ill", "Male", "Female"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
    }

}
