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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.nfp.update.DownloadTask.OnDownloadProgress;

import java.io.File;
import java.io.FileInputStream;

public class DownLoadProgress extends Activity {

    private final static String TAG = "DownloadProgress";
    private DownloadTask mDownloadTask = null;
    private static final int MSG_INIT = 0;
    private static final int MSG_INIT1 =1;
    private boolean downFlag = true;
    private ProgressBar pb;
    private TextView mTextView;
    private TextView percent;
    int pro = 0;
    private String messages;
    public String packageName;
    public SharedPreferences spref;
    public static final String DEFAULT_FILE = "/storage/emulated/0/software.dat";
    public static final String FOTA_FILE = "/fota/softwareupdate.dat";
    private final static String DOWNLOAD_UPDATE_FILE = "download.cgi";
    private static String TEST = "?VER=SII%20602SI%20v001%20/l001%20356475080000000%2000000001234%20000000000001234%20001%20B162";
    private final static int INT_DOWNLOAD_UPDATE_FILE = 0x03;
    private long hadDownload = 0;
    Handler mmHandler = new Handler (){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb.setProgress(msg.what);
            percent.setText(""+pro+"%");
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_file);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        DataCache.getInstance(this).setDownloadPath(DownloadService.DOWNLOAD_PATH);

        pb=(ProgressBar)findViewById(R.id.down_pb);
        mTextView=(TextView)findViewById(R.id.tv);
        percent=(TextView)findViewById(R.id.percent);
        messages = this.getResources().getString(R.string.downlaod_back_idle);

        DownloadTask.setOnDownloadProgress (new OnDownloadProgress () {
            @Override
            public void onDownloadProgress (int progress, boolean is_finish) {
                pb.setProgress (progress);
                Log.v ("yingbo","mesgsdgfuishiuhfi_________--");

                percent.setText (""+progress+"%");
            }

        });

