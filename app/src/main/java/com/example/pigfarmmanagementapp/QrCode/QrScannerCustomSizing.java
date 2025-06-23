package com.example.pigfarmmanagementapp.QrCode;

import android.os.Bundle;

import com.example.pigfarmmanagementapp.R;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QrScannerCustomSizing extends CaptureActivity {

    private DecoratedBarcodeView barcodeScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner_custom_sizing); // Your custom layout

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.initializeFromIntent(getIntent());
        barcodeScannerView.decodeContinuous(getBarcodeCallback()); // important
    }

    private com.journeyapps.barcodescanner.BarcodeCallback getBarcodeCallback() {
        return result -> {
            if (result.getText() != null) {
                // Do something with result.getText()
                // Or pass it back using setResult()
                setResult(RESULT_OK, getIntent().putExtra("SCAN_RESULT", result.getText()));
                finish(); // closes scanner
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }
}
