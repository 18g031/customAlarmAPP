package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
    int alarmId;
    String[] Time ={};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        //データベースヘルパーオブジェクトを作成
        DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            //読み込むためのＳＱＬ。
            String sql = "SELECT * FROM alarmList";
            Cursor cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                int idxId = cursor.getColumnIndex("_id");
                alarmId = cursor.getInt(idxId);
                int idxAlTH = cursor.getColumnIndex("tAlmHour");
                int idxAlTM = cursor.getColumnIndex("tAlmMinute");
                int idxAnTH = cursor.getColumnIndex("tAnnHour");
                int idxAnTM = cursor.getColumnIndex("tAnnMinute");
                int alTH= cursor.getInt(idxAlTH);
                int alTM= cursor.getInt(idxAlTM);
                int anTH= cursor.getInt(idxAnTH);
                int anTM= cursor.getInt(idxAnTM);
                String alT = alTH + ":" + alTM;
                String anT = anTH + ":" + anTM;
                Time[alarmId] = alT+"    "+anT;
            }


        } finally {
            db.close();
        }

//
//        //リスト（ListView）
//        String[] Time ={"ダミー　　　データ","08:10　　　09:10"};   //AlarmList.javaのalTime[]を参照したい。アラーム、アナウンスの時間を保持
//        String[] name ={"アラーム　　　出発"};
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i=0; i<Time.length; i++){                  //リストを作成
            Map<String, String> item = new HashMap<String, String>();
            item.put("SettingHour", Time[i]);
            Log.v("aaa",Time[i]);
            item.put("Subject", name[0]);
            data.add(item);
        }
        ListView alList = findViewById(R.id.alList);
        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                new String[] { "SettingHour","Subject" },
                new int[] { android.R.id.text1, android.R.id.text2});
        alList.setAdapter(adapter);

        //リストビューにリスナーを追加
        alList.setOnItemClickListener(new ListItemClickListener());
    }

    //リスト（ListView）
    //String[] Time ={"8:10　　　　9:10","15:30　　　16:00"};   //AlarmList.javaのalTime[]を参照したい。アラーム、アナウンスの時間を保持
    String[] name ={"アラーム　　　出発"};


    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Log.v("MainActList","onClick"+position);    //タップしたリストのログ表示
            String item = Time[position];
            String alTime = item.substring(0,5);
            String anTime = item.substring(item.length()-5);
            Log.v("MainActTime",alTime+","+anTime);     //alTime,anTimeに格納されたものをログ表示
            Intent intent = new Intent(MainActivity.this, AlarmCreateActivity.class);
            intent.putExtra("ALKEY", alTime);//第一引数key、第二引数渡したい値
            intent.putExtra("ANKEY", anTime);
            startActivity(intent);
        }

    }

    public void onClickConfig(View view) {
        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
        startActivity(intent);
    }


    public void onClickNewAlarm(View view) {
        Intent intent = new Intent(MainActivity.this, AlarmCreateActivity.class);
        startActivity(intent);

    }


}
