package com.example.pigfarmmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
LinearLayout humidLayout, tempLayout;
    ImageView swapTemp, swapHumid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        humidLayout = findViewById(R.id.humidLayout);
        tempLayout = findViewById(R.id.tempLayout);

        swapTemp = findViewById(R.id.swapTemp);
        swapHumid = findViewById(R.id.swapHumid);

        swapHumid.setOnClickListener(v -> {
            tempLayout.setVisibility(View.VISIBLE);
            humidLayout.setVisibility(View.GONE);
        });

        swapTemp.setOnClickListener(v -> {
            tempLayout.setVisibility(View.GONE);
            humidLayout.setVisibility(View.VISIBLE);
        });



    }
}