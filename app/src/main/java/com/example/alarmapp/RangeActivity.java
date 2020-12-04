package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Random;


//ランダム時間設定画面

public class RangeActivity extends AppCompatActivity {
    Random random = new Random();
    int randomValue = random.nextInt(30);

    private String mhours() {
        TextView ahour = (TextView) this.findViewById(R.id.hour);
        String hour = ahour.getText().toString();
        return hour;
    }


    private String mminutes() {
        TextView aminutes = (TextView) this.findViewById(R.id.minute);
        String minute = aminutes.getText().toString();
        return minute;
    }


}
