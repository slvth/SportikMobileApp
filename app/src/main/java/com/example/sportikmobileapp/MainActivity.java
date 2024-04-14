package com.example.sportikmobileapp;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sportikmobileapp.database.notification.MyForegroundService;
import com.example.sportikmobileapp.database.notification.PgNotificationListener;
import com.example.sportikmobileapp.fragment.AboutUsFragment;
import com.example.sportikmobileapp.fragment.AccountFragment;
import com.example.sportikmobileapp.fragment.BookingFragment;
import com.example.sportikmobileapp.fragment.InventoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    InventoryFragment inventoryFragment = new InventoryFragment();
    BookingFragment bookingFragment = new BookingFragment();
    AccountFragment accountFragment = new AccountFragment();
    AboutUsFragment aboutUsFragment = new AboutUsFragment();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if(!foregroundServiceRunning()) {
            Intent serviceIntent = new Intent(this,
                    MyForegroundService.class);
            startForegroundService(serviceIntent);
        }*/


        /*
        Thread notificationListenerThread = null;
        try {
            notificationListenerThread = new Thread(new PgNotificationListener(this));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        notificationListenerThread.start();*/

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
                    case R.id.about_us:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, aboutUsFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    //Убирать фокус с ввода текста
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean foregroundServiceRunning(){
        /*
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(MyForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }*/
        return false;
    }
}