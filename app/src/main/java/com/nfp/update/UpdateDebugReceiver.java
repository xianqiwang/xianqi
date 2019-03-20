/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.nfp.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.nfp.update.R;

import java.io.File;

public class UpdateDebugReceiver extends BroadcastReceiver {

    private Context mContext;
    @Override
    public void onReceive(final Context context, Intent intent) {
        int c1 = intent.getIntExtra("fotaset", 0);
        int c2 = intent.getIntExtra("fotaserver", 0);
        int c3 = intent.getIntExtra("fotapolset", 0);
        int c4 = intent.getIntExtra("fotapoltimer", 0);
        int c5 = intent.getIntExtra("fotaretry", 1440);
        mContext = context;
        StartDebugService(c1, c2, c3, c4, c5);
    }

    public void StartDebugService(int ct1, int ct2, int ct3, int ct4, int ct5) {
        Intent intent= new Intent(mContext, DebugService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("fset", ct1);
        intent.putExtra("fserver", ct2);
        intent.putExtra("fpolset", ct3);
        intent.putExtra("fpoltimer", ct4);
        intent.putExtra("fretry", ct5);
        mContext.startService(intent);
    }
}
