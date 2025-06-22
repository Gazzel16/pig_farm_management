package com.example.pigfarmmanagementapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    private int tempStatusResult =50;
    private int humidStatusResult = 70;
    private Button pigStatusAdviceBtn;

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initially empty or placeholder data
        pigStatusAdviceList.add(new PigStatusAdvice(tempStatusResult, humidStatusResult, "High", ""));
        adapter = new PigStatusAdviceAdapter(pigStatusAdviceList);
        recyclerView.setAdapter(adapter);

        pigStatusAdviceBtn.setOnClickListener(v -> {
            new GenerateAdvisoryTask().execute(tempStatusResult);
        });

        return view;
    }

    private class GenerateAdvisoryTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... temps) {
            try {
                URL url = new URL("http://192.168.1.36:5000/generate-advisory");
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
            String cleanedResult = cleanGeneratedText(result);

            // ✅ Use cleanedResult instead of result
            pigStatusAdviceList.clear();
            pigStatusAdviceList.add(new PigStatusAdvice(tempStatusResult, humidStatusResult, "High", cleanedResult));
            adapter.notifyDataSetChanged();
        }

        private String cleanGeneratedText(String rawText) {
            // Remove #, *, and - from the beginning of lines
            return rawText.replaceAll("(?m)^[-#*]+\\s*", "");
        }

    }
}
