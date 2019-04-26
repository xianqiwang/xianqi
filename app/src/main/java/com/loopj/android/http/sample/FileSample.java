package com.loopj.android.http.sample;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.sample.util.FileUtil;

import java.io.File;

import com.nfp.update.CommonUtils;
import com.nfp.update.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class FileSample extends SampleParentActivity {
    private static final String LOG_TAG = "FileSample";

    @Override
    public int getSampleTitle() {
        return R.string.title_file_sample;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return false;
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return true;
    }

    @Override
    public String getDefaultURL() {
        return CommonUtils.ServerUrlDownloadTwo;
    }

    @Override
    public ResponseHandlerInterface getResponseHandler() {
        return new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onStart() {
                clearOutputs();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.io.File response) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                debugFile(response);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, java.io.File file) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                debugThrowable(LOG_TAG, throwable);
                debugFile(file);
            }

            private void debugFile(java.io.File file) {
                if (file == null || !file.exists()) {
                    debugResponse(LOG_TAG, "Response is null");
                    return;
                }
                try {
                    debugResponse(LOG_TAG, file.getAbsolutePath() + "\r\n\r\n" + com.loopj.android.http.sample.util.FileUtil.getStringFromFile(file));
                } catch (Throwable t) {
                    android.util.Log.e(LOG_TAG, "Cannot debug file contents", t);
                }
                if (!deleteTargetFile()) {
                    android.util.Log.d(LOG_TAG, "Could not delete response file " + file.getAbsolutePath());
                }
            }
        };
    }

    @Override
    public RequestHandle executeSample(AsyncHttpClient client, String URL, Header[] headers, HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        client.setUserAgent ("SB-901SI");
        return client.get(this, URL, headers, null, responseHandler);
    }
}
