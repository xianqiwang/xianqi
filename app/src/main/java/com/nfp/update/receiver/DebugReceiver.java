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

package com.nfp.update.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nfp.update.service.DebugService;

public class DebugReceiver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(final Context context, Intent intent) {

        mContext = context;
        String url = "";
        int data = 0;
        String name = intent.getStringExtra("commandName");
        String sread = intent.getStringExtra("sr");
        if(sread.equals("-l")){
            url = intent.getStringExtra("setData");
        }else if(sread.equals("-s")){
            data = intent.getIntExtra("setData", 0);
        }
        StartDebugService(name, sread, url, data);
        Log.d("lhc", "receive debug broadcast");
    }

    public void StartDebugService(String cname, String sr, String url,  int cdata) {
        Intent intent= new Intent(mContext, DebugService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("fName", cname);
        intent.putExtra("setread", sr);
        if(sr.equals("-l")){
            intent.putExtra("nValue", url);
        }else if(sr.equals("-s")) {
            intent.putExtra("nValue", cdata);
        }
        mContext.startService(intent);
    }
}
