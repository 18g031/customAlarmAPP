package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

//読み上げ時間設定画面

public class AnnTimeActivity extends AppCompatActivity {
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ann);



        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is checkBox checked?
                if (((CheckBox) v).isChecked()) {

                }

            }

        });
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is checkBox checked?
                if (((CheckBox) v).isChecked()) {

                }

            }

        });
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is checkBox checked?
                if (((CheckBox) v).isChecked()) {

                }

            }

        });
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is checkBox checked?
                if (((CheckBox) v).isChecked()) {

                }

            }

        });
        checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is checkBox checked?
                if (((CheckBox) v).isChecked()) {

                }

            }

        });
        checkBox6 = (CheckBox) findViewById(R.id.checkBox6);
        checkBox6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is checkBox checked?
                if (((CheckBox) v).isChecked()) {

                }

            }

        });
    }}