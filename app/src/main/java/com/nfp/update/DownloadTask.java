package com.nfp.update;

import android.content.Context;

import com.nfp.update.service.DownloadService;
import com.nfp.update.widget.CommonUtils;
import com.nfp.update.widget.FileInfo;

import java.net.HttpURLConnection;

public class DownloadTask {
    private android.content.Context mContext = null;
    private FileInfo mFileInfo = null;
    private ThreadDAOImpl mThreadDAO = null;
    private long mFinished = 0;
    private static boolean is_Finish=false;
    public boolean isPause = false;
    public int progress_g;
    public static OnDownloadProgress downloadProgress;

    public DownloadTask(Context context, FileInfo mFileInfo) {
        this.mContext = context;
        this.mFileInfo = mFileInfo;
        mThreadDAO = new ThreadDAOImpl(mContext);
    }

    public void download() {
        //读取数据库的线程信息
        java.util.List<ThreadInfo> threadInfos = mThreadDAO.getThread(mFileInfo.getUrl());
        android.util.Log.e("threadsize==", threadInfos.size() + "");
        ThreadInfo info;
        if (threadInfos.size() == 0) {
            //初始化线程信息
            info = new ThreadInfo(0, mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
        } else {
            info = threadInfos.get(0);
        }
        //创建子线程进行下载
        new DownloadTask.DownloadThread(info).start();
    }

    interface OnDownloadProgress{
       void onDownloadProgress(int progress,boolean is_finish);
    }

    public static void setOnDownloadProgress(OnDownloadProgress onDownloadProgress){
        downloadProgress=onDownloadProgress;
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread {
        private ThreadInfo threadInfo;

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            httpDownLoad();
        }

        private void httpDownLoad() {
            //向数据库插入线程信息
            android.util.Log.e("isExists==", mThreadDAO.isExists(threadInfo.getUrl(), threadInfo.getId()) + "");
            java.net.HttpURLConnection connection;
            java.io.RandomAccessFile raf;
            java.io.InputStream is;
			java.io.File file = null;
            try {
                java.net.URL url = new java.net.URL(threadInfo.getUrl());
                connection = (java.net.HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
				connection.setReadTimeout(25000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty ("User-Agent","SB-901SI");
                connection.setRequestProperty("Charset", "UTF-8");
                connection.setRequestProperty(
                        "Accept",
                        "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                connection.setRequestProperty("Connection", "Keep-Alive");
                //设置下载位置
                long start = threadInfo.getStart() + threadInfo.getFinish();
                connection.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());
                android.content.Intent intent = new android.content.Intent(DownloadService.ACTION_UPDATE);
                mFinished += threadInfo.getFinish();
                android.util.Log.e("getResponseCode ===", connection.getResponseCode() + "");
                String fingerprint = connection.getHeaderField("fingerprint");
                String md5 = connection.getHeaderField("md5");
				file = new java.io.File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
				//!fingerprint.equals(Build.FINGERPRINT)
				if (false) {
					android.util.Log.e("DownloadTask", "fingerprint compare fail!");
					if (connection != null) {
						connection.disconnect();
					}
					CommonUtils.showToastInService(mContext, R.string.toast_fingerprint_error);
					if(file != null) {
					boolean isdelete = file.delete();
					android.util.Log.e("wesker","fingerprint fail delete file : " + isdelete);
					}
					return;
				}
                raf = new java.io.RandomAccessFile(file, "rwd");
                raf.seek(start);

                //开始下载
                if (connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {

                    android.util.Log.e("getContentLength==", connection.getContentLength() + "");
                    //读取数据
                    is = connection.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = is.read(buffer)) != -1) {

                        //下载暂停时，保存进度
                        if (isPause) {

                            android.util.Log.e("mfinished==", mFinished + "");
                            mThreadDAO.updateThread(mFileInfo.getUrl(), mFileInfo.getId(), mFinished);
                            return;
                        }

                        //写入文件
                        raf.write(buffer, 0, len);
                        //把下载进度发送广播给Activity
                        mFinished += len;
                        if (System.currentTimeMillis() - time > 1000) {//减少UI负载
                            time = System.currentTimeMillis();
                            intent.putExtra("finished", (int)(mFinished * 100 / mFileInfo.getLength()));
                            progress_g=(int)(mFinished * 100 / mFileInfo.getLength());
                            mContext.sendBroadcast(intent);
                            android.util.Log.e(" mFinished percent===", mFinished * 100 / mFileInfo.getLength() + "");
                            downloadProgress.onDownloadProgress ((int)(mFinished * 100 / mFileInfo.getLength()),is_Finish);
                        }

                    }

                    is_Finish=true;
					android.content.Intent completeIntent = new android.content.Intent();
					completeIntent.setAction("com.wtwd.action.download.success");
                    completeIntent.putExtra("complete", true);
                    completeIntent.putExtra("fingerprint", fingerprint);
                    completeIntent.putExtra("md5", md5);
					completeIntent.putExtra("fileinfo", mFileInfo);
					mContext.sendBroadcast(completeIntent);
                    //intent.putExtra("finished",100);
                    //intent.putExtra("complete", true);
                    //intent.putExtra("fingerprint", fingerprint);
                    //intent.putExtra("md5", md5);
					//intent.putExtra("fileinfo", mFileInfo);
                    //mContext.sendBroadcast(intent);
                    //删除线程信息
                    mThreadDAO.deleteThread(mFileInfo.getUrl(), mFileInfo.getId());
                    is.close();
                }
                raf.close();
                connection.disconnect();
            } catch (Exception e) {
				android.util.Log.e("WtwdReceive","download fail : " + e.toString());
				if(file != null) {
					boolean isdelete = file.delete();
					android.util.Log.e("wesker","download fail delete file : " + isdelete);
				}
				CommonUtils.showDownloadFailDialog(mContext);
                e.printStackTrace();
            }
//            finally {
//                try {
//                    if (connection != null && raf != null && is != null) {
//                        is.close();
//                        raf.close();
//                        connection.disconnect();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }

	}

}

