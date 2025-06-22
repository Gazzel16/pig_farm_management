package com.example.pigfarmmanagementapp.DashBoardFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pigfarmmanagementapp.AddCageActivity;
import com.example.pigfarmmanagementapp.PigAdviceStatusAdviceFragment;
import com.example.pigfarmmanagementapp.PigStatusAdviceFragment;
import com.example.pigfarmmanagementapp.QrCode.QrScannerActivity;
import com.example.pigfarmmanagementapp.R;
import com.example.pigfarmmanagementapp.adapter.CategoriesAdapter;
import com.example.pigfarmmanagementapp.model.Categories;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    LinearLayout humidLayout, tempLayout;
    ImageView swapTemp, swapHumid;
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

        swapTemp = view.findViewById(R.id.swapTemp);
        swapHumid = view.findViewById(R.id.swapHumid);

        recyclerView = view.findViewById(R.id.recyclerView);

        swapHumid.setOnClickListener(v -> {
            tempLayout.setVisibility(View.VISIBLE);
            humidLayout.setVisibility(View.INVISIBLE);
        });

        swapTemp.setOnClickListener(v -> {
            tempLayout.setVisibility(View.INVISIBLE);
            humidLayout.setVisibility(View.VISIBLE);
        });

        List<Categories> categoryList = new ArrayList<>();
        categoryList.add(new Categories(R.drawable.logo, "Scanner", "Scan pig ID easily"));
        categoryList.add(new Categories(R.drawable.logo, "PigCage", "Assign pigs to cages"));
        categoryList.add(new Categories(R.drawable.logo, "PigStatus", "Lorem Ipsum"));
        categoryList.add(new Categories(R.drawable.logo, "Analytics", "Lorem Ipsum"));

        CategoriesAdapter adapter = new CategoriesAdapter(categoryList, category -> {
            if (category.getTitle().equals("Scanner")) {
                startActivity(new Intent(requireContext(), QrScannerActivity.class));
            }
            if (category.getTitle().equals("PigCage")) {
                startActivity(new Intent(requireContext(), AddCageActivity.class));
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
