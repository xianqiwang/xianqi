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

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.AbsListView;

public class MainActivity extends Activity {
    private ListView mListView;
    private ArrayList<String> mList = new ArrayList<String>();

    private DefDialog mDefDialog;


    private static Context context;

    public static Context getInstance(){
        return context;
    }

    private int getScale(Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        if (fontScale < 1.1) {
            return 0;
        } else if (fontScale > 1.3) {
            return 2;
        } else {
            return 1;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spref = this.getSharedPreferences("debug_comm", 0);
        if(spref.getInt("IS_OPEN", 0)==1){
            finish();
        }
        context = this;
        final Intent intent = new Intent();
        setContentView(R.layout.activity_list);
        mListView = (ListView) findViewById(R.id.list);

        mList.add(getString(R.string.software_update));
        mList.add(getString(R.string.auto_update));
        mList.add(getString(R.string.update_schedule));


        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        mDefDialog = new DefDialog (this, 0, 0, view, R.style.styledialog);
        mDefDialog.setCancelable(true);
        mDefDialog.setTitle ("Downloading");
        mDefDialog.setMessage (
                "fota is downloading from remote server ,please make sure you have correct ip address!\n Don't interupt this dialog!!");
/*
        mDefDialog.setBackground (android.graphics.Color.GRAY, android.graphics.Color.BLACK);
*/

        mDefDialog.show();


        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(this, R.layout.main_item, mList);
        mListView.setAdapter(myArrayAdapter);
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                switch (position) {
                    case 0:
                        intent.setClass(MainActivity.this, SoftwareUpdate.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(MainActivity.this, AutoUpdate.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, UpdateSchedule.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
           }
        });
        HttpClient.cancleRequest(true);
        UpdateUtil.judgePolState(this, 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class ItemListAdapter extends ArrayAdapter<String> {
        private int resource;
        private Context context;

        public ItemListAdapter(Context context, int resourceId, ArrayList<String> list) {
            super(context, resourceId, list);
            resource = resourceId;
            this.context = context;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LinearLayout listItem = new LinearLayout(getContext());
                convertView = LayoutInflater.from(context).inflate(resource, listItem, true);
            }
            int fontSize = getScale(context);
            if (0 == fontSize) {
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 80));
            } else if (1 == fontSize) {
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 80));
            } else {
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 80));
            }
            ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position));
            return convertView;
        }
    }
}
