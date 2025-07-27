package com.example.pigfarmmanagementapp.Chart.ChartUtils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class BarChartHelper {

    public static void updateChart(BarChart barChart, int totalCages, int totalPigs, int vaccinated,
                                   int totalNotVaccinatedPigs, int ill, int totalNoSickPigs,
                                   int male, int female) {

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, totalCages));
        entries.add(new BarEntry(1, totalPigs));
        entries.add(new BarEntry(2, vaccinated));
        entries.add(new BarEntry(3, totalNotVaccinatedPigs));
        entries.add(new BarEntry(4, ill));
        entries.add(new BarEntry(5, totalNoSickPigs));
        entries.add(new BarEntry(6, male));
        entries.add(new BarEntry(7, female));

        BarDataSet dataSet = new BarDataSet(entries, "Pig Stats");
        dataSet.setColors(new int[]{
                Color.GRAY,                        // totalCages
                Color.BLUE,                        // totalPigs
                Color.parseColor("#4CAF50"),       // vaccinated
                Color.parseColor("#FF9800"),       // not vaccinated
                Color.parseColor("#F44336"),       // ill
                Color.parseColor("#009688"),       // no illness
                Color.parseColor("#3F51B5"),       // male
                Color.parseColor("#E91E63")        // female
        });

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);
        barChart.setData(data);

        barChart.setFitBars(true);
        barChart.invalidate();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDragEnabled(false);
    }
}