        spref = PreferenceManager.getDefaultSharedPreferences(DownLoadProgress.this);
        try{
            TEST = UpdateUtil.getTestVersion(DownLoadProgress.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UpdateUtil.judgePolState(this, 2);
        startDownload();
    }



    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INT_DOWNLOAD_UPDATE_FILE:
                    HttpClient.get(DownLoadProgress.this, CommonUtils.ServerUrlDownloadTwo, null, FOTA_FILE, new FileAsyncHttpResponseHandler(new File(FOTA_FILE), true) {

                        @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                            Log.d(TAG,"Download -> onSuccess");
                            String fileName = null;
                            pb.setProgress(0);
                            for (cz.msebera.android.httpclient.Header header : headers){
                                Log.d(TAG,"head = " + header.getName() + ":" + header.getValue());
                                if (header.getName().equals("Content-Disposition")
                                        || header.getName().equals("Content-disposition")){
                                    String string = header.getValue();
                                    String str[] = string.split("=");
                                    fileName = str[1].substring(1,str[1].length() - 1);
                                }
                            }
                            Log.d(TAG, "name = " + fileName);
                            if (UpdateUtil.getAvailableInternalMemorySize()*1024 < getFileSize(file)){
                                startLowVolume();
                            }else{
                                if (fileName != null && fileName.length() > 0){
                                    if (file != null && file.exists()){
                                        Log.d(TAG,"renameTo successed");
                                        insertEventLog(getApplicationContext (),0, getString(R.string.download), 0, getString(R.string.end), null, null);
                                        SharedPreferences.Editor pEdit = spref.edit();
                                        pEdit.putString("PAC_NAME", "softwareupdate.dat");
                                        pEdit.commit();
                                        downResult(true);
                                    }
                                }
                            }
                        }

                        @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                              Throwable throwable, File file) {
                            Log.d(TAG,"Download -> onFailure");
                            insertEventLog(getApplicationContext (),0, getString(R.string.download), 0, getString(R.string.fail), null, null);
                            UpdateUtil.showFotaNotification(DownLoadProgress.this, R.string.Notification_download_failed, 0);
                            HttpClient.cancleRequest(true);
                            UpdateUtil.judgePolState(DownLoadProgress.this, 0);
                            downResult(false);
                            restartPolling();
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            // TODO Auto-generated method stub
                            //int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                            long total = totalSize + hadDownload;
                            if(downFlag){
                                downFlag = false;
                                SharedPreferences.Editor pEdit = spref.edit();
                                pEdit.putLong("total_size", total);
                                pEdit.commit();
                            }
                            int count = (int) (((bytesWritten + hadDownload) * 1.0 / total) * 100);
                            Log.d(TAG, "Download -> onProgress, count = " + count);
                            pb.setProgress(count);
                        }
                    });
                    break;
            }
        };
    };
    private android.net.Uri insertEventLog(android.content.Context context, int eventNo, String eventName, int tid, String factor1, String factor2, String factor3) {
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
    private void startDownload() {
        HttpClient.cancleRequest(true);
        initialProgress();
        insertEventLog(getApplicationContext (),0, getString(R.string.download), 0, getString(R.string.start), null, null);
        Message message = new Message();
        message.what = INT_DOWNLOAD_UPDATE_FILE;
        mhandler.removeMessages(INT_DOWNLOAD_UPDATE_FILE);
        mhandler.sendMessage(message);
    }


    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    private void downResult(boolean flag) {
        Intent mIntent = new Intent();
        Log.d(TAG,"hadDownload =111 " );
        if(spref.getBoolean("download_notification", false)){
            Log.d(TAG,"hadDownload =222 ");
            UpdateUtil.setDownloadNotification(DownLoadProgress.this, false);
            mIntent.setClass(DownLoadProgress.this, SoftwareUpdate.class);
            mIntent.addFlags(mIntent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtra("download_results", flag);
            this.startActivity(mIntent);
        }else{
            Log.d(TAG,"hadDownload =333 ");
            mIntent.putExtra("download_result", flag);
            setResult(1, mIntent);
        }

        if(flag)
            UpdateUtil.startUpdateService(getApplicationContext(), 1);
        finish();
    }

    private void initialProgress() {
        hadDownload = UpdateUtil.getFileLength(FOTA_FILE);
        long total = spref.getLong("total_size", 0);
        if(hadDownload!= 0 && total!=0){
            int count = (int) ((hadDownload * 1.0 /total ) * 100);
            pb.setProgress(count);
            Log.d(TAG,"hadDownload  count= " + String.valueOf(count));
        }else{
            pb.setProgress(0);
        }

        Log.d(TAG,"hadDownload = " + hadDownload);
    }

    private void backTopActivity() {
        Intent intent= new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startLowVolume() {
        Intent intent= new Intent(this, LessVolumeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }

    public long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try{
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available()/1024;
            }catch(Exception e){

            }
        }
        Log.e("kevin", "getFileSize="+String.valueOf(size));
        return size;
    }

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void cancleDownload() {
        HttpClient.cancleRequest(true);
        UpdateUtil.judgePolState(DownLoadProgress.this, 0);
        String confirmation = this.getResources().getString(R.string.confirmation);
        String messages = this.getResources().getString(R.string.cancel_download);
        String yes = this.getResources().getString(R.string.yes);
        String no = this.getResources().getString(R.string.no);

        SharedPreferences.Editor pEdits = spref.edit();
        pEdits.putInt("click_cancel_download",0);
        pEdits.commit();

        AlertDialog.Builder mbuild = new AlertDialog.Builder(this)
                .setTitle(confirmation)
                .setIcon(R.drawable.ic_dialog_confirm)
                .setMessage(messages)
                .setNegativeButton(no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //startDownload();
                            }
                        }
                )
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences.Editor pEdits = spref.edit();
                                pEdits.putInt("click_cancel_download",1);
                                pEdits.commit();
                                backTopActivity();
                                restartPolling();
                            }
                        }
                ).setOnDismissListener(new DialogInterface.OnDismissListener (){
                    @Override
                    public void onDismiss(DialogInterface dialog){
                        if(spref.getInt("click_cancel_download",0)!= 1)
                            startDownload();
                    }
                });
        mbuild.show();
    }



    public void restartPolling() {
        if(spref.getInt("pol_switch",1)!=0){
            UpdateUtil.stopPollingService(this);
            UpdateUtil.startPollingService(this, UpdateUtil.getPollStartTime(this));
        }
    }

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_DPAD_CENTER||keyCode ==KeyEvent.KEYCODE_BACK){
            cancleDownload();
            return true;
        }
        return super.onKeyUp(keyCode,event);
    }
/*       new Thread(new Runnable() {
           @Override
           public void run() {
               int max = pb.getMax();
               try {
                   //子线程循环间隔消息
                   while (pro < max) {
                       pro += 10;
                       Message msg = new Message();
                       msg.what = pro;

                       if(msg == null){

                           mHandler.postDelayed(this, 2000L);

                       }

                       mHandler.sendMessage(msg);

                       Thread.sleep(1000);
                   }
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }).start();*/
}