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

public class SoftwareUpdate{
    private String TAG = "SoftwareUpdate";
    private TextView mTextView;
    private SharedPreferences spref;
    private final static String CONFIR_UPDATE_FILE = "confirm.cgi";
    private final static String DOWNLOAD_UPDATE_FILE = "download.cgi";
    private static String TEST = "?VER=SII%20602SI%20v001%20/l001%20356475080000000%2000000001234%20000000000001234%20001%20B162";
    private final static int INT_CONFIR_UPDATE_FILE = 0x01;
    private static Context context;
    private Intent intent;
    private boolean downResults = false;
    private Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INT_CONFIR_UPDATE_FILE:
                    HttpClient.get(context, CONFIR_UPDATE_FILE + TEST, null, new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            Log.d("yingbo","confirm latest sw situation");
                            if(i == 200){
                                String error = new String(bytes);
                                switch (error){
                                    case "error00":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
                                        Log.d(TAG,"When server connect success, check there is file update  situation");
                                        //SharedPreferences sp = getSharedPreferences("down_file", MODE_WORLD_WRITEABLE);
                                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
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
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.no_latest_sw);
*/
                                        Log.d(TAG,"This is the newest version!");
                                        break;
                                    case "error10":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error10);
*/
                                        break;
                                    case "error20":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error20);
*/
                                        break;
                                    case "error21":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error21);
*/
                                        break;
                                    case "error22":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error22);
*/
                                        break;
                                    case "error25":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error25);
*/
                                        break;
                                    case "error26":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error26);
*/
                                        break;
                                    case "error27":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error27);
*/
                                        break;
                                    case "error28":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error28);
*/
                                        break;
                                    case "error29":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error29);
*/
                                        break;
                                    case "error30":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
/*
                                        mTextView.setText(R.string.server_error30);
*/
                                        break;
                                    case "error31":
                                        insertEventLog(context,0, context.getString(R.string.update_result),
                                                0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
//                                        mTextView.setText(R.string.server_error31);
                                        Log.d(TAG,error);
                                        break;
                                    default:
                                        insertEventLog(context,0, context.getString(R.string.update_result),
                                                0, context.getString(R.string.fail), context.getString(R.string.ver_result), "other");
/*
                                        mTextView.setText(R.string.server_error_other);
*/
                                        Log.d("yingbo","Other Errors");
                                        break;
                                }
                            }else{
                                String a = context.getResources().getString(R.string.network_connect_error);
/*
                                 String connectError = String.format(a, i);
*/

                                HttpClient.cancleRequest(true);
                                UpdateUtil.judgePolState(context, 0);
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            String errorValue = String.valueOf(i);
                            String errorCode = "404";//UpdateUtil.autoGenericCode(errorValue, 3);
                            String a = context.getResources().getString(R.string.network_connect_error);
                            String connectError = String.format(a, errorCode);
                            HttpClient.cancleRequest(true);
                            UpdateUtil.judgePolState(context, 0);
                            Log.d("yingbo","confir failed "+errorValue);
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                        Log.v ("yingbo","totalSize"+totalSize);
                        }

                    });
                    break;
            }
        };
    };

    SoftwareUpdate(Context context){
        this.context=context;

            spref = PreferenceManager.getDefaultSharedPreferences(context);



            connectServer();

            downloadComplete(downResults);

            try{
                TEST = UpdateUtil.getTestVersion(context);
            } catch (Exception e) {
                e.printStackTrace();
            }


    }




    private void downloadError() {

        try{
            openConfirmDialog(2);
        }catch(Exception e){
            Log.e("yingbo" , e.getMessage());
        }
    }

    private void prepareUpdate() {

        SharedPreferences sprefs = context.getSharedPreferences("debug_comm", 0);

        if(sprefs.getInt("AUTO_UPDATE", 0) ==0){
            Log.d("yingbo","auto update start");
            intent.setClass(context, PrepareUpdateActivity.class);
            context.startActivity(intent);
        }else{
            Log.d("yingbo","auto update sse");
            confirmInstall();
        }

    }

    private void confirmInstall() {
        intent.setClass(context, UpdateDialog.class);
        context.startActivity(intent);
    }

    private void openConfirmDialog(final int identity) {

        if(identity==1){
            SharedPreferences.Editor pEdits = spref.edit();
            pEdits.putInt("click_yes",1);
            pEdits.commit();

//            intent.setClass(context, DownloadProgress.class);

        }else if(identity==2){

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

        if (tid < 1 || tid > 256) {
            Log.w(TAG, "Invalid tid : " + tid);
        } else {
            values.put("TID", new Integer(tid));
        }

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



    public void downloadComplete(boolean result) {
        if(result){
            prepareUpdate();
        }else{
            downloadError();
        }
    }

}
