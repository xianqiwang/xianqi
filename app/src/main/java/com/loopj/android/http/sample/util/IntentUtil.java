package com.loopj.android.http.sample.util;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class IntentUtil {

    public static String[] serializeHeaders(cz.msebera.android.httpclient.Header[] headers) {
        if (headers == null) {
            return new String[0];
        }
        String[] rtn = new String[headers.length * 2];
        int index = -1;
        for (cz.msebera.android.httpclient.Header h : headers) {
            rtn[++index] = h.getName();
            rtn[++index] = h.getValue();
        }
        return rtn;
    }

    public static cz.msebera.android.httpclient.Header[] deserializeHeaders(String[] serialized) {
        if (serialized == null || serialized.length % 2 != 0) {
            return new cz.msebera.android.httpclient.Header[0];
        }
        cz.msebera.android.httpclient.Header[] headers = new cz.msebera.android.httpclient.Header[serialized.length / 2];
        for (int i = 0, h = 0; h < headers.length; i++, h++) {
            headers[h] = new cz.msebera.android.httpclient.message.BasicHeader(serialized[i], serialized[++i]);
        }
        return headers;
    }

}
