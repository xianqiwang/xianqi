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

public class SoftKey {

    public SoftKey(int index,CharSequence text,boolean keyEnabled){

        mKeyIndex = index;
        mKeyEnabled = keyEnabled;
        mKeyText = text;
        mFinalKeyEnabled = keyEnabled;
        mFinalKeyText = text;
    }

    private int mKeyIndex;
    private boolean mKeyEnabled;
    private CharSequence mKeyText ;

    private boolean mFinalKeyEnabled;
    private CharSequence mFinalKeyText ;


    public int getKeyIndex() {
        return mKeyIndex;
    }
    public void setKeyIndex(int keyIndex) {
        mKeyIndex = keyIndex;
    }
    public boolean getKeyEnabled() {
        return mFinalKeyEnabled;
    }
    public void setKeyEnabled(boolean keyEnabled) {
        mKeyEnabled = keyEnabled;
    }
    public CharSequence getKeyText() {
        return mFinalKeyText;
    }
    public void setKeyText(CharSequence keyText) {
        mKeyText = keyText;
    }
    public void invalidateValue() {
        mFinalKeyText = mKeyText;
        mFinalKeyEnabled = mKeyEnabled;
    }
}
