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

    public static void alarmDelete(int listPosition ,SQLiteDatabase db){
        //データベースヘルパーオブジェクトの作成はメソッドの呼び出し元であらかじめ行う
//        DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.context);
//        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<Integer> idArray = new ArrayList<>();      //取得した_idを格納するリスト
        try{
            //MainActivityでタップされたpositionから削除したい_idを取得する
            String sql = "SELECT * FROM alarmList";
            Cursor cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                int idx_id = cursor.getColumnIndex("_id");
                int alId = cursor.getInt(idx_id);
                idArray.add(alId);
            }
            int delId = idArray.get(listPosition);      //タップされたリストの_idを格納
            String sqlDelete = "DELETE FROM alarmList WHERE _id = ?";
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            stmt.bindLong(1, delId);
            stmt.executeUpdateDelete();     //削除SQL文の実行
        } finally {
            //データベースは明示的にcloseしない方がよい？　→　http://hobby.txt-nifty.com/t1000/2010/11/sqliteandroid-f.html
            //db.close();
        }
    }

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
            //String sqlInsert = "INSERT INTO alarmList (_id, tAlmHour, tAlmMinute, tAnnHour, tAnnMinute, ランダム化したアラームの時間(H), ランダム化したアラームの時間(M), 繰り返し曜日設定(アラーム),繰り返し曜日（アナウンス）,アナウンスタイミング) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            SQLiteStatement stmt = db.compileStatement(sqlInsert);  //プリペアドステートメントを取得
            stmt.bindLong(1,alarmId);       //alarmListの1つ目のVALUESにalarmIdを入れる
            stmt.bindLong(2,tAlmHour);
            stmt.bindLong(3,tAlmMinute);
            stmt.bindLong(4,tAnnHour);
            stmt.bindLong(5,tAnnMinute);
            stmt.executeInsert();       //SQL文を実行（データベースに保存）
        }
        finally {
        }
    }
}
