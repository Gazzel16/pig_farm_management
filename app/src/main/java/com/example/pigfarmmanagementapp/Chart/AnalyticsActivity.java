package com.example.pigfarmmanagementapp.Chart;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pigfarmmanagementapp.Chart.ChartUtils.BarChartHelper;
import com.example.pigfarmmanagementapp.Chart.ChartUtils.DonutChartHelperForVaccinatedPigStatus;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Cage;
import com.example.pigfarmmanagementapp.model.Pig;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnalyticsActivity extends AppCompatActivity {

    BarChart barChartView;
    PieChart donutChartViewForVaccinatedPigs, donutChartPigsNotVaccinated;

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
                donutChartViewForVaccinatedPigs = findViewById(R.id.donutChartPigsVaccinated);
                donutChartPigsNotVaccinated = findViewById(R.id.donutChartPigsNotVaccinated);

                BarChartHelper.updateChart(barChartView, totalCages, totalPigs, vaccinated,
                        totalNotVaccinatedPigs, ill, totalNoSickPigs,
                        male, female);

                DonutChartHelperForVaccinatedPigStatus.donutChartSetUpForPigsVaccinated(donutChartViewForVaccinatedPigs,
                        maleVaccinated, femaleVaccinated);

                DonutChartHelperForVaccinatedPigStatus.donutChartSetUpForNotVaccinatedPigs(donutChartPigsNotVaccinated,
                        maleNotVaccinated, femaleNotVaccinated);


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



}
