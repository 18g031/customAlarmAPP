package com.example.alarmapp;

import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import android.widget.Toast;

import android.database.sqlite.SQLiteDatabase;
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
        String value1 = intent.getStringExtra("ALKEY");//MainActivityのリストから画面遷移した時のデータ
        String value2 = intent.getStringExtra("ANKEY");//変数名value1(アラームの時間を格納),value2(アナウンスの時間を格納)は適当
        int listPosition = intent.getStringExtras("POSITION");      //削除用、ポジション
*/

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //データベースヘルパーオブジェクトを作成
                DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                //AlarmListクラスでアラームデータをデータベースに保存
                AlarmList.alarmAdd(tAlmHour, tAlmMinute, tAnnHour, tAnnMinute, db); //左2個がアラームの時、分　右がアナウンス

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


                    //ここにデータベースにランダム時間をセットする。//
                }


            }
        });

    } 
    }
