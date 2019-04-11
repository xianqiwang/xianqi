package com.nfp.update;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SPUtils {

    public static final String FILE_NAME = "fota_setings_parameter";
    private static Context context;

    public SPUtils(Context context){
       this.context=context;
    }

    public void put(String key, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else {
            editor.putString(key, (String) obj);
        }
        editor.commit();
    }

    public Object get(String key, Object defaultObj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        if (defaultObj instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Float) {
            return sp.getFloat(key, (Float) defaultObj);
        } else if (defaultObj instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Long) {
            return sp.getLong(key, (Long) defaultObj);
        } else if (defaultObj instanceof String) {
            return sp.getString(key, (String) defaultObj);
        }
        return null;
    }

    public void remove(String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public Map<String, ?> getAll() {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        Map<String, ?> map = sp.getAll();
        return map;
    }


    public void clear() {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public boolean contains(String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.contains(key);
    }

}
