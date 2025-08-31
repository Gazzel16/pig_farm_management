package com.example.pigfarmmanagementapp.handler;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.pigfarmmanagementapp.CageDetailsActivity;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditPigHandler {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri selectedImageUri;

    private ImageView imagePlaceHolder;

    public void editPig(Context context, Pig pig, String cageId, Runnable onPigUpdated) {
        if (pig == null || pig.getId() == null) {
            Toast.makeText(context, "Error: Pig data or key is missing!", Toast.LENGTH_SHORT).show();
            return;
        }


        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_pig, null);

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

        Button updatePig = dialogView.findViewById(R.id.updatePig);
        ImageView backBtn = dialogView.findViewById(R.id.backBtn);
        Button nextBtn = dialogView.findViewById(R.id.nextBtn);

        LinearLayout page1 = dialogView.findViewById(R.id.page1);
        LinearLayout page2 = dialogView.findViewById(R.id.page2);

        imagePlaceHolder = dialogView.findViewById(R.id.imagePlaceHolder);

        // Populate input fields
        etPigBreed.setText(pig.getBreed());
        etPigBirthDate.setText(pig.getBirthDate());
        etPigWeight.setText(String.valueOf(pig.getWeight()));
        etPigPrice.setText(String.valueOf(pig.getPrice()));


        imagePlaceHolder.setOnClickListener(v -> { //this line is opening the file to pick a image
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            Log.d("image", "image selected" + imagePlaceHolder + " imagePlaceHolder");
        });

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

        etPigLastCheckUp.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    v.getContext(), // ✅ Use context from the clicked view
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

        etPigNextCheckUp.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    v.getContext(), // ✅ Use context from the clicked view
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

        etPigBirthDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    v.getContext(), // ✅ Use context from the clicked view
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


        String[] pigStatus = {
                "Select Status",
                "Alive",
                "Dead"
        };

        String[] pigGender = {
                "Select Gender",
                "Male",
                "Female"
        };

        // Setup spinner
        // Spinner data
        String[] vaccinationStatuses = {
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
                "None"
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, vaccinationStatuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaccinationStatus.setAdapter(adapter);

        ArrayAdapter<String> pigIllnessAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, pigIllness);
        pigIllnessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPigIllness.setAdapter(pigIllnessAdapter);

        ArrayAdapter<String> pigGenderAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, pigGender);
        pigGenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPigGender.setAdapter((pigGenderAdapter));

        ArrayAdapter<String> pigStatusAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, pigStatus);
        pigStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPigStatus.setAdapter((pigStatusAdapter));

        if (pig.getVaccinationStatus() != null) {
            int spinnerPosition = adapter.getPosition(pig.getVaccinationStatus());
            if (spinnerPosition >= 0) spinnerVaccinationStatus.setSelection(spinnerPosition);
        }

        // Set gender spinner selection
        if (pig.getGender() != null && !pig.getGender().trim().isEmpty()) {
            int genderPosition = pigGenderAdapter.getPosition(pig.getGender().trim());
            spinnerPigGender.setSelection(genderPosition >= 0 ? genderPosition : 0); // Fallback to "Select Gender"
        }

// Set illness spinner selection
        if (pig.getPigIllness() != null && !pig.getPigIllness().trim().isEmpty()) {
            int illnessPosition = pigIllnessAdapter.getPosition(pig.getPigIllness().trim());
            spinnerPigIllness.setSelection(illnessPosition >= 0 ? illnessPosition : 0); // Fallback to "Select Illness"
        }
// Set status spinner selection
        if (pig.getStatus() != null && !pig.getStatus().trim().isEmpty()) {
            int statusPosition = pigStatusAdapter.getPosition(pig.getStatus().trim());
            spinnerPigStatus.setSelection(statusPosition >= 0 ? statusPosition : 0);
        }

