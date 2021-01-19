package com.example.alarmapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import com.example.alarmapp.R;

//アプリ設定画面

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();
    }

}