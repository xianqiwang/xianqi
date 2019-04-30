package com.nfp.update.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String COLUMN_ID        = "id";
    private final static String COLUMN_TIMES     = "settime";
    private final static String COLUMN_CREATE    = "creattime";
    private final static String COLUMN_IMEI      = "imeinumber";
    private final static String TABLE_NAME       = "usertime";
    private Context mContext;

    public DatabaseHelper(Context context, String name, CursorFactory factory,int version)
    {
        super(context, name/*"my.db"*/, factory, 1);
        mContext=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

/*        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID     + "INTEGER PRIMARY KEY AUTOINCREMENT ,"   +

                COLUMN_CREATE +" VARCHAR(20)"+

                COLUMN_TIMES  + "VARCHAR(20)" +

                COLUMN_IMEI   + " VARCHAR(20)" +
                    ");");*/
        db.execSQL("CREATE TABLE usertime(id INTEGER PRIMARY KEY AUTOINCREMENT,settime VARCHAR(20),creattime VARCHAR(20),imeinumber VARCHAR(20))");



        Toast.makeText (mContext,"create success!", Toast.LENGTH_SHORT).show ();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
    }


}
