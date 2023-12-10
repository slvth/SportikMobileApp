package com.example.sportikmobileapp;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sportikmobileapp.fragment.AccountFragment;
import com.example.sportikmobileapp.fragment.BookingFragment;
import com.example.sportikmobileapp.fragment.InventoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    InventoryFragment inventoryFragment = new InventoryFragment();
    BookingFragment bookingFragment = new BookingFragment();
    AccountFragment accountFragment = new AccountFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, inventoryFragment).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.inventory:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, inventoryFragment).commit();
                        return true;
                    case R.id.booking:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, bookingFragment).commit();
                        return true;
                    case R.id.account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, accountFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}