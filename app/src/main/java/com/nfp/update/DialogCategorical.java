package com.nfp.update;
import android.content.Context;
import android.view.View;
import android.content.res.Resources;
import java.util.List;
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

    DialogCategorical(Context context, int width, int height , View layout){

        mDefDialog = new DefDialog (context, 0, 0, layout, R.style.styledialog);

        mContext=context;

    }

    public void B_D_11(String title,boolean cancel){

        Resources r=mContext.getResources();



          mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
          @Override
          public void onOkKey () {
              callbackConfirmKey.onConfirm ();
          }

          @Override
          public void onCenterKey () {

          }

      });

        mDefDialog.setCancelable(cancel);
        mDefDialog.setTitle (title);
        mDefDialog.setMessage (r.getString (R.string.Download_reservation));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.show();
    }


   public void B_D_12(String title,boolean cancel){
       Resources r=mContext.getResources();



           mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
               @Override
               public void onOkKey () {
                   callbackConfirmKey.onConfirm ();
               }

               @Override
               public void onCenterKey () {

               }

           });



       mDefDialog.setCancelable(cancel);
       mDefDialog.setTitle (title);
       mDefDialog.setMessage (r.getString (R.string.ask_exit));
       mDefDialog.setButtonCancel (r.getString (R.string.no));
       mDefDialog.setButtonConfirm (r.getString (R.string.yes));
       mDefDialog.setCenterKeyGone ();
       mDefDialog.show();
   }

    public void B_D_13(String title,boolean cancel){
        Resources r=mContext.getResources();



            mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
                @Override
                public void onOkKey () {
                    callbackConfirmKey.onConfirm ();
                }

                @Override
                public void onCenterKey () {

                }

            });

        mDefDialog.setCancelable(cancel);
        mDefDialog.setTitle (title);
        mDefDialog.setMessage (r.getString (R.string.select_other_day));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone ();
        mDefDialog.show();
    }

    public void B_D_02(String title, boolean cancel, List items){
        Resources r=mContext.getResources();

        mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
            @Override
            public void onOkKey () {
                callbackConfirmKey.onConfirm ();
            }

            @Override
            public void onCenterKey () {

            }

        });

/*        java.util.ArrayList<String> item =new java.util.ArrayList<String> ();

        items.add ("1");
        items.add ("2");
        items.add ("3");
        items.add ("5");
        items.add ("7");
        items.add ("9");*/
        mDefDialog.setListViewVisible ();
        mDefDialog.setProgressGone ();
        mDefDialog.setListviewDialog (mContext,items);
        mDefDialog.setCancelable(cancel);
        mDefDialog.setTitle (title);
        mDefDialog.setMessage (r.getString (R.string.select_other_day));
        mDefDialog.setButtonCancel (r.getString (R.string.cancel));
        mDefDialog.setButtonConfirm (r.getString (R.string.softkey_select));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyVisible ();
        mDefDialog.show();

    }

}
