package com.loopj.android.http.sample;

import android.util.Log;
import com.nfp.update.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class ResumeDownloadSample extends SampleParentActivity {

    private static final String LOG_TAG = "ResumeDownloadSample";
    private java.io.File downloadTarget;

    private java.io.File getDownloadTarget() {
        try {
            if (downloadTarget == null) {
                downloadTarget = java.io.File.createTempFile("download_", "_resume", getCacheDir());
            }
        } catch (java.io.IOException e) {
            android.util.Log.e(LOG_TAG, "Couldn't create cache file to download to");
        }
        return downloadTarget;
    }

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {
        return new com.loopj.android.http.RangeFileAsyncHttpResponseHandler(getDownloadTarget()) {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, java.io.File file) {
                debugStatusCode(LOG_TAG, statusCode);
                debugHeaders(LOG_TAG, headers);
                debugThrowable(LOG_TAG, throwable);
                if (file != null) {
                    addView(getColoredView(LIGHTGREEN, "Download interrupted (" + statusCode + "): (bytes=" + file.length() + "), path: " + file.getAbsolutePath()));
                }
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.io.File file) {
                debugStatusCode(LOG_TAG, statusCode);
                debugHeaders(LOG_TAG, headers);
                if (file != null) {
                    addView(getColoredView(LIGHTGREEN, "Request succeeded (" + statusCode + "): (bytes=" + file.length() + "), path: " + file.getAbsolutePath()));
                }
            }
        };
    }

    @Override
    public String getDefaultHeaders() {
        return "Range=bytes=10-20";
    }

    @Override
    public String getDefaultURL() {
        return PROTOCOL + "www.google.com/images/srpr/logo11w.png";
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return true;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return false;
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_resume_download;
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        return client.get(this, URL, headers, null, responseHandler);
    }
}
