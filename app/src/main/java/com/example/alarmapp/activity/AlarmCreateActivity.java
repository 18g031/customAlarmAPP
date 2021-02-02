package com.example.alarmapp.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.alarmapp.receiver.AlarmReceiver;

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

    private final boolean[] mWeekCheckedItems = {false, false, false, false, false, false, false};
    private final boolean[] mAlmCheckedItems = {false, false, false, false, false};
    private final boolean[] mAnnCheckedItems = {false, false, false, true, false, true};
    int checkedItem = 0;

    TextView tvAlmTimer, tvAnnTimer, tvWeek, tv_alm_checkbox, tv_ann_checkbox;
    int setTAlmHour, setTAlmMinute, setTAnnHour, setTAnnMinute;
    int alarmId = -1;
    int annId = -1;
    int rTime;
    //timePickerで使用している変数名（tAlmHour, tAlmMinute,tAnnHour, tAnnMinute）をデータベース保存時も使用

    //以下timePicker用フォーマット変数（複数回使っていたので頭にまとめました）
    SimpleDateFormat f24Hours = new SimpleDateFormat(
            "HH:mm"
    );
    SimpleDateFormat f12Hours = new SimpleDateFormat(
            "hh:mm aa"
    );
    int kekka;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.clock);
        tvAlmTimer = findViewById(R.id.tv_alm_timer);
        tvAnnTimer = findViewById(R.id.tv_ann_timer);
        tvWeek = findViewById(R.id.tv_week);
        tv_alm_checkbox = findViewById(R.id.tv_alm_checkbox);
        tv_ann_checkbox = findViewById(R.id.tv_ann_checkbox);

        final Intent intent = getIntent();
        //前の画面(MainActivity)でタップされたアラームの_idをtapIdに格納する。
        //_idが存在しない(新規作成)ならば、-1を格納する。
        //削除メソッド(DatabaseHelper.alarmDelete)にtapIdを渡すだけで削除できるはず。
        final int tapId = intent.getIntExtra("TAPID", -1);
        if (tapId != -1) {
            List<Integer> dataArray = new ArrayList<>();
            Log.v("ACA_76", "tapId is " + tapId);//確認用（削除予定）
            DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);    //データベースヘルパーオブジェクトを作成
            SQLiteDatabase db = helper.getWritableDatabase();
            String sql = "SELECT * FROM alarmList";
            Cursor cursor = db.rawQuery(sql, null);//SQL文を実行して結果をcursorに格納
            while (cursor.moveToNext()) { //cursorの最後まで繰り返す
                int idx_id = cursor.getColumnIndex("_id");  //columnが_idの値を取り出す
                if (idx_id == tapId) {    //idが同じなら他の値も取り出してdataArrayに格納して繰り返しを終了する。
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

                    //変数に初期値入力（データベースのupdate文で変数名がかぶってしまったため、一部変数名を変更しています
                    setTAlmHour = alTH;
                    setTAlmMinute = alTM;
                    setTAnnHour = anTH;
                    setTAnnMinute = anTM;

                    int alarmId = tapId;

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


        tvWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder
                        (AlarmCreateActivity.this, android.R.style.Theme_Material_Dialog);

                final String[] items = {"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"};
                alertDialog.setTitle("繰り返し曜日");
                alertDialog.setMultiChoiceItems(items, mWeekCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mWeekCheckedItems[which] = isChecked;
                    }
                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx) {
                        String str = null;
                        for (int i = 0; i < mWeekCheckedItems.length; i++) {
                            if (mWeekCheckedItems[i] == true) {
                                str += items[i];
                            }
                        }
                        if (str == null) {
                            str = "No Selected";
                        }
                        Toast.makeText(AlarmCreateActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
            }
        });
