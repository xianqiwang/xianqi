package com.nfp.update;
import android.content.Context;
import android.view.View;
import android.content.res.Resources;
public class DialogCategorical {
/*
* 每个页面的对话框在这里调用
* dialog的形成在DefDialog.java里面调用
* 若果进度调不满足需求就用ProgressActivity.java/TestProgress.java
*
* */
    Context mContext;

    DefDialog mDefDialog;
    CallbackConfirmKey callbackConfirmKey;
    CallbackOtherKey callbackOtherKey;

    interface CallbackConfirmKey{

        void onConfirm();

    }
    interface CallbackOtherKey{

        void onOther();

    }


    public void setCallbackConfirmKey(CallbackConfirmKey callbackConfirmKey){
        this.callbackConfirmKey=callbackConfirmKey;
    }

    public void setCallbackOtherKey(CallbackConfirmKey callbackConfirmKey){
        this.callbackOtherKey=callbackOtherKey;
    }

    DialogCategorical(Context context, int width, int height , android.view.View layout){

        mDefDialog = new DefDialog (context, 0, 0, layout, R.style.styledialog);

        mContext=context;

    }
    
    public void B_D_11(boolean okkey,String title,boolean cancel,String massage){

        Resources r=mContext.getResources();

      if(okkey==true){

          mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
          @Override
          public void onOkKey () {
              callbackConfirmKey.onConfirm ();
          }

          @Override
          public void onCenterKey () {

          }
      });

     }

        mDefDialog.setCancelable(cancel);
        mDefDialog.setTitle (title);
        mDefDialog.setMessage (r.getString (R.string.Download_reservation));
        mDefDialog.witchNeedTwoKey ();

    }






}
