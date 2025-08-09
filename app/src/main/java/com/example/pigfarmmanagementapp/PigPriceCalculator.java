package com.example.pigfarmmanagementapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PigPriceCalculator extends AppCompatActivity {

    EditText weight, pricePerKg, feedCost;
    TextView result;
    Button calculate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pig_price_calculator);

        weight = findViewById(R.id.weight);
        pricePerKg = findViewById(R.id.pricePerKg);
        feedCost = findViewById(R.id.feedCost);
        calculate = findViewById(R.id.calculate);
        result = findViewById(R.id.result);

        calculate.setOnClickListener(view -> {

            String weightStr = weight.getText().toString().trim();
            String pigPricePerKgStr = pricePerKg.getText().toString().trim();
            String feedCostPriceStr = feedCost.getText().toString().trim();

           int pigWeightInt = Integer.parseInt(weightStr);
            int pigPricePerKgInt = Integer.parseInt(pigPricePerKgStr);
            int feedCostPriceInt = Integer.parseInt(feedCostPriceStr);

            int totalCost = (pigWeightInt * pigPricePerKgInt) + feedCostPriceInt;

            result.setText("Total Price: ₱" + totalCost);

        });



    }
}