package com.example.pigfarmmanagementapp.adapter;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;  // Add this import for Context
import android.app.AlertDialog;  // Add this import for AlertDialog
import android.widget.Toast;

import com.example.pigfarmmanagementapp.handler.EditPigHandler;
import com.example.pigfarmmanagementapp.handler.DeletePigHandler;

public class PigAdapter extends RecyclerView.Adapter<PigAdapter.PigViewHolder> implements Filterable {

    private List<Pig> pigList;
    private String cageName;
    private List<Pig> pigListFull;
    private String cageId;
    public PigAdapter(List<Pig> pigList, String cageName, String cageId) {
        this.pigList = pigList;
        this.pigListFull = new ArrayList<>(pigList); // Make a copy for filtering
        this.cageName = cageName;
        this.cageId = cageId; // Set the cageId
    }

    @NonNull
    @Override
    public PigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pig, parent, false);
        return new PigViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PigViewHolder holder, int position) {
        Pig pig = pigList.get(position);

        holder.tvPigBreed.setText(pig.getBreed());
        holder.tvPigBirthDate.setText( pig.getBirthDate());
        holder.tvPigWeight.setText(pig.getWeight() + " kg");
        holder.tvPigStatus.setText(pig.vaccinationStatus());
        holder.tvPigIllness.setText(pig.pigIllness());

        holder.tvCageName.setText(cageName);
        holder.tvGender.setText(pig.gender());
        holder.tvPiglastCheckUpDate.setText("Illness: " + pig.lastCheckUp());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Match your format
        Date today = new Date();


            try {
                Date checkUpDate = sdf.parse(pig.getLastCheckUp());
                long diffInMillies = today.getTime() - checkUpDate.getTime();
                long daysSinceCheckUp = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if (daysSinceCheckUp > 30) {
                    holder.tvPiglastCheckUpDate.setText("Last Checkup: " + pig.lastCheckUp() + " (Overdue)");
                    Log.d("Overdue", "Pig ID: " + pig.getId() + " is overdue by " + daysSinceCheckUp + " days");
                }
            }catch (Exception e){
                e.printStackTrace();
            }



        if (pig.isPurchase()) {
            holder.tvBuyerName1.setText(pig.getBuyerName());
            holder.sold.setVisibility(View.VISIBLE);
        } else {
            holder.tvBuyerName1.setText("No Buyer Yet");
            holder.sold.setVisibility(View.GONE);
        }


        holder.btnEdit.setOnClickListener(v -> {
            // Make sure cageId is being passed correctly from the activity/fragment
            if (cageId == null) {
                Log.e("EditPigHandler", "cageId is null");
                Toast.makeText(v.getContext(), "Error: cageId is missing!", Toast.LENGTH_SHORT).show();
                return;
            }

            EditPigHandler.editPig(v.getContext(), pig, cageId, () -> {
                // Code to refresh the data or notify the adapter
                notifyDataSetChanged();
            });
        });


        // Delete button functionality
        // Delete button functionality
        // Use holder.getAdapterPosition() when needed
        holder.btnDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();  // Get the current position dynamically

            if (currentPosition != RecyclerView.NO_POSITION) {
                // Call the delete handler with the current position
                DeletePigHandler.deletePig(v.getContext(), pig, cageId, new Runnable() {
                    @Override
                    public void run() {
                        pigList.remove(currentPosition);  // Remove the pig from the list
                        notifyItemRemoved(currentPosition);  // Notify the adapter
                    }
                });
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            View dialogView = LayoutInflater.from(v.getContext())
                    .inflate(R.layout.dialog_pig_details, null);

            ((TextView) dialogView.findViewById(R.id.tvPigId)).setText(pig.getId());
            ((TextView) dialogView.findViewById(R.id.tvPigBreed)).setText(pig.getBreed());
            ((TextView) dialogView.findViewById(R.id.tvPigGender)).setText(pig.getGender());
            ((TextView) dialogView.findViewById(R.id.tvPigBirthDate)).setText(pig.getBirthDate());
            ((TextView) dialogView.findViewById(R.id.tvPigWeight)).setText(pig.getWeight() + " kg");
            ((TextView) dialogView.findViewById(R.id.tvPigIllness)).setText(pig.getPigIllness());
            ((TextView) dialogView.findViewById(R.id.tvPigVaccine)).setText(pig.getVaccinationStatus());
            ((TextView) dialogView.findViewById(R.id.tvPigCage)).setText(cageName);

            ((TextView) dialogView.findViewById(R.id.buyerName)).setText( pig.getBuyerName());
            ((TextView) dialogView.findViewById(R.id.buyerContact)).setText(pig.getBuyerContact());


            TextView checkupTextView = dialogView.findViewById(R.id.tvPigCheckup);
                try {
                    Date checkUpDate = sdf.parse(pig.getLastCheckUp());
                    long diffInMillies = today.getTime() - checkUpDate.getTime();
                    long daysSinceCheckUp = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    if (daysSinceCheckUp > 30) {
                        checkupTextView.setText(pig.lastCheckUp() + " (Overdue)");
                        Log.d("Overdue", "Pig ID: " + pig.getId() + " is overdue by " + daysSinceCheckUp + " days");
                    }else {
                        checkupTextView.setText(pig.lastCheckUp() + " (On Schedule)");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                    .setView(dialogView)
                    .create();

            dialog.show();


            return true;
        });


        // Declare qrBitmap outside of the try block
        Bitmap qrBitmap = null;

        // Declare qrData outside of the try block
        final String qrData = String.format("{\"id\":\"%s\", \"breed\":\"%s\", \"weight\":\"%s\", \"status\":\"%s\"}",
                pig.getId(), pig.getBreed(), pig.getWeight(), pig.vaccinationStatus());

        try {
            // Generate QR Code
            qrBitmap = QRCodeGenerator.generateQRCode(qrData);

            // Set QR code to the ImageView
            holder.qrCode.setImageBitmap(qrBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Show modal when QR code is clicked
        Bitmap finalQrBitmap = qrBitmap;
        holder.qrCode.setOnClickListener(v -> {
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
        return pigList.size();
    }

    static class PigViewHolder extends RecyclerView.ViewHolder {
        TextView tvPigBreed,tvPigBirthDate, tvPigWeight,
                tvPigStatus, tvPigIllness, tvGender,
                tvPiglastCheckUpDate, tvCageName, tvBuyerName, tvBuyerContact, tvBuyerName1;
        ImageView qrCode, sold;
        ImageView btnEdit, btnDelete;

        PigViewHolder(View itemView) {
            super(itemView);
            tvPigBreed = itemView.findViewById(R.id.tvPigBreed);
            tvPigBirthDate = itemView.findViewById(R.id.tvPigBirthDate);
            tvPigWeight = itemView.findViewById(R.id.tvPigWeight);
            tvPigStatus = itemView.findViewById(R.id.tvPigStatus);
            tvPigIllness = itemView.findViewById(R.id.tvPigIllness);
            qrCode = itemView.findViewById(R.id.qrCode);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            tvCageName = itemView.findViewById(R.id.tvCageName);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvPiglastCheckUpDate = itemView.findViewById(R.id.tvPiglastCheckUpDate);

            tvBuyerName = itemView.findViewById(R.id.buyerName);
            tvBuyerContact = itemView.findViewById(R.id.buyerContact);

            tvBuyerName1 = itemView.findViewById(R.id.tvBuyerName1);



            sold = itemView.findViewById(R.id.sold);
        }
    }

    @Override
    public Filter getFilter() {
        return pigFilter;
    }

    private Filter pigFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Pig> filteredList = new ArrayList<>();
            String filterPattern = constraint.toString().toLowerCase().trim();
            Log.d("FilterDebug", "Filtering for: " + filterPattern);  // Log the filter input

            // If the filter is empty, show all pigs
            if (filterPattern.isEmpty()) {
                filteredList.addAll(pigListFull);  // All pigs in the full list
            } else {
                // Filter pigs by breed
                for (Pig pig : pigListFull) {
                    if (pig.getBreed().toLowerCase().contains(filterPattern)) {
                        filteredList.add(pig);
                    }
                }
            }

            // Set up the results to return
            FilterResults results = new FilterResults();
            results.values = filteredList;
            Log.d("FilterDebug", "Filtered list size: " + filteredList.size());  // Log the filtered list size
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                pigList.clear();
                pigList.addAll((List<Pig>) results.values);  // Add the filtered pigs to the adapter list
                notifyDataSetChanged();  // Notify the adapter to update the UI
            }
        }
    };


}
