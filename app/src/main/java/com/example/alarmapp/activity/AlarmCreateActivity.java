package com.example.alarmapp.activity;

import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlarmManager;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import android.widget.Toast;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alarmapp.AlermBroadcastReceiver;
import com.example.alarmapp.R;
import com.example.alarmapp.Util.DatabaseHelper;
import android.widget.Toast;

//アラーム、アナウンスの編集画面

public class AlarmCreateActivity extends AppCompatActivity {
    public static Context context;

            /*　　
            あとやりたいこと
            ・アラーム、アナウンスのどちらかだけを設定できるようにしたい。（レイアウトも未着手）
            ・ここで得た時間をMainActivityのリストに入れれるようにしたい。
            ・ここで得た時間に対してアラームの場合”任意の時間前に通知”の奴の計算、
                アナウンスの場合”任意のランダム範囲”の奴の計算をできるようにしたい。
            ・↑計算これについては、ここにあもんが書いてたコード（現在コメントアウト中）が使えるかも、？とのこと
                使えなさそうならコードだったものは削除でお願いします。
            */
            

    TextView tvAlmTimer,tvAnnTimer;
    int tAlmHour, tAlmMinute,tAnnHour, tAnnMinute;
    //timePickerで使用している変数名（tAlmHour, tAlmMinute,tAnnHour, tAnnMinute）をデータベース保存時も使用


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.clock);
        tvAlmTimer = findViewById(R.id.tv_alm_timer);
        tvAnnTimer = findViewById(R.id.tv_ann_timer);


        //アラームのTimePickerの処理
        tvAlmTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AlarmCreateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker View, int hourOfDay, int minute) {
                                tAlmHour = hourOfDay;
                                tAlmMinute = minute;
                                String time = tAlmHour + ":" + tAlmMinute;

                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    tvAlmTimer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(tAlmHour, tAlmMinute);
                timePickerDialog.show();
            }
        });


        //アナウンスのTimePickerの処理
        tvAnnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AlarmCreateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker View, int hourOfDay, int minute) {
                                tAnnHour = hourOfDay;
                                tAnnMinute = minute;
                                String time = tAnnHour + ":" + tAnnMinute;

                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    tvAnnTimer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(tAnnHour, tAnnMinute);
                timePickerDialog.show();
            }
        });


/*        setContentView(R.layout.clock);
        Intent intent = getIntent();
        int tapId = intent.getStringExtras("TAPID");      //MainActivityでタップされたアラームのid。削除メソッド(DatabaseHelper.alarmDelete)にこれを渡すだけで削除できるはず
        //_idがtapIdのアラームのデータを読み込み
        String sql = "SELECT _id FROM alarmList WHERE _id = tapId";
        Cursor cursor = db.rawQuery(sql, null);//SQL文を実行して結果をcursorに格納
         int idxAlTH = cursor.getColumnIndex("tAlmHour");
         int idxAlTM = cursor.getColumnIndex("tAlmMinute");
         int idxAnTH = cursor.getColumnIndex("tAnnHour");
         int idxAnTM = cursor.getColumnIndex("tAnnMinute");
         int alTH= cursor.getInt(idxAlTH);
         int alTM= cursor.getInt(idxAlTM);
         int anTH= cursor.getInt(idxAnTH);
         int anTM= cursor.getInt(idxAnTM);

*/

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                //データベースヘルパーオブジェクトを作成
                DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                //AlarmListクラスでアラームデータをデータベースに保存
                ////AlarmListクラスでアラームデータをデータベースに保存
                //AlarmList.alarmAdd(tAlmHour,tAlmMinute,tAnnHour,tAnnMinute,db);
                //AlarmList.alarmAdd(tAlmHour,tAlmMinute,tAnnHour,tAnnMinute,ランダム化したアラームの時間(H)の変数名,ランダム化したアラームの時間(M)の変数名,db);

                try{
                    Log.v("try","try の先頭を実行");
                    //保存されている最大の_idを取得するSQL文
                    String sql = "SELECT * FROM alarmList";
                    Cursor cursor = db.rawQuery(sql, null);//SQL文を実行して結果をcursorに格納
                    int alarmId = -1;
                    String str ;
                    while(cursor.moveToNext()){
                        int idxId = cursor.getColumnIndex("_id");
                        str = cursor.getString(idxId);
                        alarmId = Integer.parseInt(str);
                        Log.v("try",""+alarmId);
                    }
                    alarmId +=1;
                    //保存するためのＳＱＬ。変数によって値が変わる場所は？にする
                    String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute) VALUES (?, ?, ?, ?, ?)";
                    //String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute, rAlmHour, tAlmMinute, 繰り返し曜日設定(アラーム),繰り返し曜日（アナウンス）,アナウンスタイミング) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    SQLiteStatement stmt = db.compileStatement(sqlInsert);  //プリペアドステートメントを取得
                    stmt.bindLong(1,alarmId);       //alarmListの1つ目のVALUESにalarmIdを入れる
                    stmt.bindLong(2,tAlmHour);
                    stmt.bindLong(3,tAlmMinute);
                    stmt.bindLong(4,tAnnHour);
                    stmt.bindLong(5,tAnnMinute);
//            stmt.bindLong(6,ランダム化したアラームの時間(H));
//            stmt.bindLong(7,ランダム化したアラームの時間(M));
//            stmt.bindLong(8,繰り返し曜日設定(アラーム));
//            stmt.bindLong(9,繰り返し曜日設定(アナウンス));
//            stmt.bindLong(10,アナウンスタイミング);

                    stmt.executeInsert();       //SQL文を実行（データベースに保存）
                }
                finally {
                }




                Random random = new Random();
                int randomValue = random.nextInt(30);

                setContentView(R.layout.clock);
                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("mm");//date型に変えるためのインスタンス
                    String strtime = Integer.toString(tAlmMinute);//intをstringに直す
                    Date date = sdf.parse(strtime);//ｓｔｒDateをdate型に変換

                    Calendar keisan = Calendar.getInstance();//計算処理
                    keisan.setTime(date);
                    keisan.add(Calendar.MINUTE, -randomValue);//minuteには鳴る時間がはいってる。

                } catch (ParseException e) {

                }
                //明示的なBroadCast
                Intent intent = new Intent(getApplicationContext(),
                        AlermBroadcastReceiver.class);
                PendingIntent pending = PendingIntent.getBroadcast(
                        getApplicationContext(), 0, intent, 0);

                // アラームをセットする
                Calendar calendar = Calendar.getInstance();
                //設定した時間-現在時刻
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (am != null) {
                    //am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);

                    Toast.makeText(getApplicationContext(),
                            "Set Alarm ", Toast.LENGTH_SHORT).show();
                    am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pending);



                    //ここにデータベースにランダム時間をセットする。//
                }

                Intent intent2 = new Intent(AlarmCreateActivity.this, MainActivity.class); //保存を押したらメインにもどる
                startActivity(intent2);
            }
        });
        findViewById(R.id.alList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(getApplicationContext(), com.example.alarmapp.receiver.AlarmReceiver.class);
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                pending.cancel();
                alarmManager.cancel(pending);
            }
        });
    }

}
