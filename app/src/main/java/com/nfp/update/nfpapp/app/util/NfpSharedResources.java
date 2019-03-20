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

import android.content.res.AssetManager;
import android.content.res.Resources;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.TypedValue;
import android.util.Log;
import android.content.Context;

public class NfpSharedResources {

    static final String TAG = "NfpSharedResources";
    static final String PACKAGE_NAME = "android";
    private static android.content.res.Resources resources;

    public static int getIdentifier(String name, String defType) {
         resources = android.content.res.Resources.getSystem();
         return resources.getIdentifier(name, defType, PACKAGE_NAME);
    }

    public String getString( int id) {
        final String res = resources.getString(id);
        return res;
    }

    public Drawable getDrawable( int id) {
        final android.graphics.drawable.Drawable res = resources.getDrawable(id);
              return res;
    }



}

