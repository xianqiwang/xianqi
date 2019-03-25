package com.nfp.update.polling;

import java.io.File;
import java.io.FileInputStream;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.os.Message;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import com.nfp.update.DownloadProgress;
import com.nfp.update.PrepareUpdateActivity;
import com.nfp.update.LessVolumeActivity;
import com.nfp.update.HttpClient;
import com.nfp.update.UpdateUtil;
import com.nfp.update.DebugReceiver;
import com.nfp.update.R;

public class PollingService extends Service {

    private static final String TAG = "PollingService";

    public static final String DEFAULT_FILE = "/storage/emulated/0/software.dat";
    public static final String FOTA_FILE = "/fota/softwareupdate.dat";
    private final static String CONFIR_UPDATE_FILE = "confirm.cgi";
    private final static String DOWNLOAD_UPDATE_FILE = "download.cgi";
    private static String TEST = "?VER=SII%20602SI%20v001%20/l001%20356475080000000%2000000001234%20000000000001234%20001%20B162";
    private long hadDownload = 0;
    private static Context context;
    private SharedPreferences sp;

    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    public static Context getInstance(){
        return context;
    }

    public PollingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "PollingService -> onCreate");
        context = PollingService.this;
        try{
            TEST = UpdateUtil.getTestVersion(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.FULL_WAKE_LOCK, "aquireCPUrunning");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "PollingService -> onStart()");

        /*if(intent==null){
            return;
        }*/

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor pEdits = sp.edit();
        pEdits.putInt("pol_switch",1);
        pEdits.commit();

        if(sp.getInt("pol_service", 0)==2||intent==null){
            stopSelf();
            return;
        }

        String packageFile = sp.getString("PAC_NAME", null);
        File files = new File("/fota/softwareupdate.dat");
        if(packageFile == null||!files.exists()){
            if (UpdateUtil.hasSimCard(PollingService.this)){
                if (UpdateUtil.checkNetworkConnection()){
                    if (UpdateUtil.getAvailableInternalMemorySize() > 100){
                        UpdateUtil.judgePolState(context, 1);
                        Message message = new Message();
                        message.what = 0;
                        mhandler.sendMessage(message);
                    } else {
                        UpdateUtil.showFotaNotification(context, R.string.Notification_storage_low, 0);
                        stopSelf();
                    }
                } else {
                    Intent mIntent = new Intent();
                    mIntent.setAction("android.intent.action.RETRY");
                    context.sendBroadcast(mIntent);
                }
            } else {
                UpdateUtil.showFotaNotification(context, R.string.Notification_no_simcard, 0);
                stopSelf();
            }
        }
    }

    private Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Log.d(TAG, "HttpClient.get()  -> confir");
                    HttpClient.get(context, CONFIR_UPDATE_FILE + TEST, null, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.d(TAG, "confir -> onSuccess()");
                            String error = new String(responseBody);
                            switch (error) {
                                case "error00":
                                    Log.d(TAG, "There is update file");
                                    if(!wakeLock.isHeld())
                                        wakeLock.acquire();

                                    insertEventLog(context,0, getString(R.string.polling_result), 0, getString(R.string.check_new_file), null, null);
                                    Message message = new Message();
                                    message.what = 1;
                                    mhandler.sendMessage(message);
                                    UpdateUtil.showFotaNotification(context, R.string.Notification_download_start, 0);
                                    if(wakeLock.isHeld())
                                        wakeLock.release();
                                    break;
                                case "error23":
                                    insertEventLog(context,0, getString(R.string.polling_result), 0, getString(R.string.check_old_file), null, null);
                                    Log.d(TAG, "This is the newest version!");
                                    break;
                                case "error24":
                                    Log.d(TAG, "There is no update file.");
                                    break;
                                case "error10":
                                    Log.d(TAG, "Agent Error");
                                    break;
                                case "error20":
                                    Log.d(TAG, "Method Error");
                                    break;
                                case "error21":
                                    Log.d(TAG, "IMEI Error");
                                    break;
                                case "error22":
                                    Log.d(TAG, "Index File Error");
                                    break;
                                case "error25":
                                    Log.d(TAG, "Update File Error");
                                    break;
                                case "error26":
                                    Log.d(TAG, "Log File Error");
                                    break;
                                case "error27":
                                    Log.d(TAG, "UE Error");
                                    break;
                                case "error28":
                                    Log.d(TAG, "CRC Error");
                                    break;
                                case "error29":
                                    Log.d(TAG, "Range Error (Range=0)");
                                    break;
                                case "error30":
                                    Log.d(TAG, "Group Number Error");
                                    break;
                                case "error31":
                                    Log.d(TAG, "Maker Name Error");
                                    break;
                                default:
                                    Log.d(TAG, "Other Errors");
                                    break;
                            }
                            if(!error.equals("error00")){
                                cyclePolling();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d(TAG, "confir -> onFailure()");
                            cyclePolling();
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {

                        }
                    });
                    break;
                case 1:
                    Log.d(TAG, "HttpClient.get()  -> download");
                    hadDownload = UpdateUtil.getFileLength(FOTA_FILE);
                    if(hadDownload > 0){
                        UpdateUtil.showFotaNotification(context, R.string.Notification_download_ing, 6);
                    }
                    HttpClient.get(context, DOWNLOAD_UPDATE_FILE + TEST, null, FOTA_FILE, new FileAsyncHttpResponseHandler(new File(FOTA_FILE), true) {
                        @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            Log.d(TAG,"download -> onSuccess()");
                            if(!wakeLock.isHeld())
                                wakeLock.acquire();
                            UpdateUtil.judgePolState(context, 0);
                            String fileName = null;
                            for (Header header : headers){
                                if (header.getName().equals("Content-disposition")
                                        || header.getName().equals("Content-Disposition")){
                                    String string = header.getValue();
                                    String str[] = string.split("=");
                                    fileName = str[1].substring(1,str[1].length() - 1);
                                }
                            }
                            if (UpdateUtil.getAvailableInternalMemorySize()*1024 < getFileSize(file)){
                                startLowVolume();
                            }else{
                                if (fileName != null && fileName.length() > 0){
                                    if (file != null && file.exists()){
                                        Log.d(TAG,"download Success -> move file success");
                                        UpdateUtil.stopPollingService(context);
                                        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(context);
                                        SharedPreferences.Editor editor = spref.edit();
                                        editor.putString("PAC_NAME", "softwareupdate.dat");
                                        editor.commit();
                                        prepareUpdate();
                                    }
                                }
                            }







                            if(wakeLock.isHeld())
                                wakeLock.release();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                            if(!wakeLock.isHeld())
                                wakeLock.acquire();
                            UpdateUtil.showFotaNotification(context, R.string.Notification_download_failed, 0);
                            UpdateUtil.judgePolState(context, 0);
                            if(wakeLock.isHeld())
                                wakeLock.release();
                            stopSelf();
                            if(sp.getInt("pol_switch",1)!=0){
                                UpdateUtil.stopPollingService(context);
                                UpdateUtil.startPollingService(context, UpdateUtil.getPollStartTime(context));
                            }
                            Log.d(TAG,"download -> onFailure()" + "  statusCode = "+ statusCode);
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            // TODO Auto-generated method stub
                            int count = (int) (((bytesWritten + hadDownload) * 1.0 / (totalSize + hadDownload)) * 100);
                            if(count ==1){
                                UpdateUtil.showFotaNotification(context, R.string.Notification_download_ing, 6);
                                Log.d(TAG," terminal is downloading ");
                            }else if(count ==100){
                                Log.d(TAG,"download : " + count + "%");
                            }
                        }
                    });
                    break;
            }
        };
    };

    private android.net.Uri insertEventLog(Context context, int eventNo, String eventName,
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

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    public void prepareUpdate() {
        SharedPreferences spref = this.getSharedPreferences("debug_comm", 0);
        if(spref.getInt("AUTO_UPDATE", 0) ==0){
            Log.d(TAG, " download complete ,start to new actvity");
            /*Intent intent = new Intent();
            intent.setClass(PollingService.this, PrepareUpdateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            UpdateUtil.startUpdateService(getApplicationContext(), 1);
            UpdateUtil.showFotaNotification(context, R.string.Notification_download_successed, 3);
        }else{
            UpdateUtil.showFotaNotification(context, R.string.Notification_download_successed, 1);
        }
        stopSelf();
    }





    public void cyclePolling() {
       UpdateUtil.judgePolState(context, 0);
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
       SharedPreferences.Editor pEdit = sharedPreferences.edit();
       pEdit.putBoolean("had_poll",true);

       if (sharedPreferences.getBoolean("is_retry", false)){
           UpdateUtil.stopPollingService(context);
           UpdateUtil.startPollingService(context, UpdateUtil.getPollStartTime(context));
           pEdit.putBoolean("is_retry",false);
       } else {
           UpdateUtil.stopPollingService(context);
           UpdateUtil.startPollingService(context, UpdateUtil.getCycleTime(context));
       }

       pEdit.commit();
    }

    public void startLowVolume() {
        Intent intent= new Intent(PollingService.this, LessVolumeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        UpdateUtil.judgePolState(context, 0);
    }
}
