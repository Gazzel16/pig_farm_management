package com.example.pigfarmmanagementapp.handler;

import android.app.DatePickerDialog;
import android.content.Context;
import android.media.Image;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.pigfarmmanagementapp.CageDetailsActivity;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class AddPigHandlerDialog {


    private final Context context;

    public AddPigHandlerDialog(Context context) {
        this.context = context;
    }

    public void show(String cageId, DatabaseReference databasePigs,
                     boolean isPurchase, String buyerName, String buyerContact, String purchaseDateTime) {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_pig, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        EditText etPigBreed = dialogView.findViewById(R.id.etPigBreed);
        EditText etPigBirthDate = dialogView.findViewById(R.id.etPigBirthDate);
        EditText etPigWeight = dialogView.findViewById(R.id.etPigWeight);
        Spinner spinnerPigIllness = dialogView.findViewById(R.id.spinnerPigIllness);
        Spinner spinnerVaccinationStatus = dialogView.findViewById(R.id.spinnerVaccinationStatus);
        Spinner spinnerPigGender = dialogView.findViewById(R.id.spinnerPigGender);
        Spinner spinnerPigStatus = dialogView.findViewById(R.id.spinnerPigStatus);

        EditText etPigLastCheckUp = dialogView.findViewById(R.id.etPigLastCheckUp);
        EditText etPigNextCheckUp = dialogView.findViewById(R.id.etPigNextCheckUp);
        EditText etPigPrice = dialogView.findViewById(R.id.etPigPrice);

        Button btnAddPig = dialogView.findViewById(R.id.btnAddPig);
        ImageView backBtn = dialogView.findViewById(R.id.backBtn);
        Button nextBtn = dialogView.findViewById(R.id.nextBtn);

        LinearLayout page1 = dialogView.findViewById(R.id.page1);
        LinearLayout page2 = dialogView.findViewById(R.id.page2);

        nextBtn.setOnClickListener(view -> {
            page2.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.VISIBLE);

            page1.setVisibility(View.GONE);
        });

        backBtn.setOnClickListener(view -> {
            page1.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);

            page2.setVisibility(View.GONE);
        });

        etPigBirthDate.setInputType(InputType.TYPE_NULL); // Prevent keyboard
        etPigBirthDate.setFocusable(false);

        etPigNextCheckUp.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context, // replace with 'this' if you're inside onCreate
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedYear + "-"
                                + String.format("%02d", (selectedMonth + 1)) + "-"
                                + String.format("%02d", selectedDay);
                        etPigNextCheckUp.setText(formattedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        etPigLastCheckUp.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context, // replace with 'this' if you're inside onCreate
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
                    context, // replace with 'this' if you're inside onCreate
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

        String[] pigStatus = {
                "Select Status",
                "Alive",
                "Dead"
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
                "Tetanus",
                "None",
                "Others"
        };

        // Spinner2 data
        String[] pigIllness = {
                "Select Illness",
                "Swine Dysentery",
                "Swine Flu (Influenza)",
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

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, vaccinationStatus);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaccinationStatus.setAdapter(statusAdapter);

        ArrayAdapter<String> pigIllnessAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, pigIllness);
        pigIllnessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPigIllness.setAdapter(pigIllnessAdapter);

        ArrayAdapter<String>pigGenderAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, pigGender);
        pigGenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPigGender.setAdapter((pigGenderAdapter));

        ArrayAdapter<String>pigStatusAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, pigStatus);
        pigStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPigStatus.setAdapter((pigStatusAdapter));

        AlertDialog dialog = builder.create();

        btnAddPig.setOnClickListener(v -> {
            String pigBreed = etPigBreed.getText().toString().trim();
            String pigBirthDate = etPigBirthDate.getText().toString().trim();
            String pigWeightStr = etPigWeight.getText().toString().trim();
            String pigPriceStr = etPigPrice.getText().toString().trim();

            String pigLastCheckUp = etPigLastCheckUp.getText().toString().trim();
            String pigNextCheckUp = etPigNextCheckUp.getText().toString().trim();

            String selectedPigGender = spinnerPigGender.getSelectedItem()
                    != null ? spinnerPigGender.getSelectedItem().toString() : "";

            String selectedPigStatus = spinnerPigStatus.getSelectedItem()
                    != null ? spinnerPigStatus.getSelectedItem().toString() : "";

            String selectedPigIllness = spinnerPigIllness.getSelectedItem()
                    != null ? spinnerPigIllness.getSelectedItem().toString() : "";

            String selectedVaccinationStatus = spinnerVaccinationStatus.getSelectedItem() != null
                    ? spinnerVaccinationStatus.getSelectedItem().toString()
                    : "";

            if (pigBreed.isEmpty()
                    || selectedVaccinationStatus.equalsIgnoreCase("Select Status")
                    || pigWeightStr.isEmpty()
                    || selectedVaccinationStatus.isEmpty()
                    || selectedPigGender.equalsIgnoreCase("Select Gender")
                    || selectedPigIllness.equalsIgnoreCase("Select Illness")
                    || selectedPigStatus.equalsIgnoreCase("Select Status")

            )
            {
                Toast.makeText(context, "Please fill in all fields and select a valid status.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                pigWeightStr = pigWeightStr.replace(",", ""); // Remove commas
                double pigWeight = Double.parseDouble(pigWeightStr);

                pigPriceStr = pigPriceStr.replace(",", "");
                double pigPrice = Double.parseDouble(pigPriceStr);

                // Generate a new pigId (using Firebase push() method for uniqueness)
                String pigId = String.format("%07d", (int)(Math.random() * 10000000));
                Pig newPig = new Pig(pigId, pigBreed, selectedPigGender, pigBirthDate,
                        pigWeight,selectedPigIllness, selectedVaccinationStatus,
                        pigLastCheckUp, cageId, isPurchase, buyerName,
                        buyerContact, purchaseDateTime, pigNextCheckUp, pigPrice, selectedPigStatus);

                // Store the pig data under the pigs node with the unique pigId
                databasePigs.child(pigId).setValue(newPig).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Pig added successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to add pig.", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Invalid weight format.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