/*
        tv_alm_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder
                        (AlarmCreateActivity.this,android.R.style.Theme_Material_Dialog);

                final String[] items = {"30分前から", "20分前から", "15分前から", "10分前から", "5分前から"};
                alertDialog.setTitle("ランダム範囲");
                alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        checkedItem = item;
                    }
                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx) {
                        String str = null;
                        for (int i = 0; i < mAlmCheckedItems.length; i++) {
                            if (mAlmCheckedItems[i] == true) {
                                str += items[i];
                            }
                        }
                        if (str == null) {
                            str = "No Selected";
                        }
                        Toast.makeText(AlarmCreateActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
            }
        });*/

        tv_ann_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder
                        (AlarmCreateActivity.this, android.R.style.Theme_Material_Dialog);

                final String[] items = {"30分前", "20分前", "15分前", "10分前", "5分前", "設定時刻"};
                alertDialog.setTitle("通知タイミング");
                alertDialog.setMultiChoiceItems(items, mAnnCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mAnnCheckedItems[which] = isChecked;
                    }
                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx) {
                        String str = null;
                        for (int i = 0; i < mAnnCheckedItems.length; i++) {
                            if (mAnnCheckedItems[i] == true) {
                                str += items[i];
                            }
                        }
                        if (str == null) {
                            str = "No Selected";
                        }
                        Toast.makeText(AlarmCreateActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
            }
        });

        //アラームのTimePickerの処理
        tvAlmTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AlarmCreateActivity.this,
                        android.R.style.Theme_Holo_Dialog,
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
                        android.R.style.Theme_Holo_Dialog,
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
                int flag = 0;

                //ランダム化
                Random random = new Random();
                int randomValue = random.nextInt(30);
                randomValue = randomValue - 1;
                Log.v("randV", "" + randomValue);

                setContentView(R.layout.clock);
                try {
                    int[] CBResult = {0, 0, 0, 0, 0, 0, 0};


                    Calendar calendar = Calendar.getInstance(); //現在時刻を取得
                    int dOW = calendar.get(Calendar.DAY_OF_WEEK); //今日の曜日(日=1、土=7)
                    int setYear = calendar.get(Calendar.YEAR);  //取得した現在時刻から年をint型に格納
                    int setMonth = calendar.get(Calendar.MONTH);
                    int setDate = calendar.get(Calendar.DATE);
                    // 冗長なので書き換えます
//                    if (calendar.get(Calendar.HOUR) < setTAlmHour) { //現在時刻の時間<アラームの設定時間.今日鳴らしたい
//                        dOW = dOW - 1;
//                    } else if (calendar.get(Calendar.HOUR) == setTAlmMinute) {
//                        if (calendar.get(Calendar.MINUTE) < setTAlmMinute) {//今日鳴らす
//                            dOW = dOW - 1;
//                            int m = setTAlmMinute - calendar.get(Calendar.MINUTE);
//                            if (m < 30) {//今日鳴らす。現在から30分以内の設定時間の場合、randomValueを変える
//                                randomValue = random.nextInt(m);
//                            }
//                        } else {   //今日は鳴らさない
//                            if (dOW == 7) {
//                                dOW = 0;
//                            }
//                        }
//                    } else { //今日は鳴らさない
//                        if (dOW == 7) {
//                            dOW = 0;
//                        }
//                    }
//                    for (int i = 0; i < 7; i++) {//設定した直近の曜日と今日の日数の差を求める
//                        Log.v("dOW", dOW + "");
//                        Log.v("CBResult", "" + CBResult[dOW]);
//                        if (CBResult[dOW] == 1) {
//                            dOW = dOW - calendar.get(Calendar.DAY_OF_WEEK);
//                            if (dOW < 0) {//マイナス（曜日が今週だと昨日以前）になったら、来週にする
//                                dOW = dOW + 7;
//                            }
//                            break;
//                        } else {
//                            dOW += 1;
//                            if (dOW == 7) {//曜日が7(土曜の翌日)の場合0(日曜日)に
//                                dOW = -0;
//                            }
//                            if (i == 6) {//曜日のチェックがない場合
//                                // flag = 1;
//                            }
//                        }
//                    }
//                    calendar.set(setYear, setMonth, setDate, setTAlmHour, setTAlmMinute);   //取得した今日の日付にアラームの設定時間を合わせたものを格納
//                    calendar.add(Calendar.DATE, dOW);//今日の日付に差を足す
//                    calendar.add(Calendar.MINUTE, -randomValue); //設定された時間をランダム化


//                    SimpleDateFormat sdf = new SimpleDateFormat("mm");//date型に変えるためのインスタンス
//                    String strtime = Integer.toString(setTAlmMinute);//intをstringに直す
//                    Date date = sdf.parse(strtime);//ｓｔｒDateをdate型に変換
//
//                    Calendar keisan = Calendar.getInstance();//計算処理
//                    keisan.setTime(date);
//                    keisan.add(Calendar.MINUTE, -randomValue);//minuteには鳴る時間がはいってる。

                    //データベース格納用型変換
                    Date dateRTime = calendar.getTime();  //CalendarからDateへ
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyMMddhhmm");
                    rTime = Integer.parseInt(sdf2.format(dateRTime));   //yyMMddhhmmでint型に。
                    Log.v("ACA_282", "" + rTime);


                    // } catch (ParseException e) {

                } finally {

                }

                //データベースヘルパーオブジェクトを作成
                DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                //AlarmListクラスでアラームデータをデータベースに保存
                ////AlarmListクラスでアラームデータをデータベースに保存
                //AlarmList.alarmAdd(tAlmHour,tAlmMinute,tAnnHour,tAnnMinute,db);
                //AlarmList.alarmAdd(tAlmHour,tAlmMinute,tAnnHour,tAnnMinute,ランダム化したアラームの時間(H)の変数名,ランダム化したアラームの時間(M)の変数名,db);

                try {
                    Log.v("try直下", "" + flag);
                    if (flag == 0) {
                        if (tapId == -1) {
                            Log.v("try", "try の先頭を実行");
                            //保存されている最大の_idを取得するSQL文
                            String sql = "SELECT * FROM alarmList";
                            Cursor cursor = db.rawQuery(sql, null);//SQL文を実行して結果をcursorに格納
                            String str;
                            while (cursor.moveToNext()) {
                                int idxId = cursor.getColumnIndex("_id");
                                str = cursor.getString(idxId);
                                alarmId = Integer.parseInt(str);
                                Log.v("try", "" + alarmId);
                            }
                            alarmId += 1;
                            //保存するためのＳＱＬ。変数によって値が変わる場所は？にする
                            String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute, randomTime) VALUES (?, ?, ?, ?, ?, ?)";
                            //String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute, randomTime, almRepeat, annRepeat, timing) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            SQLiteStatement stmt = db.compileStatement(sqlInsert);  //プリペアドステートメントを取得
                            stmt.bindLong(1, alarmId);       //alarmListの1つ目のVALUESにalarmIdを入れる
                            stmt.bindLong(2, setTAlmHour);
                            stmt.bindLong(3, setTAlmMinute);
                            stmt.bindLong(4, setTAnnHour);
                            stmt.bindLong(5, setTAnnMinute);
                            stmt.bindLong(6, rTime);
//            stmt.bindLong(7,繰り返し曜日設定(アラーム));
//            stmt.bindLong(8,繰り返し曜日設定(アナウンス));
//            stmt.bindLong(9,アナウンスタイミング);
//            stmt.bindLong(10,);

                            stmt.executeInsert();       //SQL文を実行（データベースに保存）
                        } else if (tapId != -1) {
                            //以下更新動作
                            ContentValues cv = new ContentValues();  //更新用
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
                    } else if (flag == 1) {
                        //Toast.makeText(context , "保存できませんでした(曜日にチェックを入れてください)", Toast.LENGTH_LONG).show();
                        Log.v("370", "保存できませんでした");
                    }
                } finally {

                }


                random = new Random();
                int RandInt = 1;
                randomValue = random.nextInt(RandInt);
                randomValue = randomValue - 1;

                setContentView(R.layout.clock);
                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("MM,dd,hh:mm");//date型に変えるためのインスタンス
                    String strtime = setTAlmHour + ":" + setTAlmMinute;//Integer.toString(setTAlmMinute);//intをstringに直す
                    Date date = sdf.parse(strtime);//ｓｔｒDateをdate型に変換

                    Calendar keisan = Calendar.getInstance();//計算処理+現在時刻比較（設定時間の範囲内の場合一週間後に
                    keisan.setTime(date);
                    keisan.add(Calendar.MINUTE, -RandInt);
                    if (keisan.getTimeInMillis() > 0) {
                        keisan.setTime(date);
                        keisan.add(Calendar.MINUTE, -randomValue);//minuteには鳴る時間がはいってる。
                    } else {
                        keisan.setTime(date);
                        keisan.add(Calendar.MINUTE, -randomValue);//minuteには鳴る時間がはいってる。
                        keisan.add(Calendar.DAY_OF_WEEK_IN_MONTH, +7);
                    }
                    //データベース格納用型変換
                    kekka = keisan.get(Calendar.MINUTE);  //Calendarからintへ
