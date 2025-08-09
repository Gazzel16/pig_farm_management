package com.example.pigfarmmanagementapp.DashBoardFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pigfarmmanagementapp.PigPriceCalculator;
import com.example.pigfarmmanagementapp.R;

public class SettingsFragment extends Fragment {

    TextView priceCalculator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        priceCalculator = view.findViewById(R.id.priceCalculator);

        priceCalculator.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), PigPriceCalculator.class);
            startActivity(intent);
        });


        return  view;
    }
}