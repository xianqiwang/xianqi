package com.nfp.update;

import android.content.Context;
import android.content.SharedPreferences;

public class DataCache {
    private static final String SHARENAME = "wtwd";
    private static volatile com.nfp.update.DataCache sInstance;

    private static android.content.SharedPreferences sPreferences;

    private DataCache(android.content.Context context) {
        if (sPreferences == null) {
            sPreferences = context.getSharedPreferences(SHARENAME, android.content.Context.MODE_PRIVATE);
        }
    }
    public static com.nfp.update.DataCache getInstance(android.content.Context context) {
        com.nfp.update.DataCache instance = sInstance;
        if (sInstance == null) {
            synchronized (com.nfp.update.DataCache.class) {
                instance = sInstance;
                if (instance == null) {
                    instance = new com.nfp.update.DataCache(context);
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    public boolean isFirstIn() {
        return sPreferences.getBoolean("isFirst", true);
    }

    public void setFirstInfalse() {
        android.content.SharedPreferences.Editor editor = sPreferences.edit();
        editor.putBoolean("isFirst", false);
        editor.commit();
    }
    public String getDeviceUpdateTime() {
        return sPreferences.getString("updateTime",null);
    }
    public void setDeviceUpdateTime(String time) {
        android.content.SharedPreferences.Editor editor = sPreferences.edit();
        editor.putString("updateTime",time);
        editor.commit();
    }

    public String getDownloadPath() {
        return sPreferences.getString("downloadPath",null);
    }
    public void setDownloadPath(String path) {
        android.content.SharedPreferences.Editor editor = sPreferences.edit();
        editor.putString("downloadPath",path);
        editor.commit();
    }
}
