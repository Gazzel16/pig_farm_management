package com.example.pigfarmmanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;

public class PigAdviceActivity extends AppCompatActivity {

    private TextView tempText;
    private TextView adviceText;
    private Button adviceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pig_advice);

        tempText = findViewById(R.id.tempText);
        adviceText = findViewById(R.id.adviceText);
        adviceButton = findViewById(R.id.adviceButton);

        adviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float currentTemp = 18.0f; // 🔁 Hardcoded for now
                tempText.setText("Temperature: " + currentTemp + "°C");
                System.out.println("Temperature: " + currentTemp + "°C");

                try {
                    PigAdvicePredictor predictor = new PigAdvicePredictor(getApplicationContext());
                    String advice = predictor.getAdvice(currentTemp);
                    adviceText.setText("Advice: " + advice);
                    System.out.println("Advice: " + advice);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    adviceText.setText("Error loading model.");
                }
            }
        });
    }
}