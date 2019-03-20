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
import com.nfp.update.nfpapp.app.ActivityManagerUtil;
import com.nfp.update.R;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UpdateSuccessActivity extends Activity{
    private TextView mTextView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent();
        setContentView(R.layout.update_result);
        mTextView = (TextView) findViewById(R.id.content);

       /* String update_result = getIntent().getStringExtra(("update_fail"));
        if(update_result==null)
            update_result = this.getResources().getString(R.string.update_complete);
        mTextView.setText(update_result);*/
        //mTextView.setText(R.string.update_complete);
        ActivityManagerUtil.setEndKeyBehavior(this, ActivityManagerUtil.ENDKEY_FINISH_TASK);

        //UpdateUtil.resetFotareference(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backIdleActivity();
    }

    private void backIdleActivity() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClassName("com.android.launcher3","com.android.launcher3.Launcher");
        startActivity(intent);
        finish();
    }

   @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_DPAD_CENTER){
            backIdleActivity();
            return true;
        }
        return super.onKeyUp(keyCode,event);
    }

}
