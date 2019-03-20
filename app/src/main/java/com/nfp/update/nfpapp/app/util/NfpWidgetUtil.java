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

import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.widget.ListView;
import android.widget.TextView;

public class NfpWidgetUtil {
    public static final int PAGESCROLL_NONE = 0;
    public static final int PAGESCROLL_SOFTKEY = 1;

    private static NfpWidgetUtil sNfpWidgetUtil;

    public static NfpWidgetUtil getInstance() {
        synchronized (com.nfp.update.nfpapp.app.util.NfpWidgetUtil.class) {
            if (sNfpWidgetUtil == null) {
                sNfpWidgetUtil = new com.nfp.update.nfpapp.app.util.NfpWidgetUtil();
            }
            return sNfpWidgetUtil;
        }
    }

    public void setListLoopable(android.widget.ListView lv, boolean isLoopable) {
        if (null != lv) {
/*
            lv.setListLoopable(isLoopable);
*/
        }
    }

    public boolean getListLoopable(android.widget.ListView lv) {
        if (null != lv) {
/*
            return lv.getListLoopable();
*/
        }
        return false;
    }

    public void setListPageScrollMode(android.widget.ListView lv, int mode) {
        if (null != lv) {
/*
            lv.setListPageScrollMode(mode);
*/
        }
    }

    public int getListPageScrollMode(android.widget.ListView lv) {
        if (null != lv) {
            return -1/*lv.getListPageScrollMode()*/;
        }
        return -1;
    }

    public void listPageScroll(android.widget.ListView lv, int direction) {
        if (null != lv) {
            /*lv.listPageScroll(direction);*/
        }
    }

    public android.widget.ListView getListView(android.preference.PreferenceFragment fragment) {
        if (null != fragment) {
            return null /*fragment.getListView()*/;
        }
        return null;
    }

    public void textPageScroll(android.widget.TextView tv, int direction) {
        if (null != tv) {
/*
            tv.textPageScroll(direction);
*/
        }
    }

    public void textLineScroll(android.widget.TextView tv, int direction) {
        if (null != tv) {
/*
            tv.textLineScroll(direction);
*/
        }
    }

    public void setDisplayShowOverflowMenuButtonEnabled(android.app.ActionBar ab, boolean showOverflowMenuButton) {
        if (null != ab) {
/*
            ab.setDisplayShowOverflowMenuButtonEnabled(showOverflowMenuButton);
*/
        }
    }
}
