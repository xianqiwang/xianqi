package com.nfp.update.widget;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.nfp.update.CustomNumberPicker;
import com.nfp.update.PickerView;
import com.nfp.update.R;
import com.nfp.update.WheelPicker;
import com.nfp.update.WheelPicker.OnItemSelectedListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TimeFotaDialog  extends Dialog {
    private CustomNumberPicker hour_1;
    private CustomNumberPicker minute_1;
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
        Log.v ("yingbo","TimeFotaDialog"+hourOfDay+":"+minute);

        hour_1  = findViewById (R.id.hour);
        setNumberPickerDividerColor(hour_1,context);
        minute_1= findViewById (R.id.minute);
        setNumberPickerDividerColor(minute_1,context);
        setWheelPicker (context);
        hour_1.setValue (hourOfDay);
        minute_1.setValue (minute);
        positive= findViewById (R.id.positive);
        positive.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {

                onTimeSetListener.onTimeSet (hour_1.getValue ()
                        ,minute_1.getValue ());
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

    }

        public void setWheelPicker (Context context) {
             minute_1.setMaxValue(59);
             minute_1.setMinValue (0);
             minute_1.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    String data;
                    if (value < 10) {
                        data = "0" + value;
                    } else {
                        data = String.valueOf(value);
                    }
                    return data;
                }
            });


            if(DateFormat.is24HourFormat (context)){

                    hour_1.setMaxValue(23);
                    hour_1.setMinValue (0);

            }else{
                    hour_1.setMaxValue(12);
                    hour_1.setMinValue (1);

            }

            minute_1.setWrapSelectorWheel(true);
            hour_1.setWrapSelectorWheel(true);

        }

    private void setNumberPickerDividerColor(CustomNumberPicker numberPicker,Context context) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {

                    pf.set(picker, new ColorDrawable (context.getResources().getColor(R.color.white)));

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


}
