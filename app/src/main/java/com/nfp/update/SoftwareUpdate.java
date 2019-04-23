package com.nfp.update;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MenuItem;
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
import cz.msebera.android.httpclient.Header;
import java.io.File;

import static java.lang.Thread.sleep;


public class SoftwareUpdate extends Activity{
    private String TAG = "SoftwareUpdate";
    private String sDownload = "";
    private TextView mTextView;
    private ProgressBar progress;
    private AlertDialog mDialog = null;
    private SharedPreferences spref;
    private Activity activity;
    CustomDialog dialog_0642_D1;
    CustomDialog dialog_0642_D2;
    CustomDialog dialog_0642_D3;
    CustomDialog dialog_0642_D4;
    CustomDialog dialog_0642_D5;
    CustomDialog dialog_0642_D6;
    private final static String CONFIR_UPDATE_FILE = "confirm.cgi";
    private final static String DOWNLOAD_UPDATE_FILE = "download.cgi";
    private static String TEST = "?VER=SII 901SI v001 /l000 12345678 00000001234 000000000001234 001 12AB";
    private final static int INT_CONFIR_UPDATE_FILE = 0x01;
    private final static int INT_REQUEST_UPDATE_FILE = 0x02;
    private final static int INT_DOWNLOAD_UPDATE_FILE = 0x03;
    private static Context context;
    private Intent intent;

