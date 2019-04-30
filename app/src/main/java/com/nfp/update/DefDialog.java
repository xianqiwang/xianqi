package com.nfp.update;

import android.app.Dialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Button;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.SimpleAdapter;
import android.widget.ProgressBar;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nfp.update.widget.MyAdapter;
import com.nfp.update.widget.entity;

class DefDialog extends Dialog {

    private TextView mTitle;
    private TextView mMessage;
    private TextView percent;
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
    private TextView spinneritems;
    private View mViewThree;
    OnOkListener mOnCenterKeyListener;
    private android.widget.NumberPicker numberPicker;
    private android.widget.EditText edWorkingAge;
    private android.widget.Button submit_workingAge;
    private android.widget.PopupWindow popupWindow;
    private com.nfp.update.WheelPicker wheelPicker;
    private LinearLayout linearLayout;

    int mPosition = 0;
    int pro = 0;

    public DefDialog(Context context,int style) {
        super(context, style);
        View layout = getLayoutInflater ().inflate (R.layout.dialog_layout, null);
        setContentView(layout);
        mTitle=layout.findViewById (R.id.tip);
        mMessage=layout.findViewById (R.id.message);
        percent=layout.findViewById (R.id.percent);
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
        wheelPicker=layout.findViewById (com.nfp.update.R.id.wheelpicker);
        linearLayout=layout.findViewById (R.id.background);
        setWheelPicker ();
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width= LayoutParams.MATCH_PARENT;
        params.height= LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(params);
        mContext=context;
        initNumberPicker(layout);

        mCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (android.view.View v) {
/*
                android.util.Log.v("yingbo","mCancel");
*/
                mOnCenterKeyListener.onCancelKey();
             //   cancel ();
            }

        });


        mConfirm.setOnClickListener (new View.OnClickListener () {


            @Override
            public void onClick (android.view.View v) {

/*
                android.util.Log.v("yingbo","onOkKey");
*/
                mOnCenterKeyListener.onOkKey();

//                setWheelPickerVisible();

/*
                android.util.Log.v ("yingbo","value"+getWheelPickerCurrentValue ());
*/

                /*
                   setViewOneGone();
                  */

            }

        });

        mCenter.setOnClickListener (new View.OnClickListener () {

            int i =17;

            @Override
            public void onClick (android.view.View v) {
              android.util.Log.v("yingbo","onCenterKey");
/*
              setWheelPickerCurrentValue (3);
*/
                i--;
/*
                setSpinnerTextSize (i);
*/

        /*        i=i-10;

                setViewOneVisible ();

                setListViewHeight (i);*/
                mOnCenterKeyListener.onCenterKey ();

            }

        });

        mHandler = new Handler (){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mProgress.setProgress(msg.what);
                percent.setText(""+pro+"%");
            }
        };

        start();
    }

    public int getWheelPickerCurrentValue(){


        wheelPicker.setOnItemSelectedListener (new WheelPicker.OnItemSelectedListener(){

            @Override
            public void onItemSelected (com.nfp.update.WheelPicker picker, Object data, int position) {

                mPosition = position;

            }
        });
        return mPosition;
    }

    public void setWheelPickerVisible(){
        wheelPicker.setVisibility (android.view.View.VISIBLE);
    }
    public void setWheelPickerGone(){
        wheelPicker.setVisibility (android.view.View.GONE);
    }

    public void setWheelPickerCurrentValue(int itemId){
        wheelPicker.setSelectedItemPosition (itemId);
    }

    public void setWheelPicker () {
        List<String>items =new ArrayList<String> ();

        String[] time = new String[]{ "1:00-2:00 am",
                "2:00-3:00 am", "3:00-4:00 am", "4:00-5:00 am",
                "5:00-6:00 am", "6:00-07:00 am", "7:00-8:00 am",
                "8:00-9:00 am", "9:00-10:00 am", "10:00-11:00 am",
                "11:00-12:00 am", "12:00-13:00 am","1:00-2:00 pm",
                "2:00-3:00 pm", "3:00-4:00 pm", "4:00-5:00 pm",
                "5:00-6:00 pm", "6:00-07:00 pm", "7:00-8:00 pm",
                "8:00-9:00 pm", "9:00-10:00 pm", "10:00-11:00 pm",
                "11:00-12:00 pm", "12:00-13:00 pm"};

        //使用for循环转换为list
        for(String str : time){
            items.add(str);
        }

        wheelPicker.setVisibleItemCount (3);
        wheelPicker.setData (items);
        wheelPicker.setBackgroundColor (android.graphics.Color.WHITE);
        wheelPicker.setItemTextColor (android.graphics.Color.BLACK);
        wheelPicker.setCurtain (true);
        wheelPicker.setCurved (true);
        wheelPicker.setCyclic (true);
        wheelPicker.setItemTextSize (60);
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

    public void setTitleGone(){
        mTitle.setVisibility(View.GONE);
    }

    public void setTitleSize(int size){

        mTitle.setTextSize (size);

    }
    public void setTitle(String title){
        mTitle.setText (title);
    }
    public String getTitle(){
        return (String) mTitle.getText ();
    }

    public void setMessageSize(int size){

        mMessage.setTextSize (size);

    }

    public void setMessageTextColor(int color){

        mMessage.setTextColor (color);

    }

    public void setmMessageLTextColor(int color){

        mMessageL.setTextColor (color);

    }

    public void setMessageGone(){

        mMessage.setVisibility (android.view.View.GONE);

    }
    public void setMessageCenter(int height_pix){
        mMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        mMessage.setPadding(0,height_pix/2, 0, height_pix/2);
    }
    public void setMessageLCenter(){

        mMessage.setGravity(Gravity.CENTER_HORIZONTAL);

    }
    public void setMessageLLeft(){

        mMessage.setGravity(Gravity.LEFT);

    }
    public void setMessage(String message){
        mMessage.setText (message);
    }
    public String getMessage(){
        return (String) mMessage.getText ();
    }

    public void setPercentVisible(){
        percent.setVisibility(View.VISIBLE);
    }
    public void setPercentGone(){
        percent.setVisibility(View.GONE);
    }
    public void setMessageLSize(int size){

        mMessageL.setTextSize (size);

    }
    public void setMessagel(String message){
        mMessageL.setText (message);
    }
    public String getMessagel(){
        return (String) mMessageL.getText ();
    }

    public void setLayoutBackground(int color){
        linearLayout.setBackgroundColor (color);
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
    public void setNumberPickerVisible(){

        numberPicker.setVisibility (android.view.View.VISIBLE);

    }

    public void setMessageTextSize(int size){
        mMessage.setTextSize (size);
    }
    public void setMessageLTextSize(int size){
        mMessageL.setTextSize (size);
    }
    public void setNumberPickerGone(){

        numberPicker.setVisibility (android.view.View.GONE);

    }

    public void setViewOneheight(int height){

        android.view.ViewGroup.LayoutParams params=mViewOne.getLayoutParams ();
        params.height=height;
        mViewOne.setLayoutParams(params);

    }
    public void setViewTwoheight(int height){
        android.view.ViewGroup.LayoutParams params=mViewTwo.getLayoutParams ();
        params.height=height;
        mViewTwo.setLayoutParams(params);

    }
    public void setViewThreeheight(int height){
        android.view.ViewGroup.LayoutParams params=mViewThree.getLayoutParams ();
        params.height=height;
        mViewThree.setLayoutParams(params);

    }
    public void setmConfirmGone(){
        mConfirm.setVisibility(android.view.View.GONE);
    }
    public void setMessageLGone(){
        mMessageL.setVisibility (android.view.View.GONE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cancel (); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setConfirmGone(){
        mConfirm.setVisibility(View.GONE);
    }

    public void setListViewHeight(int height){

        android.view.ViewGroup.LayoutParams params=listView.getLayoutParams ();
        params.height=height;
        listView.setLayoutParams(params);

    }
    public ListView getListView(){
            return listView;
    }
    public void setMessageLVisible(){
        mMessageL.setVisibility (android.view.View.VISIBLE);
    }
    public void setButtonCenterSize(int size){

        mCenter.setTextSize (size);

    }
    public void setCenter(String title){
        mCenter.setText (title);
    }
    public String getCenter(){
        return (String) mCenter.getText ();
    }

    public void setButtonCancelSize(int size){
        mCancel.setTextSize (size);
    }

    public void setButtonCancel(String text){
        mCancel.setText (text);
    }
    public void setButtonCenter(String text){
        mCenter.setText (text);
    }
    public String getButtonCancel(){
      return (String) mCancel.getText ();
    }
    public void setButtonConfirmSize(int size){

        mConfirm.setTextSize (size);

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
    public void setOkKeyGone(){

        mConfirm.setVisibility (android.view.View.GONE);

    }

    public void setCancelKeyVisible(){

        mCancel.setVisibility (android.view.View.VISIBLE);

    }
    public void setSpinnerTextSize(int size){

        spinneritems.setTextSize (size);
        
    }
    public void setSpinnerVisible(){
        spinner.setVisibility (android.view.View.VISIBLE);
    }
    public void setSpinnerGone(){
        spinner.setVisibility (android.view.View.GONE);
    }
    public void addListview_AD_21(String[] stringArray){
        listView.setVisibility (android.view.View.VISIBLE);
        ArrayList<String>   menuList = new ArrayList<String>();
        for (String str : stringArray) {
            menuList.add(str);
        }
        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(mContext, R.layout.main_item, menuList);
        listView.setAdapter(myArrayAdapter);
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
    }
    public void addListview_AD_03(String[] stringArray){
        listView.setVisibility (android.view.View.VISIBLE);

       // String[] stringArray = mContext.getResources().getStringArray(R.array.up_chose03);

        ArrayList<String>   menuList = new ArrayList<String>();
        for (String str : stringArray) {
            menuList.add(str);
        }
        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(mContext, R.layout.main_item, menuList);
        listView.setAdapter(myArrayAdapter);
   /*     listView.setOnItemClickListener(new ListView.OnItemClickListener() {
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
        });*/
    }

 public void addListview_AD_12(){
        listView.setVisibility (android.view.View.VISIBLE);

        String[] stringArray = mContext.getResources().getStringArray(R.array.up_chose);
        ArrayList<String>   menuList = new ArrayList<String>();
        for (String str : stringArray) {
            menuList.add(str);
        }
        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(mContext, R.layout.main_item, menuList);
        listView.setAdapter(myArrayAdapter);
      /*  listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                switch (position) {
                    case 0:
                        Resources res =mContext.getResources();
                        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                        DialogCategorical dialogCategorical=new DialogCategorical (mContext, 0, 0, view);
                        //A-D-21
                      //  dialogCategorical.A_D_21 ();
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
        });*/
    }
    public void addListview_AD_20(){
        listView.setVisibility (android.view.View.VISIBLE);

        String[] stringArray = mContext.getResources().getStringArray(R.array.up_chose1);
        ArrayList<String>   menuList = new ArrayList<String>();
        for (String str : stringArray) {
            menuList.add(str);
        }
        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(mContext, R.layout.main_item, menuList);
        listView.setAdapter(myArrayAdapter);
   /*     listView.setOnItemClickListener(new ListView.OnItemClickListener() {
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
        });*/
    }
public void addListview_AN_09(){
        listView.setVisibility (android.view.View.VISIBLE);

        String[] stringArray = mContext.getResources().getStringArray(R.array.set_time_arr);
        ArrayList<String>   menuList = new ArrayList<String>();
        for (String str : stringArray) {
            menuList.add(str);
        }
        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(mContext, R.layout.main_item, menuList);
        listView.setAdapter(myArrayAdapter);
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
    }

    public void addListview_BD_02(String[] stringArray){

        listView.setVisibility (android.view.View.VISIBLE);

        ArrayList<String>   menuList = new ArrayList<String>();
        for (String str : stringArray) {
            menuList.add(str);
        }
        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(mContext, R.layout.main_item, menuList);
        listView.setAdapter(myArrayAdapter);
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
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:

                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void addListview_BD_03(String[] stringArray){
        listView.setVisibility (android.view.View.VISIBLE);

        ArrayList<String>   menuList = new ArrayList<String>();
        for (String str : stringArray) {
            menuList.add(str);
        }
        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(mContext, R.layout.main_item, menuList);
        listView.setAdapter(myArrayAdapter);
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
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:

                        break;
                    case 9:

                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void addListview_BD_08(String[] stringArray){
        listView.setVisibility (android.view.View.VISIBLE);

        ArrayList<String>   menuList = new ArrayList<String>();
        for (String str : stringArray) {
            menuList.add(str);
        }
        ArrayAdapter<String> myArrayAdapter = new ItemListAdapter(mContext, R.layout.main_item, menuList);
        listView.setAdapter(myArrayAdapter);
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
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:

                        break;
                    case 9:

                        break;
                    default:
                        break;
                }
            }
        });
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

            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 80));

            ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position));
            return convertView;
        }
    }


    public void setSpinner(ArrayList<entity> data){

        myAdadpter = new MyAdapter<entity> (data,R.layout.item_spin) {
            @Override
            public void bindView (MyAdapter.ViewHolder holder, entity obj) {

                /*
                holder.setImageResource(R.id.img_icon,obj.gethIcon());
                 */

                holder.setText(R.id.txt_name,obj.gethName());

                spinneritems=findViewById (R.id.txt_name);

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

        public void onCancelKey();

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


    /**
     * 初始化滚动框布局
     */

    private void initNumberPicker(View view) {

        boolean is24 = android.text.format.DateFormat.is24HourFormat(mContext);

        /*    if(is24){
         */
        final String[] time = {
                "00:00-01:00", "01:00-02:00",
                "02:00-03:00", "03:00-04:00", "04:00-05:00",
                "05:00-06:00", "06:00-07:00", "07:00-08:00",
                "08:00-09:00", "09:00-10:00", "10:00-11:00",
                "11:00-12:00", "12:00-13:00", "13:00-14:00",
                "14:00-15:00", "15:00-16:00", "16:00-17:00",
                "17:00-18:00", "18:00-19:00","19:00-20:00",
                "20:00-21:00", "21:00-22:00","22:00-23:00",
                "23:00-24:00"};

     /*   }else{

             String[] time = { "1:00-2:00 am",
                    "2:00-3:00 am", "3:00-4:00 am", "4:00-5:00 am",
                    "5:00-6:00 am", "6:00-07:00 am", "7:00-8:00 am",
                    "8:00-9:00 am", "9:00-10:00 am", "10:00-11:00 am",
                    "11:00-12:00 am", "12:00-13:00 am","1:00-2:00 pm",
                    "2:00-3:00 pm", "3:00-4:00 pm", "4:00-5:00 pm",
                    "5:00-6:00 pm", "6:00-07:00 pm", "7:00-8:00 pm",
                    "8:00-9:00 pm", "9:00-10:00 pm", "10:00-11:00 pm",
                    "11:00-12:00 pm", "12:00-13:00 pm"};

        }
*/

        numberPicker = (android.widget.NumberPicker) view.findViewById(R.id.numberPicker);
        numberPicker.setDisplayedValues(time);
        numberPicker.setMaxValue(time.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);

        numberPicker.setFormatter(new android.widget.NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                // TODO Auto-generated method stub
                return time[value];




            }
        });
        numberPicker.setFocusable(false);
        numberPicker.setFocusableInTouchMode(false);
        numberPicker.setDescendantFocusability(android.widget.NumberPicker.FOCUS_BLOCK_DESCENDANTS); // 关闭编辑模式
        setNumberPickerDividerColor(numberPicker);

    }


    public void openPopView(){

        numberPicker.setValue(5);

    }

    public void closePopView(){

/*
        workingAge = numberPicker.getValue();
*/

    }






    /**
     * 自定义滚动框分隔线颜色
     */
    private void setNumberPickerDividerColor(android.widget.NumberPicker number) {
        java.lang.reflect.Field[] pickerFields = android.widget.NumberPicker.class.getDeclaredFields();
/*        java.lang.reflect.Field npe;
        try{
            npe=number.getClass ().getDeclaredField ("mInputText");
            npe.setAccessible (true);
            try{

                ((android.widget.EditText)npe.get (number)).setTextSize(60);

                ((android.widget.EditText)npe.get (number)).setTextColor (mContext.getResources ().getColor (com.nfp.update.R.color.red));

            }catch (IllegalAccessException e){

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace ();
        }*/

        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(number, new android.graphics.drawable.ColorDrawable (
                            android.support.v4.content.ContextCompat.getColor(mContext, com.nfp.update.R.color.white)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
