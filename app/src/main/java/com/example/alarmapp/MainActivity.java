package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//スタート画面の動作

public class MainActivity extends AppCompatActivity{

    private EditText edit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();


        String[] Time ={"8:00 9:00","8:10 9:10"};
        String[] name ={"arlarm,announce","arlarm,announce"};

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i=0; i<Time.length; i++){
            Map<String, String> item = new HashMap<String, String>();
            item.put("Subject", Time[i]);
            item.put("Comment", name[0]);
            data.add(item);
        }

        ListView alList = findViewById(R.id.alList);
        //List<String> menuList = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                new String[] { "Subject", "Comment" },
                new int[] { android.R.id.text1, android.R.id.text2});
        alList.setAdapter(adapter);

    }

    public void onClickConfig(View view) {
        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
        startActivity(intent);
    };


    public void onClickNewAlarm(View view) {
        Intent intent = new Intent(MainActivity.this, AlarmCreateActivity.class);
        startActivity(intent);

    }
}
