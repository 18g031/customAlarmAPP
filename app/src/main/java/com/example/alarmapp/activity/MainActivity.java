package com.example.alarmapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.alarmapp.R;
import com.example.alarmapp.Util.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//スタート画面の動作

public class MainActivity extends AppCompatActivity{

    private static Context context;
    //    private EditText edit = null;
//    int alarmId;
//    String[] Time ={};
    List<String> alarmArray= new ArrayList<>();
    String[] name ={"アラーム　　　出発"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        //データベースヘルパーオブジェクトを作成
        DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        //AlarmListクラスでリスト表示するためのデータをデータベースから読み込み
        alarmArray = createList(db);


        //Log.v("alarmArray.size",""+alarmArray.size());
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i=0; i<alarmArray.size(); i++){                  //リストを作成
            Map<String, String> item = new HashMap<String, String>();
            item.put("SettingHour", alarmArray.get(i));
            //Log.v("aaa",alarmArray.get(i));
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


    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//            Log.v("MainActList","onClick :"+position);    //タップしたリストのログ表示
//            String item = alarmArray.get(position);     //タップしたリストの場所
//            String alTime = item.substring(0,5);        //前から五文字（アラームの"hh:mm"）取得
//            String anTime = item.substring(item.length()-5);        //後ろから五文字（出発の"hh:mm"）取得
//            //Log.v("MainActTime","alTime,anTime :"+alTime+","+anTime);     //alTime,anTimeに格納されたものをログ表示
//            Intent intent = new Intent(MainActivity.this, AlarmCreateActivity.class);
//            intent.putExtra("ALKEY", alTime);//第一引数key、第二引数渡したい値
//            intent.putExtra("ANKEY", anTime);
//            intent.putExtra("POSITION",position);//削除時に必要。タップされたリストの位置
//            startActivity(intent);
//        }
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            String item = alarmArray.get(position);
            DatabaseHelper helper = new DatabaseHelper(MainActivity.context);
            SQLiteDatabase db = helper.getWritableDatabase();
            ArrayList<Integer> idArray = new ArrayList<>();      //取得した_idを格納するリスト
            //MainActivityでタップされたpositionから_idを取得する
            try{
                String sql = "SELECT _id FROM alarmList";
                Cursor cursor = db.rawQuery(sql, null);
                while(cursor.moveToNext()){
                    int idx_id = cursor.getColumnIndex("_id");
                    int alId = cursor.getInt(idx_id);
                    idArray.add(alId);
                }
            }
            finally{
            }
            int tapId = idArray.get(position);      //タップされたリストの_idを格納(取得したidのリストから要素数がpositionのものを格納)
            Intent intent = new Intent(MainActivity.this, AlarmCreateActivity.class);
            intent.putExtra("TAPID", tapId);//第一引数key、第二引数渡したい値
            startActivity(intent);      //idをAlaramCreateActivityへ引き渡す。
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

    public static List<String> createList(SQLiteDatabase db) {
        List<String> alarmArray= new ArrayList<>();
        try{
            //データベースから読み込むためのＳＱＬ。
            String sql = "SELECT * FROM alarmList";
            Cursor cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                int idxAlTH = cursor.getColumnIndex("tAlmHour");
                int idxAlTM = cursor.getColumnIndex("tAlmMinute");
                int idxAnTH = cursor.getColumnIndex("tAnnHour");
                int idxAnTM = cursor.getColumnIndex("tAnnMinute");
                int alTH= cursor.getInt(idxAlTH);
                int alTM= cursor.getInt(idxAlTM);
                int anTH= cursor.getInt(idxAnTH);
                int anTM= cursor.getInt(idxAnTM);
                String alT = String.format("%02d",alTH )+ ":" + String.format("%02d",alTM);     //それぞれの時間を0埋めして
                String anT = String.format("%02d",anTH) + ":" + String.format("%02d",anTM);     //hh:mmの形でString型に格納
                alarmArray.add(alT+"　　　"+anT);
            }
        } finally {
            //データベースは明示的にcloseしない方がよい？　→　http://hobby.txt-nifty.com/t1000/2010/11/sqliteandroid-f.html
            //db.close();
        }
        return alarmArray;
    }
}
