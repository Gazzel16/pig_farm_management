package com.example.pigfarmmanagementapp.Chart.ChartUtils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartForIllnessPigsHelper {
    public static void chartForPigIllnessData(BarChart chart, int maleCount, int femaleCount){

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, maleCount));
        entries.add(new BarEntry(1f, femaleCount));

        BarDataSet barDataSet = new BarDataSet(entries, "Pig Illness");

        barDataSet.setColors(new int[]{
                Color.parseColor("#2196F3"), //Female color
                Color.parseColor("#E91E63") //Male Color
        });

        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTextColor(Color.GRAY);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.4f);

        chart.setData(barData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.GRAY);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0f) return "Male";
                else if (value == 1f) return "Female";
                else return "";
            }
        });

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);
        chart.setData(data);

        chart.setFitBars(true);
        chart.invalidate();

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDragEnabled(false);
        chart.setFitBars(true);
        chart.animateY(1000);
        chart.invalidate();
    }

    public static void donutChartSetUpForPigsIllness (PieChart pieChart, int maleVaccinated, int femaleVaccinated){
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(maleVaccinated, "Male Vaccinated"));
        entries.add(new PieEntry(femaleVaccinated, "Female Vaccinated"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
                Color.parseColor("#3F51B5"), // Blue for Male
                Color.parseColor("#E91E63")  // Red for Female
        );
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Make it a donut
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(40f);

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

    public static void donutChartSetUpForNoIllness (PieChart pieChart, int maleNotVaccinated, int femaleNotVaccinated){
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(maleNotVaccinated, "Male Not Vaccinated"));
        entries.add(new PieEntry(femaleNotVaccinated, "Female Not Vaccinated"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
                Color.parseColor("#00695C"), // Blue for Male
                Color.parseColor("#880E4F")  // Red for Female
        );
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Make it a donut
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(40f);

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
