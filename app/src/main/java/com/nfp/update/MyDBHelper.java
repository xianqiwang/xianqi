package com.nfp.update;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author MR.ZHANG
 * @create 2018-06-06
 */
public class MyDBHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String DB_NAME = "download.db";
    private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement," +
            "thread_id integer,url text,start long,end long,finished long)";
    private static final String SQL_DROP = "drop table if exists thread_info";
    private static final int VERSION = 1;


    public MyDBHelper(android.content.Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}
