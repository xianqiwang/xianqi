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

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.sample.util.FileUtil;

import java.io.File;
import com.nfp.update.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class FileSample extends SampleParentActivity {
    private static final String LOG_TAG = "FileSample";

    @Override
    public int getSampleTitle() {
        return R.string.title_file_sample;
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
        return "http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/download.cgi?VER=SII%20901SI%20v000%20/l000%20123456788103254%2000000001234%20000000000001234%20001%206259";
    }

    @Override
    public ResponseHandlerInterface getResponseHandler() {
        return new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onStart() {
                clearOutputs();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.io.File response) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                debugFile(response);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, java.io.File file) {
                debugHeaders(LOG_TAG, headers);
                debugStatusCode(LOG_TAG, statusCode);
                debugThrowable(LOG_TAG, throwable);
                debugFile(file);
            }

            private void debugFile(java.io.File file) {
                if (file == null || !file.exists()) {
                    debugResponse(LOG_TAG, "Response is null");
                    return;
                }
                try {
                    debugResponse(LOG_TAG, file.getAbsolutePath() + "\r\n\r\n" + com.loopj.android.http.sample.util.FileUtil.getStringFromFile(file));
                } catch (Throwable t) {
                    android.util.Log.e(LOG_TAG, "Cannot debug file contents", t);
                }
                if (!deleteTargetFile()) {
                    android.util.Log.d(LOG_TAG, "Could not delete response file " + file.getAbsolutePath());
                }
            }
        };
    }

    @Override
    public RequestHandle executeSample(AsyncHttpClient client, String URL, Header[] headers, HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        client.setUserAgent ("SB-901SI");
        return client.get(this, URL, headers, null, responseHandler);
    }
}
