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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.Gravity;
import android.os.Handler;
import android.os.Message;
import android.content.SharedPreferences;
import com.nfp.update.nfpapp.app.util.NfpSoftkeyGuide;
import com.nfp.update.R;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PrepareUpdateActivity extends Activity{
    private String TAG = "PrepareUpdateActivity";
    private TextView mTextView;
    private Intent intent;

    private boolean isConnect = false;
    private boolean isDown = false;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent();
        setContentView(R.layout.prepare_update);
        mTextView = (TextView) findViewById(R.id.content);
        prepareUpdate();
        UpdateUtil.judgePolState(this, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void confirmInstall() {
        intent.setClass(this, UpdateDialog.class);
        startActivity(intent);
    }

    private void prepareUpdate() {
            mTextView.setText(downloadText());
    }

    private String downloadText() {
        String sDownload = "";
        String hour = "";
        String minute="";
        String lang = Locale.getDefault().getLanguage();
        //if(lang !=null && "ja".equals(lang)){
            /*SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);
            long updateTime = spref.getLong("update_time_mm",0);

            String str = String.format("%tF %<tT", updateTime);
            Log.d("kevin", "startUpdate install timer  =-> " + str);

            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.MONTH, c.get(Calendar.MONTH));
            c.set(Calendar.DATE, c.get(Calendar.DATE));
            Date d = (Date) c.getTime();
            String dateTime = formatter.format(d);*/
            long updateTime = UpdateUtil.getUpdateTime(this);
            String str = String.format("%tF %<tT", updateTime);
            Log.d("lhc", "startUpdate install timer  =-> " + str);

            if(UpdateUtil.getTempTImeFlag(this)==1){
                hour = UpdateUtil.autoGenericCode(String.valueOf(UpdateUtil.getHourTemp(this)), 2);
                minute = UpdateUtil.autoGenericCode(String.valueOf(UpdateUtil.getMinuteTemp(this)), 2);
            }else{
                hour = UpdateUtil.autoGenericCode(String.valueOf(UpdateUtil.getHour(this)), 2);
                minute = UpdateUtil.autoGenericCode(String.valueOf(UpdateUtil.getMinute(this)), 2);
            }

            String timer = str.substring(5, 7) + "月"+str.substring(8,10) + "日"+" "+hour+":"+minute;
            String updateDone = getString(R.string.update_done);
            sDownload = String.format(updateDone, str.substring(5, 7), str.substring(8,10), hour, minute);
            mTextView.setGravity(Gravity.START);
            Log.d("lhc","timer="+timer);
        //}else{
       //     sDownload = getString(R.string.update_done);
        //    mTextView.setGravity(Gravity.CENTER);
      //  }

        return sDownload;
    }

    private void backTopActivity() {
        Intent intent= new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_DPAD_CENTER){
            //finish();
            /*Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClassName("com.android.launcher3","com.android.launcher3.Launcher");
            startActivity(intent);*/
            backTopActivity();
            return true;
        }else if(keyCode ==KeyEvent.KEYCODE_F1){
            if(isDown){
                intent.setClass(this, UpdateSchedule.class);
                intent.putExtra("scheduleValue", 1);
                startActivity(intent);
                return true;
            }
        }else if(keyCode ==KeyEvent.KEYCODE_F2){
            if(isDown){
                confirmInstall();
                return true;
            }
        }
        return super.onKeyUp(keyCode,event);
    }

}
