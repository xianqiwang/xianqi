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

    public void A_D_03(String sw){
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
        mDefDialog.setMessage (sw);
        mDefDialog.setCenterKeyGone();
        mDefDialog.addListview_AD_03();
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
    public void A_D_14(){

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

        mDefDialog.setCancelable(false);
        mDefDialog.setTitle (r.getString(R.string.software_update));
        mDefDialog.setMessage (r.getString (R.string.to_update));
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
        mDefDialog.setCenterKeyGone();
        mDefDialog.setCancelKeyGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.ok_dailog));
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
        mDefDialog.setCenterKeyGone();
        mDefDialog.setButtonConfirm(r.getString (R.string.ok_dailog));
        mDefDialog.show();

    }

}
