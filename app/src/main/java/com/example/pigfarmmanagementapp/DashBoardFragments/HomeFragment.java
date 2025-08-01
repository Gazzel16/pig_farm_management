package com.example.pigfarmmanagementapp.DashBoardFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.example.pigfarmmanagementapp.model.Categories;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    LinearLayout humidLayout, tempLayout;
    RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        humidLayout = view.findViewById(R.id.humidLayout);
        tempLayout = view.findViewById(R.id.tempLayout);

        recyclerView = view.findViewById(R.id.recyclerView);


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

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }



}
