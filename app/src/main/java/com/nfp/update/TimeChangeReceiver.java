package com.nfp.update;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class TimeChangeReceiver extends BroadcastReceiver{

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.i("kevin","time change broadcast receiver");
        startNewPolling(context);
        startNewUpdate(context);
    }

    public void startNewPolling(Context context) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("debug_comm", 0);
        int isTrigger = sharedPreferences.getInt("set_fota_systime", 0);
        int pols = spref.getInt("pol_switch",1);
        File files = new File("/fota/softwareupdate.dat");
        if(pols==0&&!files.exists()){
            Log.i("kevin","time change poll timer broadcast receiver");
            UpdateUtil.stopPollingService(context);
            UpdateUtil.startPollingService(context, spref.getLong("pol_time_mm",0));
        }
    }

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    public void startNewUpdate(Context context) {
        long updateTime = UpdateUtil.getUpdateTime(context);
        long currentTime = System.currentTimeMillis();
        long uTime = updateTime/(1000*60);
        long cTime = currentTime/(1000*60);
        if(updateTime!=0 && cTime <uTime){
            Log.i("kevin","time change update time broadcast receiver");
            UpdateUtil.stopUpdateService(context, 1);
            UpdateUtil.startUpdateService(context, 1);
        }
    }
}
