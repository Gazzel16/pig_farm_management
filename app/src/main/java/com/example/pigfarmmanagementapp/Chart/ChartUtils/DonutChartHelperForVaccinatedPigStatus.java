package com.example.pigfarmmanagementapp.Chart.ChartUtils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class DonutChartHelperForVaccinatedPigStatus {

    //Pigs Vaccinated Chart
    public static void donutChartSetUpForPigsVaccinated (PieChart pieChart, int maleVaccinated, int femaleVaccinated){
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(maleVaccinated, "Male Vaccinated"));
        entries.add(new PieEntry(femaleVaccinated, "Female Vaccinated"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
                Color.parseColor("#3F51B5"), // Blue for Male
                Color.parseColor("#E91E63")  // Red for Female
        );
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Make it a donut
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(65f);

        // Optional: center text
        pieChart.setCenterText("Vaccinated");
        pieChart.setCenterTextSize(13f);
        pieChart.setCenterTextColor(Color.DKGRAY);

        // Other styling
        Description description = new Description();
        description.setText(""); // no description label
        pieChart.setDescription(description);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate(); // refresh
    }

    public static void donutChartSetUpForNotVaccinatedPigs (PieChart pieChart, int maleNotVaccinated, int femaleNotVaccinated){
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(maleNotVaccinated, "Male Not Vaccinated"));
        entries.add(new PieEntry(femaleNotVaccinated, "Female Not Vaccinated"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
                Color.parseColor("#00695C"), // Blue for Male
                Color.parseColor("#880E4F")  // Red for Female
        );
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Make it a donut
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(65f);

        // Optional: center text
        pieChart.setCenterText("Not Vaccinated");
        pieChart.setCenterTextSize(13f);
        pieChart.setCenterTextColor(Color.DKGRAY);

        // Other styling
        Description description = new Description();
        description.setText(""); // no description label
        pieChart.setDescription(description);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate(); // refresh
    }

}
