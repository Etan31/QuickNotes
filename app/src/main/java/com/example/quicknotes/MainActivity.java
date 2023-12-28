package com.example.quicknotes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView sidebarBtn;
    private LinearLayout overlayLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.activity_mains);
        sidebarBtn = findViewById(R.id.sidebar_btn);
        overlayLayout = findViewById(R.id.overlay_layout);
        navigationView = findViewById(R.id.navigation_view);

        sidebarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSidebar();
            }
        });

        // Close sidebar when clicked outside the sidebar view
        overlayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSidebar();
                overlayLayout.setVisibility(View.GONE);
            }
        });

        // Set the default fragment
        if (savedInstanceState == null) {
            showFragment(R.id.nav_home); // Default fragment when the activity is created
        }

        // Set item click listener for navigation menu
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home || itemId == R.id.nav_setting || itemId == R.id.nav_lockNotes || itemId == R.id.nav_recycleBin) {
                showFragment(itemId);
                closeSidebar();
                return true;
            }
            // Add more cases for other menu items if needed
            return false;
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        if (isSidebarOpen()) {
            closeSidebar();
        } else {
            finish();
        }
    }

    private void toggleSidebar() {
        if (isSidebarOpen()) {
            closeSidebar();
        } else {
            openSidebar();
        }
    }

    private boolean isSidebarOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    private void openSidebar() {
        drawerLayout.openDrawer(GravityCompat.START);
//        overlayLayout.setVisibility(View.VISIBLE);
    }

    private void closeSidebar() {
        drawerLayout.closeDrawer(GravityCompat.START);
        overlayLayout.setVisibility(View.GONE);
    }

    private void showFragment(int itemId) {
        Fragment fragment = null;
        String fragmentTitle = "";
        if (itemId == R.id.nav_home) {
            fragment = new HomeFragment();
            fragmentTitle = "All Notes";
        } else if (itemId == R.id.nav_setting) {
            fragment = new SettingFragment();
            fragmentTitle = "Settings";
        } else if (itemId == R.id.nav_lockNotes) {
            fragment = new LockedFragment();
            fragmentTitle = "Locked Notes";
        } else if (itemId == R.id.nav_recycleBin) {
            fragment = new RecycleBinFragment();
            fragmentTitle = "Recycle Bin";
        }
        // Add more else-if conditions for other menu items and corresponding fragments

        if (fragment != null) {
            updateTitle(fragmentTitle);
            replaceFragment(fragment);
        }
    }

    private void updateTitle(String title) {
        TextView titleTextView = findViewById(R.id.titleFrag);
        if (titleTextView != null) {
            titleTextView.setText(title);
        }
    }

        // Method to replace the fragment
        private void replaceFragment (Fragment fragment){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

