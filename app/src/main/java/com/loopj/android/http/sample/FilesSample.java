package com.loopj.android.http.sample;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import com.nfp.update.R;

public class FilesSample extends com.loopj.android.http.sample.PostSample {

    public static final String LOG_TAG = "FilesSample";

    @Override
    public int getSampleTitle() {
        return R.string.title_post_files;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return false;
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        try {
            com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
            final String contentType = com.loopj.android.http.RequestParams.APPLICATION_OCTET_STREAM;
            params.put("fileOne", createTempFile("fileOne", 1020), contentType, "fileOne");
            params.put("fileTwo", createTempFile("fileTwo", 1030), contentType);
            params.put("fileThree", createTempFile("fileThree", 1040), contentType, "customFileThree");
            params.put("fileFour", createTempFile("fileFour", 1050), contentType);
            params.put("fileFive", createTempFile("fileFive", 1060), contentType, "testingFileFive");
            params.setHttpEntityIsRepeatable(true);
            params.setUseJsonStreamer(false);
            return client.post(this, URL, params, responseHandler);
        } catch (java.io.FileNotFoundException fnfException) {
            android.util.Log.e(LOG_TAG, "executeSample failed with FileNotFoundException", fnfException);
        }
        return null;
    }

    public java.io.File createTempFile(String namePart, int byteSize) {
        try {
            java.io.File f = java.io.File.createTempFile(namePart, "_handled", getCacheDir());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
            java.util.Random r = new java.util.Random();
            byte[] buffer = new byte[byteSize];
            r.nextBytes(buffer);
            fos.write(buffer);
            fos.flush();
            fos.close();
            return f;
        } catch (Throwable t) {
            android.util.Log.e(LOG_TAG, "createTempFile failed", t);
        }
        return null;
    }
}
