package com.example.pigfarmmanagementapp.handler;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditPigHandler {

    public static void editPig(Context context, Pig pig, String cageId, Runnable onPigUpdated) {
        if (pig == null || pig.getId() == null) {
            Toast.makeText(context, "Error: Pig data or key is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_pig, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        EditText etPigBreed = dialogView.findViewById(R.id.etPigBreed);
        EditText etPigWeight = dialogView.findViewById(R.id.etPigWeight);
        Spinner spinnerVaccinationStatus = dialogView.findViewById(R.id.spinnerVaccinationStatus);
        Button updatePig = dialogView.findViewById(R.id.updatePig);

        // Populate input fields
        etPigBreed.setText(pig.getBreed());
        etPigWeight.setText(String.valueOf(pig.getWeight()));

        // Setup spinner
        String[] vaccinationStatuses = {"Unvaccinated", "Vaccinated"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, vaccinationStatuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaccinationStatus.setAdapter(adapter);

        if (pig.getVaccinationStatus() != null) {
            int spinnerPosition = adapter.getPosition(pig.getVaccinationStatus());
            if (spinnerPosition >= 0) spinnerVaccinationStatus.setSelection(spinnerPosition);
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        updatePig.setOnClickListener(v -> {
            String breed = etPigBreed.getText().toString().trim();
            String weightString = etPigWeight.getText().toString().trim();
            String vaccinationStatus = spinnerVaccinationStatus.getSelectedItem() != null
                    ? spinnerVaccinationStatus.getSelectedItem().toString()
                    : "";

            if (breed.isEmpty()) {
                etPigBreed.setError("Breed cannot be empty");
                etPigBreed.requestFocus();
                return;
            }

            if (weightString.isEmpty()) {
                etPigWeight.setError("Weight cannot be empty");
                etPigWeight.requestFocus();
                return;
            }

            double weight;
            try {
                weightString = weightString.replace(",", "");
                weight = Double.parseDouble(weightString);
            } catch (NumberFormatException e) {
                etPigWeight.setError("Invalid weight format");
                etPigWeight.requestFocus();
                return;
            }

            if (vaccinationStatus.isEmpty()) {
                Toast.makeText(context, "Please select a valid vaccination status.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pig.getId() == null || pig.getId().isEmpty()) {
                Toast.makeText(context, "Error: Pig key is missing!", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            if (cageId == null || cageId.isEmpty()) {
                Toast.makeText(context, "Cage ID is missing", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference pigRef = FirebaseDatabase.getInstance().getReference("pigs").child(cageId).child(pig.getId());

            boolean hasChanges = !breed.equals(pig.getBreed()) ||
                    weight != pig.getWeight() ||
                    !vaccinationStatus.equals(pig.getVaccinationStatus());

            if (!hasChanges) {
                Toast.makeText(context, "No changes detected.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }

            // ✅ Ensure we only send necessary fields (no "id" or "key" duplication)
            Map<String, Object> updates = new HashMap<>();
            updates.put("breed", breed);
            updates.put("weight", weight);
            updates.put("vaccinationStatus", vaccinationStatus);

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Updating pig details...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.d("FirebaseUpdate", "Updating pig at path: " + pigRef.toString());

            pigRef.updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Pig details updated successfully!", Toast.LENGTH_SHORT).show();
                            pig.setBreed(breed);
                            pig.setWeight(weight);
                            pig.setVaccinationStatus(vaccinationStatus);
                            if (onPigUpdated != null) onPigUpdated.run();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Failed to update pig details.", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

    }
}
