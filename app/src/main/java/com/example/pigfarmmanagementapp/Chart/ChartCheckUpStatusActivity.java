package com.example.pigfarmmanagementapp.Chart;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pigfarmmanagementapp.Chart.ChartUtils.ChartCheckUpStatusHelper;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Pig;
import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartCheckUpStatusActivity extends AppCompatActivity {

    BarChart barchart;
    TextView maleCountTv, femaleCountTv;

    List<Pig> pigList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.chart_check_up_status);

        barchart = findViewById(R.id.barChart);

        maleCountTv = findViewById(R.id.maleCount);
        femaleCountTv = findViewById(R.id.femaleCount);

        DatabaseReference pigRef = FirebaseDatabase.getInstance()
                .getReference("pigs");

        pigRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               int overdue = 0;
               int onSchedule = 0;

                int maleCount = 0;
                int femaleCount = 0;

                int maleOverdueCount = 0;
                int femaleOverdueCount = 0;

                int maleOnSchedCount = 0;
                int femaleOnSchedCount = 0;

                for (DataSnapshot cageSnap : snapshot.getChildren()) {
                    for (DataSnapshot pigSnap : cageSnap.getChildren()) {
                        Pig pig = pigSnap.getValue(Pig.class);

                        if(pig != null){
                            pigList.add(pig);

                            String status = pigSnap.child("checkupStatus").getValue(String.class);
                            if (status != null) {
                                String normalized = status.trim().toLowerCase();
                                if (normalized.equals("overdue")) {
                                    overdue++;
                                } else if (normalized.equals("on schedule")) {
                                    onSchedule++;
                                }
                            }

                            String gender = pig.getGender();
                            if (gender.equalsIgnoreCase("Male")){
                                maleCount++;
                            } else if (gender.equalsIgnoreCase("Female")){
                                femaleCount++;
                            }

                            // Male and Female Checkup Status sorting chart
                            if (gender.equalsIgnoreCase("Male")
                            && status.equalsIgnoreCase("Overdue")){
                                maleOverdueCount++;
                            }else if(gender.equalsIgnoreCase("Female")
                                    && status.equalsIgnoreCase("Overdue")){
                                femaleOverdueCount++;
                            }

                            if (gender.equalsIgnoreCase("Male")
                                    && status.equalsIgnoreCase("On Schedule")){
                                maleOnSchedCount++;
                            }else if(gender.equalsIgnoreCase("Female")
                                    && status.equalsIgnoreCase("On Schedule")){
                                femaleOnSchedCount++;
                            }
                        }


                    }
                }

                ChartCheckUpStatusHelper.checkUpStatus(barchart, overdue,
                        onSchedule, maleOverdueCount,
                        femaleOverdueCount, maleOnSchedCount, femaleOnSchedCount);
                maleCountTv.setText(String.valueOf(maleCount));
                femaleCountTv.setText(String.valueOf(femaleCount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}