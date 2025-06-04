package com.example.pigfarmmanagementapp.QrCode;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pigfarmmanagementapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QrScannerActivity extends AppCompatActivity {

    private TextView breed,weight,status;
    private DatabaseReference databaseReference;
    private DataSnapshot pigSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.qr_scanner);

        breed = findViewById(R.id.breed);
        weight = findViewById(R.id.weight);
        status = findViewById(R.id.status);

        databaseReference = FirebaseDatabase.getInstance().getReference("pigs");

        startQrScanner();
    }

    private void startQrScanner(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a Qr Code");
        options.setBeepEnabled(true);
        Log.d("QrScanner", "Launching QR Scanner....");
        qrLauncher.launch(options);
    }

    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> qrLauncher =
            registerForActivityResult(new ScanContract(), result ->{
                if (result.getContents() !=null){
                    Log.d("QrScanner", "Scanned Qr Code: " + result.getContents());
                    fetchPigDetails(result.getContents());

                }else {
                    Log.d("QrScanner", "QR scan returned null");
                    Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();

                }
            });

    private String getFilePathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath(); // Fallback to raw path if cursor is null
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String filePath = cursor.getString(index);
            cursor.close();
            return filePath;
        }
    }

    private void fetchPigDetails(String qrContent){
        Log.d("QrScanner", "Scanned Qr Content: " + qrContent);

        String[] lines = qrContent.split("\n");
        String pigId = null;

        for (String line : lines){
            if(line.startsWith("ID: ")){
                pigId = line.replace("ID: ", "").trim();
                break;
            }
        }

        if (pigId == null || pigId.isEmpty()){
            Toast.makeText(this, "Invalid Qr Code Format", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("QrScanner", "Extracted Pig ID" + pigId);

        DatabaseReference pigRef = FirebaseDatabase.getInstance().getReference("pigs").child(pigId);
        pigRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    pigSnapshot = dataSnapshot;
                    Log.d("QrScanner", "PigSnapshot successfully set: " + pigSnapshot);

                    String pigBreed = dataSnapshot.child("breed").getValue(String.class);
                    String pigStatus = dataSnapshot.child("vaccinationStatus").getValue(String.class);
                    String pigWeight = dataSnapshot.child("weight").getValue(String.class);

                    breed.setText("Breed: " + pigBreed);
                    status.setText("Status: " + pigStatus);
                    weight.setText("Weight: " + pigWeight);

                    if (ContextCompat.checkSelfPermission(QrScannerActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(QrScannerActivity.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String sanitizeKey(String key) {
        Log.d("QrScanner", "Original Key: " + key);
        String sanitized = key.trim().replaceAll("[.#$\\[\\]]", "").replaceAll("\\s+", "_");
        Log.d("QrScanner", "Sanitized Key: " + sanitized);
        return sanitized;
    }
}