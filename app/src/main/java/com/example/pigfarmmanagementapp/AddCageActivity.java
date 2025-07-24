package com.example.pigfarmmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.pigfarmmanagementapp.adapter.CageAdapter;
import com.example.pigfarmmanagementapp.model.Cage;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddCageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCages;
    private CageAdapter cageAdapter;
    private List<Cage> cageList = new ArrayList<>();
    private List<Pig> pigList = new ArrayList<>();
    private List<Cage> filteredCageList = new ArrayList<>();
    private DatabaseReference databaseCages;
    private EditText etSearchCage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cage);

        // Initialize Firebase Database
        databaseCages = FirebaseDatabase.getInstance().getReference("cages");

        // Set up RecyclerView
        recyclerViewCages = findViewById(R.id.recyclerViewCages);
        recyclerViewCages.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewCages.setAdapter(cageAdapter);

        // Set up the search bar
        etSearchCage = findViewById(R.id.etSearchCage);
        etSearchCage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterCages(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        loadPigsAndThenCages();
        // Set listener for the "Add Cage" button
        findViewById(R.id.btnAddCage).setOnClickListener(v -> showAddCageDialog());
    }

    private void loadPigsAndThenCages() {
        DatabaseReference pigRef = FirebaseDatabase.getInstance().getReference("pigs");
        pigRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pigList.clear();
                for (DataSnapshot cageSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot pigSnapshot : cageSnapshot.getChildren()) {
                        Object rawPig = pigSnapshot.getValue();
                        if (rawPig instanceof Boolean) {
                            // Skip if this child is just a "purchase" field (boolean)
                            continue;
                        }

                        Pig pig = pigSnapshot.getValue(Pig.class);
                        if (pig != null) pigList.add(pig);
                    }
                }

                loadCagesFromFirebase(); // ✅ Load cages only after pigs
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddCageActivity.this, "Failed to load pigs.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadCagesFromFirebase() {
        // Attach a listener to Firebase to get the data in real-time
        databaseCages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cage cage = snapshot.getValue(Cage.class);
                    if (cage != null) {
                        cageList.add(cage);
                    }
                }
                filteredCageList.clear();
                filteredCageList.addAll(cageList);

// Only now create and set the adapter (after cages and pigs are both loaded)
                cageAdapter = new CageAdapter(filteredCageList, pigList, cage -> {
                    Intent intent = new Intent(AddCageActivity.this, CageDetailsActivity.class);
                    intent.putExtra("cageId", cage.getId());
                    intent.putExtra("cageName", cage.getName());

                    startActivity(intent);
                });
                recyclerViewCages.setAdapter(cageAdapter);  // Notify the adapter to update the list
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                Toast.makeText(AddCageActivity.this, "Failed to load cages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterCages(String query) {
        if (query.isEmpty()) {
            // If the search query is empty, restore the full list
            filteredCageList.clear();
            filteredCageList.addAll(cageList);
        } else {
            // Filter cages by ID (case-sensitive exact match or partial match)
            filteredCageList.clear();
            for (Cage cage : cageList) {
                if (cage.getId().toLowerCase().contains(query.toLowerCase())
                        || cage.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredCageList.add(cage);
                }
            }
        }
        // Notify the adapter to refresh the RecyclerView
        cageAdapter.notifyDataSetChanged();
    }

    private void showAddCageDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_cage, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        EditText etCageName = dialogView.findViewById(R.id.etCageName);
        Button btnAddCage = dialogView.findViewById(R.id.btnAddCage);

        AlertDialog dialog = builder.create();

        btnAddCage.setOnClickListener(v -> {
            String cageName = etCageName.getText().toString();
            // Generate a 7-digit random number as cageId
            String cageId = String.format("%07d", (int)(Math.random() * 10000000));

            if (cageName.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the Cage object
            Cage newCage = new Cage(cageName, cageId);

            // Add the cage to Firebase
            databaseCages.child(cageId).setValue(newCage).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Cage added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add cage", Toast.LENGTH_SHORT).show();
                }
            });

            // Close the dialog
            dialog.dismiss();
        });

        dialog.show();
    }
}