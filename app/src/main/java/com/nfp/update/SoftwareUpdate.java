package com.nfp.update;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentValues;
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
    private TextView mTextView;
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
    private static String TEST = "?VER=SII%20901SI%20v000%20/l000%20123456788103254%2000000001234%20000000000001234%20001%206259";
    private final static int INT_CONFIR_UPDATE_FILE = 0x01;
    private final static int INT_REQUEST_UPDATE_FILE = 0x02;
    private final static int INT_DOWNLOAD_UPDATE_FILE = 0x03;
    private static Context context;
    private Intent intent;
    private Handler mhandler;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void dismissAll(){
        dialog_0642_D1.dismiss ();
        dialog_0642_D2.dismiss ();
        dialog_0642_D3.dismiss ();
        dialog_0642_D4.dismiss ();
        dialog_0642_D5.dismiss ();
        dialog_0642_D6.dismiss ();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SoftwareUpdate.this;
        intent = new Intent();
        DataCache.getInstance(this).setDownloadPath(DownloadService.DOWNLOAD_PATH);
        setContentView(R.layout.software_update);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        spref = PreferenceManager.getDefaultSharedPreferences(this);
        initializateDialogFor0402();

        mhandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case INT_CONFIR_UPDATE_FILE:
                        dismissAll();
                        dialog_0642_D1.show();
                        if(UpdateUtil.checkNetworkConnection ()){
                            dismissAll();
                            dialog_0642_D4.show ();
                        }
                        HttpClient httpClient=new HttpClient ();
                        httpClient.get(context, CommonUtils.ServerUrlConfirm, null, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                dismissAll();
                                dialog_0642_D2.show();
                                String error1 = new String(responseBody);
                                Log.d("yingbo","confirm latest sw situation:"+statusCode+"--error--"+error1);

                                if(statusCode == 200){

                                    String error = new String(responseBody);

                                    switch (error){

                                        case "error00":
                                            dismissAll ();
                                            dialog_0642_D6.show ();
                                            break;

                                        case "error23":
                                            dismissAll ();
                                            dialog_0642_D3.show ();
                                        case "error24":
                                            dismissAll ();
                                            dialog_0642_D3.show ();
                                            break;
                                        case "error10":
                                            break;
                                        case "error20":

                                            break;
                                        case "error21":
                                            break;
                                        case "error22":
                                            break;
                                        case "error25":
                                            break;
                                        case "error26":
                                            break;
                                        case "error27":
                                            break;
                                        case "error28":
                                            break;
                                        case "error29":
                                            break;
                                        case "error30":
                                            break;
                                        case "error31":
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

                                String errorValue = String.valueOf(statusCode);
                                String errorCode = "404";//UpdateUtil.autoGenericCode(errorValue, 3);
                                String a = context.getResources().getString(R.string.network_connect_error);
                                String connectError = String.format(a, errorCode);
                                HttpClient.cancleRequest(true);
                                UpdateUtil.judgePolState(context, 0);
                                dismissAll ();
                                dialog_0642_D4.show();
                            }

                            @Override
                            public void onProgress(long bytesWritten, long totalSize) {

                            }

                        });
                        break;
                }
            };
        };
    }

    public boolean checkDeviceStatus() {
        if(!UpdateUtil.hasSimCard(SoftwareUpdate.this)){

            return false;
        }else if(UpdateUtil.getAvailableInternalMemorySize()<100){

            return false;
        }else if(spref.getInt("pol_service", 0)==1){

            return false;
        }
        return true;

    }

    private FileInfo downloadFota() {

        String baseurl=HttpClient.TEST_URL;
        String url=baseurl;
        return new FileInfo(0, url, "news.apk", 0, 0);

    }

    public void checkVersion(){

        Intent intent = new Intent(this, DownloadService.class);
        intent.setAction(DownloadService.ACTION_START);
        intent.putExtra("fileinfo", downloadFota());

    }

    private Uri insertEventLog(Context context, int eventNo, String eventName,
                               int tid, String factor1, String factor2, String factor3) {

        final Uri uri = Uri.parse("content://com.ssol.eventlog/eventlog");

        ContentResolver mContentResolver=context.getContentResolver();

        mContentResolver.acquireContentProviderClient (uri);

        ContentValues values = new ContentValues ();

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

                    try{

                        checkVersion();

                    }catch(Exception e){
                        Log.e("AlertDialog  Exception:" , e.getMessage());
                    }

                }        })
                .setNegativeButton ("NO", new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        dialog_0642_D6.dismiss ();
                    }
                }).createTwoButtonDialog();
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

}