package com.nfp.update;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil{

    private DatabaseHelper databaseHelper;
    private Context mContext;
    private SQLiteDatabase db;

    public DatabaseUtil(Context context, String name, CursorFactory factory, int version){
        databaseHelper = new DatabaseHelper (context, name, factory, 1);
        mContext=context;
/*        String path = context.getCacheDir().getPath() + "/mtov.db";
        db = SQLiteDatabase.openOrCreateDatabase(path, null);
        String sql = "create table if not exists mtov" +
                "(id integer primary key autoincrement," +
                "name text(50),mon text(50),address text(50),number text(50))";
        db.execSQL(sql);*/

    }

    public long getCount()
    {
        db = databaseHelper.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT COUNT (*) FROM usertime",null);
        cursor.moveToFirst();
        long result = cursor.getLong(0);
        cursor.close();
        return result;
    }

    public List<RecordStorage> getScrollData(int offset, int maxResult)
    {

        List<RecordStorage> timelist = new ArrayList<RecordStorage> ();
        db = databaseHelper.getReadableDatabase();

        Cursor cursor =  db.rawQuery("SELECT * FROM usertime ORDER BY id ASC LIMIT ?,?;",
                new String[]{String.valueOf(offset),String.valueOf(maxResult)});
        while(cursor.moveToNext())
        {

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String settime = cursor.getString (cursor.getColumnIndex("settime"));
            String createtime= cursor.getString(cursor.getColumnIndex("creattime"));
            String imeinumber= cursor.getString(cursor.getColumnIndex("imeinumber"));
            RecordStorage scrollData = new RecordStorage();
            scrollData.setId(id);
            scrollData.setCreatetime (createtime);
            scrollData.setSettime (settime);
            scrollData.setImeinumber (imeinumber);
            timelist.add(scrollData) ;

        }
        cursor.close();
        return timelist;
    }


    public void addTime(RecordStorage time) {

        db = databaseHelper.getWritableDatabase();

        db.execSQL(
                "insert into usertime(creattime,settime,imeinumber) values("    +
                        String.format("'%s'", time.getCreatetime ())       + ","+
                        String.format("'%s'", time.getSettime ())          + ","+
                        String.format("'%s'", time.getImeinumber ())            +
                         ");"
        );

        db.close();

    }

    public void updateTime(RecordStorage time) {

        db = databaseHelper.getWritableDatabase();

        db.execSQL("update usertime set creattime="
                                +                         time.getCreatetime ()
                                + ","+"settime="        + time.getSettime ()
                                + ","+"imeinumber="     + time.getImeinumber ()
                                + " where id ="      + time.getId ()+";");
        db.close();
    }

    public void modifyData(RecordStorage time) {

        db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.v("yingbo","creattime"+time.getCreatetime ()+"settime"
                +time.getSettime ()+"imeinumber"+time.getImeinumber ()+"id"+time.getId ());
        contentValues.put("creattime",  time.getCreatetime ());
        contentValues.put("settime",    time.getSettime ());
        contentValues.put("imeinumber", time.getImeinumber ());
        int index = db.update("usertime", contentValues, "id =?", new String[]{String.valueOf(time.getId ())});

    }


    public void deleteTime(int id) {

        db = databaseHelper.getWritableDatabase();
        String sql = "id = ?";
        String wheres[] = { String.valueOf(id) };
        db.delete("usertime", sql, wheres);
        db.close();

    }

    public List<RecordStorage> queryAllTime() {
        List<RecordStorage> timelist = new java.util.ArrayList<RecordStorage> ();
        db = databaseHelper.getReadableDatabase();
        String sql = "select * from usertime;";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String settime = cursor.getString (cursor.getColumnIndex("settime"));
            String createtime= cursor.getString(cursor.getColumnIndex("creattime"));
            String imeinumber= cursor.getString(cursor.getColumnIndex("imeinumber"));

            RecordStorage scrollData = new RecordStorage();
            scrollData.setId(id);
            scrollData.setCreatetime (createtime);
            scrollData.setSettime (settime);
            scrollData.setImeinumber (imeinumber);
            timelist.add(scrollData) ;

        }
        cursor.close();
        db.close();
        return timelist;
    }

    public RecordStorage queryTimeById(int id) {
        RecordStorage scrollData = null;
        db = databaseHelper.getReadableDatabase();

        String[] columns = { "id", "settime", "creattime","imeinumber"};

        String selection = "id=?";

        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query("usertime", columns, selection, selectionArgs,
                null, null, null);

        if (cursor.moveToNext()) {

            scrollData = new RecordStorage();
            scrollData.setId( cursor.getInt (cursor.getColumnIndex("id"))   );
            scrollData.setCreatetime (cursor.getString (cursor.getColumnIndex ("creattime")));
            scrollData.setSettime (cursor.getString (cursor.getColumnIndex ( "settime"))    );
            scrollData.setImeinumber (cursor.getString (cursor.getColumnIndex ("imeinumber")));

        }

        return scrollData;
    }

}
