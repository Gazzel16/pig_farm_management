package com.example.pigfarmmanagementapp.adapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pigfarmmanagementapp.QrCode.QRCodeGenerator;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.model.Cage;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CageAdapter extends RecyclerView.Adapter<CageAdapter.CageViewHolder> {

    private List<Cage> cageList;
    private OnCageClickListener listener;
    private List<Pig> pigList;
    public CageAdapter(List<Cage> cageList, List<Pig> pigList, OnCageClickListener listener) {
        this.cageList = cageList;
        this.pigList = pigList; // Add this line
        this.listener = listener;
    }

    @NonNull
    @Override
    public CageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cage_item, parent, false);
        return new CageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CageViewHolder holder, int position) {
        Cage cage = cageList.get(position);
        holder.bind(cage, listener);
        holder.tvCageName.setText("Cage Name: " + cage.getName());
        holder.tvCageStatus.setText("Cage Status: " + cage.getStatus());
        holder.tvCageId.setText("Cage Id: " + cage.getId());

        int pigCount = 0;

        for (Pig pig : pigList){
            if (pig.getCageId().equals(cage.getId()) && !pig.isPurchase()){
                pigCount++;
            }
        }

        holder.tvPigsCount.setText("Pigs: " + pigCount);

        // Declare qrBitmap outside of the try block
        Bitmap qrBitmap = null;

        // Declare qrData outside of the try block
        final String qrData = String.format("{\"id\":\"%s\", \"cage\":\"%s\", \"status\":\"%s\", \"pigs\":\"%s\"}",
                cage.getId(), cage.getName(), cage.getStatus(), pigCount);

        try {
            // Generate QR Code
            qrBitmap = QRCodeGenerator.generateQRCode(qrData);

            // Set QR code to the ImageView
            holder.qrPlaceHolder.setImageBitmap(qrBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Show modal when QR code is clicked
        Bitmap finalQrBitmap = qrBitmap;
        holder.qrPlaceHolder.setOnClickListener(v -> {
            if (finalQrBitmap != null) {
                showQrCodeModal(v.getContext(), finalQrBitmap);
            }
        });
    }

    private void showQrCodeModal(Context context, Bitmap qrBitmap) {
        // Create an ImageView to hold the QR code
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(qrBitmap); // Set the QR code bitmap to the ImageView

        // Create the AlertDialog with the ImageView
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("QR Code")
                .setView(imageView) // Set the ImageView as the dialog's content
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Download", (dialog, which) -> {
                    // Handle the download action when clicked
                    downloadQRCode(context, qrBitmap);
                })
                .show();
    }

    private void downloadQRCode(Context context, Bitmap qrBitmap) {
        // Define the file name for the QR code
        String fileName = "qr_code.png";

        // For Android 10 and above (Scoped storage), use MediaStore to save to the Downloads folder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);  // File name
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);  // Save in Downloads folder

            Uri contentUri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            if (contentUri != null) {
                try (OutputStream outputStream = context.getContentResolver().openOutputStream(contentUri)) {
                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    Toast.makeText(context, "QR Code downloaded to Downloads folder", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to download QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // For Android versions below Android 10 (API 29)
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(directory, fileName);

            // Save the QR code bitmap to the file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                Toast.makeText(context, "QR Code downloaded to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to download QR Code", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public int getItemCount() {
        return cageList.size();
    }

    public interface OnCageClickListener {
        void onCageClick(Cage cage);
    }

    static class CageViewHolder extends RecyclerView.ViewHolder {

        TextView tvCageName, tvCageStatus, tvCageId, tvPigsCount;
        ImageView qrPlaceHolder;

        public CageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCageName = itemView.findViewById(R.id.tvCageName);
            tvCageStatus = itemView.findViewById(R.id.tvCageStatus);
            tvCageId = itemView.findViewById(R.id.tvCageId);
            tvPigsCount = itemView.findViewById(R.id.tvPigsCount);

            qrPlaceHolder = itemView.findViewById(R.id.qrPlaceHolder);
        }

        void bind(Cage cage, OnCageClickListener listener) {
            tvCageName.setText(cage.getName());
            itemView.setOnClickListener(v -> listener.onCageClick(cage));
        }
    }
}
