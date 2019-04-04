package com.loopj.android.http.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.nfp.update.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.sample.services.ExampleIntentService;
import com.loopj.android.http.sample.util.IntentUtil;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class IntentServiceSample extends SampleParentActivity {

    public static final String LOG_TAG = "IntentServiceSample";
    public static final String ACTION_START = "SYNC_START";
    public static final String ACTION_RETRY = "SYNC_RETRY";
    public static final String ACTION_CANCEL = "SYNC_CANCEL";
    public static final String ACTION_SUCCESS = "SYNC_SUCCESS";
    public static final String ACTION_FAILURE = "SYNC_FAILURE";
    public static final String ACTION_FINISH = "SYNC_FINISH";
    public static final String[] ALLOWED_ACTIONS = {ACTION_START,
            ACTION_RETRY, ACTION_CANCEL, ACTION_SUCCESS, ACTION_FAILURE, ACTION_FINISH};
    private final android.content.BroadcastReceiver broadcastReceiver = new android.content.BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            String action = intent.getAction();

            // switch() doesn't support strings in older JDK.
            if (ACTION_START.equals(action)) {
                clearOutputs();
                addView(getColoredView(LIGHTBLUE, "Request started"));
            } else if (ACTION_FINISH.equals(action)) {
                addView(getColoredView(LIGHTBLUE, "Request finished"));
            } else if (ACTION_CANCEL.equals(action)) {
                addView(getColoredView(LIGHTBLUE, "Request cancelled"));
            } else if (ACTION_RETRY.equals(action)) {
                addView(getColoredView(LIGHTBLUE, "Request retried"));
            } else if (ACTION_FAILURE.equals(action) || ACTION_SUCCESS.equals(action)) {
                debugThrowable(LOG_TAG, (Throwable) intent.getSerializableExtra(com.loopj.android.http.sample.services.ExampleIntentService.INTENT_THROWABLE));
                if (ACTION_SUCCESS.equals(action)) {
                    debugStatusCode(LOG_TAG, intent.getIntExtra(com.loopj.android.http.sample.services.ExampleIntentService.INTENT_STATUS_CODE, 0));
                    debugHeaders(LOG_TAG, com.loopj.android.http.sample.util.IntentUtil.deserializeHeaders(intent.getStringArrayExtra(com.loopj.android.http.sample.services.ExampleIntentService.INTENT_HEADERS)));
                    byte[] returnedBytes = intent.getByteArrayExtra(com.loopj.android.http.sample.services.ExampleIntentService.INTENT_DATA);
                    if (returnedBytes != null) {
                        debugResponse(LOG_TAG, new String(returnedBytes));
                    }
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        android.content.IntentFilter iFilter = new android.content.IntentFilter();
        for (String action : ALLOWED_ACTIONS) {
            iFilter.addAction(action);
        }
        registerReceiver(broadcastReceiver, iFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {
        // no response handler on activity
        return null;
    }

    @Override
    public String getDefaultURL() {
        return "https://httpbin.org/get";
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return false;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return false;
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_intent_service_sample;
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        android.content.Intent serviceCall = new android.content.Intent(this, com.loopj.android.http.sample.services.ExampleIntentService.class);
        serviceCall.putExtra(com.loopj.android.http.sample.services.ExampleIntentService.INTENT_URL, URL);
        startService(serviceCall);
        return null;
    }
}
