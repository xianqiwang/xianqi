package com.nfp.update.polling;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import com.nfp.update.widget.CustomDialog;
import com.nfp.update.UpdateUtil;
import com.nfp.update.R;

public class PollReceiver extends BroadcastReceiver {

    private final static String TAG = "PollReceiver";

    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    private final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    private final String NITZ = "android.action.NITZ";
    private final String RETRY = "android.intent.action.RETRY";
    private final String ABOVE_ONE_HOUR = "com.sim.AboveOneHour";
    private final String CLEAR_FOTA_DATA = "software.update.cleardata";

    public final static String INSTALL = "/cache/redbend/result";
    public static String installResult = "";
    public static String  installValue = "";
    CustomDialog  mDialog;
    AlertDialog.Builder builder;
    SharedPreferences spref;
    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
       spref =context.getSharedPreferences("debug_comm", 0);
        if (intent.getAction().equals(ACTION_BOOT)){
            Log.d(TAG, "BOOT_COMPLETED");
            boolean updateFlag = false;
            int isAuto = spref.getInt("AUTO_UPDATE", 0);
            UpdateUtil.setBoot(context, true);
            boolean date_changed = sharedPreferences.getBoolean("date_changed", false);
            boolean hadPoll = sharedPreferences.getBoolean("had_poll", false);
            Log.d(TAG, "firstBoot = " + UpdateUtil.getFirstBoot(context) + "   date_changed = " + date_changed + "   hadPoll = " + hadPoll+"--getNitz:"+UpdateUtil.getNitz(context));

            setNewUpdateTime(context);

            installResult = readCacheResult(INSTALL);
            Log.d(TAG, "installResult = " + installResult);
            if(installResult != null && !installResult.equals("")){
                Log.d("kevin", "update complete!!");
                updateFlag = true;
                deleteUpdatePackage();
                installValue = installResult.substring(0, 1);
                if(installValue.equals("0")){
                    UpdateUtil.showFotaNotification(context, R.string.Notification_update_success, 4);
                    insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.success), null, null);
                    insertEventLog(context,0, context.getString(R.string.install_result), 0, context.getString(R.string.success), null, null);
                }else{
                    UpdateUtil.showFotaNotification(context, R.string.Notification_update_fail, 5);
                    insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), null, null);
                    insertEventLog(context,0, context.getString(R.string.install_result), 0, context.getString(R.string.fail), context.getString(R.string.other), null);
                }
                if(sharedPreferences.getInt("pol_switch", 1)==0)
                    UpdateUtil.stopPollingService(context);
                UpdateUtil.startPollingService(context, UpdateUtil.getCycleTime(context));
            }

            if (UpdateUtil.getInstallSchedule(context)){
              //  UpdateUtil.showFotaNotification(context, R.string.Notification_schedule, 2);
            }
            /*if (UpdateUtil.getFirstBoot(context)) {
                UpdateUtil.setFirstBoot(context, false);
                SharedPreferences.Editor editor = spref.edit();
                editor.putInt("AUTO_UPDATE", 1);
                editor.commit();
            }*/
