package com.loopj.android.http.sample;

import android.os.Bundle;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.loopj.android.http.sample.util.API8Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.nfp.update.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class RequestParamsDebug extends SampleParentActivity {

    public static final String LOG_TAG = "RequestParamsDebug";
    private static final String DEMO_RP_CONTENT = "array=java\n" +
            "array=C\n" +
            "list=blue\n" +
            "list=yellow\n" +
            "set=music\n" +
            "set=art\n" +
            "map=first_name\n" +
            "map=last_name\n";
    private android.widget.EditText customParams;

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {
        return new com.loopj.android.http.TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                debugStatusCode(LOG_TAG, statusCode);
                debugHeaders(LOG_TAG, headers);
                debugResponse(LOG_TAG, responseString);
                debugThrowable(LOG_TAG, throwable);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                debugStatusCode(LOG_TAG, statusCode);
                debugHeaders(LOG_TAG, headers);
                debugResponse(LOG_TAG, responseString);
            }
        };
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customParams = new android.widget.EditText(this);
        customParams.setLines(8);
        customParams.setText(DEMO_RP_CONTENT);
        customFieldsLayout.addView(customParams);
    }

    @Override
    public String getDefaultURL() {
        return PROTOCOL + "httpbin.org/get";
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
        return R.string.title_request_params_debug;
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        return getAsyncHttpClient().get(this, getDefaultURL(), getRequestParams(), getResponseHandler());
    }

    // TODO: allow parsing multiple values for each type, maybe like "type.key=value" ?
    private com.loopj.android.http.RequestParams getRequestParams() {
        com.loopj.android.http.RequestParams rp = new com.loopj.android.http.RequestParams();
        // contents of customParams custom field view
        String customParamsText = customParams.getText().toString();
        String[] pairs = customParamsText.split("\n");
        // temp content holders
        java.util.Map<String, java.util.Map<String, String>> mapOfMaps = new java.util.HashMap<>();
        java.util.Map<String, java.util.List<String>> mapOfLists = new java.util.HashMap<>();
        java.util.Map<String, String[]> mapOfArrays = new java.util.HashMap<>();
        java.util.Map<String, java.util.Set<String>> mapOfSets = new java.util.HashMap<>();
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length != 2)
                continue;
            String key = kv[0].trim();
            String value = kv[1].trim();
            if ("array".equals(key)) {
                String[] values = mapOfArrays.get(key);
                if (values == null) {
                    values = new String[]{value};
                } else {
                    values = com.loopj.android.http.sample.util.API8Util.copyOfRange(values, 0, values.length + 1);
                    values[values.length - 1] = value;
                }
                mapOfArrays.put(key, values);
            } else if ("list".equals(key)) {
                java.util.List<String> values = mapOfLists.get(key);
                if (values == null) {
                    values = new java.util.ArrayList<>();
                }
                values.add(value);
                mapOfLists.put(key, values);
            } else if ("set".equals(key)) {
                java.util.Set<String> values = mapOfSets.get(key);
                if (values == null) {
                    values = new java.util.HashSet<>();
                }
                values.add(value);
                mapOfSets.put(key, values);
            } else if ("map".equals(key)) {
                java.util.Map<String, String> values = mapOfMaps.get(key);
                if (values == null) {
                    values = new java.util.HashMap<>();
                }
                values.put(key + values.size(), value);
                mapOfMaps.put(key, values);
            }
        }
        // fill in string list
        for (java.util.Map.Entry<String, java.util.List<String>> entry : mapOfLists.entrySet()) {
            rp.put(entry.getKey(), entry.getValue());
        }
        // fill in string array
        for (java.util.Map.Entry<String, String[]> entry : mapOfArrays.entrySet()) {
            rp.put(entry.getKey(), entry.getValue());
        }
        // fill in string set
        for (java.util.Map.Entry<String, java.util.Set<String>> entry : mapOfSets.entrySet()) {
            rp.put(entry.getKey(), entry.getValue());
        }
        // fill in string map
        for (java.util.Map.Entry<String, java.util.Map<String, String>> entry : mapOfMaps.entrySet()) {
            rp.put(entry.getKey(), entry.getValue());
        }
        // debug final URL construction into UI
        debugResponse(LOG_TAG, rp.toString());
        return rp;
    }
}
