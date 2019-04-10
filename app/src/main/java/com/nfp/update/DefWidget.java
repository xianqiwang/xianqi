package com.nfp.update;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DefWidget extends Dialog {

    private Context context;
    private TextView title;
    private TextView message;
    private Button positive;
    private Button neutral;
    private Button negative;
    private ProgressBar progressBar;
    private OnClickListener onClickListener;

    public DefWidget(Context context,int width,int height,String tt,String msg,String one,String two,String three,boolean pb){
        super (context);
        this.context=context;

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity=Gravity.CENTER;
        layoutParams.width  = width;
        layoutParams.height =height;

/*
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
*/

        getWindow().setAttributes(layoutParams);
        setContent(tt,msg,one,two,three,pb);
    }


    interface OnClickListener{
       public void onNeutralClick();
       public void onPositiveClick();
       public void onNegativeClick();
    }

    public void setOnClickListener(OnClickListener callback){
         this.onClickListener=callback;
    }

    public void setContent(String tt,String msg,String one,String two,String three,boolean pb){
        setContentView(R.layout.new_define_dialog);
        Log.v ("yingbo","msg"+msg);
        title      =findViewById(R.id.title);
        message    =findViewById(R.id.message);
        positive   =findViewById(R.id.positive);
        neutral    =findViewById(R.id.neutral);
        negative   =findViewById(R.id.negative);
        progressBar=findViewById (R.id.pb);

        if(tt!=null){
            title.setText (tt);
        }else{
            title.setText ("");
        }

        if(msg!=null){
            message.setText (msg);
            message.setVisibility (View.VISIBLE);
        }else{

            message.setText ("");
            message.setVisibility (View.GONE);

        }
        if(one!=null){

            positive.setText (one);
            positive.setVisibility (View.VISIBLE);

        }else{

            title.setText ("");
            positive.setVisibility (View.GONE);

        }
        if(two!=null){
            neutral.setText (two);
            neutral.setVisibility (View.VISIBLE);
        }else{
            neutral.setText ("");
            neutral.setVisibility (View.GONE);
        }
        if(three!=null){
            negative.setVisibility (View.VISIBLE);
            negative.setText (three);
        }else{
            negative.setVisibility (View.GONE);
            negative.setText (three);
        }
        if(pb==true){
            progressBar.setVisibility (View.VISIBLE);
        }else{
            progressBar.setVisibility (View.GONE);
        }

        positive.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickListener.onPositiveClick ();
            }
        });
        negative.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                onClickListener.onNegativeClick ();

            }

        });
        neutral.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               onClickListener.onNeutralClick ();
            }

        });

        show ();

    }


/*    //自定义的东西
    //放在show()之后，不然有些属性是没有效果的，比如height和width
    Window dialogWindow = dialog.getWindow();
    WindowManager m = getWindowManager();
    Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
    // 设置高度和宽度
    p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
    p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65

    p.gravity = Gravity.TOP;//设置位置

    p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);*/

    /*    public void DefaultAlart(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请回答")//设置标题
                .setMessage("你觉得学的怎么样")//设置内容信息
                .setIcon(R.drawable.icon_user)//设置图标
                //设置选项
                .setPositiveButton("棒", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton("还行", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("不好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }*/
/*    public void CallToast(String meg){

        Toast toastCustom = new Toast(context);
        toastCustom.setGravity (BOTTOM,0,0);
        toastCustom.setDuration (Toast.LENGTH_LONG);

        //加载布局管理器
        LayoutInflater inflater = LayoutInflater.from(context);
        //将xml布局转换为view对象
        View view = inflater.inflate(R.layout.layout_toast, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_toast);
        TextView textView = (TextView) view.findViewById(R.id.tv_toast);
        imageView.setImageResource(R.drawable.icon_toast);
        textView.setText("自定义Toast");
        toastCustom.setView(view);
        toastCustom.setDuration(Toast.LENGTH_LONG);
        toastCustom.show();
  *//*      Toast.cancel();*//*
    }*/



/*   public void DefAlartList(){
       final String[] array2 = new String[]{"男","女"};
       AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
       builder2.setTitle("选择性别").setItems(array2, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

           }
       }).show();
   }*/

/*   public void DefCheckBox(){
       final String[] array3 = new String[]{"男","女"};
       AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
       builder3.setTitle("选择性别").setSingleChoiceItems(array3, 1, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

               dialog.dismiss();

           }
       }).setCancelable(false).show();

   }*/

