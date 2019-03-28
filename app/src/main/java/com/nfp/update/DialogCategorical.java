package com.nfp.update;
import android.content.Context;
import android.view.View;
import android.content.res.Resources;

import java.util.ArrayList;
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
    // yes no start lihuachun A_D_16 A_D_18 A_D_15
    public void A_N_02(String message){
        Resources r=mContext.getResources();



            mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
                @Override
                public void onOkKey () {
                   // callbackConfirmKey.onConfirm ();
                }

                @Override
                public void onCenterKey () {

                }
                @Override
                public void onSpinnerSelect () {

                }
            });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (message);
        mDefDialog.setCenterKeyGone();
        mDefDialog.setListViewGone();
        mDefDialog.setMessageLGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.yes));
        mDefDialog.setButtonCancel(r.getString (R.string.no));
        mDefDialog.setSpinnerGone();
        mDefDialog.show();
        }
    // only ok start lihuachun A_D_04 A_D_14
    public void A_D_13(String message){
        Resources r=mContext.getResources();


        mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
                @Override
                public void onOkKey () {
                    // callbackConfirmKey.onConfirm ();
                }

                @Override
                public void onCenterKey () {

                }
            @Override
            public void onSpinnerSelect () {

            }
            });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (message);
        mDefDialog.setListViewGone();
        mDefDialog.setMessageLGone();
        mDefDialog.setSpinnerGone();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.ok_dailog));

        mDefDialog.show();
        }
    public void A_D_21(String[] stringArray){
        Resources r=mContext.getResources();


        mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
            @Override
            public void onOkKey () {
                // callbackConfirmKey.onConfirm ();
            }

            @Override
            public void onCenterKey () {

            }
            @Override
            public void onSpinnerSelect () {

            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage(r.getString(R.string.up_detail));
        mDefDialog.addListview_AD_21(stringArray);
        mDefDialog.setMessageLGone();
        mDefDialog.setSpinnerGone();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.ok_dailog));

        mDefDialog.show();
    }

        // only ok end lihuachun

    public void A_D_17(String date){
        Resources r=mContext.getResources();



            mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
                @Override
                public void onOkKey () {
                    // callbackConfirmKey.onConfirm ();
                }

                @Override
                public void onCenterKey () {

                }
                @Override
                public void onSpinnerSelect () {

                }


            });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String str1=r.getString(R.string.ask_up_time_1);
        String str2=r.getString(R.string.ask_up_time_2);
        String str=str1+date+str2;
        mDefDialog.setMessage (str);
        mDefDialog.setCenterKeyGone();
        mDefDialog.setListViewGone();
        mDefDialog.setMessageLGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.select));
        mDefDialog.setButtonCancel(r.getString (R.string.cancel));
        mDefDialog.setSpinnerGone();
        mDefDialog.show();

        }


    public void A_D_12(String sw){
        Resources r=mContext.getResources();



        mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
            @Override
            public void onOkKey () {
                // callbackConfirmKey.onConfirm ();
            }

            @Override
            public void onCenterKey () {

            }
            @Override
            public void onSpinnerSelect () {

            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String str=r.getString(R.string.up_version)+"("+sw+")";
        mDefDialog.setMessage (sw);
        mDefDialog.setCenterKeyGone();
        mDefDialog.addListview_AD_12();
        mDefDialog.setMessageLGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.select));
        mDefDialog.setButtonCancel(r.getString (R.string.cancel));
        mDefDialog.setSpinnerGone();
        mDefDialog.show();
    }

    public void A_D_20(String sw){
        Resources r=mContext.getResources();



        mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
            @Override
            public void onOkKey () {
                // callbackConfirmKey.onConfirm ();
            }

            @Override
            public void onCenterKey () {

            }
            @Override
            public void onSpinnerSelect () {

            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String str=r.getString(R.string.up_version)+"("+sw+")";
        mDefDialog.setMessage (sw);
        mDefDialog.setCenterKeyGone();
        mDefDialog.addListview_AD_12();
        mDefDialog.setMessageLGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.select));
        mDefDialog.setButtonCancel(r.getString (R.string.cancel));
        mDefDialog.setSpinnerGone();
        mDefDialog.show();
    }

//select cancel lihuachun end


    public void B_D_11(boolean okkey,String title,boolean cancel,String massage){

        Resources r=mContext.getResources();

        mDefDialog.setOkClickListener(new DefDialog.OnOkListener () {
          @Override
          public void onOkKey () {
              callbackConfirmKey.onConfirm ();
          }

          @Override
          public void onCenterKey () {

          }

              @Override
           public void onSpinnerSelect () {

            }

          });

        mDefDialog.setCancelable(cancel);
        mDefDialog.setTitle (title);
        mDefDialog.setMessage (r.getString (R.string.Download_reservation));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
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

               @Override
               public void onSpinnerSelect () {

               }

           });



       mDefDialog.setCancelable(cancel);
       mDefDialog.setTitle (title);
       mDefDialog.setMessage (r.getString (R.string.ask_exit));
       mDefDialog.setButtonCancel (r.getString (R.string.no));
       mDefDialog.setButtonConfirm (r.getString (R.string.yes));
       mDefDialog.setCenterKeyGone ();
       mDefDialog.setMessageLGone ();
       mDefDialog.setSpinnerGone ();
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

                @Override
                public void onSpinnerSelect () {

                }

            });

        mDefDialog.setCancelable(cancel);
        mDefDialog.setTitle (title);
        mDefDialog.setMessage (r.getString (R.string.select_other_day));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
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

            @Override
            public void onSpinnerSelect () {

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
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCancelKeyVisible ();
        mDefDialog.setOkClickListener (new DefDialog.OnOkListener(){

            @Override
            public void onOkKey () {

            }

            @Override
            public void onCenterKey () {

            }

            @Override
            public void onSpinnerSelect () {

            }
        });

        mDefDialog.show();

    }

}
