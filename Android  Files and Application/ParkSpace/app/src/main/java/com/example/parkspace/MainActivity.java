package com.example.parkspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.parkspace.appsharedpreferences.UserSharedPreferencesInfo;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(!new UserSharedPreferencesInfo(MainActivity.this).getUsername().isEmpty() && !new UserSharedPreferencesInfo(MainActivity.this).getPassword().isEmpty()) {
                    startActivity(new Intent(MainActivity.this, BookParking.class));
                } else {
                    startActivity(new Intent(MainActivity.this, LogInSignUp.class));
                }
                finish();

            }
        },1);
    }
}