/*   public void DefMailtiChoice(){
       final String[] array4 = new String[]{"唱歌","跳舞","写作业"};
       boolean[] isSelected = new boolean[]{false,false,true};
       AlertDialog.Builder builder4 = new AlertDialog.Builder(context);
       builder4.setTitle("选择兴趣").setMultiChoiceItems(array4, isSelected, new DialogInterface.OnMultiChoiceClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which, boolean isChecked) {

           }
       }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               //
           }
       }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               //
           }
       }).show();

   }*/



   public void DefProgerss(){

/*       style="@android:style/Widget.Material.ProgressBar.Horizontal"//最新的进度条样式
       style="@android:style/Widget.ProgressBar"//旧系统的加载样式
       style="@android:style/Widget.ProgressBar.Horizontal"//旧系统的进度条样式*/
/*       mBtnStart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               handler.sendEmptyMessage(0);
           }
       });*/
/*
       Handler handler = new Handler(){
           @Override
           public void handleMessage(Message msg) {
               super.handleMessage(msg);
               if(mPb3.getProgress() < 100){
                   handler.postDelayed(runnable, 500);
               }else {
                   ToastUtil.showMsg(ProgressActivity.this, "加载完成");
               }
           }
       };*/

/*       Runnable runnable = new Runnable() {
           @Override
           public void run() {
               mPb3.setProgress(mPb3.getProgress() + 5);
               handler.sendEmptyMessage(0);
           }
       };*/


/*<animated-rotate xmlns:android="http://schemas.android.com/apk/res/android"
       android:drawable="@drawable/icon_progress"
       android:pivotX="50%"// 表示缩放/旋转起点 X 轴坐标，可以是整数值、百分数（或者小数）、百分数p 三种样式
       android:pivotY="50%">//与android:pivotX类似。
//这两个共同作用起来就是以中心为旋转轴旋转
</animated-rotate>*/

/*<style name="MyProgressBar">
    <item name="android:indeterminateDrawable">@drawable/bg_progress</item>//动画源文件
    <item name="android:indeterminate">true</item>//表示的是这个ProgressBar是模糊的，不明确的，实质就是不断地旋转
</style>*/
/*       mBtnProgress1.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               ProgressDialog progressDialog = new ProgressDialog(ProgressActivity.this);
               progressDialog.setTitle("提示");
               progressDialog.setMessage("正在加载");
               progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                   @Override
                   public void onCancel(DialogInterface dialog) {
                   }
               });
               progressDialog.setCancelable(false);
               progressDialog.show();
           }
       });*/
/*
       progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//进度条样式
*/
/*       setCancelable()表示是否能被点击对话框外部来取消

       mBtnProgress1是按钮，点击该按钮来弹出Dialog

               这里没有设置setProgressStyle,为默认样式

       setOnCancelListerer设置取消监听器，当Dialog被取消后触发*/
/*       mBtnProgress2.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               ProgressDialog progressDialog = new ProgressDialog(ProgressActivity.this);
               progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
               progressDialog.setTitle("提示");
               progressDialog.setMessage("正在下载。。。");
               progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "棒", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                   }
               });
               progressDialog.show();
           }
       });*/

/*       mBtnProgress2.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               ProgressDialog progressDialog = new ProgressDialog(ProgressActivity.this);
               progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
               progressDialog.setTitle("提示");
               progressDialog.setMessage("正在下载。。。");
               progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "棒", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       ToastUtil.showMsg(ProgressActivity.this, "已被点击");
                   }
               });
               progressDialog.show();
           }
       });*/

/*       WindowManager windowManager = getWindow().getWindowManager();
       Display display = windowManager.getDefaultDisplay();
       WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
       Point point = new Point();
       display.getSize(point);
       layoutParams.width = (int) (point.x * 0.8);//设置dialog的宽度为当前手机屏幕的宽度
       getWindow().setAttributes(layoutParams);*/


/*       View view = getLayoutInflater().inflate(R.layout.layout_pop, null);
       mPop = new PopupWindow(view, mBtnPop.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
       //以下实现对“好”按钮的响应
       TextView textView = (TextView) view.findViewById(R.id.tv_good);
       textView.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               mPop.dismiss();
               ToastUtil.showMsg(PopupWindowActivity.this, "好");
           }
       });

       //点击外部区域PopMenu消失
       mPop.setBackgroundDrawable(new BitmapDrawable());
       mPop.setOutsideTouchable(true);


       mPop.setFocusable(true);
       mPop.showAsDropDown(mBtnPop, 0, 0, Gravity.CENTER_HORIZONTAL);*/

/*use for Pickerview       minute_pv = (PickerView) findViewById(R.id.minute_pv);
       second_pv = (PickerView) findViewById(R.id.second_pv);
       List<String> data = new ArrayList<String>();
       List<String> seconds = new ArrayList<String>();
       for (int i = 0; i < 10; i++)
       {
           data.add("0" + i);
       }
       for (int i = 0; i < 60; i++)
       {
           seconds.add(i < 10 ? "0" + i : "" + i);
       }
       minute_pv.setData(data);
       minute_pv.setOnSelectListener(new onSelectListener()
       {

           @Override
           public void onSelect(String text)
           {
               Toast.makeText(MainActivity.this, "选择了 " + text + " 分",
                       Toast.LENGTH_SHORT).show();
           }
       });
       second_pv.setData(seconds);
       second_pv.setOnSelectListener(new onSelectListener()
       {

           @Override
           public void onSelect(String text)
           {
               Toast.makeText(MainActivity.this, "选择了 " + text + " 秒",
                       Toast.LENGTH_SHORT).show();
           }
       });
       minute_pv.setSelected(0);*/

   }


}
