// ChartCheckUpStatusHelper.java
package com.example.pigfarmmanagementapp.Chart.ChartUtils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartCheckUpStatusHelper {

    public static void checkUpStatus(BarChart barChart,
                                     int overdue,
                                     int onSchedule,
                                     int maleOverdueCount,
                                     int femaleOverdueCount,
                                     int maleOnSchedCount,
                                     int femaleOnSchedCount) {

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, overdue));
        entries.add(new BarEntry(1f, onSchedule));
        entries.add(new BarEntry(2f, maleOverdueCount));
        entries.add(new BarEntry(3f, femaleOverdueCount));
        entries.add(new BarEntry(4f, maleOnSchedCount));
        entries.add(new BarEntry(5f, femaleOnSchedCount));

        BarDataSet dataSet = new BarDataSet(entries, "Check-Up Status");
        dataSet.setColors(
                Color.RED,
                Color.GREEN,
                Color.parseColor("#3F51B5"),
                Color.parseColor("#E91E63"),
                Color.parseColor("#009688"),
                Color.parseColor("#BA68C8")
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
