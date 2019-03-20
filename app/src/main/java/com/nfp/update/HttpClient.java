/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nfp.update;

import android.content.SharedPreferences;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import com.nfp.update.polling.PollingService;

/**
 * Created by djw on 17-2-9.
 */

public class HttpClient {

    private final static String TAG = "HttpClient";

    private static final String COMMERCIAL_URL = "http://bcm.ms.seiko-sol.co.jp/cgi-bin/bcmdiff/";
    private static String TEST_URL = "http://p9008-ipngnfx01funabasi.chiba.ocn.ne.jp/cgi-bin/bcmdiff/";
    private static String BASE_URL =  COMMERCIAL_URL;
    private static String INITIAL_URL =  COMMERCIAL_URL;
    private static String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0.1; "+UpdateUtil.getBuildModel()+" Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.119 Mobile Safari/537.36";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setUserAgent(USER_AGENT);
        setProxy(UpdateUtil.getFotaProxy(context));
        client.setConnectTimeout(30 * 1000);
        client.setResponseTimeout(30 * 1000);
        Log.d(TAG, "USER_AGENT = " + USER_AGENT);
        client.get(context, getAbsoluteUrl(url, context), params, responseHandler);
    }

    public static void get(Context context, String url, RequestParams params, String filestr, FileAsyncHttpResponseHandler responseHandler) {
        client.setUserAgent(USER_AGENT);
        client.addHeader("Range", "bytes=" + UpdateUtil.getFileLength(filestr) + "-");
        setProxy(UpdateUtil.getFotaProxy(context));
        client.setConnectTimeout(30 * 1000);
        client.setResponseTimeout(30 * 1000);
        Log.d(TAG, "USER_AGENT = " + USER_AGENT);
        client.get(context, getAbsoluteUrl(url, context), params, responseHandler);
    }

    public static void cancleRequest(boolean mayInterruptIfRunning) {
        Log.d(TAG, "client.cancelAllRequests()");
        client.cancelAllRequests(mayInterruptIfRunning);
    }

    public static void setProxy(int flag) {
        if(flag == 0){
            client.setProxy("dmint.softbank.ne.jp", 8080);
            Log.d(TAG, "Proxy = " + "dmint.softbank.ne.jp" + "   8080");
        }
    }

    private static String getAbsoluteUrl(String relativeUrl, Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences spref = context.getSharedPreferences("debug_comm", 0);
        int url_flag = spref.getInt("fota_url",0);
        String NewURL = spref.getString("new_fota_url","");
        if(NewURL != ""){
            //INITIAL_URL = NewURL;
            url_flag = 3;
        }

        switch (url_flag){
            case 0:
                BASE_URL = COMMERCIAL_URL;
                break;
            case 1:
                BASE_URL = TEST_URL;
                break;
            case 2:
                BASE_URL = COMMERCIAL_URL;
                break;
            case 3:
                BASE_URL = NewURL;
                break;
        }
        Log.d(TAG, "url = " + BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }
}
