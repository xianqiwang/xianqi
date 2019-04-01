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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.view.KeyEvent;
import android.view.View;
import android.os.Handler;
import android.os.Message;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.nfp.update.nfpapp.app.util.NfpSoftkeyGuide;
import com.nfp.update.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.net.Uri;
import org.apache.http.Header;

public class SoftwareUpdate extends Activity{
    private String TAG = "SoftwareUpdate";
    private TextView mTextView;
    private AlertDialog mDialog = null;
    private SharedPreferences spref;
    private final static String CONFIR_UPDATE_FILE = "confirm.cgi";
    private final static String DOWNLOAD_UPDATE_FILE = "download.cgi";
    private static String TEST = "?VER=SII%20602SI%20v001%20/l001%20356475080000000%2000000001234%20000000000001234%20001%20B162";
    private final static int INT_CONFIR_UPDATE_FILE = 0x01;
    private static Context context;
    private Intent intent;
    private boolean downResults = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SoftwareUpdate.this;
        intent = new Intent();
        setContentView(R.layout.software_update);
        mTextView = (TextView) findViewById(R.id.content);

        spref = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){

            downResults = bundle.getBoolean("download_results");
            downloadComplete(downResults);
        }else{
            try{
                TEST = UpdateUtil.getTestVersion(SoftwareUpdate.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            connectServer();
        }
    }

    private void downloadError() {

        try{
            openConfirmDialog(2);
        }catch(Exception e){
            Log.e("AlertDialog  Exception:" , e.getMessage());
        }
    }

    private void prepareUpdate() {

        SharedPreferences sprefs = getSharedPreferences("debug_comm", 0);

        if(sprefs.getInt("AUTO_UPDATE", 0) ==0){
            Log.d("kevin","auto update start");
            intent.setClass(SoftwareUpdate.this, PrepareUpdateActivity.class);
            startActivity(intent);
        }else{
            Log.d("kevin","auto update sse");
            confirmInstall();
        }

    }

    private void confirmInstall() {
        intent.setClass(SoftwareUpdate.this, UpdateDialog.class);
        startActivity(intent);
    }

    private void openConfirmDialog(final int identity) {

        String messages = "";
        String confirmation = this.getResources().getString(R.string.confirmation);
        String yes = this.getResources().getString(R.string.yes);
        String no = this.getResources().getString(R.string.no);
        if(identity==1){
            messages = this.getResources().getString(R.string.download_info);
        }else if(identity==2){
            messages = this.getResources().getString(R.string.download_fail);
        }

        if(identity==1){
            SharedPreferences.Editor pEdits = spref.edit();
            pEdits.putInt("click_yes",1);
            pEdits.commit();
            intent.setClass(SoftwareUpdate.this, DownloadProgress.class);
            startActivityForResult(intent, 0);
        }else if(identity==2){
            connectServer();
        }
    }

    public void connectServer() {
            Message message = new Message();
            message.what = INT_CONFIR_UPDATE_FILE;
            mhandler.removeMessages(INT_CONFIR_UPDATE_FILE);
            mhandler.sendMessage(message);
          }

    private Uri insertEventLog(Context context, int eventNo, String eventName,
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

    private Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INT_CONFIR_UPDATE_FILE:
                    HttpClient.get(SoftwareUpdate.this, CONFIR_UPDATE_FILE + TEST, null, new AsyncHttpResponseHandler() {
                        @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            Log.d(TAG,"confirm latest sw situation");
                            if(i == 200){
                                String error = new String(bytes);
                                switch (error){
                                    case "error00":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        Log.d(TAG,"When server connect success, check there is file update  situation");
                                        //SharedPreferences sp = getSharedPreferences("down_file", MODE_WORLD_WRITEABLE);
                                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SoftwareUpdate.this);
                                        String packageFile = sp.getString("PAC_NAME", null);
                                        File files = new File("/fota/softwareupdate.dat");
                                        if(packageFile == null||!files.exists()){

                                            try{
                                                openConfirmDialog(1);
                                            }catch(Exception e){
                                                Log.e("AlertDialog  Exception:" , e.getMessage());
                                            }

                                        }else{
                                            prepareUpdate();
                                        }
                                        break;
                                    case "error23":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.no_latest_sw);
                                        Log.d(TAG,"This is the newest version!");
                                        break;
                                    case "error10":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error10);
                                        break;
                                    case "error20":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error20);
                                        break;
                                    case "error21":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error21);
                                        break;
                                    case "error22":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error22);
                                        break;
                                    case "error25":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error25);
                                        break;
                                    case "error26":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error26);
                                        break;
                                    case "error27":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error27);
                                        break;
                                    case "error28":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error28);
                                        break;
                                    case "error29":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error29);
                                        break;
                                    case "error30":
                                        insertEventLog(context,0, getString(R.string.update_result), 0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error30);
                                        break;
                                    case "error31":
                                        insertEventLog(context,0, getString(R.string.update_result),
                                                0, getString(R.string.fail), getString(R.string.ver_result), error);
                                        mTextView.setText(R.string.server_error31);
                                        Log.d(TAG,error);
                                        break;
                                    default:
                                        insertEventLog(context,0, getString(R.string.update_result),
                                                0, getString(R.string.fail), getString(R.string.ver_result), "other");
                                        mTextView.setText(R.string.server_error_other);
                                        Log.d(TAG,"Other Errors");
                                        break;
                                }
                            }else{
                                String a = getResources().getString(R.string.network_connect_error);
                                @android.annotation.SuppressLint ("StringFormatMatches") String connectError = String.format(a, i);
                                mTextView.setText(connectError);
                                HttpClient.cancleRequest(true);
                                UpdateUtil.judgePolState(SoftwareUpdate.this, 0);
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            //mTextView.setText(R.string.server_error);
                            String errorValue = String.valueOf(i);
                            String errorCode = "404";//UpdateUtil.autoGenericCode(errorValue, 3);
                            String a = getResources().getString(R.string.network_connect_error);
                            String connectError = String.format(a, errorCode);
                            mTextView.setText(connectError);
                            HttpClient.cancleRequest(true);
                            UpdateUtil.judgePolState(SoftwareUpdate.this, 0);
                            Log.d(TAG,"confir failed "+errorValue);
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {

                        }

                    });
                    break;
            }
        };
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean downResult = false;
        if(resultCode==1){
            downResult = data.getBooleanExtra("download_result", false);
            downloadComplete(downResult);
        }
    }

    public void downloadComplete(boolean result) {
        if(result){
            prepareUpdate();
        }else{
            downloadError();
        }
    }

}