    private boolean isConnect = false;
    private boolean isDown = false;
    private boolean downResults = false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SoftwareUpdate.this;
        intent = new Intent();
        setContentView(R.layout.software_update);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mTextView = (TextView) findViewById(R.id.content);
        progress = (ProgressBar) findViewById(R.id.progress);
        spref = PreferenceManager.getDefaultSharedPreferences(this);
        //初始化dialog_0642_D1 dialog_0642_D2 dialog_0642_D3 dialog_0642_D4 dialog_0642_D5 dialog_0642_D6
        initializateDialogFor0402();
        new myThread().run();
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            progress.setVisibility(View.GONE);
            downResults = bundle.getBoolean("download_results");
            downloadComplete(downResults);
        }else{
            try{
                TEST = UpdateUtil.getTestVersion(SoftwareUpdate.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
         new Thread(new Runnable() {
                @Override
                public void run() {
                   // dialog_0642_D1.show();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dialog_0642_D1.dismiss();
                    connectServer();
                }
            }).start();

        }
    }

    public void checksucess(){

        dialog_0642_D2.show();
        dialog_0642_D3.show();
        dialog_0642_D5.show();
        dialog_0642_D6.show();

    }

    public boolean checkDeviceStatus() {
        if(!UpdateUtil.hasSimCard(SoftwareUpdate.this)){
            progress.setVisibility(View.GONE);
            mTextView.setText(R.string.unserted_card);
            return false;
        }else if(UpdateUtil.getAvailableInternalMemorySize()<100){
            progress.setVisibility(View.GONE);
            mTextView.setText(R.string.volume_insuffient);
            return false;
        }else if(spref.getInt("pol_service", 0)==1){
            progress.setVisibility(View.GONE);
            mTextView.setText(R.string.confirm_latest_sw);
            return false;
        }
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void downloadError() {
        activity = SoftwareUpdate.this;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }

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

    class myThread implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Message message = new Message();
                message.what = INT_CONFIR_UPDATE_FILE;
                mhandler.removeMessages(INT_CONFIR_UPDATE_FILE);
                mhandler.sendMessage(message);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    private FileInfo downloadFota() {

        String baseurl=HttpClient.TEST_URL;
        String filecode="61";
        String url=baseurl;
        return new FileInfo(0, url, "news.apk", 0, 0);

    }

    public void checkVersion(){

 /*       WtwdFotaServer mWtwdFotaServer = new WtwdFotaServer();
        mWtwdFotaServer.checkUpdate(MainActivity.this, "MT6739", "android7.1", "yk915", "ddd", 1);
*/
        DataCache.getInstance(this).setDownloadPath(DownloadService.DOWNLOAD_PATH);
        Intent intent = new Intent(this, DownloadService.class);
        intent.setAction(DownloadService.ACTION_START);
        intent.putExtra("fileinfo", downloadFota());
        startService(intent);

    }

    private Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INT_CONFIR_UPDATE_FILE:
                    dialog_0642_D1.show();
                    //   dialog_0642_D1.dismiss();
                    HttpClient httpClient=new HttpClient ();

                    httpClient.get(context, CONFIR_UPDATE_FILE + TEST, null, new AsyncHttpResponseHandler() {

                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            dialog_0642_D1.dismiss();
                            dialog_0642_D2.show();
                            String error1 = new String(responseBody);
                            Log.d("yingbo","confirm latest sw situation:"+statusCode+"--error--"+error1);

                            if(statusCode == 200){

                                String error = new String(responseBody);

                                switch (error){

                                    case "error00":

                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
                                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                                        String packageFile = sp.getString("PAC_NAME", null);
                                        File files = new File("/fota/softwareupdate.dat");
                                        if(packageFile == null||!files.exists()){
                                            activity = SoftwareUpdate.this;
                                            while (activity.getParent() != null) {
                                                activity = activity.getParent();
                                            }

                                            try{

                                                checkVersion();

/*
                                                openConfirmDialog(1);
*/

                                            }catch(Exception e){
                                                Log.e("AlertDialog  Exception:" , e.getMessage());
                                            }

                                        }else{
                                            prepareUpdate();
                                        }
                                        break;

                                    case "error23":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
                                        dialog_0642_D3.show ();

                                    case "error24":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
                                        dialog_0642_D3.show ();


                                        break;
                                    case "error10":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);

                                        break;
                                    case "error20":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);

                                        break;
                                    case "error21":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);

                                        break;
                                    case "error22":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);

                                        break;
                                    case "error25":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);

                                        break;
                                    case "error26":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);

                                        break;
                                    case "error27":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
                                        break;
                                    case "error28":
                                        // dialog_0642_D2.dismiss();
                                        //  dialog_0642_D2.dismiss();
                                        //    insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);

                                        break;
                                    case "error29":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);

                                        break;
                                    case "error30":
                                        insertEventLog(context,0, context.getString(R.string.update_result), 0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);

                                        break;
                                    case "error31":
                                        insertEventLog(context,0, context.getString(R.string.update_result),
                                                0, context.getString(R.string.fail), context.getString(R.string.ver_result), error);
                                        Log.d(TAG,error);
                                        break;
                                    default:
                                        insertEventLog(context,0, context.getString(R.string.update_result),
                                                0, context.getString(R.string.fail), context.getString(R.string.ver_result), "other");
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
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            progress.setVisibility(View.GONE);


                            String errorValue = String.valueOf(statusCode);
                            String errorCode = "404";//UpdateUtil.autoGenericCode(errorValue, 3);
                            String a = context.getResources().getString(R.string.network_connect_error);
                            String connectError = String.format(a, errorCode);
                            HttpClient.cancleRequest(true);
                            UpdateUtil.judgePolState(context, 0);
                            //  mTextView.setText(connectError);
                            dialog_0642_D1.dismiss();
                            dialog_0642_D4.show();
                    /*        Resources res = getResources();
                            CustomDialog mDialog= new CustomDialog.Builder(SoftwareUpdate.this,200,200)
                                    .setMessage(res.getString (R.string.Network_error))
                                    .setSingleButton("Ok", new View.OnClickListener () {

                                        @Override
                                        public void onClick (View v) {
                                            final Intent intent = new Intent();
                                            intent.setClass(SoftwareUpdate.this, MainActivity.class);
                                            startActivity(intent);
                                        }

                                    }).createSingleButtonDialog();
                            mDialog.show ();*/
                            Log.d("yingbo","confir failed "+errorValue);
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            Log.v ("yingbo","totalSize"+totalSize);
                        }

                    });
                    break;
                case INT_REQUEST_UPDATE_FILE:
                    if(UpdateUtil.getAvailableInternalMemorySize()<100){
                        dialog_0642_D5.show();
                    }else{
                        //服务器有版本:
                        dialog_0642_D3.show();
                        //没版本当前是最新的:
                        dialog_0642_D6.show();
                    }
                    break;
                case INT_DOWNLOAD_UPDATE_FILE:

                    break;
            }
        };
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean downResult = false;
        if(resultCode==1){
            downResult = data.getBooleanExtra("download_result", false);
            downloadComplete(downResult);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void downloadComplete(boolean result) {
        if(result){
            prepareUpdate();
        }else{
            downloadError();
        }
    }


    public void connectServer() {
        if (true) {
            Message message = new Message();
            message.what = INT_CONFIR_UPDATE_FILE;
            mhandler.removeMessages(INT_CONFIR_UPDATE_FILE);
            mhandler.sendMessage(message);
        }
    }

    private Uri insertEventLog(Context context, int eventNo, String eventName,
                               int tid, String factor1, String factor2, String factor3) {

        final Uri uri = Uri.parse("content://com.ssol.eventlog/eventlog");

        ContentResolver mContentResolver=context.getContentResolver();

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

    private void initializateDialogFor0402(){

        Resources res = getResources();
        //dialog_0642_D2
        dialog_0642_D2= new CustomDialog.Builder(SoftwareUpdate.this,200,200)
                .setMessage(res.getString (R.string.check_up))
                .setSingleButton("Cancel", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        /*final Intent intent = new Intent();
                        intent.setClass(SoftwareUpdate.this, MainActivity.class);
                        startActivity(intent);*/
                        dialog_0642_D2.dismiss();
                    }

                }).createSingleButtonDialog();
        // dialog_0642_D3
        dialog_0642_D3= new CustomDialog.Builder(SoftwareUpdate.this,200,200)
                .setMessage(res.getString (R.string.up_to_date))
                .setSingleButton("OK", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        final Intent intent = new Intent();
                        intent.setClass(SoftwareUpdate.this, MainActivity.class);
                        startActivity(intent);
                    }

                }).createSingleButtonDialog();
        dialog_0642_D4= new CustomDialog.Builder(SoftwareUpdate.this,200,200)
                .setMessage(res.getString (R.string.Network_error))
                .setSingleButton("OK", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        final Intent intent = new Intent();
                        intent.setClass(SoftwareUpdate.this, MainActivity.class);
                        startActivity(intent);
                    }

                }).createSingleButtonDialog();

        dialog_0642_D5= new CustomDialog.Builder(SoftwareUpdate.this,200,200)
                .setMessage(res.getString (R.string.no_space))
                .setSingleButton("OK", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        final Intent intent = new Intent();
                        intent.setClass(SoftwareUpdate.this, MainActivity.class);
                        startActivity(intent);
                    }

                }).createSingleButtonDialog();
        dialog_0642_D6= new CustomDialog.Builder(SoftwareUpdate.this,200,200)
                .setMessage(res.getString (R.string.soft_download))
                .setPositiveButton("Ok", new View.OnClickListener () {            @Override
                public void onClick (View v) {
                    final Intent intent = new Intent();
                    intent.setClass(SoftwareUpdate.this, DownloadProgress.class);
                    startActivity(intent);

                }        })
                .setNegativeButton ("NO", new View.OnClickListener () {
                    @Override            public void onClick (View v) {                dialog_0642_D6.dismiss ();            }        }).createTwoButtonDialog();
        //dialog_0642_D6.show();


        dialog_0642_D1= new CustomDialog.Builder(SoftwareUpdate.this,200,200)
                .setMessage(res.getString (R.string.Conn_ser))
                .setSingleButton("Cancel", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        final Intent intent = new Intent();
                        intent.setClass(SoftwareUpdate.this, MainActivity.class);
                        startActivity(intent);
                        finish ();
                    }

                }).createSingleButtonDialog();

    }
    @RequiresApi (api = VERSION_CODES.JELLY_BEAN_MR1)
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
        SharedPreferences.Editor pEdits = spref.edit();
        pEdits.putInt("click_yes",0);
        pEdits.commit();
        AlertDialog.Builder mbuild = new AlertDialog.Builder(activity)
                .setTitle(confirmation)
                /*  .setIcon(com.android.internal.R.drawable.ic_dialog_confirm)*/
                .setMessage(messages)
                .setNegativeButton(no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(identity==1){
                                    finish();
                                }else if(identity==2){
                                    Intent intent = new Intent("android.intent.action.MAIN");
                                    intent.setClassName("com.android.launcher3","com.android.launcher3.Launcher");
                                    startActivity(intent);
                                    //ActivityManagerUtil.setEndKeyBehavior(SoftwareUpdate.this, ActivityManagerUtil.ENDKEY_FINISH_TASK);
                                }
                            }
                        }
                )
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
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
                        }
                )
                .setOnDismissListener(new DialogInterface.OnDismissListener (){
                    @Override
                    public void onDismiss(DialogInterface dialog){
                        if(spref.getInt("click_yes",0)!=1 || identity!=1)
                            finish();
                    }
                });
        mDialog = mbuild.create();
        mDialog.show();
    }

}