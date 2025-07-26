package com.example.pigfarmmanagementapp.Chart.ChartUtils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartHelperForVaccinatedPigStatus {

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

    public static void showHorizontalBarChart(HorizontalBarChart chart, int maleCount, int femaleCount, String vaccineName) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, maleCount));   // Male
        entries.add(new BarEntry(1f, femaleCount)); // Female

        BarDataSet dataSet = new BarDataSet(entries, "Vaccinated Pigs");
        dataSet.setColors(new int[]{Color.parseColor("#2196F3"), Color.parseColor("#E91E63")});
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.4f);

        chart.setData(barData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0f) return "Male";
                else if (value == 1f) return "Female";
                else return "";
            }
        });

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setAxisMinimum(0f);
        chart.getAxisRight().setGranularity(1f);
        chart.getAxisRight().setTextSize(12f);

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false); // 🔻 Disable the description

        chart.setFitBars(true);
        chart.animateY(1000);
        chart.invalidate();
    }





}
