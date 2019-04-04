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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import com.nfp.update.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HttpContext;

public abstract class SampleParentActivity extends android.app.Activity implements com.loopj.android.http.sample.SampleInterface {

    protected static final String PROTOCOL_HTTP = "http://";
    protected static final String PROTOCOL_HTTPS = "https://";
    protected static final int LIGHTGREEN = android.graphics.Color.parseColor("#00FF66");
    protected static final int LIGHTRED = android.graphics.Color.parseColor("#FF3300");
    protected static final int YELLOW = android.graphics.Color.parseColor("#FFFF00");
    protected static final int LIGHTBLUE = android.graphics.Color.parseColor("#99CCFF");
    private static final String LOG_TAG = "SampleParentActivity";
    private static final int MENU_USE_HTTPS = 0;
    private static final int MENU_CLEAR_VIEW = 1;
    private static final int MENU_LOGGING_VERBOSITY = 2;
    private static final int MENU_ENABLE_LOGGING = 3;
    protected static String PROTOCOL = PROTOCOL_HTTPS;
    private final java.util.List<com.loopj.android.http.RequestHandle> requestHandles = new java.util.LinkedList<com.loopj.android.http.RequestHandle>();
    public android.widget.LinearLayout customFieldsLayout;
    private com.loopj.android.http.AsyncHttpClient asyncHttpClient = new com.loopj.android.http.AsyncHttpClient() {

        @Override
        protected com.loopj.android.http.AsyncHttpRequest newAsyncHttpRequest(cz.msebera.android.httpclient.impl.client.DefaultHttpClient client, cz.msebera.android.httpclient.protocol.HttpContext httpContext, cz.msebera.android.httpclient.client.methods.HttpUriRequest uriRequest, String contentType, com.loopj.android.http.ResponseHandlerInterface responseHandler, android.content.Context context) {
            com.loopj.android.http.AsyncHttpRequest httpRequest = getHttpRequest(client, httpContext, uriRequest, contentType, responseHandler, context);
            return httpRequest == null
                    ? super.newAsyncHttpRequest(client, httpContext, uriRequest, contentType, responseHandler, context)
                    : httpRequest;
        }
    };
    private android.widget.EditText urlEditText, headersEditText, bodyEditText;
    protected final android.view.View.OnClickListener onClickListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            switch (v.getId()) {
                case R.id.button_run:
                    onRunButtonPressed();
                    break;
                case R.id.button_cancel:
                    onCancelButtonPressed();
                    break;
            }
        }
    };

    private android.widget.LinearLayout responseLayout;
    private boolean useHttps = true;
    private boolean enableLogging = true;

    protected static String throwableToString(Throwable t) {
        if (t == null)
            return null;

        java.io.StringWriter sw = new java.io.StringWriter();
        t.printStackTrace(new java.io.PrintWriter(sw));
        return sw.toString();
    }

    public static int getContrastColor(int color) {
        double y = (299 * android.graphics.Color.red(color) + 587 * android.graphics.Color.green(color) + 114 * android.graphics.Color.blue(color)) / 1000;
        return y >= 128 ? android.graphics.Color.BLACK : android.graphics.Color.WHITE;
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.parent_layout);
        setTitle(getSampleTitle());

        setHomeAsUpEnabled();

        urlEditText = (android.widget.EditText) findViewById(R.id.edit_url);
        headersEditText = (android.widget.EditText) findViewById(R.id.edit_headers);
        bodyEditText = (android.widget.EditText) findViewById(R.id.edit_body);
        customFieldsLayout = (android.widget.LinearLayout) findViewById(R.id.layout_custom);
        android.widget.Button runButton = (android.widget.Button) findViewById(R.id.button_run);
        android.widget.Button cancelButton = (android.widget.Button) findViewById(R.id.button_cancel);
        android.widget.LinearLayout headersLayout = (android.widget.LinearLayout) findViewById(R.id.layout_headers);
        android.widget.LinearLayout bodyLayout = (android.widget.LinearLayout) findViewById(R.id.layout_body);
        responseLayout = (android.widget.LinearLayout) findViewById(R.id.layout_response);

        urlEditText.setText(getDefaultURL());
        headersEditText.setText(getDefaultHeaders());

        bodyLayout.setVisibility(isRequestBodyAllowed() ? android.view.View.VISIBLE : android.view.View.GONE);
        headersLayout.setVisibility(isRequestHeadersAllowed() ? android.view.View.VISIBLE : android.view.View.GONE);

        runButton.setOnClickListener(onClickListener);
        if (cancelButton != null) {
            if (isCancelButtonAllowed()) {
                cancelButton.setVisibility(android.view.View.VISIBLE);
                cancelButton.setOnClickListener(onClickListener);
            } else {
                cancelButton.setEnabled(false);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        android.view.MenuItem useHttpsMenuItem = menu.findItem(MENU_USE_HTTPS);
        if (useHttpsMenuItem != null) {
            useHttpsMenuItem.setChecked(useHttps);
        }
        android.view.MenuItem enableLoggingMenuItem = menu.findItem(MENU_ENABLE_LOGGING);
        if (enableLoggingMenuItem != null) {
            enableLoggingMenuItem.setChecked(enableLogging);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        menu.add(android.view.Menu.NONE, MENU_USE_HTTPS, android.view.Menu.NONE, R.string.menu_use_https).setCheckable(true);
        menu.add(android.view.Menu.NONE, MENU_CLEAR_VIEW, android.view.Menu.NONE, R.string.menu_clear_view);
        menu.add(android.view.Menu.NONE, MENU_ENABLE_LOGGING, android.view.Menu.NONE, "Enable Logging").setCheckable(true);
        menu.add(android.view.Menu.NONE, MENU_LOGGING_VERBOSITY, android.view.Menu.NONE, "Set Logging Verbosity");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case MENU_USE_HTTPS:
                useHttps = !useHttps;
                PROTOCOL = useHttps ? PROTOCOL_HTTPS : PROTOCOL_HTTP;
                urlEditText.setText(getDefaultURL());
                return true;
            case MENU_ENABLE_LOGGING:
                enableLogging = !enableLogging;
                getAsyncHttpClient().setLoggingEnabled(enableLogging);
                return true;
            case MENU_LOGGING_VERBOSITY:
                showLoggingVerbosityDialog();
                return true;
            case MENU_CLEAR_VIEW:
                clearOutputs();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public com.loopj.android.http.AsyncHttpRequest getHttpRequest(cz.msebera.android.httpclient.impl.client.DefaultHttpClient client, cz.msebera.android.httpclient.protocol.HttpContext httpContext, cz.msebera.android.httpclient.client.methods.HttpUriRequest uriRequest, String contentType, com.loopj.android.http.ResponseHandlerInterface responseHandler, android.content.Context context) {
        return null;
    }

    public java.util.List<com.loopj.android.http.RequestHandle> getRequestHandles() {
        return requestHandles;
    }

    @Override
    public void addRequestHandle(com.loopj.android.http.RequestHandle handle) {
        if (null != handle) {
            requestHandles.add(handle);
        }
    }

    private void showLoggingVerbosityDialog() {
        android.app.AlertDialog ad = new android.app.AlertDialog.Builder(this)
                .setTitle("Set Logging Verbosity")
                .setSingleChoiceItems(new String[]{
                        "VERBOSE",
                        "DEBUG",
                        "INFO",
                        "WARN",
                        "ERROR",
                        "WTF"
                }, getAsyncHttpClient().getLoggingLevel() - 2, new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        getAsyncHttpClient().setLoggingLevel(which + 2);
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .setNeutralButton("Cancel", null)
                .create();
        ad.show();
    }

    public void onRunButtonPressed() {
        addRequestHandle(executeSample(getAsyncHttpClient(),
                getUrlText(getDefaultURL()),
                getRequestHeaders(),
                getRequestEntity(),
                getResponseHandler()));
    }

    public void onCancelButtonPressed() {
        asyncHttpClient.cancelRequests(com.loopj.android.http.sample.SampleParentActivity.this, true);
    }

    public java.util.List<cz.msebera.android.httpclient.Header> getRequestHeadersList() {
        java.util.List<cz.msebera.android.httpclient.Header> headers = new java.util.ArrayList<cz.msebera.android.httpclient.Header>();
        String headersRaw = headersEditText.getText() == null ? null : headersEditText.getText().toString();

        if (headersRaw != null && headersRaw.length() > 3) {
            String[] lines = headersRaw.split("\\r?\\n");
            for (String line : lines) {
                try {
                    int equalSignPos = line.indexOf('=');
                    if (1 > equalSignPos) {
                        throw new IllegalArgumentException("Wrong header format, may be 'Key=Value' only");
                    }

                    String headerName = line.substring(0, equalSignPos).trim();
                    String headerValue = line.substring(1 + equalSignPos).trim();
                    android.util.Log.d(LOG_TAG, String.format("Added header: [%s:%s]", headerName, headerValue));

                    headers.add(new cz.msebera.android.httpclient.message.BasicHeader(headerName, headerValue));
                } catch (Throwable t) {
                    android.util.Log.e(LOG_TAG, "Not a valid header line: " + line, t);
                }
            }
        }
        return headers;
    }

    public cz.msebera.android.httpclient.Header[] getRequestHeaders() {
        java.util.List<cz.msebera.android.httpclient.Header> headers = getRequestHeadersList();
        return headers.toArray(new cz.msebera.android.httpclient.Header[headers.size()]);
    }

    public cz.msebera.android.httpclient.HttpEntity getRequestEntity() {
        String bodyText;
        if (isRequestBodyAllowed() && (bodyText = getBodyText()) != null) {
            try {
                return new cz.msebera.android.httpclient.entity.StringEntity(bodyText);
            } catch (java.io.UnsupportedEncodingException e) {
                android.util.Log.e(LOG_TAG, "cannot create String entity", e);
            }
        }
        return null;
    }

    public String getUrlText() {
        return getUrlText(null);
    }

    public String getUrlText(String defaultText) {
        return urlEditText != null && urlEditText.getText() != null
                ? urlEditText.getText().toString()
                : defaultText;
    }

    public String getBodyText() {
        return getBodyText(null);
    }

    public String getBodyText(String defaultText) {
        return bodyEditText != null && bodyEditText.getText() != null
                ? bodyEditText.getText().toString()
                : defaultText;
    }

    public String getHeadersText() {
        return getHeadersText(null);
    }

    public String getHeadersText(String defaultText) {
        return headersEditText != null && headersEditText.getText() != null
                ? headersEditText.getText().toString()
                : defaultText;
    }

    protected final void debugHeaders(String TAG, cz.msebera.android.httpclient.Header[] headers) {
        if (headers != null) {
            android.util.Log.d(TAG, "Return Headers:");
            StringBuilder builder = new StringBuilder();
            for (cz.msebera.android.httpclient.Header h : headers) {
                String _h = String.format(java.util.Locale.US, "%s : %s", h.getName(), h.getValue());
                android.util.Log.d(TAG, _h);
                builder.append(_h);
                builder.append("\n");
            }
            addView(getColoredView(YELLOW, builder.toString()));
        }
    }

    protected final void debugThrowable(String TAG, Throwable t) {
        if (t != null) {
            android.util.Log.e(TAG, "AsyncHttpClient returned error", t);
            addView(getColoredView(LIGHTRED, throwableToString(t)));
        }
    }

    protected final void debugResponse(String TAG, String response) {
        if (response != null) {
            android.util.Log.d(TAG, "Response data:");
            android.util.Log.d(TAG, response);
            addView(getColoredView(LIGHTGREEN, response));
        }
    }

    protected final void debugStatusCode(String TAG, int statusCode) {
        String msg = String.format(java.util.Locale.US, "Return Status Code: %d", statusCode);
        android.util.Log.d(TAG, msg);
        addView(getColoredView(LIGHTBLUE, msg));
    }

    protected android.view.View getColoredView(int bgColor, String msg) {
        android.widget.TextView tv = new android.widget.TextView(this);
        tv.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText(msg);
        tv.setBackgroundColor(bgColor);
        tv.setPadding(10, 10, 10, 10);
        tv.setTextColor(getContrastColor(bgColor));
        return tv;
    }

    @Override
    public String getDefaultHeaders() {
        return null;
    }

    protected final void addView(android.view.View v) {
        responseLayout.addView(v);
    }

    protected final void clearOutputs() {
        responseLayout.removeAllViews();
    }

    public boolean isCancelButtonAllowed() {
        return false;
    }

    public com.loopj.android.http.AsyncHttpClient getAsyncHttpClient() {
        return this.asyncHttpClient;
    }

    @Override
    public void setAsyncHttpClient(com.loopj.android.http.AsyncHttpClient client) {
        this.asyncHttpClient = client;
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    private void setHomeAsUpEnabled() {
        if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 11) {
            if (getActionBar() != null)
                getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
