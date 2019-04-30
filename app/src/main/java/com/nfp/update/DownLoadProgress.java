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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.nfp.update.DefDialog.OnOkListener;
import com.nfp.update.service.DownloadService;
import com.nfp.update.widget.CommonUtils;
import com.nfp.update.widget.CustomDialog;
import com.nfp.update.widget.DataCache;
import com.nfp.update.widget.HttpClient;

import java.io.File;
import java.io.FileInputStream;

import cz.msebera.android.httpclient.Header;

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
    private static String TEST = "?VER=SII%20602SI%20v001%20/l001%20356475080000000%2000000001234%20000000000001234%20001%20B162";
    private final static int INT_DOWNLOAD_UPDATE_FILE = 0x03;
    private final static int INT_DOWNLOAD_UPDATE_STATUS = 0x02;
    private final static int INT_DOWNLOAD_UPDATE_STATUSONE = 0x01;
    final Message msg=new Message ();
    private long hadDownload = 0;
    public CustomDialog N0645_D1;
    private DefDialog defDialog;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
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
        percent=(TextView)findViewById(R.id.percent);
        messages = this.getResources().getString(R.string.downlaod_back_idle);
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
        public void handleMessage(final Message msg) {
            switch (msg.what){
                case INT_DOWNLOAD_UPDATE_FILE:

                    if (UpdateUtil.is_last_finish (DownLoadProgress.this)){

                        CommonUtils.is_delete (CommonUtils.DOWNLOAD_PATH);

                    }
                    HttpClient.get(DownLoadProgress.this, CommonUtils.ServerUrlDownloadTwo, null, CommonUtils.DOWNLOAD_PATH, new FileAsyncHttpResponseHandler(new File(CommonUtils.DOWNLOAD_PATH+CommonUtils.UpdateFileName), true) {

                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                            UpdateUtil.set_last_finish (DownLoadProgress.this,true);
                            Log.d(TAG,"Download -> onSuccess");
                            String fileName = null;
                            pb.setVisibility (View.GONE);
                            percent.setVisibility (View.GONE);
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
                                       UpdateUtil.insertEventLog(getApplicationContext (),
                                               0, getString(R.string.download)
                                               , 0, getString(R.string.end)
                                               , null, null);
                                        SharedPreferences.Editor pEdit = spref.edit();
                                        pEdit.putString("PAC_NAME", "update.zip");
                                        pEdit.commit();
                                        downResult(true);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              Throwable throwable, File file) {

                            UpdateUtil.set_last_finish (DownLoadProgress.this,false);
                            UpdateUtil.insertEventLog(getApplicationContext ()
                                    ,0, getString(R.string.download)
                                    , 0, getString(R.string.fail), null, null);
                            UpdateUtil.showFotaNotification(DownLoadProgress.this
                                    , R.string.Notification_download_failed, 0);
                            HttpClient.cancleRequest(true);
                            UpdateUtil.judgePolState(DownLoadProgress.this, 0);
                            downResult(false);
                            SharedPreferences.Editor pEdits = spref.edit();
                            pEdits.putInt("click_cancel_download",1);
                            pEdits.commit();
                            backTopActivity();
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
                    percent.setText (count+"%");
                    pb.setProgress(count);
                }
            });
                    break;
            }

        };
    };

    private void downResult(boolean flag) {
        Intent mIntent = new Intent();

        if(spref.getBoolean("download_notification", false)){

            UpdateUtil.setDownloadNotification(DownLoadProgress.this, false);

        }

        if(flag){
            is_update_schedule();
            UpdateUtil.startUpdateService(getApplicationContext(), 1);

        }else{

            showDialog();

        }
    }

    public int getState() {

        SharedPreferences sp = getSharedPreferences("debug_comm", MODE_PRIVATE);
        int i    = sp.getInt ("AUTO_UPDATE",1);
        return i;

    }
    CustomDialog  N0645;

    public void showDialog(){

         N0645=  new CustomDialog.Builder(this,200,200)
                .setMessage(getResources ().getString (R.string.download_fail))
                .setPositiveButton("Ok", new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {

                        N0645_D1.show ();

                    }        })
                .setNegativeButton ("NO", new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        final Intent intent = new Intent();
                        intent.setClass(DownLoadProgress.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).createTwoButtonDialog();
        N0645.show ();
        N0645_D1=  new CustomDialog.Builder(this,200,200)
                .setMessage(getResources ().getString (R.string.contenus_progress))
                .setPositiveButton("Ok", new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        N0645_D1.dismiss ();
                    }})
                .setNegativeButton ("NO", new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {

                        final Intent intent = new Intent();
                        intent.setClass(DownLoadProgress.this, MainActivity.class);
                        startActivity(intent);

                    }
                }).createTwoButtonDialog();

    }
    public void dismissAll(){
        defDialog.dismiss ();
        N0645_D1.dismiss ();
        N0645.dismiss ();
    }
    @Override
    protected void onDestroy () {
        super.onDestroy ();
        dismissAll();
    }
    public void is_update_schedule(){

        if(getState ()==1){
            defDialog=  DialogCategorical.N_0670_s01 (DownLoadProgress.this,R.string.download_finish
                    ,R.string.b_now_update
                    ,R.string.b_set_time);
            defDialog.setOkClickListener (new OnOkListener () {
                @Override
                public void onOkKey () {


                    CommonUtils.showUpdateNowDialog (DownLoadProgress.this
                            ,new File (DownloadService.DOWNLOAD_PATH));

                    defDialog.dismiss ();


                }

                @Override
                public void onCenterKey () {
                }

                @Override
                public void onCancelKey () {

                    CommonUtils.setTimePicker (DownLoadProgress.this);
                    defDialog.dismiss ();

                }

                @Override
                public void onSpinnerSelect () {

                }
            });
        }else{

            defDialog= DialogCategorical.N_0671_s01 (DownLoadProgress.this,
                    R.string.download_progress_set_time
                    ,R.string.b_now_update
                    ,R.string.b_set_time,R.string.cancel);
            defDialog.setOkClickListener (new OnOkListener () {
                @Override
                public void onOkKey () {

                    CommonUtils.showUpdateNowDialog (DownLoadProgress.this
                            ,new File (DownloadService.DOWNLOAD_PATH));
                    defDialog.dismiss ();

                }

                @Override
                public void onCenterKey () {

                    CommonUtils.setTimePicker (DownLoadProgress.this);
                    defDialog.dismiss ();

                }

                @Override
                public void onCancelKey () {


                    final Intent intent = new Intent();
                    intent.setClass(DownLoadProgress.this, MainActivity.class);
                    startActivity(intent);
                    defDialog.dismiss ();

                }

                @Override
                public void onSpinnerSelect () {

                }
            });
        }

    }

    private void startDownload() {

        HttpClient.cancleRequest(true);
        initialProgress();
        UpdateUtil.insertEventLog(getApplicationContext (),
                0, getString(R.string.download),
                0, getString(R.string.start),
                null, null);
        Message message = new Message();
        message.what = INT_DOWNLOAD_UPDATE_FILE;
        mhandler.removeMessages(INT_DOWNLOAD_UPDATE_FILE);
        mhandler.sendMessage(message);

    }

    private void initialProgress() {

        hadDownload = UpdateUtil.getFileLength(CommonUtils.DOWNLOAD_PATH);
        long total = spref.getLong("total_size", 0);
        if(hadDownload!= 0 && total!=0){
            int count = (int) ((hadDownload * 1.0 /total ) * 100);
            pb.setProgress(count);
            Log.d(TAG,"hadDownload  count= " + String.valueOf(count));
        }else{
            pb.setProgress(0);
        }

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
        return size;
    }

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

}