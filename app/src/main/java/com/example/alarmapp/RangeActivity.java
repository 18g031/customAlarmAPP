package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;



//ランダム時間設定画面
//ランダム数生成
public class RangeActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("mm");//date型に変えるためのインスタンス
            Date date = sdf.parse(mminutes());//ｓｔｒDateをdate型に変換

            Calendar keisan = Calendar.getInstance();
            keisan.setTime(date);
            keisan.add(Calendar.MINUTE, -randomValue);




        }catch (ParseException e){

        }
            }


    Random random = new Random();
    int randomValue = random.nextInt(30);



    private String mminutes() { TextView aminutes = (TextView) this.findViewById(R.id.minute);
        String minute = aminutes.getText().toString();
        return minute;
        //test

    }

}
