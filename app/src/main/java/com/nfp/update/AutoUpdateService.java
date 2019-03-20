/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nfp.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.IBinder;
import android.os.Handler;
import android.os.RecoverySystem;
/*import android.os.SystemProperties;*/
import android.util.Log;

public class AutoUpdateService extends Service {

    private int count = 0;
    private boolean isCheckComplete = false;
    private BatteryReceiver batteryReceiver = null;
    class BatteryReceiver extends BroadcastReceiver{
        @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
                removeBatteryReceiver();
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                int curLevel = (level*100)/scale;
                String sss= "current battery level ="+((level*100)/scale)+"%";
                Log.d("kevin","batteryLevel ="+sss);
                if(curLevel<30){
                    lowBattery();
                    UpdateUtil.showFotaNotification(context, R.string.Notification_battery_low, 7);
                    insertEventLog(context,0, getString(R.string.install_result), 0, getString(R.string.fail), getString(R.string.no_enough_battery), null);
                }else{
                    rebootToInstall(AutoUpdateService.this);
                }
            }
        }
    }

    Handler handler = new Handler();
    Runnable task =new Runnable() {
       @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN)
       public void run() {
           // TODOAuto-generated method stub
           count = count + 1;
           handler.postDelayed(this,10000);
           judgeUpdateFile(getApplicationContext());
       }
    };
    private android.net.Uri insertEventLog(Context context, int eventNo, String eventName,
                                           int tid, String factor1, String factor2, String factor3) {
        final android.net.Uri uri = android.net.Uri.parse("content://com.ssol.eventlog/eventlog");

        android.content.ContentResolver mContentResolver=context.getContentResolver();

        mContentResolver.acquireContentProviderClient (uri);

        android.content.ContentValues values = new android.content.ContentValues ();

        if (android.text.TextUtils.isEmpty(eventName)) {
            throw new IllegalArgumentException("Invalid event name : " + eventName);
        } else {
            values.put("EVENT_NAME", eventName);
        }

        /*if (tid < 1 || tid > 256) {
            Log.w(TAG, "Invalid tid : " + tid);
        } else {
            values.put("TID", new Integer(tid));
        }*/

        if (! android.text.TextUtils.isEmpty(factor1)) {
            values.put("FACTOR1", factor1);
        }

        if (! android.text.TextUtils.isEmpty(factor2)) {
            values.put("FACTOR2", factor2);
        }

        if (! android.text.TextUtils.isEmpty(factor3)) {
            values.put("FACTOR3", factor3);
        }

        return  mContentResolver.insert (uri,values);

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if(intent==null){
            return;
        }
    }

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN)
    public void rebootToInstall(Context context) {
        File files = new File("/fota/softwareupdate.dat");
        if(files.exists()){
            startWaitUpdate();
            PropertyUtils.set("sys.verify_delta","1" );
            handler.postDelayed(task, 5000);
        }else{
            UpdateUtil.showUpdateResult(context);
        }
    }

    public void startWaitUpdate() {
        Intent intent= new Intent(this, CheckDeltaProgressDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    public void checkComplete() {
        handler.removeCallbacks(task);
        Intent i = new Intent("check.deltaFile.complete");
        this.sendBroadcast(i);
        stopSelf();
    }

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN)
    public void judgeUpdateFile(Context context) {
         String result = "";
         boolean judge_fail= false;
         File file = new File("/cache/redbend/verify_result");
         if(file.exists()){
            try {
               InputStreamReader inputReader = new InputStreamReader(new FileInputStream("/cache/redbend/verify_result"));
               BufferedReader bufReader = new BufferedReader(inputReader);
               result = bufReader.readLine();
               file.delete();
               Log.d("kevin","start to run  judge result="+result);

              if(result.substring(0,1).equals("0")){
                  checkComplete();
                  startUpdate(context);
              }else{
                 printVerifyLogs();
                 judge_fail = true;
             }
              Log.d("kevin","start to run  judge result="+result.substring(0,1));
            } catch (IOException e) {
                e.printStackTrace();
            }
         }
         Log.d("kevin","start to run reboot judgeUpdateFile");
         if(count==12||judge_fail){
             UpdateUtil.showUpdateResult(context);
             checkComplete();
         }
    }

    public void printVerifyLogs(){
         File file = new File("/cache/redbend/ualog.txt");
         if(file.exists()){
            String result=null;
            InputStreamReader inputReader;
            BufferedReader bufReader=null;
            try {
               inputReader = new InputStreamReader(new FileInputStream("/cache/redbend/ualog.txt"));
               bufReader = new BufferedReader(inputReader);
               result = bufReader.readLine();
               while ((result=bufReader.readLine())!=null)
                   Log.d("kevin",result);
            }catch(Exception e){
                   Log.d("kevin","read logs(/cache/redbend/ualog.txt) failed ");
                   e.printStackTrace();
            }finally{
		try{
                  bufReader.close();
		}catch(Exception e){}
           }
        }
    }

    public void startUpdate(Context context) {
        try{
            Log.d("kevin","start = recovery mode");
            UpdateUtil.resetFotareference(context);
            RecoverySystem.installPackage(context, new File("/fota/rb_ota.zip"));
        }catch(IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @android.annotation.SuppressLint ("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("kevin","AutoUpdateService onStartCommand");
        if(intent!=null){
            prepareInstall();
        }
        return super.onStartCommand(intent, Service.START_NOT_STICKY, startId);
    }

   public void prepareInstall() {
	IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	batteryReceiver = new BatteryReceiver();
	registerReceiver(batteryReceiver, intentFilter);
    }

    private void lowBattery() {
        Intent intent= new Intent(AutoUpdateService.this, LowBatteryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopSelf();
    }

    public void removeBatteryReceiver() {
        if(batteryReceiver !=null)
            unregisterReceiver(batteryReceiver);
            batteryReceiver = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeBatteryReceiver();
    }
}
