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

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.nfp.update.R;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Redirect302Sample extends com.loopj.android.http.sample.GetSample {

    private static final int MENU_ENABLE_REDIRECTS = 10;
    private static final int MENU_ENABLE_CIRCULAR_REDIRECTS = 11;
    private static final int MENU_ENABLE_RELATIVE_REDIRECTs = 12;
    private boolean enableRedirects = true;
    private boolean enableRelativeRedirects = true;
    private boolean enableCircularRedirects = true;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        menu.add(android.view.Menu.NONE, MENU_ENABLE_REDIRECTS, android.view.Menu.NONE, "Enable redirects").setCheckable(true);
        menu.add(android.view.Menu.NONE, MENU_ENABLE_RELATIVE_REDIRECTs, android.view.Menu.NONE, "Enable relative redirects").setCheckable(true);
        menu.add(android.view.Menu.NONE, MENU_ENABLE_CIRCULAR_REDIRECTS, android.view.Menu.NONE, "Enable circular redirects").setCheckable(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        android.view.MenuItem menuItemEnableRedirects = menu.findItem(MENU_ENABLE_REDIRECTS);
        if (menuItemEnableRedirects != null)
            menuItemEnableRedirects.setChecked(enableRedirects);
        android.view.MenuItem menuItemEnableRelativeRedirects = menu.findItem(MENU_ENABLE_RELATIVE_REDIRECTs);
        if (menuItemEnableRelativeRedirects != null)
            menuItemEnableRelativeRedirects.setChecked(enableRelativeRedirects);
        android.view.MenuItem menuItemEnableCircularRedirects = menu.findItem(MENU_ENABLE_CIRCULAR_REDIRECTS);
        if (menuItemEnableCircularRedirects != null)
            menuItemEnableCircularRedirects.setChecked(enableCircularRedirects);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.isCheckable()) {
            item.setChecked(!item.isChecked());
            if (item.getItemId() == MENU_ENABLE_REDIRECTS) {
                enableRedirects = item.isChecked();
            } else if (item.getItemId() == MENU_ENABLE_RELATIVE_REDIRECTs) {
                enableRelativeRedirects = item.isChecked();
            } else if (item.getItemId() == MENU_ENABLE_CIRCULAR_REDIRECTS) {
                enableCircularRedirects = item.isChecked();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getDefaultURL() {
        return PROTOCOL + "httpbin.org/redirect/6";
    }

    @Override
    public int getSampleTitle() {
        return R.string.title_redirect_302;
    }

    @Override
    public com.loopj.android.http.AsyncHttpClient getAsyncHttpClient() {
        com.loopj.android.http.AsyncHttpClient ahc = super.getAsyncHttpClient();
        cz.msebera.android.httpclient.client.HttpClient client = ahc.getHttpClient();
        if (client instanceof cz.msebera.android.httpclient.impl.client.DefaultHttpClient) {
            android.widget.Toast.makeText(this,
                    String.format("redirects: %b\nrelative redirects: %b\ncircular redirects: %b",
                            enableRedirects, enableRelativeRedirects, enableCircularRedirects),
                    android.widget.Toast.LENGTH_SHORT
            ).show();
            ahc.setEnableRedirects(enableRedirects, enableRelativeRedirects, enableCircularRedirects);
        }
        return ahc;
    }
}