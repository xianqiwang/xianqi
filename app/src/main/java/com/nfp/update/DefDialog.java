package com.nfp.update;

import android.app.Dialog;
import android.content.Context;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Button;
import android.view.WindowManager.LayoutParams;
import android.app.AlertDialog;
import android.view.KeyEvent;
class DefDialog extends Dialog {
    TextView mTitle;
    TextView mMessage;
    Button   mConfirm;
    Button   mCancel;
    android.content.Context mContext;
    OnOkListener mOnCenterKeyListener;

    //    style引用style样式
    public DefDialog(Context context,
                     int width, int height,
                     View layout, int style) {

        super(context, style);

        setContentView(layout);
        mTitle=layout.findViewById (R.id.tip);
        mMessage=layout.findViewById (R.id.message);
        mCancel=layout.findViewById (R.id.buttoncancel);
        mConfirm=layout.findViewById (R.id.buttonconfirm);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width= LayoutParams.MATCH_PARENT;
        params.height= LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(params);
        mContext=context;
        mCancel.setOnClickListener (new View.OnClickListener () {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick (android.view.View v) {
                cancel ();
            }

        });
    }
    public void setBackground(int color,int font){
        mTitle.setBackgroundColor (color);
        mTitle.setTextColor (font);
/*
        mMessage.setBackgroundColor (color);
*/
        mMessage.setTextColor (font);
        mConfirm.setTextColor (font);
        mCancel.setTextColor (font);
        mConfirm.setBackgroundColor (color);
        mCancel.setBackgroundColor (color);
    }
    public void setTitle(String title){
        mTitle.setText (title);
    }
    public String getTitle(){
        return (String) mTitle.getText ();
    }

    public void setMessage(String message){
        mMessage.setText (message);
    }
    public String getMessage(){
        return (String) mMessage.getText ();
    }

    public void setButtonCancel(String text){
        mCancel.setText (text);
    }
    public String getButtonCancel(){
      return (String) mCancel.getText ();
    }

    public void setButtonConfirm(String text){
        mConfirm.setText (text);
    }
    public String getButtonConfirm(){
        return (String) mConfirm.getText ();
    }
    public void onClickOk(){

        mCancel.setOnClickListener (new View.OnClickListener () {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick (android.view.View v) {

                mOnCenterKeyListener.onOkKey();

            }

        });

    }


    public void witchNeedOnlyKey(){
    mCancel.setVisibility (android.view.View.GONE);
    }

/*    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if(mOnCenterKeyListener != null ){
                    mOnCenterKeyListener.onOkKey();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }*/


    public void setOkClickListener(OnOkListener listener) {
        mOnCenterKeyListener = listener;
    }

    public interface OnOkListener
    {
        public void onOkKey();
    }
    @Override
    public int hashCode () {
        return super.hashCode ();
    }
}
