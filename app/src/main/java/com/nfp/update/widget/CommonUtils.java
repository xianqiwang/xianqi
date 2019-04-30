package com.nfp.update.widget;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.RecoverySystem;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nfp.update.DialogCategorical;
import com.nfp.update.MainActivity;
import com.nfp.update.R;
import com.nfp.update.UpdateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.BATTERY_SERVICE;


public class CommonUtils {

    private static int mProgress;
    public static final String UpdateFileName="update.zip";
    public static String DOWNLOAD_PATH = "/cache/";
    public static final String ServerUrlConfirm="http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/confirm.cgi?VER=SII%20901SI%20v000%20/l000%20123456788103254%2000000001234%20000000000001234%20001%206259";
    public static final String ServerUrlDownload="http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/download.cgi?VER=SII%20901SI%20v000%20/l000%20123456788103254%2000000001234%20000000000001234%20001%206259";
    public static final String ServerUrlConfirmOne="http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/confirm.cgi?VER=SII%20901SI%20v001%20/l000%20123456788103254%2000000001234%20000000000001234%20001%20476C";
    public static final String ServerUrlDownloadOne="http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/download.cgi?VER=SII%20901SI%20v001%20/l000%20123456788103254%2000000001234%20000000000001234%20001%20476C";
    public static final String ServerUrlConfirmTwo="http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/confirm.cgi?VER=SII%20901SI%20v002%20/l000%20123456788103254%2000000001234%20000000000001234%20001%202833";
    public static final String ServerUrlDownloadTwo="http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/download.cgi?VER=SII%20901SI%20v002%20/l000%20123456788103254%2000000001234%20000000000001234%20001%202833";
    public static final String ServerUrlConfirmThree="http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/confirm.cgi?VER=SII%20901SI%20v003%20/l000%20123456788103254%2000000001234%20000000000001234%20001%200D06";
    public static final String ServerUrlDownloadThree="http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/download.cgi?VER=SII%20901SI%20v003%20/l000%20123456788103254%2000000001234%20000000000001234%20001%200D06";

/*    v000/l000 ==> v001/l000 : dummy file
    v001/l000 ==> v002/l000 : update.zip (in Large folder)
    v002/l000 ==> v003/l000 : update.zip (in Small folder)*/
    private static final String TAG = "CommonUtils";
    private static CustomDialog mDialog_0641_D1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private static CustomDialog    customDialog;
    private static CustomDialog    mDialog_0670_D3;
    private static DialogCategorical dialogCategorical;

