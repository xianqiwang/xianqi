package com.loopj.android.http.sample.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.sample.IntentServiceSample;
import com.loopj.android.http.sample.util.IntentUtil;

import cz.msebera.android.httpclient.Header;

public class ExampleIntentService extends android.app.IntentService {

    public static final String LOG_TAG = "ExampleIntentService:IntentServiceSample";
    public static final String INTENT_URL = "INTENT_URL";
    public static final String INTENT_STATUS_CODE = "INTENT_STATUS_CODE";
    public static final String INTENT_HEADERS = "INTENT_HEADERS";
    public static final String INTENT_DATA = "INTENT_DATA";
    public static final String INTENT_THROWABLE = "INTENT_THROWABLE";

    private final com.loopj.android.http.AsyncHttpClient aClient = new com.loopj.android.http.SyncHttpClient();

    public ExampleIntentService() {
        super("ExampleIntentService");
    }

    @Override
    public void onStart(android.content.Intent intent, int startId) {
        android.util.Log.d(LOG_TAG, "onStart()");
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(android.content.Intent intent) {
        if (intent != null && intent.hasExtra(INTENT_URL)) {
            aClient.get(this, intent.getStringExtra(INTENT_URL), new com.loopj.android.http.AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    sendBroadcast(new android.content.Intent(IntentServiceSample.ACTION_START));
                    android.util.Log.d(LOG_TAG, "onStart");
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    android.content.Intent broadcast = new android.content.Intent(IntentServiceSample.ACTION_SUCCESS);
                    broadcast.putExtra(INTENT_STATUS_CODE, statusCode);
                    broadcast.putExtra(INTENT_HEADERS, com.loopj.android.http.sample.util.IntentUtil.serializeHeaders(headers));
                    broadcast.putExtra(INTENT_DATA, responseBody);
                    sendBroadcast(broadcast);
                    android.util.Log.d(LOG_TAG, "onSuccess");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    android.content.Intent broadcast = new android.content.Intent(IntentServiceSample.ACTION_FAILURE);
                    broadcast.putExtra(INTENT_STATUS_CODE, statusCode);
                    broadcast.putExtra(INTENT_HEADERS, com.loopj.android.http.sample.util.IntentUtil.serializeHeaders(headers));
                    broadcast.putExtra(INTENT_DATA, responseBody);
                    broadcast.putExtra(INTENT_THROWABLE, error);
                    sendBroadcast(broadcast);
                    android.util.Log.d(LOG_TAG, "onFailure");
                }

                @Override
                public void onCancel() {
                    sendBroadcast(new android.content.Intent(IntentServiceSample.ACTION_CANCEL));
                    android.util.Log.d(LOG_TAG, "onCancel");
                }

                @Override
                public void onRetry(int retryNo) {
                    sendBroadcast(new android.content.Intent(IntentServiceSample.ACTION_RETRY));
                    android.util.Log.d(LOG_TAG, String.format("onRetry: %d", retryNo));
                }

                @Override
                public void onFinish() {
                    sendBroadcast(new android.content.Intent(IntentServiceSample.ACTION_FINISH));
                    android.util.Log.d(LOG_TAG, "onFinish");
                }
            });
        }
    }
}
