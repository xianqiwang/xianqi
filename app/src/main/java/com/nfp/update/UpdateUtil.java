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
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import android.os.RecoverySystem;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.nfp.update.R;
import com.nfp.update.polling.PollingService;

import java.text.SimpleDateFormat;

public class UpdateUtil {

    private final static String TAG = "UpdateUtil";
    public static Context context;
    public static Context mContext;
    //private static String dateTime = "";
    private static long randomTime = 0;

    public static Context getContext () {
        if (PollingService.getInstance () != null) {
            context = PollingService.getInstance ();
        } else {
            context = MainActivity.getInstance ();
        }
        return context;
    }

    //start polling service
    public static void startPollingService (Context context, long startTime) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor pEdits = spref.edit ();
        insertEventLog (context, 0, context.getString (R.string.polling), 0, context.getString (R.string.start), null, null);
        insertEventLog (context, 0, context.getString (R.string.polling_time_setting), 0, setPollTimeEvent (startTime), null, null);
        AlarmManager manager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);

        Intent intent = new Intent ("com.nfp.update.ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String str = String.format ("%tF %<tT", startTime);
        String startPol = str.replace ("-", "/");
        pEdits.putLong ("pol_time_mm", startTime);
        pEdits.putString ("pol_timer", startPol);
        pEdits.putInt ("pol_switch", 0);
        pEdits.commit ();
        Log.d (TAG, "startTime = " + startPol);
        manager.set (AlarmManager.RTC_WAKEUP, startTime, pendingIntent);
    }

    private static android.net.Uri insertEventLog (Context context, int eventNo, String eventName, int tid, String factor1, String factor2, String factor3) {
        final android.net.Uri uri = android.net.Uri.parse ("content://com.ssol.eventlog/eventlog");

        android.content.ContentResolver mContentResolver = context.getContentResolver ();

        mContentResolver.acquireContentProviderClient (uri);

        android.content.ContentValues values = new android.content.ContentValues ();

        if (android.text.TextUtils.isEmpty (eventName)) {
            throw new IllegalArgumentException ("Invalid event name : " + eventName);
        } else {
            values.put ("EVENT_NAME", eventName);
        }

        /*if (tid < 1 || tid > 256) {
            Log.w(TAG, "Invalid tid : " + tid);
        } else {
            values.put("TID", new Integer(tid));
        }*/

        if (! android.text.TextUtils.isEmpty (factor1)) {
            values.put ("FACTOR1", factor1);
        }

        if (! android.text.TextUtils.isEmpty (factor2)) {
            values.put ("FACTOR2", factor2);
        }

        if (! android.text.TextUtils.isEmpty (factor3)) {
            values.put ("FACTOR3", factor3);
        }

        return mContentResolver.insert (uri, values);

    }

    //stop polling service
    public static void stopPollingService (Context context) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor pEdits = spref.edit ();
        pEdits.putInt ("pol_switch", 1);
        pEdits.commit ();
        AlarmManager manager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);
        Intent intent = new Intent ("com.nfp.update.ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d (TAG, "manager.cancel()");
        manager.cancel (pendingIntent);
    }

    /**start auto update timer, flag = 1 is defined auto update schedure broadcast
     flag = 2 is defined auto update install broadcast.
     **/

    public static void startUpdateService (Context context, int flag) {
        int hour = 0;
        int minute = 0;
        SharedPreferences sprefs = context.getSharedPreferences ("debug_comm", 0);
        if (getTempTImeFlag (context) == 1) {
            hour = getHourTemp (context);
            minute = getMinuteTemp (context);
        } else {
            hour = getHour (context);
            minute = getMinute (context);
        }

        if (sprefs.getInt ("AUTO_UPDATE", 0) != 1) {

            long timeInMillis = 0;
            long deltaTimes = 0;
            Intent intent;
            PendingIntent pi;

            closePollingTime (context);
            Calendar calendar = Calendar.getInstance ();
            AlarmManager alarm = (AlarmManager) context.getSystemService (context.ALARM_SERVICE);

            if (flag == 1) {

                calendar.set (Calendar.HOUR_OF_DAY, hour);
                calendar.set (Calendar.MINUTE, minute);
                calendar.set (Calendar.SECOND, 0);
                calendar.set (Calendar.MILLISECOND, 0);

                deltaTimes = calendar.getTimeInMillis () - System.currentTimeMillis ();
                if (deltaTimes <= 0) {

                    timeInMillis = calendar.getTimeInMillis () + AlarmManager.INTERVAL_DAY;
                    Log.i ("kevin", "timeInMillis +1day-5min");

                } else {
                    closePollingTime (context);
                    timeInMillis = calendar.getTimeInMillis ();
                    Log.i ("kevin", "timeInMillis ");
                }

                intent = new Intent ("com.nfp.update.SCHEDULE");
                intent.putExtra ("delta_times", deltaTimes);
                pi = PendingIntent.getBroadcast (context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            } else {

                calendar.set (Calendar.HOUR_OF_DAY, hour);
                calendar.set (Calendar.MINUTE, minute);
                calendar.set (Calendar.SECOND, 0);
                calendar.set (Calendar.MILLISECOND, 0);
                timeInMillis = calendar.getTimeInMillis () + 300000;
                intent = new Intent ("com.nfp.update.UPDATE");
                pi = PendingIntent.getBroadcast (context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                closePollingTime (context);

            }

            alarm.setExact (AlarmManager.RTC_WAKEUP, timeInMillis, pi);
            //alarm.setAlarmClock(new AlarmManager.AlarmClockInfo(timeInMillis, pi), pi);

            setUpdateTime (context, timeInMillis);
            String str = String.format ("%tF %<tT", timeInMillis);
            String start_update = str.replace ("-", "/");

        }
    }

    public static String getTimeFromMillisecond (Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("HH:mm");
        Date date = new Date (millisecond);
        String timeStr = simpleDateFormat.format (date);
        return timeStr;
    }

    //start auto update timer delay next day
    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    public static void startUpdateServiceNext (Context context, long updateTime) {
        closePollingTime (context);

        /*long timeInMillis = 0;
        long one_day = 24*60*60*1000;
        long CurrentTime = System.currentTimeMillis();
        long deltaTimes = updateTime - CurrentTime;
        if(deltaTimes <= 0){
            timeInMillis = updateTime + one_day;
            while(timeInMillis<=CurrentTime){
                timeInMillis = timeInMillis + one_day;
            }
            Log.i("kevin","startUpdateServiceNext timeInMillis +1day-5min");
        }else{
            timeInMillis = updateTime;
            Log.i("kevin","timeInMillis  updateTime");
        }*/

        AlarmManager alarm = (AlarmManager) context.getSystemService (context.ALARM_SERVICE);
        Intent intent = new Intent ("com.nfp.update.SCHEDULE");
        PendingIntent pi = PendingIntent.getBroadcast (context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.setExact (AlarmManager.RTC_WAKEUP, updateTime, pi);
        //alarm.setAlarmClock(new AlarmManager.AlarmClockInfo(updateTime, pi), pi);

        setUpdateTime (context, updateTime);
        String str = String.format ("%tF %<tT", updateTime);
        String start_update = str.replace ("-", "/");
        Log.d (TAG, "startUpdateServiceNext =-> " + start_update);
        Log.d ("kevin", "222 NEXT getUpdateTime(context,updateTime)==" + getUpdateTime (context));
    }

    //stop auto update timer
    public static void stopUpdateService (Context context, int flag) {
        Log.d ("kevin", "333  cancel update");
        String action = "";

        if (flag == 1) {
            action = "com.nfp.update.SCHEDULE";
        } else {
            action = "com.nfp.update.UPDATE";
        }

        AlarmManager manager = (AlarmManager) context.getSystemService (Context.ALARM_SERVICE);
        Intent intent = new Intent (action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast (context, flag, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel (pendingIntent);
        setUpdateTime (context, 0);
    }

    public static void closePollingTime (Context context) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences (context);
        File files = new File ("/fota/softwareupdate.dat");
        if (spref.getInt ("pol_switch", 1) == 0 && files.exists ())
            stopPollingService (context);
    }

    public static void setUpdateTime (Context context, long time) {
        Log.d (TAG, "setUpdateTime =" + time);
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor pEdits = spref.edit ();
        pEdits.putLong ("update_time_mm", time);
        pEdits.commit ();
    }

    public static long getUpdateTime (Context context) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences (context);
        return spref.getLong ("update_time_mm", 0);
    }

    public static void setUpdateState (Context context, int state) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor pEdits = spref.edit ();
        pEdits.putInt ("update_state", state);
        pEdits.commit ();
    }

    public static int getUpdateState (Context context) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences (context);
        return spref.getInt ("update_state", 0);
    }

    public static long getRandomTime () {
        Log.d (TAG, "getRandomTime()");
        Random random = new Random ();
        randomTime = (long) random.nextInt (86399) * 1000;
        return randomTime;
    }

    public static void setHourMinute (Context context, int hour, int minute) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putInt ("update_hour", hour);
        pEdit.putInt ("update_minute", minute);
        pEdit.commit ();
    }

    public static void setTempHourMinute (Context context, int hour, int minute) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putInt ("temp_update_flag", 1);
        pEdit.putInt ("temp_update_hour", hour);
        pEdit.putInt ("temp_update_minute", minute);
        pEdit.commit ();
    }

    public static void clearTempHourMinute (Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.remove ("temp_update_flag");
        pEdit.remove ("temp_update_hour");
        pEdit.remove ("temp_update_minute");
        pEdit.commit ();
    }

    public static int getHour (Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        return sharedPreferences.getInt ("update_hour", 3);
    }

    public static int getMinute (Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        return sharedPreferences.getInt ("update_minute", 0);
    }

    public static int getHourTemp (Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        int tempH = sharedPreferences.getInt ("temp_update_hour", getHour (context));
        return tempH;
    }

    public static int getMinuteTemp (Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        int tempM = sharedPreferences.getInt ("temp_update_minute", getMinute (context));
        return tempM;
    }

    public static int getTempTImeFlag (Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        int tempF = sharedPreferences.getInt ("temp_update_flag", 0);
        return tempF;
    }

    public static void setRetryTime (int time, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putInt ("retry_time", time);
        pEdit.putBoolean ("isSetPollInterval", true);
        pEdit.commit ();
    }

    public static int getRetryTime (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        return sharedPreferences.getInt ("retry_time", - 1);
    }

    public static int getPollDay (Context context) {
        int time = 0;
        if (android.support.v4.app.ActivityCompat.checkSelfPermission (context, android.Manifest.permission.READ_PHONE_STATE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }
        String imei = ((TelephonyManager) context.getSystemService (Context.TELEPHONY_SERVICE))
                .getDeviceId ();
        Log.d (TAG, "imei number = " + imei);
        if (imei != null && imei.length () > 4) {
            time = Integer.parseInt (imei.substring (imei.length () - 4));
            time = time % 14;
            Log.d (TAG, "imei number = " + imei);
        }

        if (time == getCurDay ()) {
            time = 14;
        } else {
            Calendar current_time = Calendar.getInstance ();
            int weekNumber = current_time.get (Calendar.WEEK_OF_YEAR);
            int dayofweek = current_time.get (Calendar.DAY_OF_WEEK);
            time = calculatePollDay (time, weekNumber, dayofweek - 1);
        }
        return time;
    }

    public static void judgePolState (Context context, int value) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor editor = spref.edit ();
        editor.putInt ("pol_service", value);
        editor.commit ();
    }

    private static int calculatePollDay (int imei, int weeknumber, int dayofweek) {
        Log.d (TAG, "4 lower imei mod 14 = " + imei + "   weeknumber = " + weeknumber + "   dayofweek = " + dayofweek);
        int pollday;
        if (weeknumber % 2 == 1) {
            if (imei < 7) {
                if (imei <= dayofweek) {
                    pollday = imei - dayofweek + 14;
                } else {
                    pollday = imei - dayofweek;
                }
            } else {
                pollday = imei - dayofweek;
            }
        } else {
            if (imei < 7) {
                pollday = imei - dayofweek + 7;
            } else {
                if (imei - 7 <= dayofweek) {
                    pollday = imei - dayofweek + 7;
                } else {
                    pollday = imei - dayofweek - 7;
                }
            }
        }
        Log.d (TAG, "pollday = " + pollday);
        return pollday;
    }

    public static int getCurDay () {
        Calendar cur_time = Calendar.getInstance ();
        int week = cur_time.get (Calendar.WEEK_OF_MONTH);
        int week_num = cur_time.get (Calendar.DAY_OF_WEEK);
        if (week % 2 != 0) {
            week_num = week_num - 1;
        } else {
            week_num = week_num + 6;
        }

        Log.d ("kevin", "week_num=" + String.valueOf (week_num) + "  week=" + String.valueOf (week));
        return week_num;
    }

    public static void setPollStartTime (int time, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putInt ("polling_start_time", time);
        pEdit.commit ();
    }

    public static long getPollStartTime (Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("HH:mm:ss");
        String[] randomTime = simpleDateFormat.format (getRandomTime ()).split (":");

        Calendar pollStart = Calendar.getInstance ();
        if (randomTime != null && randomTime.length == 3) {
            pollStart.add (Calendar.DAY_OF_YEAR, getPollDay (context));
            pollStart.set (Calendar.HOUR_OF_DAY, Integer.parseInt (randomTime[ 0 ]));
            pollStart.set (Calendar.MINUTE, Integer.parseInt (randomTime[ 1 ]));
            pollStart.set (Calendar.SECOND, Integer.parseInt (randomTime[ 2 ]));

            if (pollStart.getTimeInMillis () - System.currentTimeMillis () <= 0) {
                pollStart.add (Calendar.DAY_OF_YEAR, 14);
            }
        }

       /* SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int pollTimers= sharedPreferences.getInt("polling_start_time", -1);
        if(pollTimers != -1){
            Calendar pollS = Calendar.getInstance();
            pollS.add(Calendar.MINUTE, pollTimers);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date afterDate = (Date) pollS.getTime();
            String StartTime = formatter.format(afterDate);
            Log.d(TAG, "getPollStartTime() poltimer test request-> " + StartTime);
            return pollS.getTimeInMillis();
        }*/

        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        Date afterDate = (Date) pollStart.getTime ();
        String pollStartTime = formatter.format (afterDate);
        Log.d (TAG, "getPollStartTime() -> " + pollStartTime);
        return pollStart.getTimeInMillis ();
    }

    public static long getCycleTime (Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("HH:mm:ss");
        String[] randomTime = simpleDateFormat.format (getRandomTime ()).split (":");

        Calendar pollStart = Calendar.getInstance ();
        if (randomTime != null && randomTime.length == 3) {
            pollStart.add (Calendar.DAY_OF_YEAR, 14);
            pollStart.set (Calendar.HOUR_OF_DAY, Integer.parseInt (randomTime[ 0 ]));
            pollStart.set (Calendar.MINUTE, Integer.parseInt (randomTime[ 1 ]));
            pollStart.set (Calendar.SECOND, Integer.parseInt (randomTime[ 2 ]));
        }

        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int pollTimers = sharedPreferences.getInt("polling_start_time", -1);
        if(pollTimers != -1){
            Calendar pollS = Calendar.getInstance();
            pollS.add(Calendar.MINUTE, pollTimers);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date afterDate = (Date) pollS.getTime();
            String StartTime = formatter.format(afterDate);
            Log.d(TAG, "getCycleTime() poltimer test request-> " + StartTime);
            return pollS.getTimeInMillis();
        }*/

        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        Date afterDate = (Date) pollStart.getTime ();
        String pollStartTime = formatter.format (afterDate);
        Log.d (TAG, "getCycleTime() -> " + pollStartTime);
        return pollStart.getTimeInMillis ();
    }

    public static long getRetryPollStartTime (Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("HH:mm:ss");
        String[] randomTime = simpleDateFormat.format (getRandomTime ()).split (":");

        Calendar pollStart = Calendar.getInstance ();
        if (randomTime != null && randomTime.length == 3) {
            //pollStart.set(Calendar.DAY_OF_YEAR, 0);
            pollStart.add (Calendar.HOUR_OF_DAY, Integer.parseInt (randomTime[ 0 ]));
            pollStart.add (Calendar.MINUTE, Integer.parseInt (randomTime[ 1 ]));
            pollStart.add (Calendar.SECOND, Integer.parseInt (randomTime[ 2 ]));
        }


        int retry = getRetryTime (context);
        if (retry != - 1) {
            int hour = retry / 60;
            int minute = retry % 60;
            Calendar retryStart = Calendar.getInstance ();
            retryStart.add (Calendar.HOUR_OF_DAY, hour);
            retryStart.add (Calendar.MINUTE, minute);

            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
            Date afterDate = (Date) retryStart.getTime ();
            String pollStartTime = formatter.format (afterDate);
            Log.d (TAG, "getRetryPollStartTime() ss-> " + pollStartTime);
            return retryStart.getTimeInMillis ();
        }
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        Date afterDate = (Date) pollStart.getTime ();
        String pollStartTime = formatter.format (afterDate);
        Log.d (TAG, "getRetryPollStartTime() -> " + pollStartTime);
        return pollStart.getTimeInMillis ();
    }
    class NewNotification extends Notification{

        @android.annotation.TargetApi (android.os.Build.VERSION_CODES.O)
        @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN)
        public void setLatestEventInfo(Context context,
                                       CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
            if (context.getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1){
                Log.e(TAG, "setLatestEventInfo() is deprecated and you should feel deprecated.",
                        new Throwable());
            }

            if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.N) {
                extras.putBoolean(EXTRA_SHOW_WHEN, true);
            }

            // ensure that any information already set directly is preserved
            final Notification.Builder builder = new Notification.Builder(context, "");

            // now apply the latestEventInfo fields
            if (contentTitle != null) {
                builder.setContentTitle(contentTitle);
            }
            if (contentText != null) {
                builder.setContentText(contentText);
            }
            builder.setContentIntent(contentIntent);

            builder.build(); // callers expect this notification to be ready to use
        }

    }
    public static android.widget.RemoteViews getRemoteViews(Context context, String info) {
        android.widget.RemoteViews remoteviews = new android.widget.RemoteViews (context.getPackageName(), com.nfp.update.R.layout.main_item);
/*        remoteviews.setImageViewResource(R.id.download_promp_icon,R.mipmap.ic_launcher_round);
        remoteviews.setTextViewText(R.id.download_title,mUpdateInfo.getAppname());
        remoteviews.setTextViewText(R.id.download_promp_info,info);*/
        //找到对应的控件（R.id.download_notification_root），为控件添加点击事件getPendingIntent(context)
        remoteviews.setOnClickPendingIntent(com.nfp.update.R.string.Notification_software_update,getPendingIntent(context));
        return remoteviews;
    }

    private static PendingIntent getPendingIntent (Context context) {
        Intent intent = new Intent(context, com.nfp.update.UpdateDialog.class);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("msg","Test发送到Service");
        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,0);
        return pendingIntent;
    }

    private void getPendingIntent() {

       /*  Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("msg","从通知栏点击进来的");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);*/

    }


    /* type = 0, back to initial interface; type = 1, back to update interface;type = 2, back to schedule interface;*/
    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN)
    public static void showFotaNotification (android.content.Context context, int text, int type) {
        Intent notificationIntent;
        PendingIntent contentIntent;
        NotificationCompat.Builder builder;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService (context.NOTIFICATION_SERVICE);
       // NewNotification notification = (com.nfp.update.UpdateUtil.NewNotification) new android.app.Notification ();
       // notification.icon = com.nfp.update.R.drawable.pict_software_update;
       /* Notification notification = new Notification();
        notification.icon = R.drawable.pict_software_update;*/
        NotificationChannel chan1 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan1 = new NotificationChannel("static", "Primary Channel", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(chan1);
            builder = new NotificationCompat.Builder(context, "static");
        }

        builder = new NotificationCompat.Builder(context, "static");


        if (type == 1 || type == 3) {
     //       notification.flags = Notification.FLAG_NO_CLEAR;
            builder.setAutoCancel(true);
        } else {
           // notification.flags = Notification.FLAG_AUTO_CANCEL;
            builder.setAutoCancel(false);
        }

        android.content.Context mcontext = context.getApplicationContext ();
        CharSequence contentTitle = context.getResources ().getString (com.nfp.update.R.string.Notification_software_update);
        CharSequence contentText = context.getResources ().getString (text);

        if (type == 1) {
            notificationIntent = new android.content.Intent (mcontext, com.nfp.update.UpdateDialog.class);
            //notificationIntent.putExtra("FOTA_FROM_NOTIFICATION", true);
            contentIntent = android.app.PendingIntent.getActivity (mcontext, 0, notificationIntent, 0);
        } else if (type == 2) {
            notificationIntent = new android.content.Intent (mcontext, com.nfp.update.UpdateSchedule.class);
            contentIntent = android.app.PendingIntent.getActivity (mcontext, 0, notificationIntent, 0);
        } else if (type == 3) {
            notificationIntent = new android.content.Intent (mcontext, com.nfp.update.PrepareUpdateActivity.class);
            contentIntent = android.app.PendingIntent.getActivity (mcontext, 0, notificationIntent, 0);
        } else if (type == 4) {
            notificationIntent = new android.content.Intent (mcontext, com.nfp.update.UpdateSuccessActivity.class);
            contentIntent = android.app.PendingIntent.getActivity (mcontext, 0, notificationIntent, 0);
        } else if (type == 5) {
            notificationIntent = new android.content.Intent (mcontext, com.nfp.update.UpdateFailActivity.class);
            contentIntent = android.app.PendingIntent.getActivity (mcontext, 0, notificationIntent, 0);
        } else if (type == 6) {
            notificationIntent = new android.content.Intent (mcontext, com.nfp.update.DownLoadProgress.class);
            //notificationIntent.putExtra("DOWNLOAD_FROM_NOTIFICATION", true);
            contentIntent = android.app.PendingIntent.getActivity (mcontext, 0, notificationIntent, 0);
            setDownloadNotification (context, true);
        } else if (type == 7) {
            notificationIntent = new android.content.Intent (mcontext, com.nfp.update.LowBatteryActivity.class);
            contentIntent = android.app.PendingIntent.getActivity (mcontext, 0, notificationIntent, 0);
        } else {
            notificationIntent = new android.content.Intent (mcontext, com.nfp.update.MainActivity.class);
            contentIntent = android.app.PendingIntent.getActivity (mcontext, 0, notificationIntent, 0);
        }
        builder.setContentTitle(contentTitle) //设置通知栏标题
                .setContentText(contentText) //设置通知栏显示内容
                .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知优先级
                .setSmallIcon(R.drawable.icon)
                .setDefaults(1)
                .setAutoCancel(true); //设置这个标志当用户单击面板就可以将通知取消
       // notification.setLatestEventInfo(mcontext, contentTitle, contentText, contentIntent);

      //  notification.setLatestEventInfo (mcontext, contentTitle, contentText, contentIntent);
        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(0, builder.build());
        if (type == 2) {

           // mNotificationManager.notify (0x02, notification);
        } else {
           // mNotificationManager.notify (0x01, notification);
        }


        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        android.content.SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putInt ("notification_type", type);
        pEdit.commit ();
    }

    public static void cancelFotaNotification (Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService (context.NOTIFICATION_SERVICE);
        Notification notification = new Notification ();
        mNotificationManager.cancel (0x01);
    }

    public static void cancelScheduleNotification (Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService (context.NOTIFICATION_SERVICE);
        Notification notification = new Notification ();
        mNotificationManager.cancel (0x02);
        setInstallSchedule (context, false);
    }

    public static boolean hasSimCard (Context context) {

        TelephonyManager telMgr = (TelephonyManager) context.getSystemService (context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState ();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false;
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        Log.d (TAG, result ? "有SIM卡" : "无SIM卡");
        return result;
    }

    public static long getAvailableInternalMemorySize () {

        File path = new File ("/cache");
        long available = path.getFreeSpace () / (1024 * 1024);
        return available;
    }

    public static boolean checkNetworkConnection () {

        ConnectivityManager connec = (ConnectivityManager) getContext ().getSystemService (getContext ().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo (0).getState () ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo (0).getState () ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo (1).getState () ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo (1).getState () == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connec.getNetworkInfo (0).getState () ==
                android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo (1).getState () ==
                        android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static long getFileLength (String filestr) {
        File file = new File (filestr);
        if (file.exists ()) {
            Log.d (TAG, "range = " + file.length ());
            return file.length ();
        }
        return 0;
    }

    public static String getBuildModel () {
        String pm = Build.MODEL;
       // String model = pm.substring (pm.length () - 5, pm.length ());
        String model = pm.substring (pm.length ()-1, pm.length ());

        return model;
    }

    public static void setGoupID (int num, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putInt ("group_id", num);
        pEdit.commit ();
    }

    public static int getGoupID (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        return sharedPreferences.getInt ("group_id", 1);
    }

    public static void setFotaProxy (int num, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putInt ("proxy", num);
        pEdit.commit ();
    }

    public static void setFirstBoot (Context context, Boolean flag) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putBoolean ("first_boot", flag);
        pEdit.commit ();
    }

    public static Boolean getFirstBoot (Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        return sharedPreferences.getBoolean ("first_boot", true);
    }


    public static boolean is_last_finish(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        return sharedPreferences.getBoolean ("isfinish", true);
    }

    public static void set_last_finish(Context context,boolean flag){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putBoolean ("isfinish", flag);
        pEdit.commit ();
    }

    public static void setNitz (Context context, Boolean flag) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putBoolean ("nitz", flag);
        pEdit.commit ();
    }

    public static Boolean getNitz (Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences (context);
        return sharedPreferences.getBoolean ("nitz", false);
    }

    public static void setBoot (Context context, Boolean flag) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putBoolean ("boot", flag);
        pEdit.commit ();
    }

    public static Boolean getBoot (Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences (context);
        return sharedPreferences.getBoolean ("boot", false);
    }

    public static int getFotaProxy (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        return sharedPreferences.getInt ("proxy", 0);
    }

    public static void setDownloadNotification (Context context, Boolean flag) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences (context);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putBoolean ("download_notification", flag);
        pEdit.commit ();
    }

    public static void setInstallSchedule (Context context, Boolean flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        SharedPreferences.Editor pEdit = sharedPreferences.edit ();
        pEdit.putBoolean ("install_schedule", flag);
        pEdit.commit ();
    }

    public static Boolean getInstallSchedule (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("debug_comm", 0);
        return sharedPreferences.getBoolean ("install_schedule", true);
    }

    public static String autoGenericCode (String code, int num) {
        String result = "";
        if (code != null && ! code.equals ("")) {
            result = String.format ("%0" + num + "d", Integer.parseInt (code));
        }
        return result;
    }

    public static int CRC_XModem (byte[] bytes) {
        int crc = 0x0000;          // initial value
        int polynomial = 0x1021;
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[ index ];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }

    public static String XorFormat (String str) {
        String result = "";
        for (int i = 0; i < 7; i++) {
            String sdigit = str.substring (i, i + 1);
            int digit = Integer.parseInt (sdigit);
            int num = digit ^ 1;
            result = result + String.valueOf (num);
        }

        return result;
    }

    public static String padLeft (String str, int len) {
        String pad = "0000000000000000";
        return len > str.length () && len <= 16 && len >= 0 ? pad.substring (0, len - str.length ()) + str : str;
    }

    public static String getTestVersion (Context context) {
        String testVer = "";
        String CRC = "0000";

        if (! hasSimCard (context)) {
            return "";
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService (context.TELEPHONY_SERVICE);
        if (android.support.v4.app.ActivityCompat.checkSelfPermission (context, android.Manifest.permission.READ_SMS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED &&
                android.support.v4.app.ActivityCompat.checkSelfPermission (context, android.Manifest.permission.READ_PHONE_NUMBERS)
                        != android.content.pm.PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission (context, android.Manifest.permission.READ_PHONE_STATE)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        String MSISDN = tm.getLine1Number ();
        String IMSI = tm.getSubscriberId();
        String IMEI = tm.getDeviceId();
        String fw = Build.DISPLAY;

        String substr1 = MSISDN.substring(MSISDN.length()-4, MSISDN.length());
        String vMSISDN = autoGenericCode(substr1, 11);

        String substr2 = IMSI.substring(IMSI.length()-4, IMSI.length());
        String vIMSI = autoGenericCode(substr2, IMSI.length());

        String substr3 = IMEI.substring(0, 8);
        String vIMEI = substr3 + XorFormat(IMEI.substring(IMEI.length()-7, IMEI.length()));

        String label = fw.substring(fw.length()-3, fw.length());
        String substr4 = fw.substring(fw.length()-8, fw.length()-4);
        String substr5 = getBuildModel();

        String group = autoGenericCode(String.valueOf(getGoupID(context)), 3);

        String crcSource = "SII "+substr5+" "+substr4+" /l"+label+" "+vIMEI+" "+vMSISDN+" "+vIMSI+" "+group;
        byte[] Cbyte = crcSource.getBytes();
        int crcValue = CRC_XModem(Cbyte);
        CRC = Integer.toHexString(crcValue).toUpperCase();
        String CRCs = padLeft(CRC, 4);

        testVer = "?VER=SII%20"+substr5+"%20"+substr4+"%20/l"+label+"%20"+vIMEI+"%20"+vMSISDN+"%20"+vIMSI+"%20"+group+"%20"+CRCs;
        Log.d(TAG, "ver = " + testVer);
        return testVer;
    }

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN)
    public static void showUpdateResult(Context context) {
        File files = new File("/fota/softwareupdate.dat");
        if(files.exists()){
            files.delete();
        }

        cancelFotaNotification(context);
        Intent intent= new Intent();
        intent.setClass(context, UpdateFailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        showFotaNotification(context, R.string.Notification_update_fail, 5);
    }

    public static void resetFotareference(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static boolean copyfile(String dest,String src){
        Log.d(TAG,"src file = " + src + "  dest file = " + dest);
        FileInputStream fis=null;
        FileOutputStream fos=null;
        try{
            fis=new FileInputStream(src);
            fos=new FileOutputStream(dest);

            byte[] buff=new byte[1024];
            int n;
            while (  (n = fis.read(buff,0,1024) ) != -1  ) {
                fos.write(buff,0,n);
            }
        }catch (Exception e){
            return false;
        }finally{
            try {
                if(fis!=null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(fos!=null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static String setPollTimeEvent(long startTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        return formatter.format(new Date(startTime));
    }

}
