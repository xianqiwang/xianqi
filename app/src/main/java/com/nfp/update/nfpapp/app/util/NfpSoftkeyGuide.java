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
package com.nfp.update.nfpapp.app.util;

import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.View;
import android.os.RemoteException;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.app.NotificationManager;
import com.nfp.update.nfpapp.app.core.SoftKey;
import com.nfp.update.nfpapp.app.core.SoftkeyGuide;

public class NfpSoftkeyGuide {

    public static final int INDEX_CSK = 0;
    public static final int INDEX_SK1 = 1;
    public static final int INDEX_SK2 = 2;

    public static final String SK_AUTO = "@SK_AUTO";
    public static final String SK_AUTO_MENU = "@SK_AUTO_MENU";
    public static final String SK_AUTO_SELECT = "@SK_AUTO_SELECT";
    public static final String SK_AUTO_ENTER = "@SK_AUTO_ENTER";


    private android.content.Context mContext;
    private android.view.Window mWin;
    private SoftkeyGuide mSoftkeyGuide;
    private static com.nfp.update.nfpapp.app.util.NfpSoftkeyGuide mNfpSoftkeyGuide;
    NfpSoftkeyGuide(android.view.Window win) {
        mWin = win;
        mContext = mWin.getContext();
        mSoftkeyGuide = SoftkeyGuide.getSoftkeyGuide(win);
    }
    public static com.nfp.update.nfpapp.app.util.NfpSoftkeyGuide getSoftkeyGuide(android.view.Window win){
        try {
            mNfpSoftkeyGuide = new com.nfp.update.nfpapp.app.util.NfpSoftkeyGuide(win);
            return mNfpSoftkeyGuide;
        } catch (Exception ex) {
        }
        return null;
    }

    public void setText(int index, CharSequence text) {
        if (null != mSoftkeyGuide)
            mSoftkeyGuide.setText(index,text);
    }

    public void setText(int index, int resid) {
        String text = mContext.getResources().getString(resid);
        if (null != mSoftkeyGuide && text != null)
            mSoftkeyGuide.setText(index,text);
    }

    public CharSequence getText(int index) {
        CharSequence text = null;
        if (null != mSoftkeyGuide)
            text = mSoftkeyGuide.getText(index);
        return text;
    }

    public void setEnabled(int index, boolean enabled) {
        if (null != mSoftkeyGuide)
            mSoftkeyGuide.setEnabled(index,enabled);
    }

    public boolean getEnabled(int index) {
        boolean mEnabled=true;
        if (null != mSoftkeyGuide)
            mEnabled = mSoftkeyGuide.getEnabled(index);
        return mEnabled;
    }


    public void invalidate() {
        if (null != mSoftkeyGuide)
            mSoftkeyGuide.invalidate();

    }

    public void show() {
        if (null != mSoftkeyGuide)
            mSoftkeyGuide.show();
    }

    public void hide() {
        if (null != mSoftkeyGuide)
            mSoftkeyGuide.hide();
    }
}

