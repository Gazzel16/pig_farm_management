package com.example.pigfarmmanagementapp.Chart;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pigfarmmanagementapp.Chart.ChartUtils.ChartForIllnessPigsHelper;
import com.example.pigfarmmanagementapp.Chart.ChartUtils.ChartForVaccinatedPigsHelper;
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

import java.util.ArrayList;
import java.util.List;

public class ChartIlnessActivity extends AppCompatActivity {

    BarChart barChartView;
    PieChart donutChartPigsSick, donutChartPigsNoSick;

    List<Pig> pigList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.chart_ilness);

        DatabaseReference dbRefPigs = FirebaseDatabase.getInstance()
                .getReference("pigs");

        dbRefPigs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                int maleVaccinated = 0;
                int femaleVaccinated = 0;

                int maleNotVaccinated = 0;
                int femaleNotVaccinated = 0;


                for (DataSnapshot cageSnap : snapshot.getChildren()) {
                    Cage cage = cageSnap.getValue(Cage.class);


                    for (DataSnapshot pigSnap : cageSnap.getChildren()) {
                        Pig pig = pigSnap.getValue(Pig.class);
                        if (pig != null) {
                            pigList.add(pig);

                            //Vaccinated Status
                            String status = pig.getPigIllness();

                            //Pig gender vaccinated
                            if ("male".equalsIgnoreCase(pig.getGender())
                                    && !status.equalsIgnoreCase("Select Illness")){
                                maleVaccinated++;
                            }

                            if ("female".equalsIgnoreCase(pig.getGender())
                                    && !status.equalsIgnoreCase("Select Illness")){
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


                        }

                    }

                }
                donutChartPigsNoSick = findViewById(R.id.donutChartPigsNotSick);
                donutChartPigsSick = findViewById(R.id.donutChartPigsSick);


                ChartForIllnessPigsHelper.donutChartSetUpForPigsIllness(donutChartPigsSick,
                        maleVaccinated, femaleVaccinated);

                ChartForIllnessPigsHelper.donutChartSetUpForNoIllness(donutChartPigsNoSick,
                        maleNotVaccinated, femaleNotVaccinated);


                spinnerForIllnessPigs();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChartIlnessActivity.this, "Failed to load pigs", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void spinnerForIllnessPigs() {
        Spinner vaccineSpinner = findViewById(R.id.spinnerForIllnessPigs); // Ensure the ID matches your layout
        BarChart barChartForIlnessdData = findViewById(R.id.barChartForIlnessdData); // Adjust ID accordingly

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
        vaccineSpinner.setAdapter(adapter);

        vaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedIllness = pigIllness[position];

                if (!selectedIllness.equals("Select Illness")) {
                    int maleCount = 0;
                    int femaleCount = 0;

                    for (Pig pig : pigList) { // Ensure pigList is accessible (class field or passed here)
                        if (pig.getPigIllness().contains(selectedIllness)) {
                            if (pig.getGender().equalsIgnoreCase("Male")) {
                                maleCount++;
                            } else if (pig.getGender().equalsIgnoreCase("Female")) {
                                femaleCount++;
                            }
                        }
                    }

                    ChartForIllnessPigsHelper.chartForPigIllnessData(
                            barChartForIlnessdData,
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
}