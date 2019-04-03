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

import android.content.res.Resources;
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
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.BroadcastReceiver;
import java.io.File;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.os.RecoverySystem;

import com.alibaba.fastjson.JSONObject;
public class MainActivity extends Activity {

    private ListView mListView;
    private ArrayList<String> mList = new ArrayList<String>();
    private DefDialog mDefDialog;
    private static Context context;
    private final static String CHECK_KEY = "waterworld";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    String url;
    FileInfo fileInfo;
    private NetworkCheck networkCheck;

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

        networkCheck=new NetworkCheck (this);
        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(this, R.layout.main_item, mList);
        mListView.setAdapter(myArrayAdapter);
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                switch (position) {
                    case 0:

                        Resources res =MainActivity.this.getResources();;
                        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                        DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
                        dialogCategorical.A_N_02 ();
                        dialogCategorical.setCallbackConfirmKey (new DialogCategorical.CallbackConfirmKey () {

                            @Override
                            public void onConfirm () {

/*
                                android.util.Log.v ("yingbo","click");
*/

                                checkNetwork();

                            }
                            @Override
                            public void onCancel () {
/*
                                android.util.Log.v ("yingbo","click");
*/
                                checkNetwork();

                            }
                        });

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

    private void test() {

        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        DialogCategorical dialogCategorical = new DialogCategorical(this, 0, 0, view);
        /**
         * 修改方法和参数
         */
//        "Software Update", false
//        "Software Update", false, "2019/03/28\n2:00-3:00"
        String[] strings = new  String[]{"111","22222","3","3","3","3","3","3","3","3","3","3","3",};
        dialogCategorical.A_N_12();
    }

    public void checkNetwork(){
        if(networkCheck.checkSimCard ()&&networkCheck.isWifi ()){

         if(networkCheck.isNetWorkAvailable()){

         }else{
             Toast toast = Toast.makeText(MainActivity.this,"make sure you have a available network.", Toast.LENGTH_LONG);
             toast.show ();
         }

        }else{


            /*            final Intent intent = new Intent();
            intent.setClass(MainActivity.this, SoftwareUpdate.class);
            startActivity(intent);*/
/*            Toast toast = Toast.makeText(MainActivity.this,"Input Icc card or make sure wifi is opened.", Toast.LENGTH_LONG);
            toast.show ();*/

        }


/*        checkUpdate();*/

/*
        SoftwareUpdate softwareUpdate=new SoftwareUpdate (this);
*/
    }

    private static String getUserAgent() {
        String userAgent = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = android.webkit.WebSettings.getDefaultUserAgent(getInstance());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
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
