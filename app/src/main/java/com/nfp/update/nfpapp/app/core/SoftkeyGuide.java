/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.nfp.update.nfpapp.app.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.View;
import android.os.RemoteException;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.app.NotificationManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.nfp.update.NewWindow;

public class SoftkeyGuide {

    public static final int INDEX_CSK = 0;
    public static final int INDEX_SK1 = 1;
    public static final int INDEX_SK2 = 2;

    public static final String SK_AUTO = "@SK_AUTO";
    public static final String SK_AUTO_MENU = "@SK_AUTO_MENU";
    public static final String SK_AUTO_SELECT = "@SK_AUTO_SELECT";
    public static final String SK_AUTO_ENTER = "@SK_AUTO_ENTER";

    private static com.nfp.update.nfpapp.app.core.SoftkeyGuide mSoftkeyGuide;
    private android.content.Context mContext;
    private NewWindow mWin;

    SoftkeyGuide(android.view.Window win) {
        mWin = (NewWindow)win;
        mContext = mWin.getContext();
    }

    public static SoftkeyGuide getSoftkeyGuide(Window win){
        try {
            mSoftkeyGuide = new com.nfp.update.nfpapp.app.core.SoftkeyGuide(win);
            return mSoftkeyGuide;
        } catch (Exception ex) {
        }
        return null;
    }

    public void setText(int index, CharSequence text) {
        if (index>INDEX_SK2 || index<INDEX_CSK) return;
            switch (index) {
                case INDEX_CSK:
                    mWin.getCsk().setKeyText(text);
                    break;
                case INDEX_SK1:
                    mWin.getSk1().setKeyText(text);
                    break;
                case INDEX_SK2:
                    mWin.getSk2().setKeyText(text);
                    break;
      }
    }


    public CharSequence getText(int index) {
        if (index>2||index<0) return null;
        CharSequence text = null;
         switch (index) {
             case INDEX_CSK:
                text = mWin.getCsk().getKeyText();
                break;
             case INDEX_SK1:
                text = mWin.getSk1().getKeyText();
                break;
             case INDEX_SK2:
                text = mWin.getSk2().getKeyText();
                break;
          }
        return text;
    }

    public void setEnabled(int index, boolean enabled) {
        if (index>2||index<0) return;
        switch (index) {
            case INDEX_CSK:
                mWin.getCsk().setKeyEnabled(enabled);
                break;
            case INDEX_SK1:
                mWin.getSk1().setKeyEnabled(enabled);
                break;
            case INDEX_SK2:
                mWin.getSk2().setKeyEnabled(enabled);
                break;
         }
    }
    public void setCenterKeyDown(int index, boolean enabled) {
        try {
            if (null != mContext) {
                NotificationManager svc = (NotificationManager)mContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                if (svc != null) {
/*
                        svc.setCenterKeyDown(index, enabled);
*/
                }
            }
        } catch (Exception ex) {
              throw new RuntimeException(ex);
        }
    }
    public boolean getEnabled(int index) {
        boolean mEnabled=true;
        if (index>2||index<0) return mEnabled;
         switch (index) {
             case INDEX_CSK:
                 mEnabled = mWin.getCsk().getKeyEnabled();
                 break;
             case INDEX_SK1:
                 mEnabled = mWin.getSk1().getKeyEnabled();
                 break;
             case INDEX_SK2:
                 mEnabled = mWin.getSk2().getKeyEnabled();
                 break;
         }
        return mEnabled;
    }


    private boolean handlingImeShow(android.app.NotificationManager svc) {
                if(svc != null) {
                    boolean isImeShow = false;
                    try{

                    }catch(Exception e){
                    }
                    String pkg = mContext.getPackageName();

                    if(!("jp.co.fsi.fskaren.aqua".equals(pkg)) && isImeShow){

                        boolean isServicedPkg = false;
                        try{


/*
                            isServicedPkg = imeServicedPkg.equals(pkg);
*/
                        }catch(Exception e){
                        }

                        if(isServicedPkg){
                            try{

                            }catch(Exception e){
                            }
                        }
                        return true;
                    }
                }
				return false;
	}