    public static boolean isBlank(String str) {
        
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static void setTimePicker(final Context context){

        int scheduleValue=1;
        final Calendar calendar = Calendar.getInstance();
        int hour =0;
        int minute =0;
        if(scheduleValue ==1){
            hour = UpdateUtil.getHourTemp(context);
            minute =UpdateUtil.getMinuteTemp(context);
        }else{
            hour = UpdateUtil.getHour(context);
            minute = UpdateUtil.getMinute(context);
        }
        TimeFotaDialog tp = new TimeFotaDialog (context, AlertDialog.THEME_HOLO_LIGHT, new TimeFotaDialog.OnTimeSetListener () {

            @Override
            public void onTimeSet (int hourOfDay, int minute) {
                UpdateUtil.setHourMinute (context,hourOfDay,minute);
                SharedPreferences sprefs = context.getSharedPreferences ("debug_comm", 0);

                SharedPreferences.Editor editor = sprefs.edit();
                editor.putInt("AUTO_UPDATE", 0);
                editor.commit();

                CommonUtils.showToastInService(context,R.string.auto_change);

            }

        }, hour, minute, true);
        Context context_1 = tp.getContext();

        int divierId = context_1.getResources().getIdentifier("android:id/titleDivider", null, null);

        tp.setTitle(context.getString (R.string.update_schedule_title));
        tp.show ();

    }


    public static String getUserAgent(Context context) {
        String userAgent = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = android.webkit.WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    public static boolean dateDiff(String Time) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            Date date = new Date(System.currentTimeMillis());
            String times_now=sd.format(date);
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(Time).getTime()
                    - sd.parse(times_now).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒

            if (day>=1) {
                return true;
            }else {
                if (day==0) {
                    return false;
                }else {
                    return false;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDateAndTime() {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String time = year + "/" + (month+1) + "月" + date + "日" +hour + ":" +minute + ":" + second;
        return (month+1) + "月" + date + "日";
    }

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void isUpdateFile(final Context context){
         final SharedPreferences spref = context.getSharedPreferences("debug_comm", 0);

                mDialog_0641_D1= new CustomDialog.Builder(context,400,250)
                        .setMessage(context.getResources ().getString (R.string.only_hand_update))
                        .setSingleButton(context.getResources ().getString (R.string.ok)
                                , new OnClickListener () {
                            @Override
                            public void onClick (View v) {
                                mDialog_0641_D1.dismiss ();
                            }
                        }).createSingleButtonDialog();
                mDialog_0641_D1.show ();

        HttpClient.get (context,ServerUrlConfirmTwo,null,new AsyncHttpResponseHandler (){

            @Override
            public void onSuccess (int statusCode, Header[] headers, byte[] responseBody) {

                String error = new String(responseBody);
                if(error.equals ("error00")){
                    if(spref.getInt ("AUTO_UPDATE",1)==0){



                    }
                }
                return;
            }

            @Override
            public void onFailure (int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                return;
            }

        });
    }




    public static void showToast(Context mContext, int str) {
        android.widget.Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

    public static void showToastInService(final android.content.Context mContext, final int str) {
        android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                android.widget.Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static boolean isServiceWork(android.content.Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(android.content.Context.ACTIVITY_SERVICE);
        java.util.List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public static int getBatteryPercent(Context mContext) {
        android.os.BatteryManager batteryManager = (android.os.BatteryManager) mContext.getSystemService(BATTERY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY);
        }
        return 0;
    }

    public static long readSDCard() {
        String state = android.os.Environment.getExternalStorageState();
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            java.io.File sdcardDir = android.os.Environment.getExternalStorageDirectory();
            android.os.StatFs sf = new android.os.StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();
            return availCount * blockSize;
        }
        return 0;
    }

    public static long readSystem() {
        File root = android.os.Environment.getRootDirectory();
        android.util.Log.e("wesker", "root.getPath() : " + root.getPath());
        android.os.StatFs sf = new android.os.StatFs("/data");
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
        return availCount * blockSize;
    }

    //判断文件是否存在
    public static File fileIsExists(String path, String strFile) {
        try {
            java.io.File f = new java.io.File(path, strFile);
            if (f.exists()) {
                return f;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static void is_delete(String path){

            File file = new File(path);
            if(file.exists()){
                file.delete();
            }
    }

    public static void showUpdateNowDialog(final Context context, final File file) {
       customDialog=  new CustomDialog.Builder(context,200,200)
                .setMessage(context.getResources ().getString (R.string.not_using_while_update))
                .setPositiveButton("Ok", new OnClickListener () {
                    @Override
                    public void onClick (View v) {

                        //Execute System Update
                        int mBatteryPercent = CommonUtils.getBatteryPercent(context);
                        android.util.Log.d(TAG, "Current BatteryPercent --> " + mBatteryPercent);
                        if (mBatteryPercent >= 30) {

                            installProgress(context);
                            updateFirmware(context, file);

                        } else {

                            mDialog_0670_D3= new CustomDialog.Builder(context,200,200)
                                    .setMessage(context.getResources ().getString (R.string.battery_insufficient))
                                    .setSingleButton("Ok", new OnClickListener () {
                                        @Override
                                        public void onClick (View v) {
                                            mDialog_0670_D3.dismiss ();
                                        }
                                    }).createSingleButtonDialog();
                            mDialog_0670_D3.show ();

                        }

                    }        }).setNegativeButton ("ON", new OnClickListener () {
                   @Override
                   public void onClick (View v) {
                       final Intent intent = new Intent();
                       intent.setClass(context, MainActivity.class);
                       context.startActivity (intent);
                       customDialog.dismiss ();

                   }
               })
                .createTwoButtonDialog ();
                customDialog.show ();

    }
    public static void showDownloadFailDialog(final android.content.Context context) {
		android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
		        builder.setTitle("Download Fail");
		        builder.setMessage("Make sure your network is already connected?");
		        builder.setPositiveButton("OK", null);
		        android.app.AlertDialog alertDialog = builder.create();
		        alertDialog.getWindow().setType(android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		        alertDialog.setCanceledOnTouchOutside(false);
		        alertDialog.show();
            }
        });
    }

    public static void copyFile(String oldPath, String newPath) {

        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (oldfile.exists()) { //文件存在时
                Log.e("lhc","exists"+oldfile.canRead()+oldfile.isFile());
                Log.e("lhc","exists1"+newfile.canWrite()+newfile.isFile());
                FileInputStream fileInputStream = new FileInputStream(oldPath);
                Log.e("lhc","exists2"+newfile.canWrite()+newfile.isFile());
                FileOutputStream fileOutputStream = new FileOutputStream(newPath);
                Log.e("lhc","exists3"+newfile.canWrite()+newfile.isFile());

                byte[] buffer = new byte[1024];
                int byteRead;
                while (-1 != (byteRead = fileInputStream.read(buffer))) {
                    fileOutputStream.write(buffer, 0, byteRead);
                }
                fileInputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();

            }
        }
        catch (Exception e) {
            //System.out.println("复制单个文件操作出错");
            Log.e("lhc",e.toString()

            );
            e.printStackTrace();

        }

    }
    private static void updateFirmware(Context mContext, File packageFile) {
        Log.v ("yingbo","updateFirmware"+packageFile.toString ());


        if (!packageFile.exists()) {
            android.util.Log.e(TAG, "packageFile not exists");
            return;
        }
        try {

            RecoverySystem.installPackage(mContext,packageFile);
        } catch (java.io.IOException mE) {
            Log.e(TAG, "Install SystemUpdate Package failure");
            mE.printStackTrace();
        }

    }

    public static int installProgress (final Context context){

        try {

            RecoverySystem.verifyPackage(new File ("/cache/update.zip"), new RecoverySystem.ProgressListener(){

                @Override
                public void onProgress (int progress) {

                    dialogCategorical.N_0646_S01 (context,progress, R.string.fota_install);

                }
            } , null);

        } catch (GeneralSecurityException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }

        return mProgress;
    }
}