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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.content.Context;
import android.util.Log;
import android.content.SharedPreferences;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import com.nfp.update.R;

import java.io.File;
import java.io.IOException;

public class UpdateDialog extends Activity implements OnCancelListener {

    private String TAG = "UpdateDialog";
    //private boolean needCancel=false;
    /*private BatteryReceiver batteryReceiver = null;
     class BatteryReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
				int level = intent.getIntExtra("level", 0);
				int scale = intent.getIntExtra("scale", 100);
				int curLevel = (level*100)/scale;
                                String sss= "current battery level ="+((level*100)/scale)+"%";
                                Log.d(TAG,"batteryLevel ="+sss);
                                if(curLevel<30){
                                    lowBattery();
                                    getContentResolver().insertEventLog(0, getString(R.string.install_result), 0, getString(R.string.fail), getString(R.string.no_enough_battery), null);
                                }else{
                                    Intent intents= new Intent(context, AutoUpdateService.class);
                                    intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startService(intents);
                               }
			}
		}
    }*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.showDialog(1);
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog mDialog = null;
        String confirmation = this.getResources().getString(R.string.confirmation);
        String yes = this.getResources().getString(R.string.yes);
        String no = this.getResources().getString(R.string.no);
        String messages = this.getResources().getString(R.string.confirmation_messages);

        mDialog  = new AlertDialog.Builder(this)
                .setTitle(confirmation).setOnCancelListener(this)
                .setIcon(R.drawable.ic_dialog_confirm)
                .setMessage(messages)
                .setNegativeButton(no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {

                                backTopActivity();
                                finish();
                            }
                        }
                )
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                insertEventLog(UpdateDialog.this,0, getString(R.string.install), 0, getString(R.string.manual_install), null, null);
                                startInstall();
                                finish();
                            }
                        }
                ).create();

        return mDialog;
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
    private void backTopActivity() {
        Intent intent= new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*private void lowBattery() {
        Intent intent= new Intent(this, LowBatteryActivity.class);
        startActivity(intent);
    }

   public void prepareInstall() {
	IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	batteryReceiver = new BatteryReceiver();
	registerReceiver(batteryReceiver, intentFilter);
    }*/

   public void startInstall() {
	Intent intents= new Intent(this, AutoUpdateService.class);
	intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	this.startService(intents);
    }

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.i("kevin","UpdateDialog onDestroy =");
        UpdateUtil.stopUpdateService(getApplicationContext(), 2);
        UpdateUtil.startUpdateService(getApplicationContext(), 1);
       // if(batteryReceiver !=null)
        //    unregisterReceiver(batteryReceiver);
        Intent intent=new Intent();
        intent.setAction("destroy.update.dialog");
        sendStickyBroadcast(intent);
        finish();
    }

    public void onCancel(DialogInterface dialog) {
        backTopActivity();
        finish();
    }
}
