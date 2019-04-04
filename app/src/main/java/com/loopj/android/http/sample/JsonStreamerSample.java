/*
    Android Asynchronous Http Client Sample
    Copyright (c) 2014 Marek Sebera <marek.sebera@gmail.com>
    https://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.loopj.android.http.sample;

import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;
import com.nfp.update.R;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * This sample demonstrates how to upload JSON data using streams, resulting
 * in a low-memory footprint even with extremely large data.
 * <p/>
 * Please note: You must prepare a server-side end-point to consume the uploaded
 * data. This is because the data is uploaded using "application/json" content
 * type and regular methods, expecting a multi-form content type, will fail to
 * retrieve the POST'ed data.
 *
 * @author Noor Dawod <github@fineswap.com>
 */
public class JsonStreamerSample extends com.loopj.android.http.sample.PostSample {

    private static final String LOG_TAG = "JsonStreamSample";

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
        params.setUseJsonStreamer(true);
        org.json.JSONObject body;
        if (isRequestBodyAllowed() && (body = getBodyTextAsJSON()) != null) {
            try {
                java.util.Iterator keys = body.keys();
                android.util.Log.d(LOG_TAG, "JSON data:");
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    android.util.Log.d(LOG_TAG, "  " + key + ": " + body.get(key));
                    params.put(key, body.get(key).toString());
                }
            } catch (org.json.JSONException e) {
                android.util.Log.w(LOG_TAG, "Unable to retrieve a JSON value", e);
            }
        }
        return client.post(this, URL, headers, params,
                com.loopj.android.http.RequestParams.APPLICATION_JSON, responseHandler);
    }

    @Override
    public cz.msebera.android.httpclient.HttpEntity getRequestEntity() {
        // Unused in this sample.
        return null;
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_json_streamer_sample;
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return false;
    }

    protected org.json.JSONObject getBodyTextAsJSON() {

        String bodyText = getBodyText();
        if (bodyText != null && ! android.text.TextUtils.isEmpty(bodyText)) {
            try {
                return new org.json.JSONObject(bodyText);
            } catch (org.json.JSONException e) {
                android.util.Log.e(LOG_TAG, "User's data is not a valid JSON object", e);
            }
        }
        return null;
    }
}
