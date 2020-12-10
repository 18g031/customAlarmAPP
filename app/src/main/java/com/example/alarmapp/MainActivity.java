package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//スタート画面の動作

public class MainActivity extends AppCompatActivity{

    private EditText edit = null;
    static String fileName = "test.txt";    //内部ストレージの名前
    static String fileNameid = "testid.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        //AlarmCreate file = new AlarmCreate(this);
        AlarmCreate sub = new AlarmCreate(this);
        AlarmString x = sub.alReed(fileName,fileNameid);
        String dummyTime[]={null};
        String dummyData;
        int dummyID = -1;
        Log.v("21234567892ooo22",x.text);

        if ((x.text).equals(null)){
            dummyData ="08:00";
            dummyID = 0;
        }else{
            dummyID = Integer.parseInt(x.textid);
            dummyData = x.text;
            for (int i= 0; i<=dummyID;i++){
                dummyTime[i] = dummyData.substring(i*5,5);
            }
        }

        //AlarmCreate.alReed(fileName);
        //デモ用ダミー
//        String dummyTime[]={null};
//        String dummyData;
//        AlarmCreate file = new AlarmCreate(this);
//        AlarmCreate sub = new AlarmCreate(this);
//        AlarmString x = sub.alReed(fileName,fileNameid);
//        dummyID = Integer.parseInt(x.textid);
//        dummyData = x.text;
//        for (int i= 0; i<=dummyID;i++){
//            dummyTime[i] = dummyData.substring(i*5,5);
//        }

        //デモ用リスト（ListView）
        String[] name ={"アラーム"};
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i=0; i<dummyTime.length; i++){                  //リストを作成
            Map<String, String> item = new HashMap<String, String>();
            item.put("SettingHour", dummyTime[i]);
            item.put("Subject", name[0]);
            data.add(item);
        }
        ListView alList = findViewById(R.id.alList);
        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                new String[] { "SettingHour","Subject" },
                new int[] { android.R.id.text1, android.R.id.text2});
        alList.setAdapter(adapter);
    }


//        //リスト（ListView）
//    String[] Time ={"ダミー　　　データ","8:10　　　　9:10"};   //AlarmList.javaのalTime[]を参照したい。アラーム、アナウンスの時間を保持
//    String[] name ={"アラーム　　　出発"};
//    List<Map<String, String>> data = new ArrayList<Map<String, String>>();
//        for (int i=0; i<Time.length; i++){                  //リストを作成
//        Map<String, String> item = new HashMap<String, String>();
//        item.put("SettingHour", Time[i]);
//        item.put("Subject", name[0]);
//        data.add(item);
//    }
//    ListView alList = findViewById(R.id.alList);
//    SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
//            new String[] { "SettingHour","Subject" },
//            new int[] { android.R.id.text1, android.R.id.text2});
//        alList.setAdapter(adapter);
//}



    public void onClickConfig(View view) {
        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
        startActivity(intent);
    };


    public void onClickNewAlarm(View view) {
        Intent intent = new Intent(MainActivity.this, AlarmCreateActivity.class);
        startActivity(intent);

    }


}
