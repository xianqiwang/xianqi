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

import com.loopj.android.http.sample.WaypointsActivity;
import com.nfp.update.DefDialog.OnOkListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView mListView;
    private ArrayList<String> mList = new ArrayList<String>();
    private static Context context;
    String url;
    private SharedPreferences spref;
    public static Context getInstance(){
        return context;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handlerListView();
        spref = this.getSharedPreferences("debug_comm", 0);
        if(spref.getInt("IS_OPEN", 0)==1){
            finish();
        }
        context = this;
        CommonUtils.verifyStoragePermissions (this);
        CommonUtils.isUpdateFile (context);
        HttpClient.cancleRequest(true);
        UpdateUtil.judgePolState(this, 0);

    }


    public void handlerListView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(getApplicationContext())) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,100);

            }
        }
        final Intent intent = new Intent();
        setContentView(R.layout.activity_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mListView = (ListView) findViewById(R.id.list);
        mList.add(getString(R.string.new_hand_update));
        mList.add(getString(R.string.new_hand_update_settings));
        mList.add(getString(R.string.new_hand_update_version));
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
                    Toast.makeText(this,"ACTION_MANAGE_OVERLAY_PERMISSION",Toast.LENGTH_SHORT).show();;
                }
            }

        }
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
}
