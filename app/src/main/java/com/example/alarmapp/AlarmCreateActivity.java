package com.example.alarmapp;

import android.app.TimePickerDialog;
import android.content.Context;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//アラーム、アナウンスの編集画面

public class AlarmCreateActivity extends AppCompatActivity {

/////////////////////////////////////
    ///////////////////////////

    TextView tvTimer;
    int tHour, tMinute;
    String time;
    int a;//出力確認用
    //AlarmCreate file = new AlarmCreate(this);

    ///////////////////////////
/////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/////////////////////////////////////
        ///////////////////////////

        setContentView(R.layout.clock);
        tvTimer = findViewById(R.id.tv_timer);

        tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AlarmCreateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker View, int hourOfDay, int minute) {
                                tHour = hourOfDay;
                                tMinute = minute;
                                time = tHour + ":" + tMinute;
                                a=tMinute;//確認用

                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    tvTimer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(tHour, tMinute);
                timePickerDialog.show();
            }
        });

        ///////////////////////////
/////////////////////////////////////

/*        setContentView(R.layout.clock);
        Intent intent = getIntent();
        String value1 = intent.getStringExtra("ALKEY");//MainActivityのリストから画面遷移した時のデータ
        String value2 = intent.getStringExtra("ANKEY");//変数名value1(アラームの時間を格納),value2(アナウンスの時間を格納)は適当
*/

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                String fileName =MainActivity.fileName;
                String fileNameid = MainActivity.fileNameid;
                alCreate(fileName,time,fileNameid);
                Intent intent = new Intent(AlarmCreateActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }

    public void alCreate(String file,String alTime,String fileid){     //内部ストレージ書き込み
        try {
            int dummyID = MainActivity.dummyID + 1;
            String dID = String.valueOf(dummyID);
            FileOutputStream fileOutputStream = openFileOutput(file, MODE_PRIVATE | MODE_APPEND);
            FileOutputStream fileOutputStreamid = openFileOutput(fileid, MODE_PRIVATE);
            fileOutputStream.write(alTime.getBytes());
            fileOutputStreamid.write(dID.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String alTime = h + "," + m;
        AlarmCreate file = new AlarmCreate(this);
        String fileName =MainActivity.fileName;
        file.alCreate(fileName,alTime);
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