    public void invalidate() {
        try {
            if (null != mContext ) {
                if (mContext.getPackageName()!=null && mContext.getPackageName().equals("jp.co.softbank.mb.pim")) {
                    android.app.NotificationManager svc = (android.app.NotificationManager)mContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                    mWin.getCsk().invalidateValue();
                    mWin.getSk1().invalidateValue();
                    mWin.getSk2().invalidateValue();

                    if(handlingImeShow(svc)){
                        return;
                    }

                    if (svc != null && !(mWin.forceNoReFreshSoftkeyGuide) /*&& mWin.getCurrentWindowIsFocus()*/) {
/*                        svc.setNavigationBarText(mWin.getCsk().getKeyIndex(), mWin.getCsk().getKeyText().toString(),false);
                        svc.setNavigationBarText(mWin.getSk1().getKeyIndex(), mWin.getSk1().getKeyText().toString(),mWin.hasFeature(android.view.Window.FEATURE_OPTIONS_MENU));
                        svc.setNavigationBarText(mWin.getSk2().getKeyIndex(), mWin.getSk2().getKeyText().toString(),false);

                        svc.setNavigationBarButtonEnabled(mWin.getCsk().getKeyIndex(),mWin.getCsk().getKeyEnabled());
                        svc.setNavigationBarButtonEnabled(mWin.getSk1().getKeyIndex(),mWin.getSk1().getKeyEnabled());
                        svc.setNavigationBarButtonEnabled(mWin.getSk2().getKeyIndex(),mWin.getSk2().getKeyEnabled());*/

                        //modify for bug 16114 by zhaokun {
                        //Log.d("SoftkeyGuide","zhaokun 160 mWin.getSk2().getKeyText().toString()="+mWin.getSk2().getKeyText().toString());
                        //try{int a = 1/0;}catch(Exception e){e.printStackTrace();}

                        }
                        //modify for bug 16114 by zhaokun }
                    }
                }else {
                    android.app.NotificationManager svc = (android.app.NotificationManager)mContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                    mWin.getCsk().invalidateValue();
                    mWin.getSk1().invalidateValue();
                    mWin.getSk2().invalidateValue();
                    if(handlingImeShow(svc)){
                        return;
                    }

                    if (svc != null && !(mWin.forceNoReFreshSoftkeyGuide)) {
/*                        svc.setNavigationBarText(mWin.getCsk().getKeyIndex(), mWin.getCsk().getKeyText().toString(),false);
                        svc.setNavigationBarText(mWin.getSk1().getKeyIndex(), mWin.getSk1().getKeyText().toString(),mWin.hasFeature(android.view.Window.FEATURE_OPTIONS_MENU));
                        svc.setNavigationBarText(mWin.getSk2().getKeyIndex(), mWin.getSk2().getKeyText().toString(),false);

                        svc.setNavigationBarButtonEnabled(mWin.getCsk().getKeyIndex(),mWin.getCsk().getKeyEnabled());
                        svc.setNavigationBarButtonEnabled(mWin.getSk1().getKeyIndex(),mWin.getSk1().getKeyEnabled());
                        svc.setNavigationBarButtonEnabled(mWin.getSk2().getKeyIndex(),mWin.getSk2().getKeyEnabled());*/

                        //modify for bug 16114 by zhaokun {
                        //Log.d("SoftkeyGuide","zhaokun 160 mWin.getSk2().getKeyText().toString()="+mWin.getSk2().getKeyText().toString());
                        //try{int a = 1/0;}catch(Exception e){e.printStackTrace();}

                        //modify for bug 16114 by zhaokun }

                }
            }

        } catch (Exception ex) {
              throw new RuntimeException(ex);
        }
    }
    public void invalidateWindow() {
      /*  try {
            if (mContext.getPackageName()!=null && mContext.getPackageName().equals("jp.co.softbank.mb.pim")) {
                if (null != mContext && !(mWin.forceNoReFreshSoftkeyGuide) ) {
                    android.app.NotificationManager svc = (android.app.NotificationManager)mContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
          *//*          if (svc != null&& mWin.getCurrentWindowIsFocus()) {
                        svc.setNavigationBarText(mWin.getCsk().getKeyIndex(), mWin.getCsk().getKeyText().toString(),false);
                        svc.setNavigationBarText(mWin.getSk1().getKeyIndex(), mWin.getSk1().getKeyText().toString(),
                                mWin.hasFeature(android.view.Window.FEATURE_OPTIONS_MENU));
                        svc.setNavigationBarText(mWin.getSk2().getKeyIndex(), mWin.getSk2().getKeyText().toString(),false);

                        svc.setNavigationBarButtonEnabled(mWin.getCsk().getKeyIndex(),mWin.getCsk().getKeyEnabled());
                        svc.setNavigationBarButtonEnabled(mWin.getSk1().getKeyIndex(),mWin.getSk1().getKeyEnabled());
                        svc.setNavigationBarButtonEnabled(mWin.getSk2().getKeyIndex(),mWin.getSk2().getKeyEnabled());*//*

                        //modify for bug 16114 by zhaokun {
                        //Log.d("SoftkeyGuide","zhaokun 194 mWin.getSk2().getKeyText().toString()="+mWin.getSk2().getKeyText().toString());
                        //try{int a = 1/0;}catch(Exception e){e.printStackTrace();}
                        SystemUtilsManager sum= (SystemUtilsManager)mContext.getSystemService("systemutils_manager");
                        if(sum != null ) {
                            sum.setCurrentSoftkeyGuideInfo(mWin.getSk1().getKeyText().toString(),
                                mWin.getCsk().getKeyText().toString(),
                                mWin.getSk2().getKeyText().toString(),
                                mWin.getSk1().getKeyEnabled(),
                                mWin.getCsk().getKeyEnabled(),
                                mWin.getSk2().getKeyEnabled(),
                                mWin.hasFeature(14));
                        }
                        //modify for bug 16114 by zhaokun }
                    }
                } else {
                    return;
                }
            } else {
    *//*            if (null != mContext && !(mWin.forceNoReFreshSoftkeyGuide) ) {
                    android.app.NotificationManager svc = (android.app.NotificationManager)mContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
               *//**//*     if (svc != null) {
                        svc.setNavigationBarText(mWin.getCsk().getKeyIndex(), mWin.getCsk().getKeyText().toString(),false);
                        svc.setNavigationBarText(mWin.getSk1().getKeyIndex(), mWin.getSk1().getKeyText().toString(),
                                mWin.hasFeature(android.view.Window.FEATURE_OPTIONS_MENU));
                        svc.setNavigationBarText(mWin.getSk2().getKeyIndex(), mWin.getSk2().getKeyText().toString(),false);

                        svc.setNavigationBarButtonEnabled(mWin.getCsk().getKeyIndex(),mWin.getCsk().getKeyEnabled());
                        svc.setNavigationBarButtonEnabled(mWin.getSk1().getKeyIndex(),mWin.getSk1().getKeyEnabled());
                        svc.setNavigationBarButtonEnabled(mWin.getSk2().getKeyIndex(),mWin.getSk2().getKeyEnabled());

                        //modify for bug 16114 by zhaokun {
                        //Log.d("SoftkeyGuide","zhaokun 194 mWin.getSk2().getKeyText().toString()="+mWin.getSk2().getKeyText().toString());
                        //try{int a = 1/0;}catch(Exception e){e.printStackTrace();}
                        SystemUtilsManager sum= (SystemUtilsManager)mContext.getSystemService("systemutils_manager");
                        if(sum != null ) {
                            sum.setCurrentSoftkeyGuideInfo(mWin.getSk1().getKeyText().toString(),
                                mWin.getCsk().getKeyText().toString(),
                                mWin.getSk2().getKeyText().toString(),
                                mWin.getSk1().getKeyEnabled(),
                                mWin.getCsk().getKeyEnabled(),
                                mWin.getSk2().getKeyEnabled(),
                                mWin.hasFeature(14));
                        }*//**//*
                        //modify for bug 16114 by zhaokun }
                    }*//*
                } else {
                    return;
                }


            }
        } catch (Exception ex) {
              throw new RuntimeException(ex);
        }*/
    }

    public void show() {
        try {
            int systemUIVisibility = 0;
            systemUIVisibility &= ~ android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            mWin.getDecorView().setSystemUiVisibility(systemUIVisibility);
        } catch (Exception ex) {
              throw new RuntimeException(ex);
        }
    }

    public void showFromWindow() {
        try {
            int systemUIVisibility = 0;
            systemUIVisibility &= ~ android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            mWin.getDecorView().setSystemUiVisibility(systemUIVisibility);
            invalidateWindow();
        } catch (Exception ex) {
              throw new RuntimeException(ex);
        }
    }

    public void hide() {
        try {
            mWin.getDecorView().setSystemUiVisibility(
                android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );
        } catch (Exception ex) {
              throw new RuntimeException(ex);
        }
    }
}

