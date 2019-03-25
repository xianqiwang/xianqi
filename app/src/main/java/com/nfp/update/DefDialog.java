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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.SimpleAdapter;
import android.content.DialogInterface;


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
        mConfirm.setOnClickListener (new View.OnClickListener () {
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

    private void setListviewDialog(android.content.Context context,List data) {

        //自定义一个布局文件
        android.widget.LinearLayout linearLayoutMain = new LinearLayout(context);
        linearLayoutMain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        //自定义一个listview
        android.widget.ListView listView = new ListView(context);
        listView.setFadingEdgeLength(0);

        //建立一个数组存储listview上显示的数据
        java.util.List<java.util.Map<String, String>> nameList = new java.util.ArrayList<java.util.Map<String, String>> ();
        for (int m = 0; m < data.size(); m++) {//initData为一个list类型的数据源
            Map<String, String> nameMap = new java.util.HashMap<String, String> ();
            nameMap.put("name", data.get(m).toString());
            nameList.add(nameMap);
        }

        android.widget.SimpleAdapter adapter = new SimpleAdapter(context, nameList, R.layout.main_item,
                new String[] { "name" }, null);
        listView.setAdapter(adapter);

        linearLayoutMain.addView(listView);//往这个布局中加入listview

        final AlertDialog dialog = new AlertDialog.Builder(context).setTitle("fota").setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                .setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {



                    public void onClick(android.content.DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                }).create();

        dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
        dialog.show();

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {//响应listview中的item的点击事件

            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {

                dialog.cancel();

            }
        });
    }




    @Override
    public int hashCode () {
        return super.hashCode ();
    }
}
