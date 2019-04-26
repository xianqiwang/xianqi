package com.nfp.update;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.io.File;

public class HandlerHttpServer {

    private static final String TAG = "HandlerHttpServer";

    private static Context context;
    private static Handler mHandler = new android.os.Handler(Looper.getMainLooper());
    public static void checkUpdate(final Context mContext) {
        context=mContext;
        if (NetStatUtils.getNetWorkConnectionType(mContext) == -1) {
            CommonUtils.showToastInService(mContext,R.string.toast_network_unavailable);
            return ;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                        @Override
                        public void run() {
/*                                String mDownloadPath = DataCache.getInstance(mContext).getDownloadPath();
                                File mFile = CommonUtils.fileIsExists(mDownloadPath, CommonUtils.UpdateFileName);
                                if (mFile != null) {

                                    CommonUtils.showUpdateNowDialog(mContext, mFile);

                                } else {*/

                                    if (NetStatUtils.getNetWorkConnectionType(mContext) == -1) {
                                        CommonUtils.showToastInService(mContext, R.string.toast_network_unavailable);
                                        return;
                                    }

                                    Intent intent = new Intent(mContext, DownloadService.class);
                                    intent.setAction(DownloadService.ACTION_START);
                                    intent.putExtra("fileinfo",
                                            new FileInfo(0, CommonUtils.ServerUrlDownloadTwo,
                                                    "update.zip", 0, 0));
                                    mContext.startService(intent);

                                /*}*/
                        }
                    });
                }

        }).start();
    }
}
