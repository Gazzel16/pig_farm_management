package com.example.pigfarmmanagementapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pigfarmmanagementapp.DashBoardFragments.AboutFragment;
import com.example.pigfarmmanagementapp.DashBoardFragments.HomeFragment;
import com.example.pigfarmmanagementapp.DashBoardFragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Load the default fragment
        loadFragment(new HomeFragment());

        // Set item selected listener
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.about) {
                selectedFragment = new AboutFragment();
            } else if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }

            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
