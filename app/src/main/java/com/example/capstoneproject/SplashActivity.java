package com.example.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoggedIn) {
                    // User is already logged in, navigate to the main activity
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish(); // Close the login activity to prevent going back
                } else {
                    // User is not logged in, show the login page
                    Intent intent=new Intent(SplashActivity.this, login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }

}