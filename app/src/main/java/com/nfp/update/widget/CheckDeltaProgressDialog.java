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

package com.nfp.update.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.content.Context;
import android.view.KeyEvent;
import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import com.nfp.update.R;
import com.nfp.update.nfpapp.app.ActivityManagerUtil;

public class CheckDeltaProgressDialog extends Activity{

    private String TAG = "CheckDeltaProgressDialog";
    private String action = "check.deltaFile.complete";
    private ProgressDialog mSendingProgressDlg = null;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(action.equals(action) && mSendingProgressDlg !=null){
                mSendingProgressDlg.dismiss();
            }
        }
    };

    @SuppressLint("InvalidWakeLockTag")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.showDialog(1);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "KeepCPUrunning");
        wakeLock.acquire();

        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        registerReceiver(mReceiver, filter);

        ActivityManagerUtil.turnOnListenerKey(this, true);
        ActivityManagerUtil.setListenerKey(this, new int[] { KeyEvent.KEYCODE_ENDCALL });
    }

    protected Dialog onCreateDialog(int id) {
        mSendingProgressDlg = new ProgressDialog(CheckDeltaProgressDialog.this);
        mSendingProgressDlg.setTitle(getString(R.string.confirmation));
        mSendingProgressDlg.setIcon(R.drawable.ic_dialog_confirm);
        mSendingProgressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mSendingProgressDlg.setMessage(getString(R.string.wait_checking_delta));
        mSendingProgressDlg.setCancelable(false);
        mSendingProgressDlg.setOnDismissListener( new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog){
                finish();
            }
        });

        return mSendingProgressDlg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if(wakeLock.isHeld())
            wakeLock.release();
        ActivityManagerUtil.turnOnListenerKey(this, false);
        android.util.Log.i("kevin","CheckDeltaProgressDialog onDestroy =");
    }

   @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_ENDCALL){
            Log.d("kevin", "clickj end key");
            return true;
        }
        return super.onKeyUp(keyCode,event);
    }
}
