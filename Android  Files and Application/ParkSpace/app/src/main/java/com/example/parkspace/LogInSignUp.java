package com.example.parkspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class LogInSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_sign_up);

        ViewPager viewPager = findViewById(R.id.view_pager);

        LogInSignUpAdapter logInSignUpAdapter = new LogInSignUpAdapter(getSupportFragmentManager());

        viewPager.setAdapter(logInSignUpAdapter);

        TabLayout tableLayout = findViewById(R.id.sliding_tabs);
        tableLayout.setupWithViewPager(viewPager);
    }
}
