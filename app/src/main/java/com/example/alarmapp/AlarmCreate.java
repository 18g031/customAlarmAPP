package com.example.alarmapp;

import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
public class AlarmCreate {
    static Context c;
    public AlarmCreate(Context ctx) {
        this.c = ctx;
    }
    public static void alCreate(String file,String alTime){     //内部ストレージ書き込み
        try {
            AlarmList.dummyID = AlarmList.dummyID + 1;
            FileOutputStream fileOutputStream = c.openFileOutput(file, Context.MODE_PRIVATE | Context.MODE_APPEND);
            fileOutputStream.write(alTime.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String alReed(String file){     //内部ストレージ読み込み
        String text = null;
        try {
            FileInputStream fileInputStream;
            fileInputStream = c.openFileInput(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
            String lineBuffer;
            while (true){
                lineBuffer = reader.readLine();
                text=lineBuffer;
                if (lineBuffer != null){
                    break;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}