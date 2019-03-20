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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.nfp.update.R;
import android.text.TextUtils;
import com.nfp.update.polling.PollingService;
import android.telephony.TelephonyManager;
import java.io.File;
import android.content.ContentResolver;
import android.content.ContentValues;


public class AlarmReceiver extends BroadcastReceiver {

    Intent intents;

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(final Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        WakeLock mWakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SimpleTimer");
        WakeLock mWake = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleT");

        if (intent.getAction().equals("com.nfp.update.ALARM")) {
            Log.d("kevin", "receive polling services broadcast");
            mWakelock.acquire();
            intents= new Intent(context, PollingService.class);
            intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(intents);
            if(mWakelock.isHeld())
                mWakelock.release();
        }else if (intent.getAction().equals("com.nfp.update.SCHEDULE")) {
            Log.d("kevin", "receive schedule services broadcast");
            long updateTime = UpdateUtil.getUpdateTime(context);
            long currentTime = System.currentTimeMillis();
            long uTime = updateTime/(1000*60);
            long cTime = currentTime/(1000*60);
            if(cTime<=uTime){
                mWake.acquire();
                UpdateUtil.setUpdateTime(context,0);
                TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                if(tm.getCallState()!=TelephonyManager.CALL_STATE_IDLE){
                    Log.d("kevin", "Do not  rebootToInstall getCallState  = "+tm.getCallState());
                    UpdateUtil.startUpdateService(context, 1);
                }else{
                    intents= new Intent(context, UpdateDialog.class);
                    intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intents);
                    UpdateUtil.startUpdateService(context, 2);
                    if(UpdateUtil.getTempTImeFlag(context)==1)
                        UpdateUtil.clearTempHourMinute(context);
                }
                mWake.release();
            }else{
                UpdateUtil.stopUpdateService(context, 1);
                UpdateUtil.startUpdateService(context, 1);
            }
        }else if (intent.getAction().equals("com.nfp.update.UPDATE")) {
            Log.d("kevin", "receive schedule update broadcast");


            insertEventLog(context,0, context.getString(R.string.install), 0, context.getString(R.string.auto_install), null, null);

            mWake.acquire();
            UpdateUtil.setUpdateTime(context,0);
            intents= new Intent(context, AutoUpdateService.class);
            intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(intents);
            mWake.release();
        }
    }

    private Uri insertEventLog(Context context,int eventNo, String eventName, int tid,String factor1, String factor2, String factor3) {
        final Uri uri = Uri.parse("content://com.ssol.eventlog/eventlog");

        ContentResolver mContentResolver=context.getContentResolver();

        mContentResolver.acquireContentProviderClient (uri);

        ContentValues values = new ContentValues ();

        if (TextUtils.isEmpty(eventName)) {
            throw new IllegalArgumentException("Invalid event name : " + eventName);
        } else {
            values.put("EVENT_NAME", eventName);
        }

        /*if (tid < 1 || tid > 256) {
            Log.w(TAG, "Invalid tid : " + tid);
        } else {
            values.put("TID", new Integer(tid));
        }*/

        if (!TextUtils.isEmpty(factor1)) {
            values.put("FACTOR1", factor1);
        }

        if (!TextUtils.isEmpty(factor2)) {
            values.put("FACTOR2", factor2);
        }

        if (!TextUtils.isEmpty(factor3)) {
            values.put("FACTOR3", factor3);
        }

        return  mContentResolver.insert (uri,values);

    }
}
