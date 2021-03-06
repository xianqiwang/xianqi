/*
    Copyright (c) 2015 Marek Sebera <marek.sebera@gmail.com>

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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class HeadSample extends com.loopj.android.http.sample.FileSample {

    private static final String LOG_TAG = "HeadSample";

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {
        return new com.loopj.android.http.AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                debugStatusCode(LOG_TAG, statusCode);
                debugHeaders(LOG_TAG, headers);
                debugResponse(LOG_TAG, String.format("Response of size: %d", responseBody == null ? 0 : responseBody.length));
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                addView(getColoredView(LIGHTRED, String.format("Progress %d from %d", bytesWritten, totalSize)));
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable throwable) {
                debugStatusCode(LOG_TAG, statusCode);
                debugHeaders(LOG_TAG, headers);
                debugThrowable(LOG_TAG, throwable);
                debugResponse(LOG_TAG, String.format("Response of size: %d", responseBody == null ? 0 : responseBody.length));
            }
        };
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        return client.head(this, URL, headers, null, responseHandler);
    }
}
