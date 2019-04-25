package com.nfp.update;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";
    //初始化
    private static final int MSG_INIT = 0;
    //开始下载
    public static final String ACTION_START = "ACTION_START";
    //暂停下载
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    //结束下载
    public static final String ACTION_FINISHED = "ACTION_FINISHED";
    //更新UI
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    //下载路径
    //public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wesker";

	public static String DOWNLOAD_PATH = "/cache";
	
    private DownloadTask mDownloadTask = null;

    private Intent intent;

    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId) {

        this.intent=intent;
        //获得Activity传来的参数
        if (ACTION_START.equals(intent.getAction())) {

            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            android.util.Log.e(TAG, "onStartCommand: ACTION_START-" + fileInfo.toString());
            new DownloadService.InitThread(fileInfo).start();

        } else if (ACTION_PAUSE.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            android.util.Log.e(TAG, "onStartCommand:ACTION_PAUSE- " + fileInfo.toString());
            if (mDownloadTask != null) {
                mDownloadTask.isPause = true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        return null;
    }


    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            android.util.Log.e(TAG, "fileinfo.toString()");

            switch (msg.what) {
                case MSG_INIT:
                    FileInfo fileinfo = (FileInfo) msg.obj;
                    Log.e(TAG, fileinfo.toString());
                    //启动下载任务
                    mDownloadTask = new DownloadTask(DownloadService.this, fileinfo);
                    mDownloadTask.download();
                    break;
            }
        }
    };

    /**
     * 初始化 子线程
     */
    class InitThread extends Thread {
        private FileInfo tFileInfo;

        public InitThread(FileInfo tFileInfo) {
            this.tFileInfo = tFileInfo;
        }

        @Override
        public void run() {
            java.net.HttpURLConnection conn = null;
            java.io.RandomAccessFile raf = null;
            try {
                //连接网络文件
                URL url = new URL(tFileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
				conn.setReadTimeout(25000);
                conn.setRequestMethod("GET");
                int length = -1;
                Log.e(TAG, conn.getResponseCode() + "");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //获取文件长度
                    length = conn.getContentLength();
                    android.util.Log.e(TAG, length + "");
                }
                if (length < 0) {
                    return;
                }

				boolean b = MD5Util.chmod755(DownloadService.DOWNLOAD_PATH);
				Log.e(TAG, "chmodd 755 : " + b);
                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    boolean mMkdir = dir.mkdir();
                    Log.d(TAG,"mMkdir : " + mMkdir);
                }
                //在本地创建文件
                File file = new File(dir, tFileInfo.getFileName());
               /* file.createNewFile();*/
                file.setWritable(Boolean.TRUE);
                file.setReadable (Boolean.TRUE);
                Process p;
                p = Runtime.getRuntime().exec("chmod 777 " + file);
                p = Runtime.getRuntime().exec("chmod 777 " + file.getParentFile());
                int status = p.waitFor();

                raf = new java.io.RandomAccessFile(file, "rwd");
                //设置本地文件长度
                raf.setLength(length);
                tFileInfo.setLength(length);
                android.util.Log.e(TAG, tFileInfo.getLength() + "");
                mHandler.obtainMessage(MSG_INIT, tFileInfo).sendToTarget();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null && raf != null) {
                        raf.close();
                        conn.disconnect();
                    }
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
