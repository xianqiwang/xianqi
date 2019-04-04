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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HttpContext;

public interface SampleInterface {

    java.util.List<com.loopj.android.http.RequestHandle> getRequestHandles ();

    void addRequestHandle(com.loopj.android.http.RequestHandle handle);

    void onRunButtonPressed ();

    void onCancelButtonPressed ();

    cz.msebera.android.httpclient.Header[] getRequestHeaders ();

    cz.msebera.android.httpclient.HttpEntity getRequestEntity ();

    com.loopj.android.http.AsyncHttpClient getAsyncHttpClient ();

    void setAsyncHttpClient(com.loopj.android.http.AsyncHttpClient client);

    com.loopj.android.http.AsyncHttpRequest getHttpRequest(cz.msebera.android.httpclient.impl.client.DefaultHttpClient client, cz.msebera.android.httpclient.protocol.HttpContext httpContext, cz.msebera.android.httpclient.client.methods.HttpUriRequest uriRequest, String contentType, com.loopj.android.http.ResponseHandlerInterface responseHandler, android.content.Context context);

    com.loopj.android.http.ResponseHandlerInterface getResponseHandler ();

    String getDefaultURL ();

    String getDefaultHeaders ();

    boolean isRequestHeadersAllowed ();

    boolean isRequestBodyAllowed ();

    int getSampleTitle ();

    boolean isCancelButtonAllowed ();

    com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler);
}