String hour=String.valueOf(spref.getInt("update_hour", 1));
            String minute=String.valueOf(spref.getInt("update_minute", 1));
            Log.d(TAG, "AUTO_UPDATE:"+spref.getInt("AUTO_UPDATE", 1)+"hour:"+hour+"---"+"minute:"+minute+"--switch:"+spref.getInt("dialog_switch", 1));
            if(spref.getInt("AUTO_UPDATE", 1)==1&&!hour.equals("")&&hour!=null&&spref.getInt("dialog_switch", 1)==1) {
                String on;
                if(spref.getInt("AUTO_UPDATE", 1)==1){

                    on="on";

                }
                else{
                    on="off";
                }
                String hour1=null,minute1=null;
                if(hour.length()==2){
                    hour1=hour;
                }
                if(minute.length()==2){
                    minute1=minute;

                }
                if(minute.length()==1){
                    minute1="0"+minute;

                }
                if(hour.length()==1){
                    hour1="0"+hour;

                }
                //   if (UpdateUtil.getFirstBoot(context)) {
                //  UpdateUtil.setBoot(context, false);
                mDialog = new CustomDialog.Builder(context, 400, 480)
                        .setMessage("Automatic software updates" + "\n" +
                                ", automatic update time" + "\n" +
                                "has been set as follows.\n" +
                                "--------------------------------\n" +
                                "Automatic updates　["+on+"]\n" +
                                "automatic update time["+hour1+":"+minute1+"]"+"\n" +
                                "--------------------------------")
                        .setSingleButton("Ok", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor editor = spref.edit();
                                editor.putInt("dialog_switch", 0);
                                editor.commit();
                                mDialog.dismiss();
                            }

                        }).createSingleButtonDialog();
                //需要把对话框的类型设为TYPE_SYSTEM_ALERT，否则对话框无法在广播接收器里弹出
                mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                mDialog.show();
            }
           // }
            Log.d(TAG, "firstBoot1 = " + UpdateUtil.getFirstBoot(context) + "   date_changed = " + date_changed + "   hadPoll = " + hadPoll+"--getNitz:"+UpdateUtil.getNitz(context));

            if (UpdateUtil.getFirstBoot(context)){
                //UpdateUtil.showFotaNotification(context, R.string.Notification_schedule, 2);
                if (UpdateUtil.getNitz(context)){
                    UpdateUtil.startPollingService(context, UpdateUtil.getPollStartTime(context));
                    UpdateUtil.setBoot(context, false);
                    UpdateUtil.setNitz(context, false);
                    UpdateUtil.setFirstBoot(context, false);
                }
            } else {
                if(!updateFlag){
                    File files = new File("/fota/softwareupdate.dat");
                    int type = spref.getInt("notification_type", 0);
                    Log.d("kevin", "update updateFlag!!=="+String.valueOf(type));
                    if((type == 1||type == 3)&&files.exists()){
                        UpdateUtil.showFotaNotification(context, R.string.Notification_download_successed, type);
                    }
                }

                if (date_changed){
                    if (hadPoll){
                        if(installResult != null && !installResult.equals("")){
                            installValue = installResult.substring(0, 1);
                            if(installValue.equals("0")){
                                UpdateUtil.startPollingService(context, UpdateUtil.getPollStartTime(context));
                            } else {
                                UpdateUtil.startPollingService(context, UpdateUtil.getCycleTime(context));
                            }
                            SharedPreferences.Editor pEdit = sharedPreferences.edit();
                            pEdit.putBoolean("had_poll", false);
                            pEdit.commit();
                        }
                    }else {
                        Calendar calendar = Calendar.getInstance();
                        Long cur = calendar.getTimeInMillis();
                        Long tt = sharedPreferences.getLong("pol_time_mm", 0);
                        Log.d("kevin","cur="+cur+"; tt="+tt);
                        if(cur>tt){
                            UpdateUtil.startPollingService(context, UpdateUtil.getRetryPollStartTime(context));
                            SharedPreferences.Editor pEdit = sharedPreferences.edit();
                            pEdit.putBoolean("is_retry", true);
                            pEdit.commit();
                        }
                    }
                }
            }
        }else if (intent.getAction().equals(NITZ)){
            Log.d(TAG, "NITZ");
            UpdateUtil.setNitz(context, true);
            SharedPreferences.Editor pEdit = sharedPreferences.edit();
            pEdit.putBoolean("date_changed",true);
            pEdit.commit();
            if (UpdateUtil.getFirstBoot(context) && UpdateUtil.getBoot(context)) {
                UpdateUtil.startPollingService(context, UpdateUtil.getPollStartTime(context));
                UpdateUtil.setBoot(context, false);
                UpdateUtil.setNitz(context, false);
                UpdateUtil.setFirstBoot(context, false);
            }
        } else if (intent.getAction().equals(RETRY)){
            Log.d(TAG, "RETRY");
            UpdateUtil.stopPollingService(context);
            UpdateUtil.startPollingService(context, UpdateUtil.getRetryPollStartTime(context));
            SharedPreferences.Editor pEdit = sharedPreferences.edit();
            pEdit.putBoolean("is_retry", true);
            pEdit.commit();
        } else if (intent.getAction().equals(ABOVE_ONE_HOUR)){
            int isTrigger = spref.getInt("set_fota_systime", 0);
            long updateTimes = UpdateUtil.getUpdateTime(context);
            Log.d(TAG, "ABOVE_ONE_HOUR    isTrigger="+String.valueOf(isTrigger));
            if(isTrigger==0&& updateTimes==0){
                if(sharedPreferences.getInt("pol_switch", 1)==0)
                    UpdateUtil.stopPollingService(context);
                UpdateUtil.startPollingService(context, UpdateUtil.getPollStartTime(context));
            }
        }else if (intent.getAction().equals(CLEAR_FOTA_DATA)){
            Log.d("kevin", "clear fota data");
            UpdateUtil.stopPollingService(context);
            UpdateUtil.startPollingService(context, UpdateUtil.getPollStartTime(context));
        }else  if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            if(UpdateUtil.getUpdateTime(context)!=0){
                UpdateUtil.stopUpdateService(context, 2);
                UpdateUtil.stopUpdateService(context, 1);
                UpdateUtil.setUpdateState(context, 1);
            }
        }
    }

    private android.net.Uri insertEventLog(Context context, int eventNo, String eventName, int tid, String factor1, String factor2, String factor3) {
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

   @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
   public void setNewUpdateTime(Context context) {
        Log.d("kevin", "start setNewUpdateTime !!");
        if (UpdateUtil.getUpdateState(context)==1){
            UpdateUtil.startUpdateService(context, 1);
        }
    }

    public String readCacheResult(String filePath) {
        String result = "";

        File file = new File(filePath);
        if (file == null|| !file.exists()) {
            return "";
        }

        try {
            InputStreamReader inputReader = new InputStreamReader(  new FileInputStream(filePath));
            BufferedReader bufReader = new BufferedReader(inputReader);
            result=bufReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.exists()){
            file.delete();
        }
        return result;
    }

   public void deleteUpdatePackage() {
        Log.d("kevin", "start delete !!");
        File files = new File("/fota/softwareupdate.dat");
        if (files.exists()){
            files.delete();
        }
    }

   public String setPollTimeEvent() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmm");
        Calendar c = Calendar.getInstance();
        Date pollDate = (Date) c.getTime();

        return formatter.format(pollDate);
    }
}
