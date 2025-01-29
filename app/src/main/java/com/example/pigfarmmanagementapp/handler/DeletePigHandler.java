package com.example.pigfarmmanagementapp.handler;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeletePigHandler {
    public static void deletePig(Context context, Pig pig, String cageId, Runnable onDeleteConfirmed) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Pig")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Perform delete action
                    deletePigFromFirebase(pig, cageId, onDeleteConfirmed);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private static void deletePigFromFirebase(Pig pig, String cageId, Runnable onDeleteConfirmed) {
        // Get Firebase Database reference to the pig inside the specific cage
        DatabaseReference pigRef = FirebaseDatabase.getInstance().getReference("cages")
                .child(cageId) // Navigate to the cage
                .child("pigs") // Navigate to the pigs under this cage
                .child(pig.getId()); // Target the specific pig by ID

        // Delete the pig from Firebase
        pigRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Notify that deletion was successful
                    onDeleteConfirmed.run(); // Notify the adapter or refresh the UI
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    // For example, show a Toast or alert if the deletion failed
                });
    }
}
