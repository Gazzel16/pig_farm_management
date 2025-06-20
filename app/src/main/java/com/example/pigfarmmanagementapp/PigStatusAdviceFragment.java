package com.example.pigfarmmanagementapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PigStatusAdviceFragment extends Fragment {
    Button pigStatusAdviceBtn;
    TextView tempResult, humidResult, advisory, stressLevel;
    int tempStatusResult = 40;
    int humidStatusResult = 70;

    public PigStatusAdviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pig_status_advice, container, false);

        pigStatusAdviceBtn = view.findViewById(R.id.pigStatusAdviceBtn);
        tempResult = view.findViewById(R.id.tempResult);
        humidResult = view.findViewById(R.id.humidResult);
        advisory = view.findViewById(R.id.advisory);
        stressLevel = view.findViewById(R.id.stressLevel);

        tempResult.setVisibility(View.GONE);
        humidResult.setVisibility(View.GONE);
        advisory.setVisibility(View.GONE);
        stressLevel.setVisibility(View.GONE);

        pigStatusAdviceBtn.setOnClickListener(v -> {
            new GenerateAdvisoryTask().execute(tempStatusResult);
        });

        return view;
    }
    // AsyncTask to call your Flask API
    private class GenerateAdvisoryTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... temps) {
            try {
                URL url = new URL("http://192.168.1.36:5000/generate-advisory"); // Replace with your local or public IP
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("temperature", temps[0]);

                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                conn.disconnect();

                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getString("advisory");

            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            tempResult.setVisibility(View.VISIBLE);
            humidResult.setVisibility(View.VISIBLE);
            advisory.setVisibility(View.VISIBLE);
            stressLevel.setVisibility(View.VISIBLE);

            tempResult.setText("Temperature: " + tempStatusResult);
            humidResult.setText("Humidity: " + humidStatusResult + "%");
            advisory.setText("Advisory: " + result);
            stressLevel.setText("Stress Level: High");
        }
    }
}