package com.example.pigfarmmanagementapp.Chart;

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

import com.example.pigfarmmanagementapp.Chart.ChartUtils.BarChartHelper;
import com.example.pigfarmmanagementapp.Chart.ChartUtils.ChartFor_Illness_Vaccine;
import com.example.pigfarmmanagementapp.R;
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
    PieChart donutChartViewForVaccinatedPigs, donutChartPigsNotVaccinated;

    List<Pig> pigList = new ArrayList<>();

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
                donutChartViewForVaccinatedPigs = findViewById(R.id.donutChartPigsVaccinated);
                donutChartPigsNotVaccinated = findViewById(R.id.donutChartPigsNotVaccinated);

                BarChartHelper.updateChart(barChartView, totalCages, totalPigs, vaccinated,
                        totalNotVaccinatedPigs, ill, totalNoSickPigs,
                        male, female);


                ChartFor_Illness_Vaccine.donutChartSetUpForPigsVaccinated(donutChartViewForVaccinatedPigs,
                        maleVaccinated, femaleVaccinated);

                ChartFor_Illness_Vaccine.donutChartSetUpForNotVaccinatedPigs(donutChartPigsNotVaccinated,
                        maleNotVaccinated, femaleNotVaccinated);


              spinnerForVaccinatedPigs();
              spinnerForIllPigs();

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

    private void spinnerForVaccinatedPigs() {
        Spinner vaccineSpinner = findViewById(R.id.spinnerForVaccinatedPigs); // Ensure the ID matches your layout
        HorizontalBarChart barChartForVaccinatedData = findViewById(R.id.barChartForVaccinatedData); // Adjust ID accordingly

        String[] vaccinationStatus = {
                "Select Vaccine",
                "Mycoplasma hyopneumoniae (Enzootic Pneumonia)",
                "Erysipelothrix rhusiopathiae (Swine Erysipelas)",
                "Actinobacillus pleuropneumoniae (APP)",
                "Lawsonia intracellularis (Ileitis)",
                "Salmonella spp.",
                "Porcine Circovirus Type 2 (PCV2)",
                "Porcine Reproductive and Respiratory Syndrome (PRRS)",
                "Classical Swine Fever (CSF)",
                "Foot-and-Mouth Disease (FMD)",
                "Pseudorabies (Aujeszky’s Disease)",
                "Swine Influenza (SIV)",
                "Clostridium perfringens Type C",
                "Escherichia coli (Neonatal Scours)",
                "Tetanus",
                "Others"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vaccinationStatus);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccineSpinner.setAdapter(adapter);

        vaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedVaccine = vaccinationStatus[position];

                if (!selectedVaccine.equals("Select Vaccine")) {
                    int maleCount = 0;
                    int femaleCount = 0;

                    for (Pig pig : pigList) { // Ensure pigList is accessible (class field or passed here)
                        if (pig.getVaccinationStatus().contains(selectedVaccine)) {
                            if (pig.getGender().equalsIgnoreCase("Male")) {
                                maleCount++;
                            } else if (pig.getGender().equalsIgnoreCase("Female")) {
                                femaleCount++;
                            }
                        }
                    }

                    ChartFor_Illness_Vaccine.chartForPigsVaccinatedData(
                            barChartForVaccinatedData,
                            maleCount,
                            femaleCount
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: you can clear the chart here
            }
        });
    }

    private void spinnerForIllPigs(){
        Spinner illnessSpinner = findViewById(R.id.spinnerForIllPigs);
        HorizontalBarChart horizontalChartForIllPig = findViewById(R.id.horizontalChartForIllPig);

        String[] pigIllness = {
                "Select Illness",
                "Swine Dysentery",
                "Swine Flu (Influenza)",
                "Mycoplasma Pneumonia",
                "Actinobacillus Pleuropneumonia (APP)",
                "Erysipelas",
                "Salmonellosis",
                "Clostridial Infections",
                "Tetanus",
                "Neonatal Diarrhea (Scours)",
                "Porcine Circovirus Associated Disease (PCVAD)",
                "Foot-and-Mouth Disease (FMD)",
                "Classical Swine Fever (CSF)",
                "Pseudorabies (Aujeszky's Disease)",
                "Leptospirosis",
                "Gastric Ulcers",
                "Greasy Pig Disease",
                "Mastitis Metritis Agalactia (MMA)",
                "Internal Parasites (e.g., Ascaris suum)",
                "None"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pigIllness);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        illnessSpinner.setAdapter(adapter);

        illnessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedIllness = pigIllness[position];

                if(!selectedIllness.equalsIgnoreCase("Select Illness")){
                    int maleCount = 0;
                    int femaleCount = 0;

                    for (Pig pig : pigList){
                        if (pig.getPigIllness().contains(selectedIllness)){
                         if (pig.getGender().equalsIgnoreCase("male")){
                             maleCount++;
                         }else if (pig.getGender().equalsIgnoreCase("female")){
                             femaleCount++;
                         }
                        }
                    }

                    ChartFor_Illness_Vaccine.chartForPigIllnessData(horizontalChartForIllPig, maleCount, femaleCount);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}
