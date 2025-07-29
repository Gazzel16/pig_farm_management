package com.example.pigfarmmanagementapp.Chart;

import android.os.Bundle;
import android.util.Log;

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
    List<Pig> pigList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.chart_check_up_status);

        barchart = findViewById(R.id.barChart);
        DatabaseReference pigRef = FirebaseDatabase.getInstance().getReference();

        pigRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date today = new Date();

               int overdue = 0;
               int onSchedule = 0;


               for (DataSnapshot pigSnap : snapshot.getChildren()){

                   Pig pig = pigSnap.getValue(Pig.class);
                   if (pig != null){

                       try {

                           String lastCheckUpStr = pig.getLastCheckUp();
                           String nextCheckUpStr = pig.getNextCheckUp();

                           if (lastCheckUpStr == null || nextCheckUpStr == null ||
                                   lastCheckUpStr.isEmpty() || nextCheckUpStr.isEmpty()) {
                               throw new IllegalArgumentException("Date string is null or empty");
                           }

                           Date lastCheckupDate = sdf.parse(lastCheckUpStr);
                           Date nextCheckUpDate = sdf.parse(nextCheckUpStr);

                           if(today.after(lastCheckupDate)){
                               overdue++;
                           }else {
                               onSchedule++;
                           }



                       }catch (Exception e){
                           Log.e("CheckupParsingError", "Error parsing dates for pig: " + pig.getId(), e);
                       }
                   }

                   ChartCheckUpStatusHelper.checkUpStatus(barchart, overdue, onSchedule);
               }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}