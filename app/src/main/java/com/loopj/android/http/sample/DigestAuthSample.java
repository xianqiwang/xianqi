package com.loopj.android.http.sample;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import com.nfp.update.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.auth.AuthScope;
import cz.msebera.android.httpclient.auth.UsernamePasswordCredentials;

public class DigestAuthSample extends com.loopj.android.http.sample.GetSample {

    private android.widget.EditText usernameField, passwordField;

    @Override
    public String getDefaultURL() {
        return PROTOCOL + "httpbin.org/digest-auth/auth/user/passwd2";
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_digest_auth;
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usernameField = new android.widget.EditText(this);
        passwordField = new android.widget.EditText(this);
        usernameField.setHint("Username");
        passwordField.setHint("Password");
        usernameField.setText("user");
        passwordField.setText("passwd2");
        customFieldsLayout.addView(usernameField);
        customFieldsLayout.addView(passwordField);
    }

    @Override
    public com.loopj.android.http.RequestHandle executeSample(com.loopj.android.http.AsyncHttpClient client, String URL, cz.msebera.android.httpclient.Header[] headers, cz.msebera.android.httpclient.HttpEntity entity, com.loopj.android.http.ResponseHandlerInterface responseHandler) {
        setCredentials(client, URL);
        return client.get(this, URL, headers, null, responseHandler);
    }

    @Override
    public boolean isCancelButtonAllowed() {
        return true;
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return true;
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return false;
    }

    private void setCredentials(com.loopj.android.http.AsyncHttpClient client, String URL) {
        android.net.Uri parsed = android.net.Uri.parse(URL);
        client.clearCredentialsProvider();
        client.setCredentials(
                new cz.msebera.android.httpclient.auth.AuthScope(parsed.getHost(), parsed.getPort() == -1 ? 80 : parsed.getPort()),
                new cz.msebera.android.httpclient.auth.UsernamePasswordCredentials(
                        usernameField.getText().toString(),
                        passwordField.getText().toString()
                )
        );
    }
}
