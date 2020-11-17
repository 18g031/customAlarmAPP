package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
    }

    public void onClickConfig(View view) {
        Intent intent = new Intent(MainActivity.this,subActivity.class);
        startActivity(intent);
    }

    public void onClickNewAlarm(View view) {
        Intent intent = new Intent(clock.this,subActivity.class);
        startActivity(intent);

    }
}