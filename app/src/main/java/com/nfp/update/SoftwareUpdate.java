package com.nfp.update;

import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nfp.update.service.DownloadService;
import com.nfp.update.widget.CommonUtils;
import com.nfp.update.widget.CustomDialog;
import com.nfp.update.widget.DataCache;
import com.nfp.update.widget.HttpClient;
import com.nfp.update.widget.NetStatUtils;

import cz.msebera.android.httpclient.Header;

import static java.lang.Thread.sleep;


public class SoftwareUpdate extends Activity{
    private String TAG = "SoftwareUpdate";
    CustomDialog dialog_0642_D1;
    CustomDialog dialog_0642_D2;
    CustomDialog dialog_0642_D3;
    CustomDialog dialog_0642_D4;
    CustomDialog dialog_0642_D5;
    CustomDialog dialog_0642_D6;
    private static Context context;
    private Intent intent;
    private SharedPreferences spref;

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
    protected void onDestroy () {
        super.onDestroy ();
        dismissAll();
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
        connectServer();
    }

    public void connectServer(){

        dismissAll();
        dialog_0642_D1.show();
        if(NetStatUtils.hasNetWorkConnection(this)){
            dismissAll();
            dialog_0642_D4.show ();
        }

        HttpClient.get(context, CommonUtils.ServerUrlConfirmTwo, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                dismissAll();

                dialog_0642_D2.show();

                if(statusCode == 200){

                    String error = new String(responseBody);

                    switch (error){

                        case "error00":
                             dismissAll ();
                             if(UpdateUtil.getAvailableInternalMemorySize()<100){
                                 dialog_0642_D5.show ();
                             }else{

                                 dialog_0642_D6.show ();
                             }
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
                                break;
                    }

                }else{

                    HttpClient.cancleRequest(true);
                    UpdateUtil.judgePolState(context, 0);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                dismissAll ();
                dialog_0642_D4.show();
                String errorValue = String.valueOf(statusCode);
                String errorCode = "404";
                String a = context.getResources().getString(R.string.network_connect_error);
                String connectError = String.format(a, errorCode);
                HttpClient.cancleRequest(true);
                UpdateUtil.judgePolState(context, 0);

            }

        });

    }

    public void checkVersion(){
/*
        HandlerHttpServer.checkUpdate (this);
*/
    Intent intent=new Intent ();
    intent.setClass (this,DownLoadProgress.class);
    startActivity (intent);

    }

    private void initializateDialogFor0402(){

        Resources res = getResources();

        dialog_0642_D2= new CustomDialog.Builder(SoftwareUpdate.this,400,200)
                .setMessage(res.getString (R.string.check_up))
                .setSingleButton("Cancel", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        dialog_0642_D2.dismiss();
                    }

                }).setProgressBarVisible().createSingleButtonDialog();

        dialog_0642_D3= new CustomDialog.Builder(SoftwareUpdate.this,400,200)
                .setMessage(res.getString (R.string.up_to_date))
                .setSingleButton("OK", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        final Intent intent = new Intent();
                        intent.setClass(SoftwareUpdate.this, MainActivity.class);
                        startActivity(intent);
                    }

                }).createSingleButtonDialog();
        dialog_0642_D4= new CustomDialog.Builder(SoftwareUpdate.this,400,200)
                .setMessage(res.getString (R.string.Network_error))
                .setSingleButton("OK", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        final Intent intent = new Intent();
                        intent.setClass(SoftwareUpdate.this, MainActivity.class);
                        startActivity(intent);
                    }

                }).createSingleButtonDialog();

        dialog_0642_D5= new CustomDialog.Builder(SoftwareUpdate.this,400,200)
                .setMessage(res.getString (R.string.no_space))
                .setSingleButton("OK", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        final Intent intent = new Intent();
                        intent.setClass(SoftwareUpdate.this, MainActivity.class);
                        startActivity(intent);
                    }

                }).createSingleButtonDialog();
        dialog_0642_D6= new CustomDialog.Builder(SoftwareUpdate.this,400,200)
                .setMessage(res.getString (R.string.soft_download))
                .setPositiveButton("Ok", new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {

                        try{

                            checkVersion();
                            dialog_0642_D6.dismiss ();


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

        dialog_0642_D1= new CustomDialog.Builder(SoftwareUpdate.this,400,200)
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

    private void dismissAll(){
        dialog_0642_D1.dismiss ();
        dialog_0642_D2.dismiss ();
        dialog_0642_D3.dismiss ();
        dialog_0642_D4.dismiss ();
        dialog_0642_D5.dismiss ();
        dialog_0642_D6.dismiss ();
    }
}