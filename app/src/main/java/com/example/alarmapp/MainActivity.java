package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText edit = null;

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
        Intent intent = new Intent(MainActivity.this,clock.class);
        startActivity(intent);

    }
}