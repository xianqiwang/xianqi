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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nfp.update.R;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.Base64;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.sample.util.SampleJSON;

import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * This sample demonstrates how to implement HTTP 401 Basic Authentication.
 *
 * @author Noor Dawod <github@fineswap.com>
 */
public class Http401AuthSample extends com.loopj.android.http.sample.GetSample {

    private static final String LOG_TAG = "Http401AuthSample";
    private static final String HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_REALM_PREFIX = "realm=";
    private static final String HEADER_BASIC = "basic";

    private static final String SECRET_USERNAME = "ahc";
    private static final String SECRET_PASSWORD = "LetMeIn";

    private String userName;
    private String passWord;

    public void retryRequest() {
        // File is still smaller than remote file; send a new request.
        onRunButtonPressed();
    }

    @Override
    public String getDefaultURL() {
        return PROTOCOL + "httpbin.org/basic-auth/" + SECRET_USERNAME + "/" + SECRET_PASSWORD;
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_401_unauth;
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        return client.get(this, URL, headers, null, responseHandler);
    }

    @Override
    public cz.msebera.android.httpclient.Header[] getRequestHeaders() {
        java.util.List<cz.msebera.android.httpclient.Header> headers = getRequestHeadersList();

        // Add authentication header.
        if (userName != null && passWord != null) {
            byte[] base64bytes = com.loopj.android.http.Base64.encode(
                    (userName + ":" + passWord).getBytes(),
                    com.loopj.android.http.Base64.DEFAULT
            );
            String credentials = new String(base64bytes);
            headers.add(new cz.msebera.android.httpclient.message.BasicHeader(HEADER_AUTHORIZATION, HEADER_BASIC + " " + credentials));
        }

        return headers.toArray(new cz.msebera.android.httpclient.Header[headers.size()]);
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

                // Ask the user for credentials if required by the server.
                if (statusCode == 401) {
                    String realm = "Protected Page";
                    String authType = null;

                    // Cycle through the headers and look for the WWW-Authenticate header.
                    for (cz.msebera.android.httpclient.Header header : headers) {
                        String headerName = header.getName();
                        if (HEADER_WWW_AUTHENTICATE.equalsIgnoreCase(headerName)) {
                            String headerValue = header.getValue().trim();
                            String headerValueLowerCase = headerValue.toLowerCase(java.util.Locale.US);

                            // Get the type of auth requested.
                            int charPos = headerValueLowerCase.indexOf(' ');
                            if (0 < charPos) {
                                authType = headerValueLowerCase.substring(0, charPos);

                                // The second part should begin with a "realm=" prefix.
                                if (headerValueLowerCase.substring(1 + charPos).startsWith(HEADER_REALM_PREFIX)) {
                                    // The new realm value, including any possible wrapping quotation.
                                    realm = headerValue.substring(1 + charPos + HEADER_REALM_PREFIX.length());

                                    // If realm starts with a quote, remove surrounding quotes.
                                    if (realm.charAt(0) == '"' || realm.charAt(0) == '\'') {
                                        realm = realm.substring(1, realm.length() - 1);
                                    }
                                }
                            }
                        }
                    }

                    // We will support basic auth in this sample.
                    if (authType != null && HEADER_BASIC.equals(authType)) {
                        // Show a dialog for the user and request user/pass.
                        android.util.Log.d(LOG_TAG, HEADER_REALM_PREFIX + realm);

                        // Present the dialog.
                        postRunnable(new com.loopj.android.http.sample.Http401AuthSample.DialogRunnable(realm));
                    }
                }
            }

            @Override
            protected com.loopj.android.http.sample.util.SampleJSON parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), com.loopj.android.http.sample.util.SampleJSON.class).next();
            }
        };
    }

    private class DialogRunnable implements Runnable, android.content.DialogInterface.OnClickListener {

        final String realm;
        final android.view.View dialogView;

        public DialogRunnable(String realm) {
            this.realm = realm;
            this.dialogView = android.view.LayoutInflater
                    .from(com.loopj.android.http.sample.Http401AuthSample.this)
                    .inflate(R.layout.credentials, new android.widget.LinearLayout(com.loopj.android.http.sample.Http401AuthSample.this), false);

            // Update the preface text with correct credentials.
            android.widget.TextView preface = (android.widget.TextView) dialogView.findViewById(R.id.label_credentials);
            String prefaceText = preface.getText().toString();

            // Substitute placeholders, and re-set the value.
            preface.setText(String.format(prefaceText, SECRET_USERNAME, SECRET_PASSWORD));
        }

        @Override
        public void run() {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(com.loopj.android.http.sample.Http401AuthSample.this);
            builder.setTitle(realm);
            builder.setView(dialogView);
            builder.setPositiveButton(android.R.string.ok, this);
            builder.setNegativeButton(android.R.string.cancel, this);
            builder.show();
        }

        @Override
        public void onClick(android.content.DialogInterface dialog, int which) {
            switch (which) {
                case android.content.DialogInterface.BUTTON_POSITIVE:
                    // Dismiss the dialog.
                    dialog.dismiss();

                    // Update the username and password variables.
                    userName = ((android.widget.EditText) dialogView.findViewById(R.id.field_username)).getText().toString();
                    passWord = ((android.widget.EditText) dialogView.findViewById(R.id.field_password)).getText().toString();

                    // Refetch the remote file.
                    retryRequest();

                    break;

                case android.content.DialogInterface.BUTTON_NEGATIVE:
                    // Dismiss the dialog.
                    dialog.dismiss();

                    break;
            }
        }
    }
}
