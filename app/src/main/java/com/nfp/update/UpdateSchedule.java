package com.nfp.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateSchedule extends Activity implements TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener{

    public int scheduleValue = 0;
    private ListView listview;
    private MyAdapter myAdapter;
    private ArrayList<Boolean> checkList = new ArrayList<Boolean>();

    //设置选中的位置，将其他位置设置为未选
    public void checkPosition(int position) {
        for (int i = 0; i < checkList.size(); i++) {
            if (position == i) {// 设置已选位置
              //  Toast.makeText(UpdateSchedule.this,"off",Toast.LENGTH_SHORT).show();
                Log.e("lhc","defaule:"+position);
                if(position==0){
                    SharedPreferences sprefs = getSharedPreferences("debug_comm", 0);

                    SharedPreferences.Editor editor = sprefs.edit();
                    editor.putInt("AUTO_UPDATE", 1);
                    editor.commit();
                    final Calendar calendar = Calendar.getInstance();
                    int hour =0;
                    int minute =0;
                    if(scheduleValue ==1){
                        hour = UpdateUtil.getHourTemp(this);
                        minute =UpdateUtil.getMinuteTemp(this);
                    }else{
                        hour = UpdateUtil.getHour(this);
                        minute = UpdateUtil.getMinute(this);
                    }
                    TimePickerDialog tp = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, true);
                    tp.setTitle(getString(R.string.update_schedule_title));
                    tp.show();
                }else{

                    Toast.makeText(UpdateSchedule.this,getResources().getString(R.string.set_off),Toast.LENGTH_LONG).show();
                    SharedPreferences sprefs = getSharedPreferences("debug_comm", 0);

                    SharedPreferences.Editor editor = sprefs.edit();
                    editor.putInt("AUTO_UPDATE", 0);
                    editor.commit();

                }
                checkList.set(i, true);
            } else {
                //Toast.makeText(UpdateSchedule.this,"on",Toast.LENGTH_SHORT).show();

                checkList.set(i, false);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    public void init() {
        listview = (ListView) findViewById(R.id.list);
        /*for (int i = 0; i < list.size(); i++) {
            checkList.add(false);
        }*/
        SharedPreferences sprefs = getSharedPreferences("debug_comm", 0);

        int defaule=sprefs.getInt("AUTO_UPDATE", 1);

if(defaule==1){
    checkList.add(0,true);
    checkList.add(1,false);
}else{
    checkList.add(0,false);
    checkList.add(1,true);
}

        myAdapter = new MyAdapter(this, list);
        listview.setAdapter(myAdapter);
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        init();
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
            scheduleValue = bundle.getInt("scheduleValue");
        UpdateUtil.cancelScheduleNotification(this);
        showDialog(0);
    }

    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setTime(this, hourOfDay, minute);

        if(scheduleValue ==1){
            UpdateUtil.setTempHourMinute(this, hourOfDay, minute);
            startUpdateSchedule(hourOfDay, minute);
        }else{
            UpdateUtil.setHourMinute(this, hourOfDay, minute);
            if(UpdateUtil.getTempTImeFlag(UpdateSchedule.this)!=1)
                startUpdateSchedule(hourOfDay, minute);
        }

        Toast.makeText(this, R.string.auto_change, Toast.LENGTH_LONG).show();
      //  finish();
    }

    @Override
    public Dialog onCreateDialog(int id) {
        final Calendar calendar = Calendar.getInstance();
        int hour =0;
        int minute =0;
        if(scheduleValue ==1){
            hour = UpdateUtil.getHourTemp(this);
            minute =UpdateUtil.getMinuteTemp(this);
        }else{
            hour = UpdateUtil.getHour(this);
            minute = UpdateUtil.getMinute(this);
        }
        TimePickerDialog tp = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, true);
        tp.setTitle(getString(R.string.update_schedule_title));

        //tp.setOnCancelListener();
        return tp;
    }


   static void setTime(Context context, int hourOfDay, int minute) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmm");
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();

        Date updateDate = (Date) c.getTime();
        String dateTime = formatter.format(updateDate);
        Log.d("kevin","updates chedule dateTime="+ dateTime);
        insertEventLog(context,0, context.getString(R.string.auto_update_setting), 0, dateTime, null, null);
    }

    private static android.net.Uri insertEventLog (Context context, int eventNo, String eventName, int tid, String factor1, String factor2, String factor3) {
        final android.net.Uri uri = android.net.Uri.parse("content://com.ssol.eventlog/eventlog");

        android.content.ContentResolver mContentResolver=context.getContentResolver();

        mContentResolver.acquireContentProviderClient (uri);

        android.content.ContentValues values = new android.content.ContentValues ();

        if (android.text.TextUtils.isEmpty(eventName)) {
            throw new IllegalArgumentException("Invalid event name : " + eventName);
        } else {
            values.put("EVENT_NAME", eventName);
        }

        /*if (tid < 1 || tid > 256) {
            Log.w(TAG, "Invalid tid : " + tid);
        } else {
            values.put("TID", new Integer(tid));
        }*/

        if (! android.text.TextUtils.isEmpty(factor1)) {
            values.put("FACTOR1", factor1);
        }

        if (! android.text.TextUtils.isEmpty(factor2)) {
            values.put("FACTOR2", factor2);
        }

        if (! android.text.TextUtils.isEmpty(factor3)) {
            values.put("FACTOR3", factor3);
        }

        return  mContentResolver.insert (uri,values);

    }
    @android.support.annotation.RequiresApi (api = android.os.Build.VERSION_CODES.KITKAT)
    public void startUpdateSchedule(int hourOfDay, int minute){
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(UpdateSchedule.this);
        SharedPreferences sprefs = getSharedPreferences("debug_comm", 0);
        File files = new File("/fota/softwareupdate.dat");
        String packageFile = spref.getString("PAC_NAME", null);
        if(packageFile!=null&&files.exists()){
            UpdateUtil.stopUpdateService(getApplicationContext(), 1);
            if(sprefs.getInt("AUTO_UPDATE", 0) == 0){
                Log.d("kevin","11133"+ "  hour="+String.valueOf(hourOfDay)+"   minute="+String.valueOf(minute));
                UpdateUtil.startUpdateService(getApplicationContext(), 1);
            }
        }
    }

    public void onCancel(DialogInterface dialog){
        finish();
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


    private ArrayList<String> list = new ArrayList<String>() {
        {
            add("ON");
            add("OFF");

        }
    };

    //自定义adapter
    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ArrayList<String> myList;

        public MyAdapter(Context context, ArrayList<String> myList) {
            this.inflater = LayoutInflater.from(context);
            this.myList = myList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return myList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            Log.i("aaa", "getview");
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.main_item_checkbox, null);
                holder = new ViewHolder();
                holder.txt = (TextView) convertView.findViewById(R.id.txt);
                holder.checkBox = (CheckBox) convertView
                        .findViewById(R.id.checkBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt.setText(myList.get(position));
            holder.checkBox.setChecked(checkList.get(position));
            holder.checkBox
                    .setOnCheckedChangeListener(new OnCheckedChangeListener () {//单击checkbox实现单选，根据状态变换进行单选设置

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {
                                checkPosition(position);
                            } else {
                                checkList.set(position, false);//将已选择的位置设为选择
                            }
                        }
                    });
            convertView.setOnClickListener(new OnClickListener () {//item单击进行单选设置

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    checkPosition(position);
                }
            });

            return convertView;
        }

        public final class ViewHolder {
            public TextView txt;
            public CheckBox checkBox;
        }
    }




}
