package com.nfp.update;

public class HandlerHttpServer {

    private static final String TAG = "HandlerHttpServer";

    private static final String BASE_URL = "http://static3.iyuba.cn/android/apk/news/news.apk";

    private android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    public  void checkUpdate(final android.content.Context mContext, String hardware, String system, String board, String customer, int build) {

        if (NetStatUtils.getNetWorkConnectionType(mContext) == -1) {
            android.util.Log.e(TAG, "The NetWorkConnection is incorrect");
            CommonUtils.showToastInService(mContext,R.string.toast_network_unavailable);
            return ;
        }

        if (CommonUtils.isBlank(hardware) || CommonUtils.isBlank(system) ||
                CommonUtils.isBlank(board) || CommonUtils.isBlank(customer) || CommonUtils.isBlank(build + "")) {
            android.util.Log.e(TAG, "The parameter is incorrect");
            return ;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                                String mDownloadPath = DataCache.getInstance(mContext).getDownloadPath();

                                java.io.File mFile = CommonUtils.fileIsExists(mDownloadPath, "update_" + downloadFota()+ ".zip");
                                if (mFile != null) {
                                    CommonUtils.showUpdateNowDialog(mContext, mFile);
                                } else {

                                    if (NetStatUtils.getNetWorkConnectionType(mContext) == -1) {
                                        CommonUtils.showToastInService(mContext, R.string.toast_network_unavailable);
                                        return;
                                    } else if (CommonUtils.readSystem() > 100) {
                                        DownloadService.DOWNLOAD_PATH = "/data";
                                    } else if (CommonUtils.readSDCard() > 100) {
                                        DownloadService.DOWNLOAD_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/system_update";
                                    } else {
                                        CommonUtils.showToastInService(mContext, R.string.toast_storage_not_enough);
                                        DataCache.getInstance(mContext).setDownloadPath("null");
                                        return;
                                    }

                                    DataCache.getInstance(mContext).setDownloadPath(DownloadService.DOWNLOAD_PATH);
                                    android.content.Intent intent = new android.content.Intent(mContext, DownloadService.class);
                                    intent.setAction(DownloadService.ACTION_START);
                                    intent.putExtra("fileinfo",downloadFota());
                                    mContext.startService(intent);

                                }

                        }
                    });
                }

        }).start();
    }


    private FileInfo downloadFota() {

        String baseurl="http://static3.iyuba.cn/android/apk/news/news.apk";
        String filecode="61";
        String url=baseurl;
        return new FileInfo(0, url, "news.apk", 0, 0);

    }






}