// Set last check-up field
        if (pig.getLastCheckUp() != null && !pig.getLastCheckUp().trim().isEmpty()) {
            etPigLastCheckUp.setText(pig.getLastCheckUp().trim());
        }

        if (pig.getNextCheckUp() != null && !pig.getNextCheckUp().trim().isEmpty()) {
            etPigNextCheckUp.setText(pig.getNextCheckUp().trim());
        }

        //image handler

        if (pig.getImageUrl() != null && !pig.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(pig.getImageUrl())
                    .placeholder(R.drawable.add_image_icon) // your placeholder
                    .into(imagePlaceHolder);
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        updatePig.setOnClickListener(v -> {
            String breed = etPigBreed.getText().toString().trim();
            String birthDate = etPigBirthDate.getText().toString().trim();

            String weightString = etPigWeight.getText().toString().trim();


            String pigLastCheckUp = etPigLastCheckUp.getText().toString().trim();

            String pigNextCheckUp = etPigNextCheckUp.getText().toString().trim();

            String selectedPigGender = spinnerPigGender.getSelectedItem()
                    != null ? spinnerPigGender.getSelectedItem().toString() : "";

            String selectedPigIllness = spinnerPigIllness.getSelectedItem()
                    != null ? spinnerPigIllness.getSelectedItem().toString() : "";

            String vaccinationStatus = spinnerVaccinationStatus.getSelectedItem() != null
                    ? spinnerVaccinationStatus.getSelectedItem().toString()
                    : "";

            String status = spinnerPigStatus.getSelectedItem() != null
                    ? spinnerPigStatus.getSelectedItem().toString()
                    : "";

            if (selectedPigGender.equals("Select Gender")
                    || selectedPigIllness.equals("Select Illness")) {

                Toast.makeText(context, "Please fill in all fields and select a valid status.", Toast.LENGTH_SHORT).show();
                return;
            }

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

            if (cageId == null || cageId.isEmpty()) {
                Toast.makeText(context, "Cage ID is missing", Toast.LENGTH_SHORT).show();
                return;
            }


            DatabaseReference pigRef = FirebaseDatabase.getInstance().getReference("pigs").child(cageId).child(pig.getId());

            boolean hasChanges = !breed.equals(pig.getBreed()) ||
                    weight != pig.getWeight() ||
                    !vaccinationStatus.equals(pig.getVaccinationStatus()) ||
                    !birthDate.equals(pig.getBirthDate()) ||
                    !selectedPigIllness.equals(pig.getPigIllness()) ||
                    !selectedPigGender.equals(pig.getGender()) ||
                    !pigLastCheckUp.equals(pig.getLastCheckUp()) ||
                    !pigNextCheckUp.equalsIgnoreCase(pig.getNextCheckUp()) ||
                    !status.equalsIgnoreCase(pig.getStatus()) ||
                    selectedImageUri != null;


            Log.d("DEBUG", "Old birthDate: " + pig.getBirthDate() + ", New: " + birthDate);


            if (!hasChanges) {
                Toast.makeText(context, "No changes detected.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }


            // ✅ Ensure we only send necessary fields (no "id" or "key" duplication)
            Map<String, Object> updates = new HashMap<>();
            updates.put("breed", breed);
            updates.put("gender", selectedPigGender);
            updates.put("birthDate", birthDate);
            updates.put("weight", weight);
            updates.put("vaccinationStatus", vaccinationStatus);
            updates.put("lastCheckUp", pigLastCheckUp);
            updates.put("nextCheckUp", pigNextCheckUp);
            updates.put("pigIllness", selectedPigIllness);
            updates.put("status", status);

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Updating pig details...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.d("FirebaseUpdate", "Updating pig at path: " + pigRef.toString());

            // ✅ If a new image is selected, upload to Cloudinary first
            if (selectedImageUri != null) {
                ImageHandler.uploadImageToCloudinary(context, selectedImageUri, uploadedUrl -> {
                    if (uploadedUrl == null || uploadedUrl.isEmpty()) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Image upload failed. Please try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    updates.put("imageUrl", uploadedUrl); // Save new Cloudinary URL
                    updatePigData(pigRef, updates, pig, breed, selectedPigGender, birthDate,
                            weight, selectedPigIllness, vaccinationStatus,
                            pigLastCheckUp, pigNextCheckUp, status,
                            progressDialog, dialog, onPigUpdated);
                });
            } else {
                // No image change — just update DB
                updatePigData(pigRef, updates, pig, breed, selectedPigGender, birthDate,
                        weight, selectedPigIllness, vaccinationStatus,
                        pigLastCheckUp, pigNextCheckUp, status,
                        progressDialog, dialog, onPigUpdated);
            }
        });


    }

    private static void updatePigData(DatabaseReference pigRef, Map<String, Object> updates, Pig pig,
                                      String breed, String gender, String birthDate, double weight,
                                      String illness, String vaccinationStatus, String lastCheckUp,
                                      String nextCheckUp, String status,
                                      ProgressDialog progressDialog, AlertDialog dialog, Runnable onPigUpdated) {
        pigRef.updateChildren(updates)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(dialog.getContext(), "Pig details updated successfully!", Toast.LENGTH_SHORT).show();
                        pig.setBreed(breed);
                        pig.setGender(gender);
                        pig.setBirthDate(birthDate);
                        pig.setWeight(weight);
                        pig.setPigIllness(illness);
                        pig.setVaccinationStatus(vaccinationStatus);
                        pig.setLastCheckUp(lastCheckUp);
                        pig.setNextCheckUp(nextCheckUp);
                        pig.setStatus(status);
                        if (updates.containsKey("imageUrl")) {
                            pig.setImageUrl((String) updates.get("imageUrl"));
                        }
                        if (onPigUpdated != null) onPigUpdated.run();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(dialog.getContext(), "Failed to update pig details.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setSelectedImageUri(Uri uri) { // this line will handle the image from my activity
        Log.d("EditPigHandler", "setSelectedImageUri() called with URI: " + uri);
        this.selectedImageUri = uri;

        if (imagePlaceHolder == null) {
            Log.e("EditPigHandler", "imagePlaceHolder is NULL! Cannot set image.");
        } else {
            if (uri == null) {
                Log.e("EditPigHandler", "URI is NULL! No image to set.");
            } else {
                imagePlaceHolder.setImageURI(uri); // show image
                Log.d("EditPigHandler", "Image successfully set in imagePlaceHolder");
            }
        }
    }

}
