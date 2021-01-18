package com.example.alarmapp.activity;

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
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.sqlite.SQLiteDatabase;

import com.example.alarmapp.R;
import com.example.alarmapp.Util.DatabaseHelper;

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
         int alTH= cursor.getInt(idxAlTH);      //_idがtapIdのtAlmHourの値をalTHに格納
         int alTM= cursor.getInt(idxAlTM);
         int anTH= cursor.getInt(idxAnTH);
         int anTM= cursor.getInt(idxAnTM);

         if(tapId != null){
            tAlmHour=alTH;
            tAlmMinute=alTM;
            tAnnHour=anTH;
            tAnnMinute=anTM;
         }

*/

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                //データベースヘルパーオブジェクトを作成
                DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
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
                    //String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute, rAlmHour, tAlmMinute, almRepeat, annRepeat, timing) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

                Intent intent = new Intent(AlarmCreateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

 /*    //時を取得
   private String mhours() {
        EditText Ehour = (EditText) this.findViewById(R.id.hour);
        String hour = Ehour.getText().toString();
        return hour;
    }

    //分を取得
    private String mminutes() {
        EditText Eminute = (EditText) this.findViewById(R.id.minute);
        String minute = Eminute.getText().toString();
        return minute;
    }

    //設定した時刻を表示
    private void timeConf(String h, String m) {
        TextView confview = (TextView) this.findViewById(R.id.confview);
        confview.setText("設定した時間は　" + h + "：" + m);
    }
/*    public void timerSet(Calendar calendar){
   //実行するサービスを指定
        Intent intent = new Intent(getApplicationContext(), messageService.class);
        Context ct = getApplication();
        PendingIntent pendingIntent = PendingIntent.getService(ct, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // AlarmManager の設定・開始
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }*/

    }


