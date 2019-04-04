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

import android.os.Bundle;
import com.nfp.update.R;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.sample.util.SampleJSON;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.CookieStore;

public class PersistentCookiesSample extends com.loopj.android.http.sample.SampleParentActivity {

    private static final String LOG_TAG = "PersistentCookiesSample";

    private cz.msebera.android.httpclient.client.CookieStore cookieStore;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        // Use the application's context so that memory leakage doesn't occur.
        cookieStore = new com.loopj.android.http.PersistentCookieStore(getApplicationContext());

        // Set the new cookie store.
        getAsyncHttpClient().setCookieStore(cookieStore);

        super.onCreate(savedInstanceState);
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_persistent_cookies;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return false;
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return true;
    }

    @Override
    public String getDefaultURL() {
        // The base URL for testing cookies.
        String url = PROTOCOL + "httpbin.org/cookies";

        // If the cookie store is empty, suggest a cookie.
        if (cookieStore.getCookies().isEmpty()) {
            url += "/set?time=" + System.currentTimeMillis();
        }

        return url;
    }

    @Override
    public com.loopj.android.http.ResponseHandlerInterface getResponseHandler() {
        return new com.loopj.android.http.BaseJsonHttpResponseHandler<com.loopj.android.http.sample.util.SampleJSON>() {
            @Override
            public void onStart() {
                clearOutputs();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String rawJsonResponse, com.loopj.android.http.sample.util.SampleJSON response) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                if (response != null) {
                    debugResponse(LOG_TAG, rawJsonResponse);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, String rawJsonData, com.loopj.android.http.sample.util.SampleJSON errorResponse) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                debugThrowable(LOG_TAG, throwable);
                if (errorResponse != null) {
                    debugResponse(LOG_TAG, rawJsonData);
                }
            }

            @Override
            protected com.loopj.android.http.sample.util.SampleJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), com.loopj.android.http.sample.util.SampleJSON.class).next();
            }
        };
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        client.setEnableRedirects(true);
        return client.get(this, URL, headers, null, responseHandler);
    }

}
