package com.example.pigfarmmanagementapp.QrCode;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pigfarmmanagementapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class QrScannerActivity extends AppCompatActivity {

    private TextView breedTv, weightTv, statusTv, birthDateTv, genderTv, lastCheckUpTv, illnessTv;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.qr_scanner);

        breedTv = findViewById(R.id.breed);
        weightTv = findViewById(R.id.weight);
        statusTv = findViewById(R.id.status);
        birthDateTv = findViewById(R.id.birthDate);

        genderTv = findViewById(R.id.gender);
        lastCheckUpTv = findViewById(R.id.lastCheckUp);
        illnessTv = findViewById(R.id.illness);

        databaseReference = FirebaseDatabase.getInstance().getReference("pigs");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching pig details...");

        startQrScanner();
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

            String pigId = jsonObject.optString("id", "").trim();
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
                            String breed = pigSnapshot.child("breed").getValue(String.class);
                            String weight = String.valueOf(pigSnapshot.child("weight").getValue());
                            String birthDate = pigSnapshot.child("birthDate").getValue(String.class);
                            String status = pigSnapshot.child("vaccinationStatus").getValue(String.class);

                            String gender = pigSnapshot.child("gender").getValue(String.class);
                            String illness = pigSnapshot.child("pigIllness").getValue(String.class);
                            String lastCheckUp = pigSnapshot.child("lastCheckUp").getValue(String.class);

                            breedTv.setText("Breed: " + breed);
                            weightTv.setText("Weight: " + weight);
                            birthDateTv.setText("B-Date: " + birthDate);
                            statusTv.setText("V-Status: " + status);

                            genderTv.setText("Gender: " + gender);
                            illnessTv.setText("Illness: " + illness);
                            lastCheckUpTv.setText("Last CheckUp: " + lastCheckUp);

                            found = true;
                            break; // Stop searching after finding the pig
                        }
                    }

                    progressDialog.dismiss();

                    if (!found) {
                        Toast.makeText(QrScannerActivity.this, "Pig not found", Toast.LENGTH_SHORT).show();
                        breedTv.setText("Breed: -");
                        weightTv.setText("Weight: -");
                        birthDateTv.setText("B-Date: -");
                        statusTv.setText("V-Status: -");

                        genderTv.setText("Gender: -" );
                        illnessTv.setText("Illness: -");
                        lastCheckUpTv.setText("Last CheckU: -");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(QrScannerActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            Toast.makeText(this, "Invalid QR Code JSON format", Toast.LENGTH_SHORT).show();
            Log.e("QrScanner", "JSON Parsing Error: ", e);
        }
    }
}
