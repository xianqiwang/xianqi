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
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
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

/*
        dialogMothed();
*/

        /*View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        DialogCategorical dialog=new  DialogCategorical(MainActivity.this, 50, 50 ,
                view);
        Resources r=MainActivity.this.getResources();
dialog.A_D_12(true,r.getString(R.string.software_update),false,r.getString(R.string.Signal_prompt));*/

/*        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        DialogCategorical dialogCategorical=new DialogCategorical (this, 0, 0, view);
        dialogCategorical.B_D_11 ("fata",true);
        dialogCategorical.setCallbackConfirmKey (new com.nfp.update.DialogCategorical.CallbackConfirmKey () {
            @Override
            public void onConfirm () {

            }
        });*/
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
                        dialogCategorical.A_N_02 (res.getString (com.nfp.update.R.string.Signal_prompt));
                        dialogCategorical.setCallbackConfirmKey (new DialogCategorical.CallbackConfirmKey () {

                            @Override
                            public void onConfirm () {

                                android.util.Log.v ("yingbo","click");

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

    private void init() {

        verifyStoragePermissions(this);
        //注册广播接收器
        android.content.IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        registerReceiver(mReceiver, filter);

        //创建文件信息对象
        url = "https://www.imooc.com/mobile/mukewang.apk";
        //fileInfo = new FileInfo(0, url, "mukewang.apk", 0, 0);
        fileInfo = downloadFota();
        android.util.Log.v ("yingbo",fileInfo.getFileName());

    }

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FileInfo downloadFota() {
        String baseurl="https://www.waterworld.xin:8444/fota/downloadDeltaVersion?";
        String filecode="61";
        String time=""+System.currentTimeMillis();
        String token=MD5Util.getMD5(filecode+CHECK_KEY+time);
        String url=baseurl+"filecode="+filecode;
        url=url+"&"+"time="+time;
        url=url+"&"+"token="+token;
        return new FileInfo(0, url, "update.zip", 0, 0);
    }

    android.content.BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished", 0);//标志下载成功
                String fingerprint = intent.getStringExtra("fingerprint");
                String md5 = intent.getStringExtra("md5");
                boolean isComplete = intent.getBooleanExtra("complete", false);

                if (isComplete) {
                    final java.io.File file = new File(DownloadService.DOWNLOAD_PATH + "/" + fileInfo.getFileName());
                    String mMd5ByFile = null;
                    try {
                        mMd5ByFile = com.nfp.update.MD5Util.getMd5ByFile(file);
                    } catch (java.io.FileNotFoundException e) {
                        e.printStackTrace ();
                    }
                    android.util.Log.e("yingbo", "mMd5ByFile : " + mMd5ByFile);
                    if (md5.equals(mMd5ByFile)) {
                        android.util.Log.e("yingbo","文件MD5校验成功");
                        updateFirmware(file);
                    }
                }
            }
        }
    };
    public void updateFirmware(File packageFile) {

        if (!packageFile.exists()) {
            android.util.Log.d("yingbo", "packageFile not exists");
            return ;
        }

        try {
            android.util.Log.d("yingbo", "installPackage");
            android.os.RecoverySystem.installPackage(this, packageFile);

        } catch (Exception e) {
            android.util.Log.e("yingbo", "install  failure : " + e.toString());
            e.printStackTrace();
        }

    }

    private void checkUpdate() {
        String CHECKUPDATE_URL="";
        JSONObject json=new JSONObject();
        String hardware="MT6739";
        String system="android7.1";
        String board="yk915";
        String customer="ddd";

        int build=1;
        long time=System.currentTimeMillis();
        String token= MD5Util.getMD5(hardware+system+board+customer+build+CHECK_KEY+time);
        json.put("hardware", hardware);
        json.put("system", system);
        json.put("board", board);
        json.put("customer", customer);
        json.put("build", build);
        json.put("time", time);
        json.put("token", token);

        HttpConnectionUtil http = new HttpConnectionUtil();
        final String result = http.postDataToServer(CHECKUPDATE_URL, json.toString());
        if (result != null) {
            android.util.Log.e("yingbo", "post result : " + result);
            String mResult = JSONObject.parseObject(result).getString("result");
            if (mResult != null && mResult.equals("1")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer mFilecode = JSONObject.parseObject(result).getInteger("filecode");

                        String mVersonNumber = JSONObject.parseObject(result).getString("versonNumber");

                        String mSize = JSONObject.parseObject(result).getString("size");

                        Long mReleaseDate = JSONObject.parseObject(result).getLong("releaseDate");

                        String mVersonNote = JSONObject.parseObject(result).getString("versonNote");

                        String mType = JSONObject.parseObject(result).getString("type");


                        Intent intent = new Intent(MainActivity.this, DownloadService.class);
                        intent.setAction(DownloadService.ACTION_START);
                        intent.putExtra("fileinfo", fileInfo);
                        startService(intent);

                    }
                });
            }
        }
    }

    private void download() {

        String baseurl="";
        String filecode="61";
        String time=""+System.currentTimeMillis();
        String token=MD5Util.getMD5(filecode+CHECK_KEY+time);
        String url=baseurl+"filecode="+filecode;
        url=url+"&"+"time="+time;
        url=url+"&"+"token="+token;
        HttpConnectionUtil http = new HttpConnectionUtil();
        String result = http.postDataToServer(url, "");
    }

    public void checkNetwork(){
        if(networkCheck.checkSimCard ()&&networkCheck.isWifi ()){

         if(networkCheck.isNetWorkAvailable()){



         }else{
             Toast toast = Toast.makeText(MainActivity.this,"make sure you have a available network.", Toast.LENGTH_LONG);
             toast.show ();
         }

        }else{


            final Intent intent = new Intent();
            intent.setClass(MainActivity.this, SoftwareUpdate.class);
            startActivity(intent);

            Toast toast = Toast.makeText(MainActivity.this,"Input Icc card or make sure wifi is opened.", Toast.LENGTH_LONG);
            toast.show ();

        }
    }

    public void dialogMothed(){

        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        mDefDialog = new DefDialog (this, 0, 0, view, R.style.styledialog);
        mDefDialog.setCancelable(true);
        mDefDialog.setTitle ("Downloading");

        ArrayList<String> items =new ArrayList<String> ();

        items.add ("1");
        items.add ("2");
        items.add ("3");
        items.add ("5");
        items.add ("7");
        items.add ("9");


        mDefDialog.setMessage (
                "fota is downloading from remote server ,please make sure you have correct ip address!\n Don't interupt this dialog!!");
/*
        mDefDialog.witchNeedOnlyKey ();
*/

        mDefDialog.setOkClickListener (new DefDialog.OnOkListener(){

            @Override
            public void onOkKey () {

          /*      Toast toast = Toast.makeText(MainActivity.this,"you click me!!! please let me update!!", Toast.LENGTH_LONG);
                toast.show ();*/
/*
                startActivity(new Intent(com.nfp.update.MainActivity.this, com.nfp.update.DialogText.class));
*/
/*
                startActivity(new Intent(com.nfp.update.MainActivity.this, com.nfp.update.TestProgress.class));
*/
            }

            @Override
            public void onCenterKey () {

/*
                startActivity(new Intent(MainActivity.this, com.nfp.update.ProgressActivity.class));
*/

         /*       Toast toast = Toast.makeText(MainActivity.this,"you click center key!!!  Update Stoped!!", Toast.LENGTH_LONG);
                toast.show ();*/

            }

            @Override
            public void onSpinnerSelect () {

            }

        });



        ArrayList<entity> mData = new java.util.ArrayList<entity> ();

/*        mData.add(new entity("1.Set date"));
        mData.add(new entity("2.Select time zone"));
        mData.add(new entity("3.Select time"));
        mData.add(new entity("4.Use 24-hour format"));*/

        mData.add(new entity("Next"));
        mData.add(new entity("2"));
        mData.add(new entity("3"));
        mData.add(new entity("4"));
        mData.add(new entity("5"));
        mData.add(new entity("6"));
        mData.add(new entity("7"));
        mData.add(new entity("8"));

        mDefDialog.setSpinner (mData);

/*
        mDefDialog.setBackground (android.graphics.Color.GRAY, android.graphics.Color.BLACK);
*/

        mDefDialog.setListviewDialog (this,items);

        mDefDialog.show();

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
