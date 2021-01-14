package com.example.alarmapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
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
import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

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

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                //データベースヘルパーオブジェクトを作成
                DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                //AlarmListクラスでアラームデータをデータベースに保存
                AlarmList.alarmAdd(tAlmHour,tAlmMinute,tAnnHour,tAnnMinute,db);

                Intent intent = new Intent(AlarmCreateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    final String[] items = {"日曜日","月曜日","火曜日","水曜日","木曜日","金曜日","土曜日"};

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.clock);

            // main.xmlからボタンオブジェクトを取得
            Button btn = (Button)findViewById(R.id.button);

            // ボタンにリスナーを設定
            btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(Dialog3Activity.this)
                    .setTitle("データを選択してください")
                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            callToast(item);
                        }
                    })
                    .setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }

        // トースト呼び出し
        public void callToast(int i){
            Toast.makeText(this, items[i] + "が選択されました", Toast.LENGTH_SHORT).show();
        }
    }

    }


