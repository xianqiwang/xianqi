package com.nfp.update.widget;

public class DataCache {
    private static final String SHARENAME = "simfota";
    private static volatile DataCache sInstance;

    private static android.content.SharedPreferences sPreferences;

    private DataCache(android.content.Context context) {
        if (sPreferences == null) {
            sPreferences = context.getSharedPreferences(SHARENAME, android.content.Context.MODE_PRIVATE);
        }
    }
    public static DataCache getInstance(android.content.Context context) {
        DataCache instance = sInstance;
        if (sInstance == null) {
            synchronized (DataCache.class) {
                instance = sInstance;
                if (instance == null) {
                    instance = new DataCache (context);
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
