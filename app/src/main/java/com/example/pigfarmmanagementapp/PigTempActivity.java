package com.example.pigfarmmanagementapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PigTempActivity extends AppCompatActivity {

    private TextView tempText, recommendationText;
    private Button checkTempBtn;

    private static final String TAG = "PigTempActivity";
    private static final String API_KEY = "AIzaSyBBFdVt0YCGzzelm2kfTj5EnIVs7uAtLcw"; // Your actual API key
    private static final double SIMULATED_TEMP = 33.5;
    private static final String MODEL_ID = "gemini-1.5-pro"; // Use the official Gemini model ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pig_temp);

        tempText = findViewById(R.id.tempText);
        recommendationText = findViewById(R.id.recommendationText);
        checkTempBtn = findViewById(R.id.checkTempBtn);

        checkTempBtn.setOnClickListener(view -> {
            tempText.setText(String.format("Current Temp: %.1f°C", SIMULATED_TEMP));

            if (SIMULATED_TEMP >= 30) {
                String prompt = String.format(
                        "The temperature in a pigpen is %.1f°C. What should I do to ensure the pigs are safe and comfortable?",
                        SIMULATED_TEMP);
                callGenerateContentAPI(prompt);
            } else {
                recommendationText.setText("Temperature is normal. No action needed.");
            }
        });
    }

    // Call Gemini model directly
    private void callGenerateContentAPI(String prompt) {
        new Thread(() -> {
            try {
                URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/" + MODEL_ID + ":generateContent?key=" + API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                // Build correct JSON request
                JSONObject root = new JSONObject();
                JSONArray contents = new JSONArray();

                JSONObject part = new JSONObject();
                part.put("text", prompt);

                JSONArray parts = new JSONArray();
                parts.put(part);

                JSONObject contentItem = new JSONObject();
                contentItem.put("role", "user");
                contentItem.put("parts", parts);

                contents.put(contentItem);
                root.put("contents", contents);

                String jsonInput = root.toString();

                OutputStream os = conn.getOutputStream();
                os.write(jsonInput.getBytes("UTF-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Log.d(TAG, "Response: " + response);


                String responseText = extractTextFromGeminiResponse(response.toString());
                runOnUiThread(() -> recommendationText.setText(responseText));

            } catch (Exception e) {
                Log.e(TAG, "Error calling Gemini API", e);
                runOnUiThread(() -> recommendationText.setText("Failed to get recommendation."));
            }
        }).start();
    }

    private String extractTextFromGeminiResponse(String json) {
        try {
            JSONObject obj = new JSONObject(json);

            if (obj.has("candidates")) {
                JSONArray candidates = obj.getJSONArray("candidates");
                if (candidates.length() > 0) {
                    JSONObject firstCandidate = candidates.getJSONObject(0);

                    if (firstCandidate.has("content")) {
                        JSONObject content = firstCandidate.getJSONObject("content");

                        if (content.has("parts")) {
                            JSONArray parts = content.getJSONArray("parts");
                            if (parts.length() > 0) {
                                return parts.getJSONObject(0).optString("text", "No text found");
                            }
                        }
                    }
                }
            }

            Log.e(TAG, "Unexpected JSON structure: " + obj.toString());
            return "No valid response.";
        } catch (Exception e) {
            Log.e(TAG, "Error parsing AI response", e);
            return "Failed to parse AI response.";
        }
    }

}
