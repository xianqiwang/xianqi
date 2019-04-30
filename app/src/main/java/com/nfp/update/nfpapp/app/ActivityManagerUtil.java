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
package com.nfp.update.nfpapp.app;

import com.nfp.update.widget.CheckDeltaProgressDialog;


public class ActivityManagerUtil {
    private static final String TAG = "ActivityManagerUtil";
    public static final int ENDKEY_FINISH_TASK = 0;
    public static final int ENDKEY_MOVE_TASK_TO_BACK = 1;
    public static final int ENDKEY_SHOW_DIALOG_AND_FINISH = 2;
    public static final int ENDKEY_SHOW_DIALOG_AND_MOVE_TO_BACK = 3;
    public static android.app.Activity mActivity=null;
    public static int mMode=0;

    public static void setEndKeyBehavior (android.app.Activity activity, int mode){
        setEndKeyBehavior(activity,mode,null);
    }
    public static void setEndKeyBehavior (android.app.Activity activity, int mode, String dialogMessage){
        mActivity = activity;
        mMode = mode;
        String packageName=(mActivity.getComponentName().getPackageName()==null) ? "" : mActivity.getComponentName().getPackageName();
        String className=(mActivity.getComponentName().getShortClassName()==null) ? "" : mActivity.getComponentName().getShortClassName();;
        String message=(dialogMessage == null) ? "" : dialogMessage;

    }
    public static void resetEndKeyBehavior (android.app.Activity activity){
        setEndKeyBehavior(activity,-1,null);
    }

    public static void turnOnListenerKey(CheckDeltaProgressDialog checkDeltaProgressDialog, boolean b) {
    }

    public static void setListenerKey(CheckDeltaProgressDialog checkDeltaProgressDialog, int[] ints) {
    }
}

