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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import com.nfp.update.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class PutSample extends SampleParentActivity {
    private static final String LOG_TAG = "PutSample";

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        return client.put(this, URL, headers, entity, null, responseHandler);
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_put_sample;
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
        return PROTOCOL + "httpbin.org/put";
    }

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {
        return new com.loopj.android.http.AsyncHttpResponseHandler() {

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

}
