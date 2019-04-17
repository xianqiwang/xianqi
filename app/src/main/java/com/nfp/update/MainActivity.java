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

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.provider.Settings;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private CustomDialog.Builder builder;
    private CustomDialog mDialog;
    String mVersonNumber;
    String url;
    FileInfo fileInfo;
    private  Resources res;
    private NetworkCheck networkCheck;

    public static Context getInstance(){
        return context;
    }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                    params.format = PixelFormat.RGBA_8888;
                   // windowManager.addView(view,params);
                }else {
                    Toast.makeText(this,"ACTION_MANAGE_OVERLAY_PERMISSION权限已被拒绝",Toast.LENGTH_SHORT).show();;
                }
            }

        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spref = this.getSharedPreferences("debug_comm", 0);
        res = this.getResources();
        if(spref.getInt("IS_OPEN", 0)==1){
            finish();
        }

        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(getApplicationContext())) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,100);
            }
        }
        handlerListView();
        HttpClient.cancleRequest(true);
        UpdateUtil.judgePolState(this, 0);
        boolean manually=false;
        //此处判断是否设定手动更新
        if(manually){

            mDialog= new CustomDialog.Builder(MainActivity.this,200,200)
                    .setMessage(res.getString (R.string.only_hand_update))
                    .setSingleButton("Ok", new View.OnClickListener () {

                        @Override
                        public void onClick (View v) {
                            mDialog.dismiss ();
                        }

                    }).createSingleButtonDialog();
            mDialog.show ();

        }

        testDatabase();
        test();
    }

     public String stampToDate(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public void handlerListView(){

        final Intent intent = new Intent();
        setContentView(R.layout.activity_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mListView = (ListView) findViewById(R.id.list);
        mList.add(getString(R.string.new_hand_update));
        mList.add(getString(R.string.new_hand_update_settings));
        mList.add(getString(R.string.new_hand_update_version));
        networkCheck=new NetworkCheck (this);
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
                        intent.setClass(MainActivity.this, UpdateSchedule.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, AutoUpdate.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }




  //检查服务器上是否有新版本

boolean checkSoftwareVersion(String softwareversion){
   if(Build.DISPLAY.equals(softwareversion))
       return false;

   return true;

}
public void A_D_12_end(){
    Resources res =MainActivity.this.getResources();;
    View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
    DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
    //A-D-12
    dialogCategorical.A_D_12 (mVersonNumber);
    ListView  listView=dialogCategorical.getmDefDialog().getListView();
    listView.setOnItemClickListener(new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1,
                                int position, long arg3) {
            switch (position) {
                case 0:
                    Intent  notificationIntent = new android.content.Intent (context, com.nfp.update.DownloadProgress.class);
                    //notificationIntent.putExtra("DOWNLOAD_FROM_NOTIFICATION", true);
                    PendingIntent contentIntent = android.app.PendingIntent.getActivity (context, 0, notificationIntent, 0);
                    UpdateUtil.setDownloadNotification (context, true);
                    break;
                case 1:
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, UpdateSchedule.class);
                    startActivity(intent);
                    break;
                case 2:
                    Resources res =MainActivity.this.getResources();
                    View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                    DialogCategorical dialogCategorical=new DialogCategorical (context, 0, 0, view);
                    //A-D-21 服务器传来的array
                    String[] str={"123","321","546"};
                    dialogCategorical.A_D_21 (str);
                    break;
                case 3:
                    Resources res1 =MainActivity.this.getResources();
                    View view1= getLayoutInflater().inflate(R.layout.dialog_layout, null);
                    DialogCategorical dialogCategorical1=new DialogCategorical (context, 0, 0, view1);
                    //A-D-21 服务器传来的array
                    String[] str1={"123","321","546"};
                    String str2="123";
                    dialogCategorical1.A_D_03(str1);
                    dialogCategorical1.setCallbackConfirmKey (new DialogCategorical.CallbackConfirmKey () {

                        @Override
                        public void onConfirm () {

                            android.util.Log.v ("yingbo","click");


                        }
                        @Override
                        public void onCancel () {

                            Resources res =MainActivity.this.getResources();;
                            View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                            DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
                            dialogCategorical.A_D_07 ();
                            dialogCategorical.setCallbackConfirmKey (new DialogCategorical.CallbackConfirmKey () {
                                @Override
                                public void onConfirm () {

                                    android.util.Log.v ("yingbo","click");


                                }
                                @Override
                                public void onCancel () {

                                    android.util.Log.v ("yingbo","click");


                                }
                            });
                        }

                    });
                    ListView  listView=dialogCategorical1.getmDefDialog().getListView();
                    listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int position, long arg3) {
                            switch (position) {
                                case 0:

                                    break;
                                case 1:

                                    break;
                                case 2:

                                    break;
                                case 3:

                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                    break;
                default:
                    break;
            }
        }
    });


}

    public void A_D_20_end(){
        Resources res =MainActivity.this.getResources();;
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
        //A-D-12
        dialogCategorical.A_D_20 (mVersonNumber);
        ListView  listView=dialogCategorical.getmDefDialog().getListView();
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                switch (position) {
                    case 0:
                        Intent  notificationIntent = new android.content.Intent (context, com.nfp.update.DownloadProgress.class);
                        //notificationIntent.putExtra("DOWNLOAD_FROM_NOTIFICATION", true);
                        PendingIntent contentIntent = android.app.PendingIntent.getActivity (context, 0, notificationIntent, 0);
                        UpdateUtil.setDownloadNotification (context, true);
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, UpdateSchedule.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Resources res =MainActivity.this.getResources();
                        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                        DialogCategorical dialogCategorical=new DialogCategorical (context, 0, 0, view);
                        //A-D-21 服务器传来的array
                        String[] str={"123","321","546"};
                        dialogCategorical.A_D_21 (str);
                        break;
                    case 3:
                        Resources res1 =MainActivity.this.getResources();
                        View view1= getLayoutInflater().inflate(R.layout.dialog_layout, null);
                        DialogCategorical dialogCategorical1=new DialogCategorical (context, 0, 0, view1);
                        //A-D-21 服务器传来的array
                        String[] str1={"123","321","546"};
                        String str2="123";
                        dialogCategorical1.A_D_03(str1);
                        dialogCategorical1.setCallbackConfirmKey (new DialogCategorical.CallbackConfirmKey () {

                            @Override
                            public void onConfirm () {

                                android.util.Log.v ("yingbo","click");


                            }
                            @Override
                            public void onCancel () {

                                Resources res =MainActivity.this.getResources();;
                                View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                                DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
                                dialogCategorical.A_D_07 ();
                                dialogCategorical.setCallbackConfirmKey (new DialogCategorical.CallbackConfirmKey () {
                                    @Override
                                    public void onConfirm () {

                                        android.util.Log.v ("yingbo","click");


                                    }
                                    @Override
                                    public void onCancel () {

                                        android.util.Log.v ("yingbo","click");


                                    }
                                });
                            }

                        });
                        ListView  listView=dialogCategorical1.getmDefDialog().getListView();
                        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1,
                                                    int position, long arg3) {
                                switch (position) {
                                    case 0:

                                        break;
                                    case 1:

                                        break;
                                    case 2:

                                        break;
                                    case 3:

                                        break;
                                    default:
                                        break;
                                }
                            }
                        });

                        break;
                    case 4:
                        //清楚设置的计划时间的prefence
                        break;
                    default:
                        break;
                }
            }
        });


    }

  public void A_D_17_end(){
      Resources res =MainActivity.this.getResources();;
      View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
      DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
      String str0="20190402";
      dialogCategorical.A_D_17(str0);
      dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {
          @Override
          public void onConfirm() {
              A_D_20_end();
          }

          @Override
          public void onCancel() {
              Resources res =MainActivity.this.getResources();;
              View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
              DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
              dialogCategorical.A_D_19 ();
              dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {
                  @Override
                  public void onConfirm() {

                  }

                  @Override
                  public void onCancel() {
                      A_D_12_end();
                  }
              });
          }
      });

  }

    public void checkNetwork(){
        if(networkCheck.checkSimCard ()&&networkCheck.isWifi ()){

         if(networkCheck.isNetWorkAvailable()){
if(checkSoftwareVersion(mVersonNumber))  {
                 SharedPreferences sprefs = context.getSharedPreferences("debug_comm", 0);
                 if(sprefs.getInt("AUTO_UPDATE", 0)==0){
                     //A-D-12
                     A_D_12_end();
                 }
                 else{
                     //自动更新开关 on
                 Resources res =MainActivity.this.getResources();
                 View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                 DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
                   String time="2019-05-01 10:00";
                   int i=2;
                   //更新次数
                   if(i>1){

                       // A-D-14 ok->A-D-12
                       dialogCategorical.A_D_14("n");
                       dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {

                           @Override
                           public void onConfirm() {

                               A_D_12_end();

                           }

                           @Override
                           public void onCancel() {

                               // finish();

                           }

                       });
                       //A-D-14->A-D-16
                       dialogCategorical.A_D_14("n");
                       dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {
                           @Override
                           public void onConfirm() {
                               Resources res = MainActivity.this.getResources();
                               ;
                               View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                               DialogCategorical dialogCategorical = new DialogCategorical(MainActivity.this, 0, 0, view);
                               dialogCategorical.A_D_16();
                               dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {
                                   @Override
                                   public void onConfirm() {
                                       A_D_12_end();
                                   }

                                   @Override
                                   public void onCancel() {
                                       Resources res = MainActivity.this.getResources();
                                       ;
                                       View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                                       DialogCategorical dialogCategorical = new DialogCategorical(MainActivity.this, 0, 0, view);
                                       dialogCategorical.A_D_19();
                                       dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {
                                           @Override
                                           public void onConfirm() {
                                               //返回主界面
                                           }

                                           @Override
                                           public void onCancel() {
//返回上一页
                                           }
                                       });
                                   }
                               });
                           }

                           @Override
                           public void onCancel() {

                           }
                       });
                       //A-D-14->A-D-17
                       dialogCategorical.A_D_14("n");
                       dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {
                           @Override
                           public void onConfirm() {
                               A_D_17_end();
                           }

                           @Override
                           public void onCancel() {

                           }
                       });
                   }else{
                     if(time!=null&&dateDiff(time)){
                         String str0="20190402";
                         dialogCategorical.A_D_17(str0);
                         dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {
                             @Override
                             public void onConfirm() {
                                 A_D_20_end();
                             }

                             @Override
                             public void onCancel() {
                                 Resources res =MainActivity.this.getResources();;
                                 View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                                 DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
                                 dialogCategorical.A_D_19 ();
                                 dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {
                                     @Override
                                     public void onConfirm() {

                                     }

                                     @Override
                                     public void onCancel() {
                                         A_D_12_end();
                                     }
                                 });
                             }
                         });

                     }
