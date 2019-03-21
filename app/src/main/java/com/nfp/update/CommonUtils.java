package com.nfp.update;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.RecoverySystem;
import android.os.StatFs;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.content.Context.BATTERY_SERVICE;


public class CommonUtils {
    private static final String TAG = "CommonUtils";
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

    public static void showToast(android.content.Context mContext, int str) {
        android.widget.Toast.makeText(mContext, str, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void showToastInService(final android.content.Context mContext, final int str) {
        android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                android.widget.Toast.makeText(mContext, str, android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：com.beidian.test.service.BasicInfoService ）
     * @return
     */
    public static boolean isServiceWork(android.content.Context mContext, String serviceName) {
        boolean isWork = false;
        android.app.ActivityManager myAM = (android.app.ActivityManager) mContext
                .getSystemService(android.content.Context.ACTIVITY_SERVICE);
        java.util.List<android.app.ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
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

    public static int getBatteryPercent(android.content.Context mContext) {
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
        java.io.File root = android.os.Environment.getRootDirectory();
        android.util.Log.e("wesker", "root.getPath() : " + root.getPath());
        android.os.StatFs sf = new android.os.StatFs("/data");
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
        return availCount * blockSize;
    }

    //判断文件是否存在
    public static java.io.File fileIsExists(String path, String strFile) {
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
    public static void showUpdateNowDialog(final android.content.Context context, final java.io.File file) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Download Success");
        builder.setMessage("Update Now?");
        builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                //Execute System Update
                int mBatteryPercent = com.nfp.update.CommonUtils.getBatteryPercent(context);
                android.util.Log.d(TAG, "Current BatteryPercent --> " + mBatteryPercent);
                if (mBatteryPercent >= 30) {
                    updateFirmware(context, file);
                } else {
                    com.nfp.update.CommonUtils.showToastInService(context,R.string.toast_battery_low);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
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
    private static void updateFirmware(android.content.Context mContext, java.io.File packageFile) {
        if (!packageFile.exists()) {
            android.util.Log.e(TAG, "packageFile not exists");
            return;
        }
        try {
            android.os.RecoverySystem.installPackage(mContext,packageFile);
        } catch (java.io.IOException mE) {
            android.util.Log.e(TAG, "Install SystemUpdate Package failure");
            mE.printStackTrace();
        }
    }
}
