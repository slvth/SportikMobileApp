package com.example.sportikmobileapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sportikmobileapp.MainActivity;
import com.example.sportikmobileapp.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView imgSplashScreen = findViewById(R.id.imgSplashScreen);
        Glide.with(this).load(R.drawable.splash_gif_main).into(imgSplashScreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
                int user_id = sharedPref.getInt(getString(R.string.pref_id), 0);

                if(user_id!=0){
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, 5000);
    }
}