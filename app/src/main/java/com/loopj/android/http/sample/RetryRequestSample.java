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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.nfp.update.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.conn.ConnectionPoolTimeoutException;

/**
 * This sample demonstrates use of
 * {@link com.loopj.android.http.AsyncHttpClient#allowRetryExceptionClass(Class)} and
 * {@link com.loopj.android.http.AsyncHttpClient#blockRetryExceptionClass(Class)} to whitelist
 * and blacklist certain Exceptions, respectively.
 *
 * @author Noor Dawod <github@fineswap.com>
 */
public class RetryRequestSample extends com.loopj.android.http.sample.GetSample {

    private static boolean wasToastShown;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The following exceptions will be whitelisted, i.e.: When an exception
        // of this type is raised, the request will be retried.
        com.loopj.android.http.AsyncHttpClient.allowRetryExceptionClass(java.io.IOException.class);
        com.loopj.android.http.AsyncHttpClient.allowRetryExceptionClass(java.net.SocketTimeoutException.class);
        com.loopj.android.http.AsyncHttpClient.allowRetryExceptionClass(cz.msebera.android.httpclient.conn.ConnectTimeoutException.class);

        // The following exceptions will be blacklisted, i.e.: When an exception
        // of this type is raised, the request will not be retried and it will
        // fail immediately.
        com.loopj.android.http.AsyncHttpClient.blockRetryExceptionClass(java.net.UnknownHostException.class);
        com.loopj.android.http.AsyncHttpClient.blockRetryExceptionClass(cz.msebera.android.httpclient.conn.ConnectionPoolTimeoutException.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!wasToastShown) {
            wasToastShown = true;
            android.widget.Toast.makeText(
                    this,
                    "Exceptions' whitelist and blacklist updated\nSee RetryRequestSample.java for details",
                    android.widget.Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    public String getDefaultURL() {
        return PROTOCOL + "httpbin.org/ip";
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_retry_handler;
    }
}
