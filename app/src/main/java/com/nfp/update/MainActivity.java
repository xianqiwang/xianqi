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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.res.Resources;
import android.os.Build;
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
import java.util.Date;

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
    String mVersonNumber;
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
             }
             else{
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
                     Resources res =MainActivity.this.getResources();;
                     View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                     DialogCategorical dialogCategorical=new DialogCategorical (MainActivity.this, 0, 0, view);
                     dialogCategorical.A_D_12 (mVersonNumber);

                 }

             });
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
