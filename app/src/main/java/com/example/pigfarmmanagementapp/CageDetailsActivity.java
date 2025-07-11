package com.example.pigfarmmanagementapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.adapter.PigAdapter;
import com.example.pigfarmmanagementapp.model.Cage;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CageDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPigs;
    private PigAdapter pigAdapter;
    private List<Pig> pigList = new ArrayList<>();
    private List<Cage> cageList = new ArrayList<>();
    private List<Pig> pigListFull = new ArrayList<>(); // Declare pigListFull
    private DatabaseReference databasePigs;
    private String cageId;

    private boolean isPurchase;
    private String cageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cage_details);

        // Retrieve cage information
        cageId = getIntent().getStringExtra("cageId");
        cageName = getIntent().getStringExtra("cageName");

        // Initialize Firebase reference for pigs in the specific cage
        databasePigs = FirebaseDatabase.getInstance().getReference("pigs").child(cageId);

        EditText etSearchPig = findViewById(R.id.etSearchPig);
        // Set up RecyclerView
        recyclerViewPigs = findViewById(R.id.recyclerViewPigs);
        recyclerViewPigs.setLayoutManager(new LinearLayoutManager(this));
        pigAdapter = new PigAdapter(pigList, cageName, cageId);
        recyclerViewPigs.setAdapter(pigAdapter);

        // Load pigs from Firebase
        loadPigsFromFirebase();

        // Set listener for the "Add Pig" button
        findViewById(R.id.btnAddPig).setOnClickListener(v -> showAddPigDialog());

        etSearchPig.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterPigsByName(charSequence.toString());  // Pass the search query
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

    // This method filters the pigs based on the name (breed in your case)
    private void filterPigsByName(String query) {
        if (query.isEmpty()) {
            // If the search query is empty, restore the full list of pigs
            pigList.clear();
            pigList.addAll(pigListFull);
        } else {
            // Filter pigs by breed (case-insensitive partial match)
            pigList.clear();
            for (Pig pig : pigListFull) {
                if (pig.getBreed().toLowerCase().contains(query.toLowerCase())) {
                    pigList.add(pig);
                }
            }
        }
        // Notify the adapter to refresh the RecyclerView
        pigAdapter.notifyDataSetChanged();
    }


    private void loadPigsFromFirebase() {
        databasePigs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pigList.clear();
                pigListFull.clear(); // Clear the full list as well
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pig pig = snapshot.getValue(Pig.class);
                    if (pig != null) {
                        pigList.add(pig);
                        pigListFull.add(pig); // Also add to the full list for filtering
                    }
                }
                pigAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CageDetailsActivity.this, "Failed to load pigs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddPigDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_pig, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Reference views from dialogView
        EditText etPigBreed = dialogView.findViewById(R.id.etPigBreed);
        EditText etPigBirthDate = dialogView.findViewById(R.id.etPigBirthDate);
        EditText etPigWeight = dialogView.findViewById(R.id.etPigWeight);
        Spinner spinnerPigIllness = dialogView.findViewById(R.id.spinnerPigIllness);
        Spinner spinnerVaccinationStatus = dialogView.findViewById(R.id.spinnerVaccinationStatus);
        Button btnAddPig = dialogView.findViewById(R.id.btnAddPig);

        Spinner spinnerPigGender = dialogView.findViewById(R.id.spinnerPigGender);
        EditText etPigLastCheckUp = dialogView.findViewById(R.id.etPigLastCheckUp);

        etPigBirthDate.setInputType(InputType.TYPE_NULL); // Prevent keyboard
        etPigBirthDate.setFocusable(false);


        etPigLastCheckUp.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CageDetailsActivity.this, // replace with 'this' if you're inside onCreate
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedYear + "-"
                                + String.format("%02d", (selectedMonth + 1)) + "-"
                                + String.format("%02d", selectedDay);
                        etPigLastCheckUp.setText(formattedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        etPigBirthDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CageDetailsActivity.this, // replace with 'this' if you're inside onCreate
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedYear + "-"
                                + String.format("%02d", (selectedMonth + 1)) + "-"
                                + String.format("%02d", selectedDay);
                        etPigBirthDate.setText(formattedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        String[] pigGender = {
                "Select Gender",
          "Male",
          "Female"
        };

        // Spinner data
        String[] vaccinationStatus = {
                "Select Vaccines",
                "Mycoplasma hyopneumoniae (Enzootic Pneumonia)",
                "Erysipelothrix rhusiopathiae (Swine Erysipelas)",
                "Actinobacillus pleuropneumoniae (APP)",
                "Lawsonia intracellularis (Ileitis)",
                "Salmonella spp.",
                "Porcine Circovirus Type 2 (PCV2)",
                "Porcine Reproductive and Respiratory Syndrome (PRRS)",
                "Classical Swine Fever (CSF)",
                "Foot-and-Mouth Disease (FMD)",
                "Pseudorabies (Aujeszky’s Disease)",
                "Swine Influenza (SIV)",
                "Clostridium perfringens Type C",
                "Escherichia coli (Neonatal Scours)",
                "Tetanus"
        };

        // Spinner2 data
        String[] pigIllness = {"Select Illness",
                "Swine Dysentery",
                "Swine Flu (Influenza)",
                "Porcine Reproductive and Respiratory Syndrome (PRRS)",
                "Mycoplasma Pneumonia",
                "Actinobacillus Pleuropneumonia (APP)",
                "Erysipelas",
                "Salmonellosis",
                "Clostridial Infections",
                "Tetanus",
                "Neonatal Diarrhea (Scours)",
                "Porcine Circovirus Associated Disease (PCVAD)",
                "Foot-and-Mouth Disease (FMD)",
                "Classical Swine Fever (CSF)",
                "Pseudorabies (Aujeszky's Disease)",
                "Leptospirosis",
                "Gastric Ulcers",
                "Greasy Pig Disease",
                "Mastitis Metritis Agalactia (MMA)",
                "Internal Parasites (e.g., Ascaris suum)",
                "None"
        };

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, vaccinationStatus);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaccinationStatus.setAdapter(statusAdapter);

        ArrayAdapter<String> pigIllnessAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, pigIllness);
        pigIllnessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPigIllness.setAdapter(pigIllnessAdapter);

        ArrayAdapter<String>pigGenderAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, pigGender);
        pigGenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPigGender.setAdapter((pigGenderAdapter));

        AlertDialog dialog = builder.create();

        btnAddPig.setOnClickListener(v -> {
            String pigBreed = etPigBreed.getText().toString().trim();
            String pigBirthDate = etPigBirthDate.getText().toString().trim();
            String pigWeightStr = etPigWeight.getText().toString().trim();

            String pigLastCheckUp = etPigLastCheckUp.getText().toString().trim();

            String selectedPigGender = spinnerPigGender.getSelectedItem()
                    != null ? spinnerPigGender.getSelectedItem().toString() : "";

            String selectedPigIllness = spinnerPigIllness.getSelectedItem()
                    != null ? spinnerPigIllness.getSelectedItem().toString() : "";

            String selectedStatus = spinnerVaccinationStatus.getSelectedItem() != null
                    ? spinnerVaccinationStatus.getSelectedItem().toString()
                    : "";

            if (pigBreed.isEmpty()
                    || selectedStatus.equals("Select Status")
                    || pigWeightStr.isEmpty()
                  || selectedStatus.isEmpty()
                    || selectedPigGender.equals("Select Gender")
                    || selectedPigIllness.equals("Select Illness")
            )
            {
                Toast.makeText(this, "Please fill in all fields and select a valid status.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                pigWeightStr = pigWeightStr.replace(",", ""); // Remove commas
                double pigWeight = Double.parseDouble(pigWeightStr);

                // Generate a new pigId (using Firebase push() method for uniqueness)
                String pigId = databasePigs.push().getKey(); // Automatically generates a unique ID
                Pig newPig = new Pig(pigId, pigBreed, selectedPigGender, pigBirthDate, pigWeight,selectedPigIllness, selectedStatus, pigLastCheckUp, cageId, isPurchase);

                // Store the pig data under the pigs node with the unique pigId
                databasePigs.child(pigId).setValue(newPig).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Pig added successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to add pig.", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid weight format.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }



}
