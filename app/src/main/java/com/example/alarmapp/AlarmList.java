package com.example.alarmapp;

//アラームに使うリスト作成
//実際に今あるのは削除用メソッドとアラーム追加用メソッドとリスト作成メソッド未使用の変数

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AlarmList{


    //AlarmCreateActivityでenterを押下したとき
    public static void alarmAdd(int tAlmHour, int tAlmMinute, int tAnnHour, int tAnnMinute, SQLiteDatabase db) {
        //データベースヘルパーオブジェクトの作成はメソッドの呼び出し元であらかじめ行う
        //DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.context);
        //SQLiteDatabase db = helper.getWritableDatabase();
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
    }
}
