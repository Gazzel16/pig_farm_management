package com.example.pigfarmmanagementapp.PigAdvisoryStatusFragment;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.adapter.PigStatusAdviceAdapter;
import com.example.pigfarmmanagementapp.model.PigStatusAdvice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference databaseReference;
    private Button pigStatusAdviceBtn;

    private int tempStatusResult;
    private int humidStatusResult;
    private String stressLevel = "";

    // Make these class-level so you can access them in AsyncTask
    private List<PigStatusAdvice> pigStatusAdviceList = new ArrayList<>();
    private PigStatusAdviceAdapter adapter;

    private boolean isDataLoaded = false;

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
        adapter = new PigStatusAdviceAdapter(pigStatusAdviceList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("pigEnvironmentData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Integer temp = snapshot.child("temperature").getValue(Integer.class);
                    Integer humid = snapshot.child("humidity").getValue(Integer.class);
                    Log.d("DEBUG", "Temp: " + temp + ", Humid: " + humid);
                    if(temp !=null && humid !=null){
                        tempStatusResult = temp;
                        humidStatusResult  = humid;

                        tempCondition.setText(tempStatusResult + "°C");
                        humidCondition.setText(humidStatusResult + "%");

                        handleTempStatus();
                        handleHumidStatus();

                        isDataLoaded = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pigStatusAdviceBtn.setOnClickListener(v -> {
            if (isDataLoaded) {
                new GenerateAdvisoryTask().execute(tempStatusResult, humidStatusResult);
            } else {
                Toast.makeText(getContext(), "Please wait... loading sensor data.", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    private void handleTempStatus() {
        if (tempStatusResult >= 25 && tempStatusResult <= 30) {
            stressLevel = "Good";
            tempStatus.setText("Status: Good");

            MediaPlayer mp = MediaPlayer.create(requireContext(), R.raw.good_condition);
            mp.start();
            mp.setOnCompletionListener(MediaPlayer::release);
        } else if (tempStatusResult >= 31 && tempStatusResult <= 37) {
            stressLevel = "High";
            tempStatus.setText("Status: High");
            MediaPlayer mp = MediaPlayer.create(requireContext(), R.raw.high_condition);
            mp.start();
            mp.setOnCompletionListener(MediaPlayer::release);
        } else if (tempStatusResult >= 38) {
            stressLevel = "Danger";
            tempStatus.setText("Status: Danger");
            MediaPlayer mp = MediaPlayer.create(requireContext(), R.raw.danger_condition);
            mp.start();
            mp.setOnCompletionListener(MediaPlayer::release);
        }

        pigStatusAdviceList.clear();
        pigStatusAdviceList.add(new PigStatusAdvice(tempStatusResult, humidStatusResult, stressLevel, ""));
        adapter.notifyDataSetChanged();
    }

    private void handleHumidStatus() {
        if (humidStatusResult >= 60 && humidStatusResult <= 70) {
            humidStatus.setText("Status: Good");
        } else if (humidStatusResult >= 50 && humidStatusResult <= 59) {
            humidStatus.setText("Status: High");
        } else if (humidStatusResult < 50) {
            humidStatus.setText("Status: Danger");
        }
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
