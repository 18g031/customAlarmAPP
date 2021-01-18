package com.example.alarmapp.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper { //アプリ初回起動時　データベースのテーブル作成
    //データベースファイルの定数フィールド
    private static final String DATABASE_NAME = "alarmList.db";
    //バージョン情報の定数フィールド。
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        //親クラスのコンストラクタ呼び出し
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){    //同じ名前のデータベースが存在しない場合に実行される。
        // 以下に変更を加えた場合はテスト前にアプリの再インストールをしないと反映されない←onUpgrade追記で解消されて再インストール不要のはず(未確認）
        //テーブル作成用SQL文字列の作成。　"_id"の主キーがほぼ必須（？）
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE alarmList (");
        sb.append("_id INTEGER PRIMARY KEY,");      //アラームのidを主キーとする
        sb.append("tAlmHour INTEGER,");    //アラームの設定時間
        sb.append("tAlmMinute INTEGER,");
//        sb.append("rAlmHour INTEGER,");     //ランダム化したアラームの設定時間
//        sb.append("rAlmMinute INTEGER,");
        sb.append("tAnnHour INTEGER,");    //出発の設定時間
        sb.append("tAnnMinute INTEGER");
//        sb.append("anTiming INTEGER,");      //アナウンスの設定時間
//        sb.append();        //アラームの設定音
//        sb.append();        //シェイクの量
//        sb.append();        //
//        sb.append("alFlag INTEGER,");       //アラームのオンオフ用フラグ（アラーム編集画面から操作するもの）
//        sb.append("anFlag INTEGER,");       //アナウンスのオンオフ用フラグ（アラーム編集画面から操作するもの）
//        sb.append("settingFlag INTEGER,");      //アラームとアナウンスのセットのオンオフフラグ(スタート画面から見えるもの)
        sb.append(");");
        String sql = sb.toString();     //appendで結合された文字列をStringに
        db.execSQL(sql);        //SQLの実行。
        Log.v("実行されたSQL文",sql);     //Logcatに実行されたSQL文を表示

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //抽象メソッドなので必須
        //アプリのアップデート時にDBの構造の更新があった場合のみ、アプリ内のDBを更新する
        db.execSQL("DROP TABLE IF EXISTS alarmList;");
        onCreate(db);

    }



    /*

            ここからデータ削除用メソッド

     */

    //データの一括削除を行わないならこのメソッドを必要とするActivityに実装する。
//    public static void alarmDelete(int listPosition ,SQLiteDatabase db){
//        DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.context);
//        SQLiteDatabase db = helper.getWritableDatabase();
//        ArrayList<Integer> idArray = new ArrayList<>();      //取得した_idを格納するリスト
//        try{
//            //MainActivityでタップされたpositionから削除したい_idを取得する
//            String sql = "SELECT * FROM alarmList";
//            Cursor cursor = db.rawQuery(sql, null);
//            while(cursor.moveToNext()){
//                int idx_id = cursor.getColumnIndex("_id");
//                int alId = cursor.getInt(idx_id);
//                idArray.add(alId);
//            }
//            int delId = idArray.get(listPosition);      //タップされたリストの_idを格納
//            String sqlDelete = "DELETE FROM alarmList WHERE _id = ?";
//            SQLiteStatement stmt = db.compileStatement(sqlDelete);
//            stmt.bindLong(1, delId);
//            stmt.executeUpdateDelete();     //削除SQL文の実行
//        } finally {
//
//        }
//    }

    //全データの一括削除を実装するならばこのメソッドのコメントアウトを解除する
    public static void alarmDelete(int delId ,SQLiteDatabase db){
        //データベースヘルパーオブジェクトの作成はメソッドの呼び出し元であらかじめ行う
//        DatabaseHelper helper = new DatabaseHelper(AlarmCreateActivity.context);
//        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<Integer> idArray = new ArrayList<>();      //取得した_idを格納するリスト
        try{
            String sqlDelete = "DELETE FROM alarmList WHERE _id = ?";
            SQLiteStatement stmt = db.compileStatement(sqlDelete);
            stmt.bindLong(1, delId);
            stmt.executeUpdateDelete();     //削除SQL文の実行
        } finally {
            //データベースは明示的にcloseしない方がよい？　→　http://hobby.txt-nifty.com/t1000/2010/11/sqliteandroid-f.html
            //db.close();
        }
    }

    /*

            ここまでデータ削除用メソッド

     */


}
