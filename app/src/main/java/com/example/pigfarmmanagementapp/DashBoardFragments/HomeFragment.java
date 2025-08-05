package com.example.pigfarmmanagementapp.DashBoardFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pigfarmmanagementapp.AddCageActivity;
import com.example.pigfarmmanagementapp.Chart.AnalyticsActivity;
import com.example.pigfarmmanagementapp.Chart.ChartIlnessActivity;
import com.example.pigfarmmanagementapp.Chart.ChartVaccinatedActivity;
import com.example.pigfarmmanagementapp.PigAdvisoryStatusFragment.PigAdviceStatusAdviceFragment;
import com.example.pigfarmmanagementapp.QrCode.QrCageScannerActivity;
import com.example.pigfarmmanagementapp.QrCode.QrPigScannerActivity;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.adapter.CategoriesAdapter;
import com.example.pigfarmmanagementapp.model.Cage;
import com.example.pigfarmmanagementapp.model.Categories;
import com.example.pigfarmmanagementapp.model.Pig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    LinearLayout humidLayout, tempLayout;
    RecyclerView recyclerView;

    TextView pigCount, cageCount, pigTotalSaleTv;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        pigCount = view.findViewById(R.id.pigCount);
        cageCount = view.findViewById(R.id.cageCount);
        pigTotalSaleTv = view.findViewById(R.id.pigTotalSale);


        List<Categories> categoryList = new ArrayList<>();
        categoryList.add(new Categories(R.drawable.qr_scanner_icon, "Pig Scanner", "Scan pig QR easily"));
        categoryList.add(new Categories(R.drawable.cage_qr, "Cage Scanner", "Scan cage QR easily"));
        categoryList.add(new Categories(R.drawable.add_pig_icon, "PigCage", "Assign pigs to cages"));
        categoryList.add(new Categories(R.drawable.advice_icon, "PigStatus", "Lorem Ipsum"));
        categoryList.add(new Categories(R.drawable.analytics_icon, "Analytics", "Lorem Ipsum"));

        CategoriesAdapter adapter = new CategoriesAdapter(categoryList, category -> {
            if (category.getTitle().equals("Pig Scanner")) {
                startActivity(new Intent(requireContext(), QrPigScannerActivity.class));
            }

            if (category.getTitle().equals("Cage Scanner")) {
                startActivity(new Intent(requireContext(), QrCageScannerActivity.class));
            }

            if (category.getTitle().equals("PigCage")) {
                startActivity(new Intent(requireContext(), AddCageActivity.class));
            }
            if (category.getTitle().equals("Analytics")) {
                startActivity(new Intent(requireContext(), AnalyticsActivity.class));
            }

            if (category.getTitle().equals("PigStatus")) {
                Fragment pigAdviceFragment = new PigAdviceStatusAdviceFragment(); // Use your fragment class name
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, pigAdviceFragment) // Replace with your container ID
                        .addToBackStack(null)
                        .commit();
            }
        });

        pigsAndCagesCount();
        saleOverView();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void saleOverView(){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("pigs");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                double malePrice = 0;
                double femalePrice = 0;

                int male = 0;
                int female = 0;

                for (DataSnapshot cageSnap : snapshot.getChildren()) {

                    for (DataSnapshot pigSnap : cageSnap.getChildren()){
                        Pig pig = pigSnap.getValue(Pig.class);

                        if (pig != null){

                            String gender = pig.getGender();
                            double price = pig.getPrice();

                            if (gender.equalsIgnoreCase("male")){
                                male++;
                                malePrice = malePrice * price;
                            }else if (gender.equalsIgnoreCase("female")){
                               female++;
                                femalePrice = femalePrice * price;
                            }


                        }
                    }
                }

                double pigTotalSale = femalePrice + malePrice;
                pigTotalSaleTv.setText("PHP " + "₱" + pigTotalSale);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void pigsAndCagesCount(){
        DatabaseReference pigRef = FirebaseDatabase.getInstance().getReference("pigs");
        DatabaseReference cageRef = FirebaseDatabase.getInstance().getReference("cages");

        cageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalCages = 0;

                for (DataSnapshot cageSnap : snapshot.getChildren()){
                    Cage cage = cageSnap.getValue(Cage.class);

                    if(cage != null){
                        totalCages++;
                    }
                }

                cageCount.setText(String.valueOf("+" + totalCages + " cages"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

                pigRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int male = 0;
                int female = 0;

                for (DataSnapshot cageSnap : snapshot.getChildren()){

                    for (DataSnapshot pigSnap : cageSnap.getChildren()){
                        Pig pig = pigSnap.getValue(Pig.class);

                        if(pig != null){

                            String gender = pig.getGender();
                            if(gender.equalsIgnoreCase("male")){
                                male++;
                            }else  if(gender.equalsIgnoreCase("female")){
                                female++;
                            }
                        }
                    }
                }

                int pigTotalCount = male + female;

                pigCount.setText(String.valueOf("+" + pigTotalCount + " pigs"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
