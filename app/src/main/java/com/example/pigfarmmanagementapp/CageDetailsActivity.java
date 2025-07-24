package com.example.pigfarmmanagementapp;

import com.example.pigfarmmanagementapp.handler.AddPigHandlerDialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pigfarmmanagementapp.adapter.PigAdapter;
import com.example.pigfarmmanagementapp.model.Cage;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CageDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPigs;
    private PigAdapter pigAdapter;
    private List<Pig> pigList = new ArrayList<>();
    private List<Cage> cageList = new ArrayList<>();
    private List<Pig> pigListFull = new ArrayList<>(); // Declare pigListFull
    private DatabaseReference databasePigs;
    private String cageId;

    private String buyerName;
    private String buyerContact;
    private boolean isPurchase;

    private String purchaseDateTime;

    private String cageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cage_details);

        // Retrieve cage information
        cageId = getIntent().getStringExtra("cageId");
        cageName = getIntent().getStringExtra("cageName");

        // Initialize Firebase reference for pigs in the specific cage
        databasePigs = FirebaseDatabase.getInstance().getReference("pigs").child(cageId);

        EditText etSearchPig = findViewById(R.id.etSearchPig);
        // Set up RecyclerView
        recyclerViewPigs = findViewById(R.id.recyclerViewPigs);
        recyclerViewPigs.setLayoutManager(new LinearLayoutManager(this));
        pigAdapter = new PigAdapter(pigList, cageName, cageId);
        recyclerViewPigs.setAdapter(pigAdapter);

        // Load pigs from Firebase
        loadPigsFromFirebase();

        // Set listener for the "Add Pig" button
        findViewById(R.id.btnAddPig).setOnClickListener(v -> {
            AddPigHandlerDialog handler = new AddPigHandlerDialog(this);
            handler.show(
                    cageId,
                    databasePigs,
                    isPurchase,
                    buyerName,
                    buyerContact,
                    purchaseDateTime
            );
        });

        etSearchPig.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterPigsByName(charSequence.toString());  // Pass the search query
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

    // This method filters the pigs based on the name (breed in your case)
    private void filterPigsByName(String query) {
        if (query.isEmpty()) {
            // If the search query is empty, restore the full list of pigs
            pigList.clear();
            pigList.addAll(pigListFull);
        } else {
            // Filter pigs by breed (case-insensitive partial match)
            pigList.clear();
            for (Pig pig : pigListFull) {
                if (pig.getBreed().toLowerCase().contains(query.toLowerCase())) {
                    pigList.add(pig);
                }
            }
        }
        // Notify the adapter to refresh the RecyclerView
        pigAdapter.notifyDataSetChanged();
    }


    private void loadPigsFromFirebase() {
        databasePigs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pigList.clear();
                pigListFull.clear(); // Clear the full list as well
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pig pig = snapshot.getValue(Pig.class);
                    if (pig != null) {
                        pigList.add(pig);
                        pigListFull.add(pig); // Also add to the full list for filtering
                    }
                }
                pigAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CageDetailsActivity.this, "Failed to load pigs", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
