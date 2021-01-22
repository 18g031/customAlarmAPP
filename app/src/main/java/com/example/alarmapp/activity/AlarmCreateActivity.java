package com.example.alarmapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmapp.R;
import com.example.alarmapp.Util.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


//import com.example.alarmapp.AlermBroadcastReceiver;

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


    TextView tvAlmTimer, tvAnnTimer;
    int setTAlmHour, setTAlmMinute, setTAnnHour, setTAnnMinute;
    //timePickerで使用している変数名（tAlmHour, tAlmMinute,tAnnHour, tAnnMinute）をデータベース保存時も使用
    //以下timePicker用フォーマット変数（複数回使っていたので頭にまとめました）
    SimpleDateFormat f24Hours = new SimpleDateFormat(
            "HH:mm"
    );
    SimpleDateFormat f12Hours = new SimpleDateFormat(
            "hh:mm aa"
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.clock);
        tvAlmTimer = findViewById(R.id.tv_alm_timer);
        tvAnnTimer = findViewById(R.id.tv_ann_timer);

        final Intent intent = getIntent();
        //前の画面(MainActivity)でタップされたアラームの_idをtapIdに格納する。
        //_idが存在しない(新規作成)ならば、-1を格納する。
        //削除メソッド(DatabaseHelper.alarmDelete)にtapIdを渡すだけで削除できるはず。
        final int tapId = intent.getIntExtra("TAPID", -1);
        if(tapId != -1){
            List<Integer> dataArray= new ArrayList<>();
            Log.v("ACA_76","tapId is "+tapId);//確認用（削除予定）
            DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);    //データベースヘルパーオブジェクトを作成
            SQLiteDatabase db = helper.getWritableDatabase();
            String sql = "SELECT * FROM alarmList";
            Cursor cursor = db.rawQuery(sql, null);//SQL文を実行して結果をcursorに格納
            while(cursor.moveToNext()){ //cursorの最後まで繰り返す
                int idx_id = cursor.getColumnIndex("_id");  //columnが_idの値を取り出す
                if(idx_id == tapId){    //idが同じなら他の値も取り出してdataArrayに格納して繰り返しを終了する。
                    int idxAlTH = cursor.getColumnIndex("tAlmHour");
                    int idxAlTM = cursor.getColumnIndex("tAlmMinute");
                    int idxAnTH = cursor.getColumnIndex("tAnnHour");
                    int idxAnTM = cursor.getColumnIndex("tAnnMinute");
                    int alTH = cursor.getInt(idxAlTH);
                    int alTM = cursor.getInt(idxAlTM);
                    int anTH = cursor.getInt(idxAnTH);
                    int anTM = cursor.getInt(idxAnTM);
                    dataArray.add(alTH);
                    dataArray.add(alTM);
                    dataArray.add(anTH);
                    dataArray.add(anTM);
                    setTAlmHour = alTH;
                    setTAlmMinute = alTM;
                    setTAnnHour = anTH;
                    setTAnnMinute = anTM;


                    //ここからtimePickerの初期データ登録
                    try {
                        //アラームデータ格納
                        String alTime = alTH + ":" + alTM;
                        Date alDate = f24Hours.parse(alTime);
                        tvAlmTimer.setText(f12Hours.format(alDate));
                        //アナウンスデータ格納
                        String anTime = anTH + ":" + anTM;
                        Date anDate = f24Hours.parse(anTime);
                        tvAnnTimer.setText(f12Hours.format(anDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    break;
                }
            }
//            tAlmHour = timeArray.get(0);
//            tAlmMinute = timeArray.get(1);
//            tAnnHour = timeArray.get(2);
//            tAnnMinute = timeArray.get(3);
        }


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
                                setTAlmHour = hourOfDay;
                                setTAlmMinute = minute;
                                String time = setTAlmHour + ":" + setTAlmMinute;


                                try {
                                    Date date = f24Hours.parse(time);
                                    tvAlmTimer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(setTAlmHour, setTAlmMinute);
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
                                setTAnnHour = hourOfDay;
                                setTAnnMinute = minute;
                                String time = setTAnnHour + ":" + setTAnnMinute;

                                try {
                                    Date date = f24Hours.parse(time);
                                    tvAnnTimer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(setTAnnHour, setTAnnMinute);
                timePickerDialog.show();
            }
        });


/*        setContentView(R.layout.clock);
        Intent intent = getIntent();
*/

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //データベースヘルパーオブジェクトを作成
                DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                //AlarmListクラスでアラームデータをデータベースに保存
                ////AlarmListクラスでアラームデータをデータベースに保存
                //AlarmList.alarmAdd(tAlmHour,tAlmMinute,tAnnHour,tAnnMinute,db);
                //AlarmList.alarmAdd(tAlmHour,tAlmMinute,tAnnHour,tAnnMinute,ランダム化したアラームの時間(H)の変数名,ランダム化したアラームの時間(M)の変数名,db);

                try {
                    if (tapId == -1) {
                        Log.v("try", "try の先頭を実行");
                        //保存されている最大の_idを取得するSQL文
                        String sql = "SELECT * FROM alarmList";
                        Cursor cursor = db.rawQuery(sql, null);//SQL文を実行して結果をcursorに格納
                        int alarmId = -1;
                        String str;
                        while (cursor.moveToNext()) {
                            int idxId = cursor.getColumnIndex("_id");
                            str = cursor.getString(idxId);
                            alarmId = Integer.parseInt(str);
                            Log.v("try", "" + alarmId);
                        }
                        alarmId += 1;
                        //保存するためのＳＱＬ。変数によって値が変わる場所は？にする
                        String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute) VALUES (?, ?, ?, ?, ?)";
                        //String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute, rAlmHour, rAlmMinute, almRepeat, annRepeat, timing) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        SQLiteStatement stmt = db.compileStatement(sqlInsert);  //プリペアドステートメントを取得
                        stmt.bindLong(1, alarmId);       //alarmListの1つ目のVALUESにalarmIdを入れる
                        stmt.bindLong(2, setTAlmHour);
                        stmt.bindLong(3, setTAlmMinute);
                        stmt.bindLong(4, setTAnnHour);
                        stmt.bindLong(5, setTAnnMinute);
//            stmt.bindLong(6,ランダム化したアラームの時間(H));
//            stmt.bindLong(7,ランダム化したアラームの時間(M));
//            stmt.bindLong(8,繰り返し曜日設定(アラーム));
//            stmt.bindLong(9,繰り返し曜日設定(アナウンス));
//            stmt.bindLong(10,アナウンスタイミング);

                        stmt.executeInsert();       //SQL文を実行（データベースに保存）
                    } else if (tapId != -1) {
                        ContentValues cv = new ContentValues();  //プリペアドステートメントを取得
                        int alarmId = tapId;

                        cv.put("_id", alarmId);
                        cv.put("tAlmHour", setTAlmHour);
                        cv.put("tAlmMinute", setTAlmMinute);
                        cv.put("tAnnHour", setTAnnHour);
                        cv.put("tAnnMinute", setTAnnMinute);
/*
                        cv.put("rAlmHou", ランダム化したアラームの時間(H));
                        cv.put("rAlmMinute", ランダム化したアラームの時間(M));
                        cv.put("almRepeat", 繰り返し曜日設定(アラーム));
                        cv.put("annRepeat", 繰り返し曜日設定(アナウンス));
                        cv.put("timing", アナウンスタイミング);
*/
                        db.update("alarmList", cv, "_id = " + alarmId, null);
                    }
                }
                finally {
                }




                Random random = new Random();
                int randomValue = random.nextInt(30);

                setContentView(R.layout.clock);
                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("mm");//date型に変えるためのインスタンス
                    String strtime = Integer.toString(setTAlmMinute);//intをstringに直す
                    Date date = sdf.parse(strtime);//ｓｔｒDateをdate型に変換

                    Calendar keisan = Calendar.getInstance();//計算処理
                    keisan.setTime(date);
                    keisan.add(Calendar.MINUTE, -randomValue);//minuteには鳴る時間がはいってる。

                } catch (ParseException e) {

                }
                //明示的なBroadCast
                Intent intent = new Intent(getApplicationContext(),
                        com.example.alarmapp.receiver.AlarmReceiver.class);
                PendingIntent pending = PendingIntent.getBroadcast(
                        getApplicationContext(), 0, intent, 0);

                // アラームをセットする
                Calendar calendar = Calendar.getInstance();
                //設定した時間-現在時刻
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (am != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    am.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), pending);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        am.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pending);
                    } else {
                        am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pending);
                    }
                    Toast.makeText(getApplicationContext(),
                            "Set Alarm ", Toast.LENGTH_SHORT).show();
                    am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pending);



                    //ここにデータベースにランダム時間をセットする。//
                }

                Intent intent2 = new Intent(AlarmCreateActivity.this, MainActivity.class); //保存を押したらメインにもどる
                startActivity(intent2);
            }
        });
        /*findViewById(R.id.alList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                pending.cancel();
                alarmManager.cancel(pending);
            }
        });*/
    }

}
