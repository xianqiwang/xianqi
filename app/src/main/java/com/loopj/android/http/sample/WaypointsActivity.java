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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.nfp.update.R;
import java.util.ArrayList;
import java.util.List;

public class WaypointsActivity extends android.app.ListActivity {

    private static final com.loopj.android.http.sample.WaypointsActivity.SampleConfig[] samplesConfig = new com.loopj.android.http.sample.WaypointsActivity.SampleConfig[]{
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_get_sample, com.loopj.android.http.sample.GetSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_post_sample, com.loopj.android.http.sample.PostSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_delete_sample, com.loopj.android.http.sample.DeleteSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_put_sample, com.loopj.android.http.sample.PutSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_patch_sample, com.loopj.android.http.sample.PatchSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_head_sample, com.loopj.android.http.sample.HeadSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_json_sample, com.loopj.android.http.sample.JsonSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_json_streamer_sample, JsonStreamerSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_sax_example, com.loopj.android.http.sample.SaxSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_file_sample, com.loopj.android.http.sample.FileSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_directory_sample, com.loopj.android.http.sample.DirectorySample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_binary_sample, com.loopj.android.http.sample.BinarySample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_gzip_sample, com.loopj.android.http.sample.GzipSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_redirect_302, com.loopj.android.http.sample.Redirect302Sample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_threading_timeout, ThreadingTimeoutSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_cancel_all, CancelAllRequestsSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_cancel_handle, CancelRequestHandleSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_cancel_tag, CancelRequestByTagSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_synchronous, SynchronousClientSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_intent_service_sample, IntentServiceSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_post_files, com.loopj.android.http.sample.FilesSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_persistent_cookies, PersistentCookiesSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_custom_ca, com.loopj.android.http.sample.CustomCASample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_retry_handler, RetryRequestSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_range_sample, RangeResponseSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_401_unauth, com.loopj.android.http.sample.Http401AuthSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_pre_post_processing, PrePostProcessingSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_content_type_http_entity, ContentTypeForHttpEntitySample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_resume_download, ResumeDownloadSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_digest_auth, com.loopj.android.http.sample.DigestAuthSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_use_pool_thread, UsePoolThreadSample.class),
            new com.loopj.android.http.sample.WaypointsActivity.SampleConfig(R.string.title_request_params_debug, RequestParamsDebug.class)
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setListAdapter(new android.widget.ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getTitlesList()));
    }

    private java.util.List<String> getTitlesList() {
        java.util.List<String> titles = new java.util.ArrayList<>();
        for (com.loopj.android.http.sample.WaypointsActivity.SampleConfig config : samplesConfig) {
            titles.add(getString(config.titleId));
        }
        return titles;
    }

    @Override
    protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
        if (position >= 0 && position < samplesConfig.length)
            startActivity(new android.content.Intent(this, samplesConfig[position].targetClass));
    }

    private static class SampleConfig {

        final int titleId;
        final Class targetClass;

        SampleConfig(int titleId, Class targetClass) {
            this.titleId = titleId;
            this.targetClass = targetClass;
        }

    }

}