else {

                         // A-D-15 ->A-D-18
                         dialogCategorical.A_D_15();
                         dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {

                             @Override
                             public void onConfirm() {

                                 A_D_12_end();

                             }

                             @Override
                             public void onCancel() {

                                 Resources res = MainActivity.this.getResources();
                                 ;
                                 View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                                 DialogCategorical dialogCategorical = new DialogCategorical(MainActivity.this, 0, 0, view);
                                 dialogCategorical.A_D_18();
                                 dialogCategorical.setCallbackConfirmKey(new DialogCategorical.CallbackConfirmKey() {

                                     @Override
                                     public void onConfirm() {
                                         //返回主界面
                                         android.util.Log.v("yingbo", "click");


                                     }

                                     @Override
                                     public void onCancel() {

                                         finish();

                                     }

                                 });
                             }

                         });
                     }
             }}
             } else {
                 Resources res =MainActivity.this.getResources();;
                 View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                 DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
                 dialogCategorical.A_D_13 ();
                 dialogCategorical.setCallbackConfirmKey (new DialogCategorical.CallbackConfirmKey () {

                     @Override
                     public void onConfirm () {

                         android.util.Log.v ("yingbo","click");

                         finish();

                     }
                     @Override
                     public void onCancel () {



                     }
                 });

             }

         }else{
             Resources res =MainActivity.this.getResources();;
             View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
             DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
             dialogCategorical.A_D_13 ();
             dialogCategorical.setCallbackConfirmKey (new DialogCategorical.CallbackConfirmKey () {

                 @Override
                 public void onConfirm () {

                     android.util.Log.v ("yingbo","click");

                     finish();

                 }
                 @Override
                 public void onCancel () {

                     android.util.Log.v ("yingbo","click");
                     Resources res =MainActivity.this.getResources();
                     View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                     DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
                     dialogCategorical.A_D_12 (mVersonNumber);

                 }

             });
         }

        }else{

            /*final Intent intent = new Intent();
            intent.setClass(MainActivity.this, SoftwareUpdate.class);
            startActivity(intent); */
/*          Toast toast = Toast.makeText(MainActivity.this,"Input Icc card or make sure wifi is opened.", Toast.LENGTH_LONG);
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

    public boolean dateDiff(String Time) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            Date date = new Date(System.currentTimeMillis());
            String times_now=sd.format(date);
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(Time).getTime()
                    - sd.parse(times_now).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒

            if (day>=1) {
                return true;
            }else {
                if (day==0) {
                    return false;
                }else {
                    return false;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }
    private DialogCategorical dialogCategorical;
    private int i=0;
    final Handler handler = new Handler (){

        @Override
        public void handleMessage (Message msg) {

            super.handleMessage (msg);

            dialogCategorical.N_0646_S01 (msg.what, R.string.fota_install);

        }
    };



    private void test()  {

/*        try {

            RecoverySystem.installPackage(this,new File ("/data/fota/updata.zip"));

        } catch (IOException e) {
            e.printStackTrace ();
            Log.v ("yingbo", "e.printStackTrace ()" + e);

        }*/
        View view = getLayoutInflater ().inflate (R.layout.dialog_layout, null);

        dialogCategorical = new DialogCategorical (this, 0, 0, view);

            handler.postDelayed (new Runnable () {
                Message msg=new Message ();
                @Override
                public void run () {
          /*          try {
                        test();
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }*/
                    i++;
                    if(i>100)
                    {
                      return;
                    }
                    msg.what=i;
      /*              handler.sendMessage (msg);*/

                }
            }, 1000);
    }


    String path;
    //String path = "/storage/emulated/0/Android/data/com.example.a14553.localdocument/files/exter_test/aaaTest";
    String fileName;
    private void testDir(){

        /*
        *
        *  
 外部存储
    Environment.getExternalStorageDirectory()	SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
    context.getExternalFilesDir(dir)	路径为:/mnt/sdcard/Android/data/< package name >/files/… 
    context.getExternalCacheDir()	路径为:/mnt/sdcard//Android/data/< package name >/cach/…

 内部存储
	context.getFilesDir()	路径是:/data/data/< package name >/files/…
    context.getCacheDir()	路径是:/data/data/< package name >/cach/…

        * */



        path =getExternalFilesDir("exter_test").getPath();


        Log.v ("yingbo","path"+path);
        fileName = "test.txt";


        //check();
        //newFile(path,fileName);
        //readFile(path,fileName);
        // save(edt.getText().toString());

    }
    public void save(String inputText){
        String data = "data to save";
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            //创建其他程序不可见的文件
            //out = openFileOutput(path+"/"+fileName, Context.MODE_PRIVATE);
            //打开本地文件
            writer = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (path+"/"+fileName)));
            writer.write(inputText);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null)
                    writer.close();
            }catch (IOException e){
                //Toast.makeText(this,"Wrong1",Toast.LENGTH_LONG).show();
            }
        }

    }

    public void check(){
        String res = "";
        Log.i("codecraeer", "getFilesDir = "+getFilesDir());
        res+="getFilesDir = "+getFilesDir()+"\n";
        Log.i("codecraeer", "getExternalFilesDir = "+getExternalFilesDir("exter_test").getAbsolutePath());
        res+="getExternalFilesDir = "+getExternalFilesDir("exter_test").getAbsolutePath()+"\n";
        Log.i("codecraeer", "getDownloadCacheDirectory = " + Environment.getDownloadCacheDirectory().getAbsolutePath());
        res+="getDownloadCacheDirectory = "+Environment.getDownloadCacheDirectory().getAbsolutePath()+"\n";
        Log.i("codecraeer", "getDataDirectory = " + Environment.getDataDirectory().getAbsolutePath());
        res+="getDataDirectory = "+Environment.getDataDirectory().getAbsolutePath()+"\n";
        Log.i("codecraeer", "getExternalStorageDirectory = " + Environment.getExternalStorageDirectory().getAbsolutePath());
        res+="getExternalStorageDirectory = "+Environment.getExternalStorageDirectory().getAbsolutePath()+"\n";
        Log.i("codecraeer", "getExternalStoragePublicDirectory = " + Environment.getExternalStoragePublicDirectory("pub_test"));
        res+="getExternalStoragePublicDirectory = "+Environment.getExternalStoragePublicDirectory("pub_test")+"\n";

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            //We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else{
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

    }


    public static String getFilePath(Context context,String dir) {
        String directoryPath="";
//判断SD卡是否可用 
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {
            directoryPath =context.getExternalFilesDir(dir).getAbsolutePath() ;
// directoryPath =context.getExternalCacheDir().getAbsolutePath() ;  
        }else{
//没内存卡就存机身内存  
            directoryPath=context.getFilesDir()+File.separator+dir;
// directoryPath=context.getCacheDir()+File.separator+dir;
        }
        File file = new File(directoryPath);
        if(!file.exists()){//判断文件目录是否存在  
            file.mkdirs();
        }
        return directoryPath;
    }

    public void newFile(String _path,String _fileName){
        File file=new File(_path+"/"+_fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readFile(String _path,String _fileName){
        String res = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader (new FileInputStream (_path+"/"+_fileName)));
            String line = "";
            while ((line = reader.readLine())!=null){
                res+=line;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return res;
    }
















    private void testDatabase(){

        DatabaseUtil databaseUtil=new DatabaseUtil (MainActivity.this,
                "my.db",null,1);

        RecordStorage recordStorage=new RecordStorage ();

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        SPUtils spUtils=new SPUtils (this);

        List<RecordStorage> recordStorages=new ArrayList<RecordStorage> ();

        for(int i=0 ;i<100;i++){

            recordStorage.setImeinumber ("123455677888778898");
            recordStorage.setCreatetime (year + "/" + (month+1) + "/" + date + " " +hour + ":" +minute + ":" + second);
            recordStorage.setSettime (year + "/" + (month+1) + "/" + date + " " +hour + ":" +minute + ":" + second);

            recordStorage.setId (i);

            spUtils.put ("i"+i,i);

            databaseUtil.getCount ();

            spUtils.get ("i"+i,0);
            spUtils.clear ();

            Log.v("yingbo","spUtils.get"+spUtils.get ("i"+i,0));

        }

        recordStorages= databaseUtil.getScrollData (0,10);

        for(RecordStorage recordStorage1 :recordStorages){
            Log.v ("yingbo","getSettime"+recordStorage1.getSettime ()+recordStorage1.getImeinumber ());
        }

        Log.v ("yingbo","getCount"+databaseUtil.getCount ());

    }
}
