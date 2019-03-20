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

import android.widget.Button;
import android.view.View;
import android.app.Dialog ;
import android.util.Log ;

/**
 * NfpDialogUtil was added by chen yunwen on Oct 12 , 2016
 */
public class NfpDialogUtil {
    public static final int SCROLL_NONE = 0;
    public static final int SCROLL_SOFTKEY = 1;
    public static com.nfp.update.nfpapp.app.util.NfpDialogUtil  sNfpDialogUtil;
    private NfpSoftkeyGuide sNfpSoftkeyGuide;
    private NfpWidgetUtil sNfpWidgetUtil;

    public static com.nfp.update.nfpapp.app.util.NfpDialogUtil getInstance() {
        if (sNfpDialogUtil == null) {
            sNfpDialogUtil = new com.nfp.update.nfpapp.app.util.NfpDialogUtil();
        }
        return sNfpDialogUtil;
    }

    public void assignButtonToSoftkey(Dialog dialog, boolean assign) {
        boolean isButton=false;
        if (assign&&dialog!=null) {
            sNfpSoftkeyGuide = NfpSoftkeyGuide.getSoftkeyGuide(dialog.getWindow());
            sNfpSoftkeyGuide.setEnabled(1, true);
            sNfpSoftkeyGuide.setEnabled(2, true);
            sNfpSoftkeyGuide.setEnabled(0, true);

            for(int i = 0; i<3; i++){

                    isButton=true;
                }
            }

            if(isButton){
                sNfpSoftkeyGuide.invalidate();
            }



    }

    public void setTextScrollMode (android.app.Dialog dialog, int mode) {
        if(dialog!=null){

        }
    }

    public int getTextScrollMode (android.app.Dialog dialog) {

        return 0;
    }
}
