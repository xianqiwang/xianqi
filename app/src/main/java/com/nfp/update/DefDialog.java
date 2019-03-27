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
import android.widget.ProgressBar;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

class DefDialog extends Dialog {

    private TextView mTitle;
    private TextView mMessage;
    private TextView mMessageL;
    private Button   mConfirm;
    private Button   mCancel;
    private Button   mCenter;
    private ProgressBar mProgress;
    private ListView listView;
    private Handler mHandler;
    private android.widget.BaseAdapter myAdadpter = null;
    private Spinner spinner;
    private Context mContext;
    private View mViewOne;
    private View mViewTwo;
    private View mViewThree;
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
        mCenter=layout.findViewById (R.id.buttoncenter);
        listView=layout.findViewById (R.id.list);
        mProgress = layout.findViewById(R.id.down_pb);
        spinner = layout.findViewById(R.id.spinner);
        mMessageL = layout.findViewById(R.id.messagel);
        mViewOne=layout.findViewById (com.nfp.update.R.id.view1);
        mViewTwo=layout.findViewById (com.nfp.update.R.id.view2);
        mViewThree=layout.findViewById (com.nfp.update.R.id.view3);


        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width= LayoutParams.MATCH_PARENT;
        params.height= LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(params);
        mContext=context;

        mCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (android.view.View v) {
/*
                android.util.Log.v("yingbo","mCancel");
*/
                cancel ();
            }

        });
        mConfirm.setOnClickListener (new View.OnClickListener () {


            @Override
            public void onClick (android.view.View v) {

/*
                android.util.Log.v("yingbo","onOkKey");
*/

                mOnCenterKeyListener.onOkKey();
                setViewOneGone();
            }

        });

        mCenter.setOnClickListener (new View.OnClickListener () {

            int i =200;

            @Override
            public void onClick (android.view.View v) {
 /*             android.util.Log.v("yingbo","onCenterKey");
                i=i-10;

                setViewOneVisible ();*/

                mOnCenterKeyListener.onCenterKey ();

            }

        });

        mHandler = new Handler (){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mProgress.setProgress(msg.what);

            }
        };

        start();
    }

    private void start()
    {
/*        mHandler.post(new Runnable() {
            @Override
            public void run() {

                int max = mProgress.getMax();
                int pro=0;
                    //子线程循环间隔消息
                    while (pro < max) {
                        pro += 10;

                        mProgress.setProgress(pro);

                    }


	           mHandler.postDelayed(this, 2000L);
            }
        });*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                int max = mProgress.getMax();
                try {

                    int pro=0;

                    //子线程循环间隔消息
                    while (pro < max) {
                        pro += 10;
                        Message msg = new Message();
                        msg.what = pro;

                        if(msg == null){

                            mHandler.postDelayed(this, 2000L);

                        }

                        mHandler.sendMessage(msg);

                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setBackground(int color,int font){
        mTitle.setBackgroundColor (color);
        mTitle.setTextColor (font);
        mMessage.setBackgroundColor (color);
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
    public void setMessagel(String message){
        mMessageL.setText (message);
    }
    public String getMessagel(){
        return (String) mMessageL.getText ();
    }

    public void setViewOneGone(){
    mViewOne.setVisibility (android.view.View.GONE);
    }
    public void setViewOneVisible(){
        mViewOne.setVisibility (android.view.View.VISIBLE);
    }
    public void setViewTwoGone(){
        mViewTwo.setVisibility (android.view.View.GONE);
    }
    public void setViewTwoVisible(){
        mViewTwo.setVisibility (android.view.View.VISIBLE);
    }
    public void setViewThreeGone(){
        mViewThree.setVisibility (android.view.View.GONE);
    }
    public void setViewThreeVisible(){
        mViewThree.setVisibility (android.view.View.VISIBLE);
    }
    public void setViewOneheight(int height){
        android.view.ViewGroup.LayoutParams params=mViewOne.getLayoutParams ();
        params.height=height;
    }
    public void setViewTwoheight(int height){
        android.view.ViewGroup.LayoutParams params=mViewTwo.getLayoutParams ();
        params.height=height;
    }
    public void setViewThreeheight(int height){
        android.view.ViewGroup.LayoutParams params=mViewThree.getLayoutParams ();
        params.height=height;
    }
    public void setMessageLGone(){
        mMessageL.setVisibility (android.view.View.GONE);
    }



    public void setMessageLVisible(){
        mMessageL.setVisibility (android.view.View.VISIBLE);
    }

    public void setCenter(String title){
        mCenter.setText (title);
    }
    public String getCenter(){
        return (String) mCenter.getText ();
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

    public void setProgressGone(){

        mProgress.setVisibility (android.view.View.GONE);

    }
    public void setProgressVisible(){

        mProgress.setVisibility (android.view.View.VISIBLE);

    }
    public void setProgressRate(int rate){
        if(0 != rate){

            mProgress.setProgress(rate);

        }

    }

    public void setListViewGone(){

        listView.setVisibility (android.view.View.GONE);

    }
    public void setListViewVisible(){

        listView.setVisibility (android.view.View.VISIBLE);

    }
    public void setCenterKeyVisible(){

        mCancel.setVisibility (android.view.View.VISIBLE);

    }
    public void setCenterKeyGone(){

        mCenter.setVisibility (android.view.View.GONE);

    }
    public void setCancelKeyGone(){

        mCancel.setVisibility (android.view.View.GONE);

    }
    public void setCancelKeyVisible(){

        mCancel.setVisibility (android.view.View.VISIBLE);

    }
    public void setSpinnerVisible(){
        spinner.setVisibility (android.view.View.VISIBLE);
    }
    public void setSpinnerGone(){
        spinner.setVisibility (android.view.View.GONE);
    }

    public void setSpinner(ArrayList<entity> data){

        myAdadpter = new MyAdapter<entity>(data,R.layout.item_spin) {
            @Override
            public void bindView (com.nfp.update.MyAdapter.ViewHolder holder, entity obj) {

                /*
                holder.setImageResource(R.id.img_icon,obj.gethIcon());
                 */

                holder.setText(R.id.txt_name,obj.gethName());


            }


        };
        spinner.setAdapter (myAdadpter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {

                android.util.Log.v("yingbo","spinner"+position+view+id);

            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

                android.util.Log.v("yingbo","spinner2"+parent);

            }

        });

    }
    public void setSpinnerIndex(int index){

        spinner.setSelection(index);

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
        public void onCenterKey();
/*
        public void onCancelKey();
*/
        public void onSpinnerSelect();

    }

    public void setListviewDialog(Context context, List data) {

        //自定义一个布局文件
      /*  android.widget.LinearLayout linearLayoutMain = new android.widget.LinearLayout(this);
        linearLayoutMain.setLayoutParams(new android.view.WindowManager.LayoutParams(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT));
*/
        //自定义一个listview

/*
        android.widget.ListView listView = new android.widget.ListView(context);
*/
        listView.setFadingEdgeLength(0);

        //建立一个数组存储listview上显示的数据

        List<Map<String, String>> nameList = new ArrayList<Map<String, String>> ();


        for (int m = 0; m < data.size(); m++) {//initData为一个list类型的数据源

            Map<String, String> nameMap = new HashMap<String, String> ();

            nameMap.put("name", data.get(m).toString());

            nameList.add(nameMap);

        }

        SimpleAdapter adapter = new SimpleAdapter(context, nameList,R.layout.main_item,
                new String[] {"name"}, new int[]{R.id.text1});

        listView.setAdapter(adapter);

        listView.setDividerHeight(0);

        listView.setDivider(null);
/*
        linearLayoutMain.addView(listView);//往这个布局中加入listview
*/

       /* final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context).setTitle("fota").setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                .setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

                    public void onClick(android.content.DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                }).create();*/

/*        dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
        dialog.show();*/

        listView.setOnItemClickListener(new android.widget.ListView.OnItemClickListener() {//响应listview中的item的点击事件

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                android.util.Log.v("yingbo","listView"+position+view+id);

            }
        });
    }

    @Override
    public int hashCode () {
        return super.hashCode ();
    }
}
