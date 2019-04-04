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

import android.app.Activity;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.nfp.update.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class AsyncBackgroundThreadSample extends com.loopj.android.http.sample.SampleParentActivity {
    private static final String LOG_TAG = "AsyncBackgroundThreadSample";

    private final java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(final com.loopj.android.http.AsyncHttpClient client, final String URL, final cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, final com.loopj.android.http.ResponseHandlerInterface responseHandler) {

        final android.app.Activity ctx = this;
        java.util.concurrent.FutureTask<com.loopj.android.http.RequestHandle> future = new java.util.concurrent.FutureTask<>(new java.util.concurrent.Callable<com.loopj.android.http.RequestHandle>() {
            public com.loopj.android.http.RequestHandle call() {
                android.util.Log.d(LOG_TAG, "Executing GET request on background thread");
                return client.get(ctx, URL, headers, null, responseHandler);
            }
        });

        executor.execute(future);

        com.loopj.android.http.RequestHandle handle = null;
        try {
            handle = future.get(5, java.util.concurrent.TimeUnit.SECONDS);
            android.util.Log.d(LOG_TAG, "Background thread for GET request has finished");
        } catch (Exception e) {
            android.widget.Toast.makeText(ctx, e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return handle;
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_async_background_thread;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return false;
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return false;
    }

    @Override
    public String getDefaultURL() {
        return PROTOCOL + "httpbin.org/get";
    }

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {

        java.util.concurrent.FutureTask<com.loopj.android.http.ResponseHandlerInterface> future = new java.util.concurrent.FutureTask<>(new java.util.concurrent.Callable<com.loopj.android.http.ResponseHandlerInterface>() {

            @Override
            public com.loopj.android.http.ResponseHandlerInterface call() throws Exception {
                android.util.Log.d(LOG_TAG, "Creating AsyncHttpResponseHandler on background thread");
                return new com.loopj.android.http.AsyncHttpResponseHandler(android.os.Looper.getMainLooper()) {

                    @Override
                    public void onStart() {
                        clearOutputs();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] response) {
                        android.util.Log.d(LOG_TAG, String.format("onSuccess executing on main thread : %B", android.os.Looper.myLooper() == android.os.Looper.getMainLooper()));
                        debugHeaders(LOG_TAG, headers);
                        debugStatusCode(LOG_TAG, statusCode);
                        debugResponse(LOG_TAG, new String(response));
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] errorResponse, Throwable e) {
                        android.util.Log.d(LOG_TAG, String.format("onFailure executing on main thread : %B", android.os.Looper.myLooper() == android.os.Looper.getMainLooper()));
                        debugHeaders(LOG_TAG, headers);
                        debugStatusCode(LOG_TAG, statusCode);
                        debugThrowable(LOG_TAG, e);
                        if (errorResponse != null) {
                            debugResponse(LOG_TAG, new String(errorResponse));
                        }
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        android.widget.Toast.makeText(com.loopj.android.http.sample.AsyncBackgroundThreadSample.this,
                                String.format("Request is retried, retry no. %d", retryNo),
                                android.widget.Toast.LENGTH_SHORT)
                                .show();
                    }
                };
            }
        });

        executor.execute(future);

        com.loopj.android.http.ResponseHandlerInterface responseHandler = null;
        try {
            responseHandler = future.get();
            android.util.Log.d(LOG_TAG, "Background thread for AsyncHttpResponseHandler has finished");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseHandler;
    }
}
