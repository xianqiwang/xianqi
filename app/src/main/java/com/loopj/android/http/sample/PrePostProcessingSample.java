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

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.AbstractHttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HttpContext;
import com.nfp.update.R;
public class PrePostProcessingSample extends com.loopj.android.http.sample.SampleParentActivity {

    protected static final int LIGHTGREY = android.graphics.Color.parseColor("#E0E0E0");
    protected static final int DARKGREY = android.graphics.Color.parseColor("#888888");
    private static final String LOG_TAG = "PrePostProcessingSample";

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        return client.post(this, URL, headers, entity, null, responseHandler);
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_pre_post_processing;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return true;
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return true;
    }

    @Override
    public String getDefaultURL() {
        return PROTOCOL + "httpbin.org/post";
    }

    @Override
    public com.loopj.android.http.AsyncHttpRequest getHttpRequest(cz.msebera.android.httpclient.impl.client.DefaultHttpClient client, cz.msebera.android.httpclient.protocol.HttpContext httpContext, cz.msebera.android.httpclient.client.methods.HttpUriRequest uriRequest, String contentType, com.loopj.android.http.ResponseHandlerInterface responseHandler, android.content.Context context) {
        return new com.loopj.android.http.sample.PrePostProcessingSample.PrePostProcessRequest(client, httpContext, uriRequest, responseHandler);
    }

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {
        return new com.loopj.android.http.AsyncHttpResponseHandler() {

            @Override
            public void onPreProcessResponse(com.loopj.android.http.ResponseHandlerInterface instance, cz.msebera.android.httpclient.HttpResponse response) {
                debugProcessing(LOG_TAG, "Pre",
                        "Response is about to be pre-processed", LIGHTGREY);
            }

            @Override
            public void onPostProcessResponse(com.loopj.android.http.ResponseHandlerInterface instance, cz.msebera.android.httpclient.HttpResponse response) {
                debugProcessing(LOG_TAG, "Post",
                        "Response is about to be post-processed", DARKGREY);
            }

            @Override
            public void onStart() {
                clearOutputs();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] response) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                debugResponse(LOG_TAG, new String(response));
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] errorResponse, Throwable e) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                debugThrowable(LOG_TAG, e);
                if (errorResponse != null) {
                    debugResponse(LOG_TAG, new String(errorResponse));
                }
            }
        };
    }

    protected void debugProcessing(String TAG, String state, String message, final int color) {
        final String debugMessage = String.format(java.util.Locale.US, "%s-processing: %s", state, message);
        android.util.Log.d(TAG, debugMessage);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addView(getColoredView(color, debugMessage));
            }
        });
    }

    private class PrePostProcessRequest extends com.loopj.android.http.AsyncHttpRequest {

        public PrePostProcessRequest(cz.msebera.android.httpclient.impl.client.AbstractHttpClient client, cz.msebera.android.httpclient.protocol.HttpContext httpContext, cz.msebera.android.httpclient.client.methods.HttpUriRequest request, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
            super(client, httpContext, request, responseHandler);
        }

        @Override
        public void onPreProcessRequest(com.loopj.android.http.AsyncHttpRequest request) {
            debugProcessing(LOG_TAG, "Pre",
                    "Request is about to be pre-processed", LIGHTGREY);
        }

        @Override
        public void onPostProcessRequest(com.loopj.android.http.AsyncHttpRequest request) {
            debugProcessing(LOG_TAG, "Post",
                    "Request is about to be post-processed", DARKGREY);
        }
    }
}
