package com.example.pigfarmmanagementapp.Chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomBarChartView extends View {
    private Paint barPaint, textPaint, axisPaint;
    private int[] data = new int[0]; // Sample values: vaccinated, unvaccinated, pigs, cages
    private String[] labels = {"Vaccinated", "Unvaccinated", "Pigs", "Cages"};
    private int[] colors = {Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW};

    public CustomBarChartView(Context context) {
        super(context);
        init();
    }

    public CustomBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(36f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(4f);
    }

    public void setData(int[] newData) {
        this.data = newData;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        // Check for empty or all-zero data
        if (data == null || data.length == 0 || getMax(data) == 0) {
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("No data available", width / 2f, height / 2f, textPaint);
            return;
        }

        int barWidth = width / (data.length * 2);
        int maxVal = getMax(data);
        if (maxVal == 0) maxVal = 1;  // Safety fallback

        int chartHeight = height - 150;

        // Draw Y-axis
        canvas.drawLine(80, 50, 80, chartHeight, axisPaint);
        // Draw X-axis
        canvas.drawLine(80, chartHeight, width - 20, chartHeight, axisPaint);

        for (int i = 0; i < data.length; i++) {
            float scaledHeight = ((float) data[i] / maxVal) * (chartHeight - 100);
            float left = 100 + i * (barWidth * 2);
            float top = chartHeight - scaledHeight;
            float right = left + barWidth;
            float bottom = chartHeight;

            barPaint.setColor(colors[i]);
            canvas.drawRect(left, top, right, bottom, barPaint);

            // Draw value above bar
            canvas.drawText(String.valueOf(data[i]), left + barWidth / 2, top - 10, textPaint);
            // Draw label below bar
            canvas.drawText(labels[i], left + barWidth / 2, chartHeight + 40, textPaint);
        }
    }


    private int getMax(int[] values) {
        int max = 1;
        for (int v : values) {
            if (v > max) max = v;
        }
        return max;
    }
}
