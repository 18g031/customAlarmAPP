package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickConfig(View view) {
        String s = getResources().getString(R.string.hello_world);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void onClickNewAlarm(View view) {

    }
}