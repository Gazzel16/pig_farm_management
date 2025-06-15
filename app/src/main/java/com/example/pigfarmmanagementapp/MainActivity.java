package com.example.pigfarmmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.QrCode.QrScannerActivity;
import com.example.pigfarmmanagementapp.adapter.CategoriesAdapter;
import com.example.pigfarmmanagementapp.model.Categories;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
LinearLayout humidLayout, tempLayout;
    ImageView swapTemp, swapHumid;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        humidLayout = findViewById(R.id.humidLayout);
        tempLayout = findViewById(R.id.tempLayout);

        swapTemp = findViewById(R.id.swapTemp);
        swapHumid = findViewById(R.id.swapHumid);

        recyclerView = findViewById(R.id.recyclerView);

        swapHumid.setOnClickListener(v -> {
            tempLayout.setVisibility(View.VISIBLE);
            humidLayout.setVisibility(View.GONE);
        });

        swapTemp.setOnClickListener(v -> {
            tempLayout.setVisibility(View.GONE);
            humidLayout.setVisibility(View.VISIBLE);
        });

        List<Categories> categoryList = new ArrayList<>();

        categoryList.add(new Categories( R.drawable.logo,"Scanner", "Scan pig ID easily"));
        categoryList.add(new Categories(R.drawable.logo,"PigCage", "Assign pigs to cages"));
        categoryList.add(new Categories(R.drawable.logo,"PigStatus", "Assign pigs to cages"));
        categoryList.add(new Categories(R.drawable.logo,"Analytics", "Assign pigs to cages"));


        CategoriesAdapter adapter = new CategoriesAdapter(categoryList, category -> {
            if (category.getTitle().equals("Scanner")) {
                Intent intent = new Intent(MainActivity.this, QrScannerActivity.class);
                startActivity(intent);
            }
            if (category.getTitle().equals("PigCage")) {
                Intent intent = new Intent(MainActivity.this, AddCageActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
}