package com.example.alarmapp.util;

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
//    public static void alCreate(String file,String alTime,String fileid){     //内部ストレージ書き込み
//        try {
//            int dummyID = AlarmList.dummyID + 1;
//            String dID = String.valueOf(dummyID);
//            FileOutputStream fileOutputStream = c.openFileOutput(file, Context.MODE_PRIVATE | Context.MODE_APPEND);
//            FileOutputStream fileOutputStreamid = c.openFileOutput(fileid, Context.MODE_PRIVATE);
//            fileOutputStream.write(alTime.getBytes());
//            fileOutputStreamid.write(dID.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static AlarmString alReed(String file,String fileid){     //内部ストレージ読み込みdummyData = AlarmCreate.alReed(fileName,fileNameid);
//        AlarmString x = new AlarmString();
//        String text = null;
//        String textid = null;
//        try {
//            FileInputStream fileInputStream = c.openFileInput(file);
//            FileInputStream fileInputStreamid = c.openFileInput(fileid);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
//            BufferedReader readerid = new BufferedReader(new InputStreamReader(fileInputStreamid, "UTF-8"));
//            String lineBuffer;
//            while (true){
//                lineBuffer = reader.readLine();
//                text=lineBuffer;
//                lineBuffer = readerid.readLine();
//                textid=lineBuffer;
//                if (lineBuffer != null){
//                    break;
//                } else {
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        x.text = text;
//        x.textid = textid;
//        return x;
//    }
}