package com.example.pigfarmmanagementapp.Chart.ChartUtils;

import android.graphics.Color;

import com.example.pigfarmmanagementapp.model.PigSalesStats;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartForPigSalesOverViewHelper {

    public static void salesOverView(BarChart barChart,
                                     int sold,
                                     int NotSold,
                                     int malePigSold,
                                     int FemalePigSold,
                                     int malePigNotSold,
                                     int FemalePigNotSold) {

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, sold));
        entries.add(new BarEntry(1f, NotSold));
        entries.add(new BarEntry(2f, malePigSold));
        entries.add(new BarEntry(3f, FemalePigSold));
        entries.add(new BarEntry(4f, malePigNotSold));
        entries.add(new BarEntry(5f, FemalePigNotSold));

        BarDataSet dataSet = new BarDataSet(entries, "Check-Up Status");
        dataSet.setColors(
                Color.parseColor("#C81F1F"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#3F51B5"),
                Color.parseColor("#E91E63"),
                Color.parseColor("#A43603"),
                Color.parseColor("#9B13B1")
        );
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.DKGRAY);

        barChart.getAxisLeft().setTextColor(Color.DKGRAY);
        barChart.getAxisRight().setEnabled(false); // Optional
        barChart.getLegend().setEnabled(false);    // Optional

        barChart.animateY(1000);
        barChart.invalidate(); // Refresh chart
    }

    public static void salesOverViewMonthlyLineChart(LineChart lineChart, Map<String, PigSalesStats> monthlyStats) {
        List<Entry> entries = new ArrayList<>();
        List<Entry> maleEntries = new ArrayList<>();
        List<Entry> femaleEntries = new ArrayList<>();
        final List<String> months = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, PigSalesStats> entry : monthlyStats.entrySet()) {
            String month = entry.getKey(); // e.g., "2025-08"
            PigSalesStats stats = entry.getValue();

            maleEntries.add(new Entry(index, stats.maleSold));
            femaleEntries.add(new Entry(index, stats.femaleSold));
            entries.add(new Entry(index, stats.sold));
            months.add(month);
            index++;
        }

        // Total Sold Dataset
        LineDataSet totalDataSet = new LineDataSet(entries, "Total Pigs Sold");
        totalDataSet.setColor(Color.GREEN);
        totalDataSet.setCircleColor(Color.GREEN);
        totalDataSet.setLineWidth(2f);

        // Male Sold Dataset
        LineDataSet maleDataSet = new LineDataSet(maleEntries, "Male Pigs Sold");
        maleDataSet.setColor(Color.BLUE);
        maleDataSet.setCircleColor(Color.BLUE);
        maleDataSet.setLineWidth(2f);

        // Female Sold Dataset
        LineDataSet femaleDataSet = new LineDataSet(femaleEntries, "Female Pigs Sold");
        femaleDataSet.setColor(Color.MAGENTA);
        femaleDataSet.setCircleColor(Color.MAGENTA);
        femaleDataSet.setLineWidth(2f);

        LineData lineData = new LineData(totalDataSet, maleDataSet, femaleDataSet);
        lineChart.setData(lineData);

        // Setup X axis with month labels
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.DKGRAY);

        // Y axis
        lineChart.getAxisLeft().setTextColor(Color.DKGRAY);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.animateX(1000);
        lineChart.invalidate(); // Refresh the chart
    }

}
