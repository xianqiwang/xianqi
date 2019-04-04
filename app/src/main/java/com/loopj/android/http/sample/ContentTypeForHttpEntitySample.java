package com.loopj.android.http.sample;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nfp.update.R;
import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class ContentTypeForHttpEntitySample extends com.loopj.android.http.sample.SampleParentActivity {
    private static final String LOG_TAG = "ContentTypeForHttpEntitySample";

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {
        return new com.loopj.android.http.TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                debugResponse(LOG_TAG, responseString);
                debugThrowable(LOG_TAG, throwable);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                debugResponse(LOG_TAG, responseString);
            }
        };
    }

    @Override
    public String getDefaultURL() {
        return "https://httpbin.org/post";
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
        return R.string.title_content_type_http_entity;
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        com.loopj.android.http.RequestParams rParams = new com.loopj.android.http.RequestParams();
        rParams.put("sample_key", "Sample String");
        try {
            java.io.File sample_file = java.io.File.createTempFile("temp_", "_handled", getCacheDir());
            rParams.put("sample_file", sample_file);
        } catch (java.io.IOException e) {
            android.util.Log.e(LOG_TAG, "Cannot add sample file", e);
        }
        return client.post(this, URL, headers, rParams, "multipart/form-data", responseHandler);
    }
}
