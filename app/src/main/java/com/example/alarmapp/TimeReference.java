package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeReference extends MainActivity{
    public static void main(String[] args) {

            // 現在日時を取得
            Date date1 = new Date();
            // System.out.println(date1); //Fri Aug 05 00:28:47 JST 2016

            // 表示形式を指定
            SimpleDateFormat sdformat1
                    = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String fdate1 = sdformat1.format(date1);
            System.out.println(fdate1); // 2016/08/05 00:28:47.646
        }

    }


