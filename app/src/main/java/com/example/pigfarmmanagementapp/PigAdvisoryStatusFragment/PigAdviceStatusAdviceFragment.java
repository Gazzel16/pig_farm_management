package com.example.pigfarmmanagementapp.PigAdvisoryStatusFragment;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.adapter.PigStatusAdviceAdapter;
import com.example.pigfarmmanagementapp.model.PigStatusAdvice;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PigAdviceStatusAdviceFragment extends Fragment {

    private TextView tempCondition, humidCondition, tempStatus, humidStatus;
    private int tempStatusResult = 31;
    private int humidStatusResult = 50;
    private Button pigStatusAdviceBtn;

    private String stressLevel = "";

    // Make these class-level so you can access them in AsyncTask
    private List<PigStatusAdvice> pigStatusAdviceList = new ArrayList<>();
    private PigStatusAdviceAdapter adapter;

    public PigAdviceStatusAdviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pig_advice_status_fragment, container, false);

        pigStatusAdviceBtn = view.findViewById(R.id.pigStatusAdviceBtn);

        tempCondition = view.findViewById(R.id.tempCondition);
        humidCondition = view.findViewById(R.id.humidCondition);

        tempStatus = view.findViewById(R.id.tempStatus);
        humidStatus = view.findViewById(R.id.humidStatus);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (tempStatusResult >= 25 && tempStatusResult <= 30) {

            stressLevel = "Good";
            tempStatus.setText("Status: Good");
            MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(), R.raw.good_condition);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release(); // Clean up after playing
            });
        } else if (tempStatusResult >= 31 && tempStatusResult <= 37) {
            stressLevel = "High";
            tempStatus.setText("Status: High");
            MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(), R.raw.high_condition);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release(); // Clean up after playing
            });
        }else if (tempStatusResult >= 38) {

            stressLevel = "Danger";
            tempStatus.setText("Status: Danger");

            MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(), R.raw.danger_condition);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release(); // Clean up after playing
            });
        }

        if (humidStatusResult >= 60 && humidStatusResult <= 70) {

            stressLevel = "Good";
            humidStatus.setText("Status: Good");

        } else if (humidStatusResult >= 50 && humidStatusResult <= 60) {

            stressLevel = "High";
            humidStatus.setText("Status: High");

        }else if (humidStatusResult < 50) {

            stressLevel = "Danger";
            humidStatus.setText("Status: Danger");

        }


        // Initially empty or placeholder data
        pigStatusAdviceList.add(new PigStatusAdvice(tempStatusResult, humidStatusResult, stressLevel, ""));
        adapter = new PigStatusAdviceAdapter(pigStatusAdviceList);
        recyclerView.setAdapter(adapter);

        tempCondition.setText(String.valueOf(tempStatusResult) + "°C");
        humidCondition.setText(String.valueOf(humidStatusResult) + "%");


        pigStatusAdviceBtn.setOnClickListener(v -> {
            new GenerateAdvisoryTask().execute(tempStatusResult, humidStatusResult);
        });


        return view;
    }

    private class GenerateAdvisoryTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {

            if (params.length < 2) {
                return "Error: Missing temperature or humidity value.";
            }


            try {

                int temperature = params[0];
                int humidity = params[1];

                URL url = new URL("http://192.168.1.36:5000/generate-advisory");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("temperature", temperature);
                jsonParam.put("humidity", humidity);

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
            String cleanedResult = cleanGeneratedText(result);

            // ✅ Use cleanedResult instead of result
            pigStatusAdviceList.clear();
            pigStatusAdviceList.add(new PigStatusAdvice(tempStatusResult, humidStatusResult, "High", cleanedResult));
            adapter.notifyDataSetChanged();
        }

        private String cleanGeneratedText(String rawText) {
            // Remove all asterisks (*) and Markdown-like formatting
            rawText = rawText.replace("*", "");         // Removes all asterisks
            rawText = rawText.replaceAll("#+", "");     // Removes hashtags
            rawText = rawText.replaceAll("-\\s*", "");  // Removes list dashes at line start
            return rawText.trim();
        }


    }
}
