package com.example.pigfarmmanagementapp.QrCode;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.ColorLong;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QrPigScannerActivity extends AppCompatActivity {

    private TextView breedTv, weightTv,
            statusTv, birthDateTv,
            genderTv, lastCheckUpTv, illnessTv, nextCheckupTv, vaccineTv;

    private Button purchase;

    private boolean isPurchase = true;
    private String pigId;

    private String cageId;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    ImageView nextBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pig_qr_scanner);

        breedTv = findViewById(R.id.breed);
        weightTv = findViewById(R.id.weight);
        statusTv = findViewById(R.id.status);
        birthDateTv = findViewById(R.id.birthDate);

        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);


        purchase = findViewById(R.id.purchase);

        genderTv = findViewById(R.id.gender);
        lastCheckUpTv = findViewById(R.id.lastCheckUp);
        nextCheckupTv = findViewById(R.id.nextCheckup);
        illnessTv = findViewById(R.id.illness);
        vaccineTv = findViewById(R.id.vaccine);

        pigId = getIntent().getStringExtra("id");

        databaseReference = FirebaseDatabase.getInstance().getReference("pigs");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching pig details...");

        purchasePig();
        startQrScanner();
    }


    private void purchasePig(){

        purchase.setOnClickListener(view -> {
            if (pigId == null || pigId.isEmpty()) {
                Toast.makeText(this, "Pig ID not available yet. Please scan a pig QR code first.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cageId == null) {
                Toast.makeText(this, "Cage ID not available.", Toast.LENGTH_SHORT).show();
                return;
            }


            View dialogView = LayoutInflater.from(this).inflate(R.layout.item_buyer, null);
            AlertDialog buyerDialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setCancelable(true)
                    .create();

// Find your views from the custom layout
            EditText etBuyerName = dialogView.findViewById(R.id.etBuyerName);
            EditText etBuyerContact = dialogView.findViewById(R.id.etBuyerContact);
            Button btnConfirmPurchase = dialogView.findViewById(R.id.purchase); // This is your custom button

            btnConfirmPurchase.setOnClickListener(v -> {
                String buyerName = etBuyerName.getText().toString().trim();
                String buyerContact = etBuyerContact.getText().toString().trim();

                if (buyerName.isEmpty() || buyerContact.isEmpty()) {
                    Toast.makeText(this, "Please fill in all buyer details", Toast.LENGTH_SHORT).show();
                    return;
                }

                isPurchase = true;

                String currentDateTime = new SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                DatabaseReference pigRef = databaseReference.child(cageId).child(pigId);

                pigRef.child("purchase").setValue(true);
                pigRef.child("buyerName").setValue(buyerName);
                pigRef.child("buyerContact").setValue(buyerContact);
                pigRef.child("purchaseDateTime").setValue(currentDateTime);

                purchase.setEnabled(false);
                purchase.setText("Thank you for purchasing this pig");


                buyerDialog.dismiss(); // ✅ Close the dialog after saving
            });

            buyerDialog.show();

        });
    }
    private void startQrScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a QR Code");
        options.setBeepEnabled(true);
        options.setCaptureActivity(QrScannerCustomSizing.class);
        Log.d("QrScanner", "Launching QR Scanner...");
        qrLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> qrLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    Log.d("QrScanner", "Scanned QR Code: " + result.getContents());
                    fetchPigDetails(result.getContents());
                } else {
                    Log.d("QrScanner", "QR scan returned null");
                    Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
                }
            });

    private void fetchPigDetails(String qrContent) {
        try {
            Log.d("QrScanner", "QR Content Raw: " + qrContent);

            JSONObject jsonObject = new JSONObject(qrContent);

            if (!jsonObject.has("id")) {
                Toast.makeText(this, "Invalid QR Code: Missing 'id'", Toast.LENGTH_SHORT).show();
                return;
            }

            pigId = jsonObject.optString("id", "").trim();
            if (pigId.isEmpty()) {
                Toast.makeText(this, "Invalid QR Code Format: Missing or empty 'id'", Toast.LENGTH_SHORT).show();
                return;
            }


            progressDialog.show();

            // Search across all cages
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean found = false;

                    for (DataSnapshot cageSnapshot : dataSnapshot.getChildren()) {
                        DataSnapshot pigSnapshot = cageSnapshot.child(pigId);


                        if (pigSnapshot.exists()) {
                            cageId = cageSnapshot.getKey();

                            Pig pig = pigSnapshot.getValue(Pig.class);

                            if (pig == null) {
                                Toast.makeText(QrPigScannerActivity.this, "Failed to load pig data", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String breed = pigSnapshot.child("breed").getValue(String.class);
                            String weight = String.valueOf(pigSnapshot.child("weight").getValue());
                            String birthDate = pigSnapshot.child("birthDate").getValue(String.class);
                            String status = pigSnapshot.child("status").getValue(String.class);

                            String gender = pigSnapshot.child("gender").getValue(String.class);
                            String illness = pigSnapshot.child("pigIllness").getValue(String.class);
                            String vaccine = pigSnapshot.child("vaccinationStatus").getValue(String.class);
                            String lastCheckUp = pigSnapshot.child("lastCheckUp").getValue(String.class);
                            String nextCheckUp = pigSnapshot.child("nextCheckUp").getValue(String.class);

                            // ✅ Get the purchase value (default to false if null)
                            Boolean purchaseValue = pigSnapshot.child("purchase").getValue(Boolean.class);
                            isPurchase = (purchaseValue != null) ? purchaseValue : false;

                            breedTv.setText(breed);
                            weightTv.setText(weight + " kg");
                            birthDateTv.setText(birthDate);
                            statusTv.setText(status);

                            genderTv.setText(gender);
                            illnessTv.setText(illness);
                            vaccineTv.setText(vaccine);
                            lastCheckUpTv.setText(lastCheckUp);
                            nextCheckupTv.setText(nextCheckUp);

                            if (isPurchase) {
                                purchase.setEnabled(false);
                                purchase.setBackgroundColor(Color.GRAY);
                                purchase.setText(pig.getBuyerName() + " purchased this pig");
                            } else {
                                purchase.setEnabled(true);
                                purchase.setText("Purchase");
                            }

                            found = true;
                            break; // Stop searching after finding the pig
                        }
                    }

                    progressDialog.dismiss();

                    if (!found) {
                        Toast.makeText(QrPigScannerActivity.this, "Pig not found", Toast.LENGTH_SHORT).show();
                        breedTv.setText("Breed: -");
                        weightTv.setText("Weight: -");
                        birthDateTv.setText("B-Date: -");
                        statusTv.setText("V-Status: -");

                        genderTv.setText("Gender: -" );
                        illnessTv.setText("Illness: -");
                        lastCheckUpTv.setText("Last CheckUp: -");
                        nextCheckupTv.setText("Next CheckUp: -");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(QrPigScannerActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            Toast.makeText(this, "Invalid QR Code JSON format", Toast.LENGTH_SHORT).show();
            Log.e("QrScanner", "JSON Parsing Error: ", e);
        }
    }
}