//                    String strKekka = String.valueOf(dateKekka);   //DateからStringへ
//                    kekka = Integer.parseInt(strKekka);     //Stringからintへ

                } catch (ParseException e) {

                }
                //明示的なBroadCast
                Intent intent = new Intent(getApplicationContext(),
                        com.example.alarmapp.receiver.AlarmReceiver.class);
                PendingIntent pending = PendingIntent.getBroadcast(
                        getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // アラームをセットする
                Calendar calendar = Calendar.getInstance();
                //設定した時間-現在時刻
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (am != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        am.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), pending);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);//getTimeInMillis:calendarと現在時刻の差分取得
                    } else {
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
                    }
                    Toast.makeText(getApplicationContext(),
                            "Set Alarm ", Toast.LENGTH_SHORT).show();
                    am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pending);


                    //ここにデータベースにランダム時間をセットする。//
                }

                //設定後メインに戻る
                Intent intent2 = new Intent(AlarmCreateActivity.this, MainActivity.class); //保存を押したらメインにもどる
                startActivity(intent2);
            }
        });
        //削除メソッド
        findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tapId != -1) {
                    //以下データベース削除
                    DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    DatabaseHelper.alarmDelete(tapId, db);

                    //以下アラームのキャンセル
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    pending.cancel();
                    alarmManager.cancel(pending);

                    Intent intent2 = new Intent(AlarmCreateActivity.this, MainActivity.class); //保存を押したらメインにもどる
                    startActivity(intent2);
                } else {
                    Intent intent2 = new Intent(AlarmCreateActivity.this, MainActivity.class); //保存を押したらメインにもどる
                    startActivity(intent2);
                }
            }


        });


    }
}
