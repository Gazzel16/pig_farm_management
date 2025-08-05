package com.example.pigfarmmanagementapp.Chart;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.Chart.ChartUtils.ChartForPigSalesOverViewHelper;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Pig;
import com.example.pigfarmmanagementapp.model.PigSalesStats;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class PigsSalesOverviewActivity extends AppCompatActivity {

    BarChart barChart;
    LineChart lineChart;
    List<Pig> pigList = new ArrayList<>();

    private TextView malePigRevenueTv, malePigsTotalSoldTv, femalePigsTotalSoldTv, femalePigRevenueTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pigs_sales_overview);

        malePigRevenueTv = findViewById(R.id.malePigRevenue);
        malePigsTotalSoldTv = findViewById(R.id.malePigsTotalSold);
        femalePigsTotalSoldTv = findViewById(R.id.femalePigsTotalSold);
        femalePigRevenueTv = findViewById(R.id.femalePigRevenue);

        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Get screen width in dp
        float density = Resources.getSystem().getDisplayMetrics().density;
        int screenWidthDp = (int) (Resources.getSystem().getDisplayMetrics().widthPixels / density);

        int columnCount;

        if (screenWidthDp >= 500 || screenWidthDp >= 900) {

            columnCount = 2;
        } else {

            columnCount = 1;
        }
        gridLayout.setColumnCount(columnCount);

        pigSaleOverViewBarChart();
        pigSaleOverViewLineChart();
        pigSaleRevenue();

    }

    public void pigSaleRevenue(){
        DatabaseReference pigRef = FirebaseDatabase.getInstance().getReference("pigs");

        pigRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int malePigsTotalSold = 0;
                int femalePigsTotalSold = 0;

                int maleSaleRevenue = 0;
                int femaleSaleRevenue = 0;
                double maleRevenue = 0.0;
                double femaleRevenue = 0.0;

                for(DataSnapshot cageSnap : snapshot.getChildren()){

                    for (DataSnapshot pigSnap : cageSnap.getChildren()){
                        Pig pig = pigSnap.getValue(Pig.class);

                        if(pig != null){

                            String gender = pig.getGender();
                            if (gender.equalsIgnoreCase("Male")){
                                malePigsTotalSold++;
                            } else if (gender.equalsIgnoreCase("Female")){
                                femalePigsTotalSold++;
                            }

                            double pigRevenue = pig.getPrice();

                            if (gender.equalsIgnoreCase("Male")){
                                maleSaleRevenue++;
                                maleRevenue = maleRevenue + pigRevenue;
                            } else if (gender.equalsIgnoreCase("Female")){
                                femaleSaleRevenue++;
                                femaleRevenue = femaleRevenue + pigRevenue;
                            }



                        }

                    }
                }
                malePigsTotalSoldTv.setText(String.valueOf("Sold: " + malePigsTotalSold));
                femalePigsTotalSoldTv.setText(String.valueOf("Sold: " + femalePigsTotalSold));

                malePigRevenueTv.setText(String.valueOf("Sale: ₱" +  maleRevenue));
                femalePigRevenueTv.setText(String.valueOf("Sale: ₱" +  femaleRevenue));

                Log.d("Revenue", "Sale " +  maleRevenue );
                Log.d("Revenue", "Sale " +  femaleRevenue );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void pigSaleOverViewBarChart(){
        DatabaseReference pigRef = FirebaseDatabase.getInstance()
                .getReference("pigs");
        pigRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sold = 0;
                int notSold = 0;

                int malePigSold = 0;
                int femalePigSold = 0;

                int malePigNotSold = 0;
                int femalePigNotSold = 0;

                Map<String, PigSalesStats> monthlyStats = new TreeMap<>(); // Sorted by month
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd H:m", Locale.getDefault());
                SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

                for (DataSnapshot cageSnap : snapshot.getChildren()){
                    for (DataSnapshot pigSnap : cageSnap.getChildren()){
                        Pig pig = pigSnap.getValue(Pig.class);

                        if (pig != null){
                            pigList.add(pig);

                            boolean isPurchase = pig.isPurchase();
                            if (!isPurchase){
                                notSold++;
                            }else {
                                sold++;
                            }

                            String gender = pig.getGender();

                            if (gender.equalsIgnoreCase("Male")){
                                if (!isPurchase){
                                    malePigNotSold++;
                                } else{
                                    malePigSold++;
                                }
                            }else if (gender.equalsIgnoreCase("Female")){
                                if (!isPurchase){
                                    femalePigNotSold++;
                                } else{
                                    femalePigSold++;
                                }
                            }

                            Log.d("Gender", "Male Sold: " + malePigSold);
                            Log.d("Gender", "Male Not Sold: " + malePigNotSold);
                            Log.d("Gender", "Female Not Sold: " + femalePigNotSold);
                            Log.d("Gender", "Female Sold: " + femalePigSold);

                            ChartForPigSalesOverViewHelper.salesOverView(
                                    barChart,
                                    sold,
                                    notSold,
                                    malePigSold,
                                    femalePigSold,
                                    malePigNotSold,
                                    femalePigNotSold
                            );
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pigSaleOverViewLineChart(){
        DatabaseReference pigRef = FirebaseDatabase.getInstance().getReference("pigs");



        pigRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, PigSalesStats> monthlyStats = new TreeMap<>(); // Sorted by month
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd H:m", Locale.getDefault());
                SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

                for (DataSnapshot cageSnap : snapshot.getChildren()){
                    for (DataSnapshot pigSnap : cageSnap.getChildren()){

                        Pig pig = pigSnap.getValue(Pig.class);
                        if (pig != null){

                            //This line of code is for Line Chart
                            boolean isSold = pig.isPurchase();
                            String purchaseDateTime = pig.getPurchaseDateTime();
                            String gender = pig.getGender();

                            if(isSold && purchaseDateTime != null && !purchaseDateTime.isEmpty()){

                                try {
                                    Date purchaseDate = inputFormat.parse(purchaseDateTime);
                                    String monthKey = monthFormat.format(purchaseDate);

                                    PigSalesStats stats = monthlyStats.getOrDefault(monthKey, new PigSalesStats());

                                    stats.sold++;

                                    if("male".equalsIgnoreCase(gender)){
                                        stats.maleSold++;
                                    } else if("female".equalsIgnoreCase(gender)){
                                        stats.femaleSold++;
                                    }

                                    monthlyStats.put(monthKey, stats);



                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            //Fake Data
//                Random random = new Random();
//                for (int i = 0; i < 6; i++){
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.add(Calendar.MONTH, -i);
//                    String fakeMonthKey = monthFormat.format(calendar.getTime());
//
//                    PigSalesStats stats = monthlyStats.getOrDefault(fakeMonthKey, new PigSalesStats());
//
//                    int fakeSold = 10 + random.nextInt(10);
//                    int fakeMaleSold = random.nextInt(fakeSold + 1);
//                    int fakeFemaleSold = fakeSold - fakeMaleSold;
//
//                    stats.sold = fakeSold * 3;
//                    stats.maleSold = fakeMaleSold * 3;
//                    stats.femaleSold = fakeFemaleSold * 3;
//
//                    monthlyStats.put(fakeMonthKey, stats);
//
//                }

                            ChartForPigSalesOverViewHelper.salesOverViewMonthlyLineChart(lineChart, monthlyStats);

                        }
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}