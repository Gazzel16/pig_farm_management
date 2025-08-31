package com.example.pigfarmmanagementapp;

import com.example.pigfarmmanagementapp.handler.AddPigHandlerDialog;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pigfarmmanagementapp.adapter.PigAdapter;
import com.example.pigfarmmanagementapp.handler.EditPigHandler;
import com.example.pigfarmmanagementapp.handler.ImageHandler;
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

    private String imageUrl;
    private String targetPigId;

    //Image Handler
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private AddPigHandlerDialog currentDialog;
    private EditPigHandler editPigHandler;


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

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width;

        if(screenWidth >= 1700){
            width = 3;
        }else if(screenWidth >= 1400){
            width = 2;
        }else {
            width = 1;
        }
        recyclerViewPigs = findViewById(R.id.recyclerViewPigs);
        recyclerViewPigs.setLayoutManager(new GridLayoutManager(this, width));
        pigAdapter = new PigAdapter(pigList, cageName, cageId);
        recyclerViewPigs.setAdapter(pigAdapter);

        // Load pigs from Firebase
        loadPigsFromFirebase();

        // Set listener for the "Add Pig" button
        findViewById(R.id.btnAddPig).setOnClickListener(v -> {
            currentDialog = new AddPigHandlerDialog(this);
            currentDialog.show(
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

    //Image Handler
    public void setEditPigHandler(EditPigHandler handler) { //this line is connected to my dialog to notify the changes if they select a image
        this.editPigHandler = handler;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // this line will show the result of their activity from selecting a image
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            if (currentDialog != null) {
                currentDialog.setSelectedImageUri(selectedImageUri);
            }

            if (editPigHandler != null) {
                Log.d("onActivityResult", "Setting image URI on editPigHandler");
                editPigHandler.setSelectedImageUri(selectedImageUri);
            }
        }
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
