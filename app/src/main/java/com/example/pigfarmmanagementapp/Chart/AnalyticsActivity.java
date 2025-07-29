package com.example.pigfarmmanagementapp.Chart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.Chart.ChartUtils.BarChartHelper;
import com.example.pigfarmmanagementapp.Chart.ChartUtils.ChartCheckUpStatusHelper;
import com.example.pigfarmmanagementapp.Chart.ChartUtils.ChartForIllnessPigsHelper;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.adapter.AnalyticsCategoriesAdapter;
import com.example.pigfarmmanagementapp.model.AnalyticsCategories;
import com.example.pigfarmmanagementapp.model.Cage;
import com.example.pigfarmmanagementapp.model.Pig;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    BarChart barChartView;
    List<Pig> pigList = new ArrayList<>();

    RecyclerView recyclerView;
    List<AnalyticsCategories> analyticsCategories = new ArrayList<>();


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

                int totalCages = 0;
                int totalPigs = 0;

                int vaccinated = 0;

                int ill = 0;

                int male = 0;
                int female = 0;

                int maleVaccinated = 0;
                int femaleVaccinated = 0;

                int maleNotVaccinated = 0;
                int femaleNotVaccinated = 0;

                int totalNotVaccinatedPigs = 0;
                int totalNoSickPigs = 0;


                for (DataSnapshot cageSnap : snapshot.getChildren()) {
                    Cage cage = cageSnap.getValue(Cage.class);

                    if(cage != null){
                        totalCages++;
                    }

                    for (DataSnapshot pigSnap : cageSnap.getChildren()) {
                        Pig pig = pigSnap.getValue(Pig.class);
                        if (pig != null) {
                            pigList.add(pig);
                            totalPigs++;

                            //Vaccinated Status
                            String status = pig.getVaccinationStatus();
                            if (status != null && !status.equals("Select Vaccines")
                                    && !status.equalsIgnoreCase("none") &&
                                    !status.trim().isEmpty()) {
                                vaccinated++;
                            }

                            if ("none".equalsIgnoreCase(status)) {
                                totalNotVaccinatedPigs++;
                            }

                            //Pig gender vaccinated
                            if ("male".equalsIgnoreCase(pig.getGender())
                                    && !status.equalsIgnoreCase("Select Vaccines")){
                                maleVaccinated++;
                            }

                            if ("female".equalsIgnoreCase(pig.getGender())
                                    && !status.equalsIgnoreCase("Select Vaccines")){
                                femaleVaccinated++;
                            }

                            //Pig gender not vaccinated
                            if ("male".equalsIgnoreCase(pig.getGender())
                                    && !status.equalsIgnoreCase("None")){
                                maleNotVaccinated++;
                            }

                            if ("female".equalsIgnoreCase(pig.getGender())
                                    && !status.equalsIgnoreCase("None")){
                                femaleNotVaccinated++;
                            }

                            //Pig illness status
                            if (pig.getPigIllness() != null &&
                                    !pig.getPigIllness().trim().isEmpty() &&
                                    !"none".equalsIgnoreCase(pig.getPigIllness().trim())) {
                                ill++;
                            }

                            if("none".equalsIgnoreCase(pig.getPigIllness())){
                                totalNoSickPigs++;
                            }

                            //Pig Gender Status
                            if ("Male".equalsIgnoreCase(pig.getGender())) {
                                male++;
                            } else if ("Female".equalsIgnoreCase(pig.getGender())) {
                                female++;
                            }

                        }

                    }

              }
                  barChartView = findViewById(R.id.groupedBarChart);
                categories();



                BarChartHelper.updateChart(barChartView, totalCages, totalPigs, vaccinated,
                        totalNotVaccinatedPigs, ill, totalNoSickPigs,
                        male, female);



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

    private void categories(){

        recyclerView = findViewById(R.id.recyclerView);

        int totalPigsCount = pigList.size();

        int vaccinated = 0;
        int notVaccinated = 0;

        int haveiIllness = 0;
        int noIllness = 0;

        for (Pig pig : pigList){
            String vaccinatedStatus = pig.getVaccinationStatus();
            String illnessStatus = pig.getPigIllness();

            if (vaccinatedStatus != null && !vaccinatedStatus.equalsIgnoreCase("none")
                    && !vaccinatedStatus.equalsIgnoreCase("Select Vaccines")
                    && !vaccinatedStatus.trim().isEmpty()) {
                vaccinated++;
            }else {
                notVaccinated++;
            }

            if (illnessStatus !=null &&
                    !illnessStatus.equalsIgnoreCase("none")
                    && !illnessStatus.equalsIgnoreCase("Select Illness")
                    && !illnessStatus.trim().isEmpty()){
                haveiIllness++;
            }
            else {
                noIllness++;
            }


        }

        // Add categorized items
        analyticsCategories.add(new AnalyticsCategories(
                "Vaccinated Pigs",
                "Vaccinated Metrics",
                R.drawable.vaccine_analytics,
                (int) ((vaccinated / (float) totalPigsCount) * 100)
        ));

        analyticsCategories.add(new AnalyticsCategories(
                "Illness Pigs",
                "Illness Metrics",
                R.drawable.illness_icon,
                (int) ((haveiIllness / (float) totalPigsCount) * 100)
        ));

        analyticsCategories.add(new AnalyticsCategories(
                "Pigs Checkup Status",
                "Checkup Metrics",
                R.drawable.checkup_icon,
                (int) ((haveiIllness / (float) totalPigsCount) * 100)
        ));


        // Setup RecyclerView
        AnalyticsCategoriesAdapter adapter = new AnalyticsCategoriesAdapter(this, analyticsCategories, category -> {
            if (category.getTitle().equalsIgnoreCase("Vaccinated Pigs")) {

                 startActivity(new Intent(this, ChartVaccinatedActivity.class));
            }

            if (category.getTitle().equalsIgnoreCase("Illness Pigs")) {

                startActivity(new Intent(this, ChartIlnessActivity.class));
            }

            if (category.getTitle().equalsIgnoreCase("Pigs Checkup Status")) {

                startActivity(new Intent(this, ChartCheckUpStatusActivity.class));
            }
        });



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

}
