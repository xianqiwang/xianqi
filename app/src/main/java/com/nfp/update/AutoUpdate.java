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

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AutoUpdate extends Activity {
    private Button mAuto;
    private SharedPreferences spref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_update);

        mAuto = (Button)findViewById(R.id.auto);

        spref = this.getSharedPreferences("debug_comm", 0);
        if(spref.getInt("AUTO_UPDATE", 0)!=0){
           mAuto.setText(this.getString(R.string.auto_update_off));
        }

        mAuto.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View v) {
                        setState();
	          }
	});
    }

    public void setState() {
        int isAuto =  0;
        if(mAuto.getText() == getString(R.string.auto_update_on)){
            mAuto.setText(getString(R.string.auto_update_off));
            isAuto = 1;
         }else{
            mAuto.setText(getString(R.string.auto_update_on));
        }
        SharedPreferences.Editor editor = spref.edit();
        editor.putInt("AUTO_UPDATE", isAuto);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
