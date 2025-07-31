package com.example.pigfarmmanagementapp.Chart.ChartUtils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

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
}
