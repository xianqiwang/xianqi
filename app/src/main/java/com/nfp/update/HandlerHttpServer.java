package com.nfp.update;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.io.File;

public class HandlerHttpServer {

    private static final String TAG = "HandlerHttpServer";

    private static final String BASE_URL = "http://static3.iyuba.cn/android/apk/news/news.apk";

    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper());

    private Context context;

    public  void checkUpdate(final Context mContext, String hardware, String system, String board, String customer, int build) {
        context=mContext;
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

                                File mFile = CommonUtils.fileIsExists(mDownloadPath, CommonUtils.UpdateFileName);
                                if (mFile != null) {

                                    CommonUtils.showUpdateNowDialog(mContext, mFile);

                                } else {

                                    if (NetStatUtils.getNetWorkConnectionType(mContext) == -1) {
                                        CommonUtils.showToastInService(mContext, R.string.toast_network_unavailable);
                                        return;
                                    } else if (UpdateUtil.getAvailableInternalMemorySize()>100) {
                                        showDialog ();
                                    }

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

    public void showDialog(){
        Resources res=context.getResources ();

        CustomDialog   dialog_0642_D5= new CustomDialog.Builder(context,200,200)
                .setMessage(res.getString (R.string.no_space))
                .setSingleButton("OK", new View.OnClickListener () {

                    @Override
                    public void onClick (View v) {
                        final Intent intent = new Intent();
                        intent.setClass(context, MainActivity.class);
                        context.startActivity(intent);
                    }

                }).createSingleButtonDialog();

    }


    private FileInfo downloadFota() {

        String baseurl=CommonUtils.ServerUrlDownload;
        return new FileInfo(0, baseurl, "update.zip", 0, 0);

    }






}
