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

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.view.KeyEvent;
import android.os.Handler;
import android.os.Message;
import android.content.SharedPreferences;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;

import com.nfp.update.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nfp.update.nfpapp.app.ActivityManagerUtil;
public class UpdateFailActivity extends Activity{
    private TextView mTextView;
    private Intent intent;
    private String action = "destroy.update.dialog";
    private UpdateDialogReceiver updateReceiver = null;
    class UpdateDialogReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(action.equals(intent.getAction())){
                Log.d("kevin", "UpdateFailActivity start polling");
                UpdateUtil.stopUpdateService(UpdateFailActivity.this, 1);
                UpdateUtil.stopUpdateService(UpdateFailActivity.this, 2);
                UpdateUtil.stopPollingService(UpdateFailActivity.this);
                UpdateUtil.startPollingService(UpdateFailActivity.this, UpdateUtil.getPollStartTime(UpdateFailActivity.this));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent();
        setContentView(R.layout.update_result);
        mTextView = (TextView) findViewById(R.id.content);

        mTextView.setText(R.string.update_fail);
        ActivityManagerUtil.setEndKeyBehavior(this, ActivityManagerUtil.ENDKEY_FINISH_TASK);

        UpdateUtil.resetFotareference(this);
        registerUpdateReceiver();
    }

   public void registerUpdateReceiver() {
        IntentFilter intentFilter = new IntentFilter(action);
        updateReceiver = new UpdateDialogReceiver();
        registerReceiver(updateReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(updateReceiver !=null)
            unregisterReceiver(updateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void backIdleActivity() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClassName("com.android.launcher3","com.android.launcher3.Launcher");
        startActivity(intent);
        finish();
    }

    /*private void backTopActivity() {
        Intent intent= new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }*/

   @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_DPAD_CENTER){
            backIdleActivity();
            finish();
            return true;
        }
        return super.onKeyUp(keyCode,event);
    }

}
