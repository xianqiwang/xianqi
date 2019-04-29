package com.nfp.update.widget;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nfp.update.R;
import com.nfp.update.WheelPicker;
import com.nfp.update.WheelPicker.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class TimeFotaDialog  extends Dialog {
    private WheelPicker hour_1;
    private WheelPicker minute_1;
    private TextView textView;
    private Button positive;
    private Button negative;
    private OnTimeSetListener onTimeSetListener;

    public interface OnTimeSetListener{

      void onTimeSet(int hour,int minute);

    }


    public TimeFotaDialog (Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super (context);

        onTimeSetListener=listener;
        View layout = getLayoutInflater ().inflate (R.layout.time_picker_dialog, null);
        setContentView(layout);
        hour_1  = findViewById (R.id.hour);
        hour_1.setSelectedItemPosition (hourOfDay);
        minute_1= findViewById (R.id.minute);
        minute_1.setSelectedItemPosition (minute);
        positive= findViewById (R.id.positive);
        positive.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {

                onTimeSetListener.onTimeSet (hour_1.getSelectedItemPosition (),minute_1.getSelectedItemPosition ());
                cancel ();
            }
        });
        negative= findViewById (R.id.negative);
        negative.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
             cancel ();
            }
        });
        textView= findViewById (R.id.message);
        setWheelPicker (context);

    }

        public void setWheelPicker (Context context) {
            List<String> hours =new ArrayList<String> ();
            List<String> minuts =new ArrayList<String> ();
            for(int i=0;i<60;i++){
                 if(i<10){
                     minuts.add("0"+String.valueOf (i));
                 }else{

                     minuts.add(String.valueOf (i));

                 }

            }
            if(DateFormat.is24HourFormat (context)){
                for(int i=0;i<24;i++){
                    hours.add(String.valueOf (i));
                }
            }else{
                for(int i=1;i<=12;i++){
                    hours.add(String.valueOf (i));
                }
            }

            minute_1.setVisibleItemCount (3);
            minute_1.setData (minuts);
            minute_1.setBackgroundColor (android.graphics.Color.WHITE);
            minute_1.setItemTextColor (android.graphics.Color.BLACK);
            minute_1.setCurtain (true);
            minute_1.setCurved (false);
            minute_1.setCyclic (true);
            minute_1.setItemTextSize (
                    (int) context.getResources ().getDimension (R.dimen.text_size_large));

            hour_1.setVisibleItemCount (3);
            hour_1.setData (hours);
            hour_1.setBackgroundColor (android.graphics.Color.WHITE);
            hour_1.setItemTextColor (android.graphics.Color.BLACK);
            hour_1.setCurtain (true);
            hour_1.setCurved (false);
            hour_1.setCyclic (true);
            hour_1.setItemTextSize (
                    (int) context.getResources ().getDimension (R.dimen.text_size_large));
        }

}
