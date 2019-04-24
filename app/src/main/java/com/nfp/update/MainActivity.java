package com.nfp.update;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.provider.FontsContract.FontInfo;
import android.provider.Settings;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem;
import android.support.v4.app.ActivityCompat;
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

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.sample.PrePostProcessingSample;
import com.loopj.android.http.sample.WaypointsActivity;

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

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {

    private ListView mListView;
    private ArrayList<String> mList = new ArrayList<String>();
    private DefDialog mDefDialog;
    private static Context context;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private CustomDialog.Builder builder;
    private CustomDialog mDialog_0641_D1;
    String mVersonNumber;
    String url;
    FileInfo fileInfo;
    private  Resources res;
    private SharedPreferences spref;
    private NetworkCheck networkCheck;
    private WakeLock wakeLock;
    private boolean iswakeLock = true;// 是否常亮
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

        spref = this.getSharedPreferences("debug_comm", 0);
        res = this.getResources();


        if(spref.getInt("IS_OPEN", 0)==1){
            finish();
        }
        JudgeManualDialog();
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
        verifyStoragePermissions(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
/*        PowerManager pm=(PowerManager) getSystemService(POWER_SERVICE);
        wakeLock=pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,"Update");
        if (iswakeLock) {
            wakeLock.acquire();
        }*/
    }
    @Override
    protected void onPause() {
        super.onPause ();
/*        if (wakeLock != null) {
            wakeLock.release ();
        }
        Process.killProcess (Process.myPid ());*/
    }

    public void JustForTest(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, WaypointsActivity.class);
        startActivity(intent);

        /*        testDatabase();
          test();*/

    }

    public void JudgeManualDialog(){


        HttpClient.get (this,HttpClient.TEST_URL,null,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess (int statusCode, Header[] headers, byte[] responseBody) {
                String error = new String(responseBody);
                if(error.equals ("error00")){
                    if(spref.getBoolean ("manual_auto",false)){

                        mDialog_0641_D1= new CustomDialog.Builder(MainActivity.this,200,200)
                                .setMessage(res.getString (R.string.only_hand_update))
                                .setSingleButton("Ok", new View.OnClickListener () {
                                    @Override
                                    public void onClick (View v) {
                                        mDialog_0641_D1.dismiss ();
                                    }
                                }).createSingleButtonDialog();
                        mDialog_0641_D1.show ();
                    }
                }
            }

            @Override
            public void onFailure (int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

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

            DialogCategorical.N_0646_S01 (MainActivity.this,msg.what, R.string.fota_install);

        }
    };

    private void test()  {

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

        for(int i=0 ;i<500;i++){

/*            recordStorage.setImeinumber ("123455677888778898");
            recordStorage.setCreatetime (year + "/" + (month+1) + "/" + date + " " +hour + ":" +minute + ":" + second);
            recordStorage.setSettime (year + "/" + (month+1) + "/" + date + " " +hour + ":" +minute + ":" + second);

            recordStorage.setId (i);

            spUtils.put ("i"+i,i);

            databaseUtil.getCount ();

            spUtils.get ("i"+i,0);
            spUtils.clear ();

            Log.v("yingbo","spUtils.get"+spUtils.get ("i"+i,0));*/

        }

        recordStorages= databaseUtil.getScrollData (0,10);

        for(RecordStorage recordStorage1 :recordStorages){
            Log.v ("yingbo","getSettime"+recordStorage1.getSettime ()+recordStorage1.getImeinumber ());
        }

        Log.v ("yingbo","getCount"+databaseUtil.getCount ());

    }

}
