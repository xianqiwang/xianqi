package com.nfp.update;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.content.res.Resources;

import com.nfp.update.widget.CommonUtils;

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
        void onCancel();
    }

    interface CallbackOtherKey{

        void onOther();

    }



    public DefDialog getmDefDialog(){
            return mDefDialog;
    }




    public void setCallbackConfirmKey(CallbackConfirmKey callbackConfirmKey){
        this.callbackConfirmKey=callbackConfirmKey;
    }

    public void setCallbackOtherKey(CallbackConfirmKey callbackConfirmKey){
        this.callbackOtherKey=callbackOtherKey;
    }

    DialogCategorical(Context context){

        mDefDialog = new DefDialog (context, R.style.styledialog);
        mContext=context;

    }

    public  static DefDialog N_0670_s01(Context context,int msg,int button1,int button2){

        DefDialog  mDefDialog = new DefDialog (context, R.style.styledialog);
        Resources r=context.getResources();
        mDefDialog.setProgressGone ();
        mDefDialog.setMessageLVisible ();
        mDefDialog.setCancelable(false);
        mDefDialog.setCenterKeyGone();
        mDefDialog.setTitle (r.getString (R.string.software_update));
        mDefDialog.setSpinnerGone();
        mDefDialog.setMessage (r.getString (msg));
        mDefDialog.setListViewGone ();
        mDefDialog.setViewTwoVisible ();
        mDefDialog.setButtonConfirm (r.getString (button1));
        mDefDialog.setButtonCancel (r.getString (button2));
        mDefDialog.setMessageLCenter ();
        mDefDialog.setViewOneheight (100);
        mDefDialog.setViewTwoheight (50);
        mDefDialog.setMessageLSize (30);
        mDefDialog.setMessageLVisible ();
        mDefDialog.setMessageSize (30);
        mDefDialog.show();
        return mDefDialog;

    }

    public  static DefDialog N_0671_s01(Context context,int msg,int button1,int button2,int button3){

        DefDialog  mDefDialog = new DefDialog (context, R.style.styledialog);

        Resources r=context.getResources();
        mDefDialog.setProgressGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setPercentGone ();
        mDefDialog.setCancelable(false);
        mDefDialog.setSpinnerGone();
        mDefDialog.setMessage (r.getString (msg)+CommonUtils.getDateAndTime ()
                +UpdateUtil.getHourTemp (context)+":"+UpdateUtil.getMinuteTemp (context));
        mDefDialog.setListViewGone ();
        mDefDialog.setViewTwoGone ();
        mDefDialog.setViewOneGone ();
        mDefDialog.setTitle (r.getString (R.string.software_update));
        mDefDialog.setButtonConfirm (r.getString (button1));
        mDefDialog.setButtonCancel (r.getString (button3));
        mDefDialog.setButtonCenter (r.getString (button2));
        mDefDialog.setMessageLVisible ();
        mDefDialog.setMessageSize (30);
        mDefDialog.show();
        return mDefDialog;

    }

    public  static void N_0645_s01(Context context, int i,int msg,boolean dismiss){

        DefDialog  mDefDialog = new DefDialog (context, R.style.styledialog);
        Resources r=context.getResources();
        if(dismiss){

         mDefDialog.dismiss ();

        }
        mDefDialog.setProgressVisible ();
        mDefDialog.setMessageLVisible ();
        mDefDialog.setCancelable(false);
        mDefDialog.setCancelKeyGone ();
        mDefDialog.setCenterKeyGone();
        mDefDialog.setTitleGone ();
        mDefDialog.setOkKeyGone();
        mDefDialog.setSpinnerGone();
        mDefDialog.setMessage (r.getString (msg));
        mDefDialog.setMessagel ("                   "+i+"%");
        mDefDialog.setListViewGone ();
        mDefDialog.setViewTwoVisible ();

        mDefDialog.setMessageLCenter ();
        mDefDialog.setViewOneheight (100);
        mDefDialog.setViewTwoheight (50);
        mDefDialog.setMessageLSize (30);
        mDefDialog.setMessageLVisible ();
        mDefDialog.setMessageSize (30);

        mDefDialog.show();
        }

    public  static void N_0646_S01(Context context, int i,int msg){

        DefDialog  mDefDialog = new DefDialog (context, R.style.styledialog);
        Resources r=context.getResources();
        mDefDialog.setCancelable(false);
        mDefDialog.setMessageTextColor (Color.WHITE);
        mDefDialog.setmMessageLTextColor (Color.WHITE);
        mDefDialog.setLayoutBackground (Color.BLACK);
        mDefDialog.setMessage (r.getString (msg));
        mDefDialog.setMessagel ("                   "+i+"%");
        mDefDialog.setListViewGone ();
        mDefDialog.setViewTwoVisible ();
        mDefDialog.setTitleGone ();
        mDefDialog.setOkKeyGone();
        mDefDialog.setMessageLCenter ();
        mDefDialog.setViewOneheight (100);
        mDefDialog.setViewTwoheight (50);
        mDefDialog.setMessageLSize (30);
        mDefDialog.setMessageLVisible ();
        mDefDialog.setMessageSize (30);
        mDefDialog.setCancelKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.setCenterKeyGone();
        mDefDialog.setSpinnerGone();
        mDefDialog.show();

    }


    public void A_D_03(String[] stringArray){
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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage ("Terms of Use Contents List");
        mDefDialog.setCenterKeyGone();
        mDefDialog.addListview_AD_03(stringArray);
        mDefDialog.setMessageLGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.select));
        mDefDialog.setButtonCancel(r.getString (R.string.cancel));
        mDefDialog.setSpinnerGone();
        mDefDialog.show();
    }

    public void A_D_04(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setMessage (r.getString (R.string.back_to_list));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_D_07(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }

        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.ask_exit));
        mDefDialog.setButtonConfirm(r.getString (R.string.yes));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_D_12(String sw){
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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String str=r.getString(R.string.up_version1)+"("+sw+")";
        mDefDialog.setMessage (str);
        mDefDialog.setCenterKeyGone();
        mDefDialog.addListview_AD_12();
        mDefDialog.setMessageLGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.select));
        mDefDialog.setButtonCancel(r.getString (R.string.cancel));
        mDefDialog.setSpinnerGone();
        mDefDialog.show();
    }

    public void A_D_13(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.been_updated));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_D_14(String times){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }

        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String str0=r.getString (R.string.to_update);
        String str1="times";
        String str=str0+times+str1;
        mDefDialog.setMessage (str);
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_D_15(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.Ready_update));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_D_16(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.Ready_update));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_D_17(String date){
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
                @Override
                public void onCancelKey () {
                    callbackConfirmKey.onCancel();
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
        mDefDialog.setButtonConfirm(r.getString (R.string.yes));
        mDefDialog.setButtonCancel(r.getString (R.string.no));
        mDefDialog.setSpinnerGone();
        mDefDialog.show();

    }

    public void A_D_18(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.ask_exit));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_D_21(String[] stringArray){
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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage(r.getString(R.string.up_detail));
        mDefDialog.addListview_AD_21(stringArray);
        mDefDialog.setMessageLGone();
        mDefDialog.setSpinnerGone();
        mDefDialog.setCenterKeyGone();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.ok_dailog));
        mDefDialog.show();
    }

    public void A_D_19(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.ask_exit));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_D_20(String sw){
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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String str=r.getString(R.string.up_version1)+"("+sw+")";
        mDefDialog.setMessage (str);
        mDefDialog.setCenterKeyGone();
        mDefDialog.addListview_AD_20();
        mDefDialog.setMessageLGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.select));
        mDefDialog.setButtonCancel(r.getString (R.string.cancel));
        mDefDialog.setSpinnerGone();
        mDefDialog.show();
        
    }

    public void A_D_22(String[] stringArray){
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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage(r.getString(R.string.up_detail));
        mDefDialog.addListview_AD_21(stringArray);
        mDefDialog.setMessageLGone();
        mDefDialog.setSpinnerGone();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setCenterKeyGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.ok_dailog));
        mDefDialog.show();

    }

    public void A_N_02(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }

        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.Signal_prompt));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_N_03(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }

        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.battery_message));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void A_N_04(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.Services_unavailable));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_N_06(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }

        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.is_auto_up));
        mDefDialog.setButtonConfirm (r.getString (R.string.up_auto));
        mDefDialog.setButtonCancel (r.getString (R.string.up_noauto));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_N_07(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.not_auto_up));
        mDefDialog.setButtonConfirm (r.getString (R.string.up_auto));
        mDefDialog.setButtonCancel (r.getString (R.string.up_noauto));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_N_08(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.already_setting));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCancelKeyGone();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void A_N_09(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setButtonConfirm(r.getString (R.string.softkey_next));
        mDefDialog.setCenterKeyGone();
        mDefDialog.setMessageGone();
        mDefDialog.addListview_AN_09();
        mDefDialog.setMessageLGone();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setSpinnerGone();
        mDefDialog.show();
    }
    //    A_N_10 A_N_11
    public void A_N_10(String time){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String string0 = r.getString (R.string.auto_up);
        String string  = string0 +' ' + time + '.';
        mDefDialog.setMessage (string);
        mDefDialog.setButtonCancel (r.getString (R.string.Replacement));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }
    //    A_N_12 D_N_14
    public void A_N_12(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.set_time));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.setNumberPickerVisible();
        mDefDialog.show();
    }
    //    A_N_13 D_N_15
    public void A_N_13(String time){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String string0 = r.getString (R.string.setup_time);
        String string = string0 + time + '.';
        mDefDialog.setMessage (string);
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    //    select cancel B_D_02 B_D_06
    public void B_D_02(String[] stringArray){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String string0 = r.getString(R.string.schedule_time);
        String string = string0 + ".";
        mDefDialog.setMessage(string);
        mDefDialog.addListview_BD_02(stringArray);
        mDefDialog.setMessageLGone();
        mDefDialog.setSpinnerGone();
        mDefDialog.setCenterKeyGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.select));
        mDefDialog.setButtonCancel(r.getString(R.string.cancel));

        mDefDialog.show();
    }

    //    select cancel B_D_03 B_D_07 B_D_08 B_D_09 B_D_14 B_D_15
    public void B_D_03(String[] stringArray, String date){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String string0 = r.getString(R.string.schedule_time);
        String string = string0 + "\n" + date;
//        String string = string0 + "\n" + date + '(' + day + ')';
        mDefDialog.setMessage(string);
        mDefDialog.addListview_BD_02(stringArray);
        mDefDialog.setMessageLGone();
        mDefDialog.setSpinnerGone();
        mDefDialog.setCenterKeyGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.select));
        mDefDialog.setButtonCancel(r.getString(R.string.cancel));

        mDefDialog.show();
    }

    public void B_D_04(String time){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String string0 = r.getString (R.string.already_sch_time);
        String string  = string0 + '\n' + time;
        mDefDialog.setMessage (string);
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void B_D_12(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.ask_exit));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void B_D_13(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.select_other_day));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void C_D_01(String date){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });
        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String str1=r.getString(R.string.ask_Cancel_sch2);
        String str=date+str1;
        mDefDialog.setMessage (str);
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setListViewGone();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void C_D_02(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.Cancel_sch2));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void C_D_03(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.server_busy1));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void D_D_01(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.server_busy));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setButtonCancel(r.getString (R.string.no));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_D_03(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.Services_unavailable));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void D_D_04(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.no_latest_sw1));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void D_D_05(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.up_diff));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void D_D_06(String date){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        String str1=r.getString(R.string.Cancel_sch4);
        String str2=r.getString(R.string.Cancel_sch3);
        String str=str1+date+str2;
        mDefDialog.setMessage (str);
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void D_N_01(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.downloading));
        mDefDialog.setButtonCancel (r.getString (R.string.cancel));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setConfirmGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressVisible();
        mDefDialog.show();
    }

    public void D_N_03(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        DisplayMetrics dm = r.getDisplayMetrics();
        int height = dm.heightPixels;

        mDefDialog.setCancelable(false);
        mDefDialog.setMessage (r.getString (R.string.updating));
        // 200 TextView Height
        mDefDialog.setMessageCenter(height - 200);
        mDefDialog.setConfirmGone();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.setTitleGone();
        mDefDialog.show();
    }

    public void D_N_04(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        DisplayMetrics dm = r.getDisplayMetrics();
        int height = dm.heightPixels;

        mDefDialog.setCancelable(false);
        mDefDialog.setMessage (r.getString (R.string.up_done_reboot));
        // 400 TextView Height
        mDefDialog.setMessageCenter(height - 400);
        mDefDialog.setConfirmGone();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.setTitleGone();
        mDefDialog.show();
    }

    public void D_N_06(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.up_success));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_N_07(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.Cancel_soft));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_N_09(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.up_message));
        mDefDialog.setButtonCancel (r.getString (R.string.up_later));
        mDefDialog.setButtonConfirm (r.getString (R.string.up_now));
        mDefDialog.setCenterKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_N_10(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.up_install_now));
        mDefDialog.setButtonCancel (r.getString (R.string.no));
        mDefDialog.setButtonConfirm (r.getString (R.string.yes));
        mDefDialog.setCenterKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_N_11(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.large_file));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_N_12(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.memory_message));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_N_13(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.download_cancel));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_N_16(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.up_install));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setButtonCancel(r.getString (R.string.cancel));
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_N_17(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.Cancel_soft));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setSpinnerGone ();
        mDefDialog.setMessageLGone ();
        mDefDialog.setCenterKeyGone ();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setProgressGone ();
        mDefDialog.show();
    }

    public void D_N_18(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.auto_message));
        mDefDialog.setButtonCancel (r.getString (R.string.time_change));
        mDefDialog.setCenter (r.getString (R.string.up_now));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void D_N_19(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.Services_unavailable));
        mDefDialog.setButtonConfirm (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

    public void F_D_01(){

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
            @Override
            public void onCancelKey () {
                callbackConfirmKey.onCancel();
            }
        });

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.session_msg));
        mDefDialog.setButtonCancel (r.getString (R.string.ok));
        mDefDialog.setCenterKeyGone();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setMessageLGone ();
        mDefDialog.setSpinnerGone ();
        mDefDialog.setProgressGone();
        mDefDialog.show();
    }

}
