package com.nfp.update;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * @author MR.ZHANG
 * @create 2018-06-05
 */
public class ThreadDAOImpl implements ThreadDAO {

    private static final String TAG = "ThreadDAOImpl";

    private MyDBHelper myDBHelper;

    public ThreadDAOImpl(android.content.Context context) {
        this.myDBHelper = new MyDBHelper(context);
    }

    @Override
    public void insertThread(ThreadInfo threadInfo) {
        android.util.Log.e("insertThread: ", "insertThread");
        android.database.sqlite.SQLiteDatabase db = myDBHelper.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
                new Object[]{threadInfo.getId(), threadInfo.getUrl(),
                        threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getFinish()});
        db.close();
    }

    @Override
    public void deleteThread(String url, int thread_id) {
        android.util.Log.e("deleteThread: ", "deleteThread");
        android.database.sqlite.SQLiteDatabase db = myDBHelper.getWritableDatabase();
        db.execSQL("delete from  thread_info where url = ? and thread_id=?",
                new Object[]{url, thread_id});
        db.close();
    }

    @Override
    public void updateThread(String url, int thread_id, long finished) {
        android.util.Log.e("updateThread: ", "updateThread");
        android.database.sqlite.SQLiteDatabase db = myDBHelper.getWritableDatabase();
        db.execSQL("update thread_info set finished = ?  where url = ? and thread_id=?",
                new Object[]{finished, url, thread_id});
        db.close();
    }

    @Override
    public java.util.List<ThreadInfo> getThread(String url) {
        android.util.Log.e("getThread: ", "getThread");
        java.util.List<ThreadInfo> list = new java.util.ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = myDBHelper.getWritableDatabase();
        android.database.Cursor cursor = db.rawQuery("select * from thread_info where url=?", new String[]{url});
        while (cursor.moveToNext()) {
            ThreadInfo thread = new ThreadInfo();
            thread.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            thread.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            thread.setStart(cursor.getLong(cursor.getColumnIndex("start")));
            thread.setEnd(cursor.getLong(cursor.getColumnIndex("end")));
            thread.setFinish(cursor.getLong(cursor.getColumnIndex("finished")));
            list.add(thread);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExists(String url, int thread_id) {
        android.database.sqlite.SQLiteDatabase db = myDBHelper.getWritableDatabase();
        android.database.Cursor cursor = db.rawQuery("select * from thread_info where url=? and thread_id = ?",
                new String[]{url, String.valueOf(thread_id)});
        boolean isExist = cursor.moveToNext();
        cursor.close();
        db.close();
        android.util.Log.e(TAG, "isExists: " + isExist);
        return isExist;
    }
}
