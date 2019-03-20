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
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import android.widget.TimePicker;
import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nfp.update.R;

public class UpdateSchedule extends Activity implements TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener{

    public int scheduleValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
            scheduleValue = bundle.getInt("scheduleValue");
        UpdateUtil.cancelScheduleNotification(this);
        showDialog(0);
    }

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setTime(this, hourOfDay, minute);

        if(scheduleValue ==1){
            UpdateUtil.setTempHourMinute(this, hourOfDay, minute);
            startUpdateSchedule(hourOfDay, minute);
        }else{
            UpdateUtil.setHourMinute(this, hourOfDay, minute);
            if(UpdateUtil.getTempTImeFlag(UpdateSchedule.this)!=1)
                startUpdateSchedule(hourOfDay, minute);
        }

        Toast.makeText(this, R.string.toast_set_complete, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public Dialog onCreateDialog(int id) {
        final Calendar calendar = Calendar.getInstance();
        int hour =0;
        int minute =0;
        if(scheduleValue ==1){
            hour = UpdateUtil.getHourTemp(this);
            minute =UpdateUtil.getMinuteTemp(this);
        }else{
            hour = UpdateUtil.getHour(this);
            minute = UpdateUtil.getMinute(this);
        }
        TimePickerDialog tp = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, true);
        tp.setTitle(getString(R.string.update_schedule_title));

        tp.setOnCancelListener(this);
        return tp;
    }

   static void setTime(Context context, int hourOfDay, int minute) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmm");
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();

        Date updateDate = (Date) c.getTime();
        String dateTime = formatter.format(updateDate);
        Log.d("kevin","updates chedule dateTime="+ dateTime);
        insertEventLog(context,0, context.getString(R.string.auto_update_setting), 0, dateTime, null, null);
    }
    private static android.net.Uri insertEventLog (Context context, int eventNo, String eventName, int tid, String factor1, String factor2, String factor3) {
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
    public void startUpdateSchedule(int hourOfDay, int minute){
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(UpdateSchedule.this);
        SharedPreferences sprefs = getSharedPreferences("debug_comm", 0);
        File files = new File("/fota/softwareupdate.dat");
        String packageFile = spref.getString("PAC_NAME", null);
        if(packageFile!=null&&files.exists()){
            UpdateUtil.stopUpdateService(getApplicationContext(), 1);
            if(sprefs.getInt("AUTO_UPDATE", 0) == 0){
                Log.d("kevin","11133"+ "  hour="+String.valueOf(hourOfDay)+"   minute="+String.valueOf(minute));
                UpdateUtil.startUpdateService(getApplicationContext(), 1);
            }
        }
    }

    public void onCancel(DialogInterface dialog){
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
