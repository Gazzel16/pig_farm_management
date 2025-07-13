package com.example.pigfarmmanagementapp.QrCode;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Cage;
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

import java.util.List;

public class QrCageScannerActivity extends AppCompatActivity {

    //TextView
    private TextView cageIdTv, cageTv, statusTv, pigCountTv, soldCountTv;

    //String
    private String cageId;

    //Database
    private DatabaseReference databaseReference;

    //Button
    private Button updatePigStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.qr_cage_scanner);

        cageIdTv = findViewById(R.id.cageId);
        cageTv = findViewById(R.id.cage);
        statusTv = findViewById(R.id.status);
        pigCountTv = findViewById(R.id.pigCount);

        soldCountTv = findViewById(R.id.soldCount);

        updatePigStatus = findViewById(R.id.updatePigStatus);

        databaseReference = FirebaseDatabase.getInstance().getReference("cages");

        updatePigStatus.setOnClickListener(view -> {
            updateStatusBtn();
        });

        startQrScanner();
    }

    private void updateStatusBtn(){

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
                    fetchCageDetails(result.getContents());
                } else {
                    Log.d("QrScanner", "QR scan returned null");
                    Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
                }
            });


    private void fetchCageDetails(String qrContent) {
        try {
            Log.d("QrScanner", "QR Content Raw: " + qrContent);

            JSONObject jsonObject = new JSONObject(qrContent);

            if (!jsonObject.has("id")) {
                Toast.makeText(this, "Invalid QR Code: Missing 'id'", Toast.LENGTH_SHORT).show();
                return;
            }

            cageId = jsonObject.optString("id", "").trim();  // Use cageId, not pigId
            if (cageId.isEmpty()) {
                Toast.makeText(this, "Invalid QR Code Format: Missing or empty 'id'", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean found = false;

                    for (DataSnapshot cageSnapshot : dataSnapshot.getChildren()) {
                        Cage cage = cageSnapshot.getValue(Cage.class);

                        if (cage != null && cageId.equals(cage.getId())) {
                            found = true;

                            String name = cage.getName();
                            String status = cage.getStatus();

                            DatabaseReference pigsRef = FirebaseDatabase.getInstance().getReference("pigs").child(cageId);
                            pigsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    int pigCount = 0;
                                    int soldCount = 0;

                                    for (DataSnapshot pigSnap : snapshot.getChildren()) {
                                        Boolean isPurchased = pigSnap.child("purchase").getValue(Boolean.class);
                                        String pigId = pigSnap.getKey();
                                        Log.d("PigDebug", "Pig: " + pigId + ", purchased = " + isPurchased);

                                        if (Boolean.TRUE.equals(isPurchased)) {
                                            soldCount++;
                                        } else {
                                            pigCount++;
                                        }
                                    }

                                    pigCountTv.setText("Available: " + pigCount);
                                    soldCountTv.setText("Sold: " + soldCount);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(QrCageScannerActivity.this, "Failed to count pigs.", Toast.LENGTH_SHORT).show();
                                }
                            });


                            cageTv.setText(name);
                            statusTv.setText(status);
                            cageIdTv.setText(cage.getId());

                            break;
                        }
                    }

                    if (!found) {
                        Toast.makeText(QrCageScannerActivity.this, "Cage not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(QrCageScannerActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } catch (JSONException e) {
            Toast.makeText(this, "Invalid QR Code JSON format", Toast.LENGTH_SHORT).show();
            Log.e("QrScanner", "JSON Parsing Error: ", e);
        }
    }
}