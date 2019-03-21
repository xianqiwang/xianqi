package com.nfp.update;

import com.nfp.update.nfpapp.app.core.Service2;

public class WtwdReceive extends android.content.BroadcastReceiver {
    private static final String TAG = "WtwdReceive";
    private static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private static final String CHECK_UPDATE = "com.wtwd.action.checkupdate";
	private static final String DOWNLOAD_SUCCESS = "com.wtwd.action.download.success";
	private static final String NETWORK_STATUS_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
	
    @Override
    public void onReceive(final android.content.Context context, android.content.Intent intent) {
        android.util.Log.e(TAG, "Receive Action --> " + intent.getAction());
        if (intent.getAction().equals(BOOT_COMPLETED)) {
            //first in, get random time of week
            if(DataCache.getInstance(context).isFirstIn()) {
                java.util.Random mRandom = new java.util.Random();
                int dayOfWeek = mRandom.nextInt(7) + 1;
                int hourOfDay = mRandom.nextInt(7);
                int minuteOfDay = mRandom.nextInt(60);
                StringBuilder timeBuilder = new StringBuilder();
                timeBuilder.append(dayOfWeek);
                timeBuilder.append("_");
                timeBuilder.append(hourOfDay);
                timeBuilder.append("_");
                timeBuilder.append(minuteOfDay);
                DataCache.getInstance(context).setDeviceUpdateTime(timeBuilder.toString());
                DataCache.getInstance(context).setFirstInfalse();
            }
            android.content.Intent i = new android.content.Intent(context, Service2.class);
            context.startService(i);
        } else if (intent.getAction().equals(CHECK_UPDATE)) {
            boolean mIsAlarm = intent.getBooleanExtra("isAlarm", false);
            if (false) {
                String mDeviceUpdateTime = DataCache.getInstance(context).getDeviceUpdateTime();
                if (!CommonUtils.isBlank(mDeviceUpdateTime)) {
                    String[] mSplit = mDeviceUpdateTime.split("_");
                    int dayOfWeek = Integer.parseInt(mSplit[0]);
                    int hourOfDay = Integer.parseInt(mSplit[1]);
                    int minute = Integer.parseInt(mSplit[2]);
                    java.util.Calendar mCalendar = java.util.Calendar.getInstance();
                    int week = mCalendar.get(java.util.Calendar.DAY_OF_WEEK);
                    int hour = mCalendar.get(java.util.Calendar.HOUR_OF_DAY);
                    int min = mCalendar.get(java.util.Calendar.MINUTE);
                    if (dayOfWeek == week && hourOfDay == hour && minute == min) {
                        WtwdFotaServer mWtwdFotaServer = new WtwdFotaServer();
                        mWtwdFotaServer.checkUpdate(context, "MT6739", "android7.1", "yk915", "ddd", 1);
                    }
                }
            } else {
                //Connect server to see if there is any update
                WtwdFotaServer mWtwdFotaServer = new WtwdFotaServer();
				String customer = com.nfp.update.PropertyUtils.get("ro.wtwd_fota_customer","unkonwn");
				int build = Integer.parseInt(com.nfp.update.PropertyUtils.get("ro.wtwd_fota_build","unknown"));
                String mBoard = android.os.Build.BOARD;
                String mHardware = android.os.Build.HARDWARE;
                String mRelease = android.os.Build.VERSION.RELEASE;
                android.util.Log.e(TAG, "mHardware : " + mHardware + "mRelease : " + mRelease + "---board : " + mBoard + "customer : " + customer + "---build : " + build );
                if (CommonUtils.isBlank(customer) || CommonUtils.isBlank(build + "") || CommonUtils.isBlank(mBoard) ||
                        CommonUtils.isBlank(mHardware) || CommonUtils.isBlank(mRelease)) {
                    android.util.Log.e(TAG, "CheckUpdate Fail! System parameter is not correct!");
                    return;
                }
                mWtwdFotaServer.checkUpdate(context, mHardware, mRelease, mBoard, customer, build);
            }
        } else if (DOWNLOAD_SUCCESS.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished", 0);
                String fingerprint = intent.getStringExtra("fingerprint");
                final String md5 = intent.getStringExtra("md5");
                boolean isComplete = intent.getBooleanExtra("complete", false);
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
                if (isComplete) {
                    final java.io.File file = new java.io.File(DownloadService.DOWNLOAD_PATH + "/" + fileInfo.getFileName());
                    try {
                        String mMd5ByFile = MD5Util.getMd5ByFile(file);
                        if (md5.equals(mMd5ByFile)) {
                            android.util.Log.e(TAG,"文件MD5校验成功");
                            CommonUtils.showUpdateNowDialog(context, file);
                        }
                    } catch (java.io.FileNotFoundException mE) {
                        mE.printStackTrace();
                    }
                }
        }else if(NETWORK_STATUS_CHANGE.equals(intent.getAction())) {
			int type = NetStatUtils.getNetWorkConnectionType(context);
			if (type != -1) {
				android.util.Log.e(TAG, "net type change : " + type);
			} else {
				CommonUtils.showToastInService(context,R.string.toast_network_unavailable);
			}
		}
    }
    private void updateFirmware(android.content.Context mContext, java.io.File packageFile) {
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
